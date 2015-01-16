package me.jetty.ti.srv;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import me.jetty.ti.etc.JettyProfile;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.SessionIdManager;
import org.eclipse.jetty.server.SessionManager;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.ovea.jetty.session.redis.RedisSessionIdManager;
import com.ovea.jetty.session.redis.RedisSessionManager;

/**
 * 
 * @author john.liu E-mail:fei.liu@yeepay.com
 * @version 1.0.0
 * @since 2015年1月12日 上午10:12:01
 */
public class JettyServer {

	private final static Logger log = Log.getLogger(JettyServer.class);

	private static final String TEMP_DIRECTORY = "../.tmp/.vfs/";

	private Server server;
	private JedisPool pool;

	private AtomicBoolean started = new AtomicBoolean(false);

	public JettyServer() {
		super();
		try {
			start();
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						stop();
					} catch (Exception e) {
						log.warn("Jetty Stop Error.", e);
					}
				}
			}));

		} catch (Exception e) {
			log.warn("Jetty Start Error.", e);
		}
	}

	private void start() throws Exception {
		if (started.compareAndSet(true, true) && isStarted()) {
			log.warn("Jetty Server has been started.");
			return;
		}
		server = new Server();

		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(JettyProfile.Server_Port);
		connector.setAcceptQueueSize(JettyProfile.Connector_AcceptQueueSize);
		connector.setAcceptors(Runtime.getRuntime().availableProcessors() * 2);
		connector.setMaxIdleTime(JettyProfile.Connector_MaxIdleTime);
		// 注册连接器
		server.addConnector(connector);

		QueuedThreadPool threadPool = new QueuedThreadPool();
		threadPool.setMaxThreads(JettyProfile.Thread_MaxThreads);
		threadPool.setMinThreads(JettyProfile.Thread_MinThreads);
		threadPool.setMaxQueued(JettyProfile.Thread_MaxQueued);
		threadPool.setMaxStopTimeMs(JettyProfile.Thread_MaxStopTimeMs);
		threadPool.setMaxIdleTimeMs(JettyProfile.Thread_MaxIdleTimeMs);
		threadPool.setDaemon(true);
		threadPool.setDetailedDump(true);
		threadPool.setName(JettyProfile.Thread_Name);
		threadPool.setThreadsPriority(Thread.NORM_PRIORITY);
		// 注册请求处理线程池
		server.setThreadPool(threadPool);

		// 设置web应用信息.
		WebApp context = new WebApp(WebApp.SESSIONS | WebApp.SECURITY);

		context.setContextPath(JettyProfile.App_ContextPath);
		context.setWar(JettyProfile.App_War);
		context.setParentLoaderPriority(true);

		context.setExtractWAR(JettyProfile.App_Extract_WAR);

		File tmp = new File(TEMP_DIRECTORY + guid());
		if (!tmp.exists()) {
			tmp.mkdirs();
		}
		context.setTempDirectory(tmp);

		SessionManager sessionManager;
		SessionIdManager sessionIdManager;
		if (JettyProfile.App_Use_Sessions) {
			// 设置session集群redis服务
			JedisPoolConfig poolConfig = new JedisPoolConfig();
			poolConfig.setMaxActive(JettyProfile.Redis_MaxActive);
			poolConfig.setMinIdle(JettyProfile.Redis_MinIdle);
			poolConfig.setMaxIdle(JettyProfile.Redis_MaxIdle);
			poolConfig.setMaxWait(JettyProfile.Redis_MaxWait);
			pool = new JedisPool(poolConfig, JettyProfile.Redis_Host, JettyProfile.Redis_Port, JettyProfile.Redis_Timeout);
			sessionManager = new RedisSessionManager(pool);
			sessionIdManager = new RedisSessionIdManager(server, pool);
		} else {
			sessionManager = new HashSessionManager();
			sessionIdManager = new HashSessionIdManager();
		}

		sessionManager.setSessionIdManager(sessionIdManager);
		context.setSessionHandler(new SessionHandler(sessionManager));
		server.setSessionIdManager(sessionIdManager);

		server.setHandler(context);

		log.info("Starting Jetty Server ...\n" + " Listen Port : " + JettyProfile.Server_Port);

		server.start();
		started.set(true);
		server.join();

		log.info("Jetty Server Started Success.\n" + " Listen Port : " + JettyProfile.Server_Port);
	}

	private void stop() throws Exception {
		if (!isStarted()) {
			return;
		}
		log.info("Stoping Jetty Server ...");
		try {
			server.stop();
		} catch (Exception e) {
			log.warn("Jetty Server Stop Error.", e);
		}
		pool.destroy();
		log.info("Stop Jetty Server Success.");
	}

	private boolean isStarted() {
		return server != null && server.isStarted();
	}

	private String guid() {
		String guid = UUID.randomUUID().toString();
		return System.currentTimeMillis() + "_" + guid.substring(0, 8) + guid.substring(9, 13) + guid.substring(14, 18) + guid.substring(19, 23) + guid.substring(24);
	}
}