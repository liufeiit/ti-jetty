package me.jetty.ti.srv;

import java.io.File;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import me.jetty.ti.etc.JettyProfile;
import me.jetty.ti.ns.NsRegistry;

import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.SessionIdManager;
import org.eclipse.jetty.server.SessionManager;
import org.eclipse.jetty.server.handler.RequestLogHandler;
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
import com.ovea.jetty.session.serializer.JsonSerializer;

/**
 * 
 * @author john.liu E-mail:fei.liu@yeepay.com
 * @version 1.0.0
 * @since 2015年1月12日 上午10:12:01
 */
public class JettyServer {

	private final static Logger log = Log.getLogger(JettyServer.class);

	private static final File Temp_Directory = new File("../.tmp/.vfs/");
	private static final File Log_Directory = new File("../logs/");
	private static final File App_Directory = new File("../app/");

	private Server server;

	private AtomicBoolean started = new AtomicBoolean(false);

	public static void main(String[] args) {
		System.out.println(String.valueOf(new BigDecimal(0.01D)));
	}

	public JettyServer() {
		super();
		if (!Temp_Directory.exists()) {
			Temp_Directory.mkdirs();
		}
		if (!Log_Directory.exists()) {
			Log_Directory.mkdirs();
		}
		if (!App_Directory.exists()) {
			App_Directory.mkdirs();
		}
	}

	void start() throws Exception {
		if (started.compareAndSet(true, true) && isStarted()) {
			log.warn("Jetty Server has been started.");
			return;
		}

		JettyProfile profile = NsRegistry.DEFAULT_NS_REGISTRY.newInstance(JettyProfile.class);

		server = new Server();

		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(profile.getPort());
		connector.setAcceptQueueSize(profile.getAcceptQueueSize());
		connector.setAcceptors(Runtime.getRuntime().availableProcessors() * 2);
		connector.setMaxIdleTime(profile.getMaxIdleTime());
		// 注册连接器
		server.addConnector(connector);

		QueuedThreadPool threadPool = new QueuedThreadPool();
		threadPool.setMaxThreads(profile.getQueuedMaxThreads());
		threadPool.setMinThreads(profile.getQueuedMinThreads());
		threadPool.setMaxQueued(profile.getQueuedMaxQueued());
		threadPool.setMaxStopTimeMs(profile.getQueuedMaxStopTimeMs());
		threadPool.setMaxIdleTimeMs(profile.getQueuedMaxIdleTimeMs());
		threadPool.setDaemon(true);
		threadPool.setDetailedDump(true);
		threadPool.setName(profile.getQueuedName());
		threadPool.setThreadsPriority(Thread.NORM_PRIORITY);
		// 注册请求处理线程池
		server.setThreadPool(threadPool);

		// 设置web应用信息.
		WebApp context = new WebApp(WebApp.SESSIONS | WebApp.SECURITY);

		context.setContextPath(profile.getContextPath());
		context.setWar(profile.getWar());
		context.setParentLoaderPriority(true);
		context.setExtractWAR(true);

		File tmp = new File(Temp_Directory, guid());
		if (!tmp.exists()) {
			tmp.mkdirs();
		}
		context.setTempDirectory(tmp);

		SessionManager sessionManager;
		SessionIdManager sessionIdManager;
		if (profile.isRequireSession()) {
			// 设置session集群redis服务
			JedisPoolConfig poolConfig = new JedisPoolConfig();
			poolConfig.setMaxActive(profile.getRedisMaxActive());
			poolConfig.setMinIdle(profile.getRedisMinIdle());
			poolConfig.setMaxIdle(profile.getRedisMaxIdle());
			poolConfig.setMaxWait(profile.getRedisMaxWait());

			JedisPool pool = new JedisPool(poolConfig, profile.getRedisHost(), profile.getRedisPort(), profile.getRedisTimeout());

			sessionManager = new RedisSessionManager(pool, new JsonSerializer());

			((RedisSessionManager) sessionManager).setSaveInterval(20);
			((RedisSessionManager) sessionManager).setSessionDomain("127.0.0.1");
			((RedisSessionManager) sessionManager).setSessionPath(profile.getContextPath());
			((RedisSessionManager) sessionManager).setMaxCookieAge(86400);
			((RedisSessionManager) sessionManager).setRefreshCookieAge(300);

			sessionIdManager = new RedisSessionIdManager(server, pool);

			((RedisSessionIdManager) sessionIdManager).setScavengerInterval(30000);
			((RedisSessionIdManager) sessionIdManager).setWorkerName("node-001");

		} else {
			sessionManager = new HashSessionManager();
			sessionIdManager = new HashSessionIdManager();
		}

		sessionManager.setSessionIdManager(sessionIdManager);
		context.setSessionHandler(new SessionHandler(sessionManager));
		System.out.println("WebInf : " + context.getWebInf());
		server.setSessionIdManager(sessionIdManager);

		RequestLogHandler logHandler = new RequestLogHandler();
		NCSARequestLog requestLog = new NCSARequestLog();
		requestLog.setFilename(new File(Log_Directory, "jetty-yyyy_mm_dd.request.log").getAbsolutePath());
		requestLog.setFilenameDateFormat("yyyy_MM_dd");
		requestLog.setRetainDays(90);
		requestLog.setAppend(true);
		requestLog.setExtended(true);
		requestLog.setLogCookies(false);
		requestLog.setLogTimeZone("GMT");

		logHandler.setRequestLog(requestLog);

		context.setHandler(logHandler);
		server.setHandler(context);

		log.info("Starting Jetty Server ...\n" + " Listen Port : " + profile.getPort());

		server.setStopAtShutdown(true);
		server.setSendServerVersion(true);

		server.start();
		started.set(true);
		server.dumpStdErr();
		log.info("Jetty Server Started Success.\n" + " Listen Port : " + profile.getPort());

		server.join();

	}

	void stop() throws Exception {
		if (!isStarted()) {
			return;
		}
		started.set(false);
		log.info("Stoping Jetty Server ...");
		try {
			server.stop();
			server = null;
		} catch (Exception e) {
			log.warn("Jetty Server Stop Error.", e);
		}
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