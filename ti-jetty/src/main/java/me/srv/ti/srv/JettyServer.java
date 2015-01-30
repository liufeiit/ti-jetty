package me.srv.ti.srv;

import java.io.File;

import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.session.AbstractSessionIdManager;
import org.eclipse.jetty.server.session.AbstractSessionManager;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import redis.clients.jedis.JedisPool;

import com.ovea.jetty.session.redis.RedisSessionIdManager;
import com.ovea.jetty.session.redis.RedisSessionManager;
import com.ovea.jetty.session.serializer.JsonSerializer;

/**
 * 
 * @author john.liu E-mail:fei.liu@yeepay.com
 * @version 1.0.0
 * @since 2015年1月12日 上午10:12:01
 */
public class JettyServer extends AbstractServer {

	private Server server;

	public JettyServer() {
		super();
	}

	protected void start0() throws Exception {
		server = new Server();
		
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(profile.getPort());
		connector.setAcceptQueueSize(profile.getAcceptQueueSize());
		connector.setAcceptors(Runtime.getRuntime().availableProcessors() * 2);
		connector.setMaxIdleTime(profile.getMaxIdleTime());
		
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
		
		server.setThreadPool(threadPool);

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

		AbstractSessionManager sessionManager;
		AbstractSessionIdManager sessionIdManager;
		if (profile.isCluster()) {
			JedisPool pool = createRedisConnectionPool(profile);
			sessionManager = new RedisSessionManager(pool, new JsonSerializer());
			((RedisSessionManager) sessionManager).setSaveInterval(profile.getSessionSaveInterval());
			sessionIdManager = new RedisSessionIdManager(server, pool);
			((RedisSessionIdManager) sessionIdManager).setScavengerInterval(profile.getSessionScavengerInterval());
		} else {
			sessionManager = new HashSessionManager();
			sessionIdManager = new HashSessionIdManager();
		}

		sessionManager.getSessionCookieConfig().setDomain(profile.getSessionDomain());
		sessionManager.getSessionCookieConfig().setPath(profile.getSessionPath());
		sessionManager.getSessionCookieConfig().setMaxAge(profile.getSessionMaxAge());
		sessionManager.setRefreshCookieAge(profile.getSessionAgeInSeconds());
		sessionIdManager.setWorkerName(profile.getSessionWorkerName());

		sessionManager.setSessionIdManager(sessionIdManager);
		SessionHandler sessionHandler = new SessionHandler(sessionManager);
		context.setSessionHandler(sessionHandler);
		server.setSessionIdManager(sessionIdManager);
		RequestLogHandler logHandler = new RequestLogHandler();
		NCSARequestLog requestLog = new NCSARequestLog();
		requestLog.setFilename(new File(Log_Directory, JETTY_REQUEST_LOG).getAbsolutePath());
		requestLog.setFilenameDateFormat(REQUEST_LOG_FORMAT);
		requestLog.setRetainDays(90);
		requestLog.setAppend(true);
		requestLog.setExtended(true);
		requestLog.setLogCookies(false);
		requestLog.setLogTimeZone(GMT);
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

	protected void stop0() throws Exception {
		log.info("Stoping Jetty Server ...");
		try {
			server.stop();
			server = null;
		} catch (Exception e) {
			log.warn("Jetty Server Stop Error.", e);
		}
		log.info("Stop Jetty Server Success.");
	}

	public boolean isStarted() {
		return super.isStarted() && server != null && server.isStarted();
	}
}