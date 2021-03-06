package me.jetty.ti.server;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.jetty.ti.etc.Connector;
import me.jetty.ti.etc.ContextMapping;
import me.jetty.ti.etc.JettyProfile;
import me.jetty.ti.etc.Session;
import me.jetty.ti.etc.SslConnector;
import me.jetty.ti.etc.ThreadPool;
import me.jetty.ti.server.handler.StartedEventHandler;
import me.jetty.ti.server.handler.StartingEventHandler;
import me.jetty.ti.server.handler.StopedEventHandler;
import me.jetty.ti.utils.ProfileHolder;
import me.jetty.ti.utils.RedisUtils;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.session.AbstractSessionIdManager;
import org.eclipse.jetty.server.session.AbstractSessionManager;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.server.ssl.SslSelectChannelConnector;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import redis.clients.jedis.JedisPool;

import com.ovea.jetty.session.redis.RedisSessionIdManager;
import com.ovea.jetty.session.redis.RedisSessionManager;
import com.ovea.jetty.session.serializer.JsonSerializer;

/**
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0.0
 * @since 2015年4月18日 下午6:20:23
 */
public abstract class AbstractServer implements Server {

	protected final Logger log = Log.getLogger(getClass());

	protected final JettyProfile profile;

	protected final Map<String, String> contextMapping;

	protected final LinkedList<StartingEventHandler> startingEventHandlers;

	protected final LinkedList<StartedEventHandler> startedEventHandlers;

	protected final LinkedList<StopedEventHandler> stopedEventHandlers;

	public AbstractServer() {
		super();
		profile = ProfileHolder.getProfile();
		log.info("Profile " + profile);
		contextMapping = new HashMap<String, String>();
		List<ContextMapping> mappings = profile.getMappings();
		if (mappings != null && !mappings.isEmpty()) {
			for (ContextMapping mapping : mappings) {
				contextMapping.put(mapping.getPath(), mapping.getMapping());
			}
		}
		init();
		startingEventHandlers = new LinkedList<StartingEventHandler>();
		startedEventHandlers = new LinkedList<StartedEventHandler>();
		stopedEventHandlers = new LinkedList<StopedEventHandler>();
		ProtectionDomain protectionDomain = getClass().getProtectionDomain();
		URL location = protectionDomain.getCodeSource().getLocation();
		log.warn("Server Location : " + location);
	}

	@Override
	public final void start() throws Exception {
		start0();
	}

	@Override
	public final void stop() throws Exception {
		stop0();
		if (!profile.isRollback()) {
			TEMP_DIRECTORY.delete();
		}
		if (!profile.isBackup()) {
			LOG_DIRECTORY.delete();
		}
	}

	@Override
	public Server addStartingEventHandler(StartingEventHandler startingEventHandler) {
		startingEventHandlers.add(startingEventHandler);
		return this;
	}

	@Override
	public Server addStartedEventHandler(StartedEventHandler startedEventHandler) {
		startedEventHandlers.add(startedEventHandler);
		return this;
	}

	@Override
	public Server addStopedEventHandler(StopedEventHandler stopedEventHandler) {
		stopedEventHandlers.add(stopedEventHandler);
		return this;
	}

	protected abstract void start0() throws Exception;

	protected abstract void stop0() throws Exception;

	protected void init() {
		if (profile.isRollback()) {
			if (!TEMP_DIRECTORY.exists()) {
				TEMP_DIRECTORY.mkdirs();
			}
		} else {
			if (TEMP_DIRECTORY.exists()) {
				TEMP_DIRECTORY.delete();
			}
			TEMP_DIRECTORY.mkdirs();
		}
		if (profile.isBackup()) {
			if (!LOG_DIRECTORY.exists()) {
				LOG_DIRECTORY.mkdirs();
			}
		} else {
			if (LOG_DIRECTORY.exists()) {
				LOG_DIRECTORY.delete();
			}
			LOG_DIRECTORY.mkdirs();
		}
		if (!APPS_DIRECTORY.exists()) {
			APPS_DIRECTORY.mkdirs();
		}
	}

	protected File[] scanWebapps() {
		File[] apps = APPS_DIRECTORY.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				if (file.isFile()) {
					return file.getName().toLowerCase().endsWith(WAR_LOWERCASE_SUFFIX);
				}
				return true;
			}
		});
		return apps;
	}

	protected QueuedThreadPool newThreadPool() {
		QueuedThreadPool threadPool = new QueuedThreadPool();
		ThreadPool pool = profile.getThreadPool();
		threadPool.setMaxThreads(pool.getMaxThreads());
		threadPool.setMinThreads(pool.getMinThreads());
		threadPool.setMaxQueued(pool.getMaxQueued());
		threadPool.setMaxStopTimeMs(pool.getMaxStopTimeMs());
		threadPool.setMaxIdleTimeMs(pool.getMaxIdleTimeMs());
		threadPool.setDaemon(pool.isDaemon());
		threadPool.setDetailedDump(true);
		threadPool.setName(pool.getName());
		threadPool.setThreadsPriority(Thread.NORM_PRIORITY);
		return threadPool;
	}

	protected SelectChannelConnector newConnector(Connector conn) {
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(conn.getPort());
		connector.setAcceptQueueSize(conn.getAcceptQueueSize());
		connector.setAcceptors(Runtime.getRuntime().availableProcessors() * 2);
		connector.setMaxIdleTime(conn.getMaxIdleTime());
		return connector;
	}

	protected SslSelectChannelConnector newSslConnector(SslConnector sslConnector) {
		SslSelectChannelConnector connector = new SslSelectChannelConnector();
		connector.setPort(sslConnector.getPort());
		connector.setAcceptQueueSize(sslConnector.getAcceptQueueSize());
		connector.setAcceptors(Runtime.getRuntime().availableProcessors() * 2);
		connector.setMaxIdleTime(sslConnector.getMaxIdleTime());
		SslContextFactory contextFactory = connector.getSslContextFactory();
		contextFactory.setKeyStorePath(sslConnector.getKeyStorePath());
		contextFactory.setKeyStorePassword(sslConnector.getKeyStorePassword());
		contextFactory.setKeyManagerPassword(sslConnector.getKeyManagerPassword());
		if (StringUtils.isNotBlank(sslConnector.getTrustStorePath())) {
			contextFactory.setTrustStore(sslConnector.getTrustStorePath());
		}
		if (StringUtils.isNotBlank(sslConnector.getTrustStorePassword())) {
			contextFactory.setTrustStorePassword(sslConnector.getTrustStorePassword());
		}
		contextFactory.setNeedClientAuth(sslConnector.isClientAuth());
		if (StringUtils.isNotBlank(sslConnector.getCertAlias())) {
			contextFactory.setCertAlias(sslConnector.getCertAlias());
		}
		return connector;
	}

	protected void setLogHandler(Application context) {
		RequestLogHandler logHandler = new RequestLogHandler();
		NCSARequestLog requestLog = new NCSARequestLog();
		requestLog.setFilename(new File(LOG_DIRECTORY, JETTY_REQUEST_LOG).getAbsolutePath());
		requestLog.setFilenameDateFormat(REQUEST_LOG_FORMAT);
		requestLog.setRetainDays(90);
		requestLog.setAppend(true);
		requestLog.setExtended(true);
		requestLog.setLogCookies(false);
		requestLog.setLogTimeZone(GMT);
		logHandler.setRequestLog(requestLog);
		context.setHandler(logHandler);
	}

	protected void setSessionHandler(org.eclipse.jetty.server.Server server, Application context) {
		Session session = profile.getSession();
		AbstractSessionManager sessionManager;
		AbstractSessionIdManager sessionIdManager;
		if (profile.isRedisSessionEnable()) {
			JedisPool pool = RedisUtils.createRedisConnectionPool();
			sessionManager = new RedisSessionManager(pool, new JsonSerializer());
			((RedisSessionManager) sessionManager).setSaveInterval(session.getSessionSaveInterval());
			sessionIdManager = new RedisSessionIdManager(server, pool);
			((RedisSessionIdManager) sessionIdManager).setScavengerInterval(session.getSessionScavengerInterval());
		} else {
			sessionManager = new HashSessionManager();
			sessionIdManager = new HashSessionIdManager();
		}
		sessionManager.getSessionCookieConfig().setDomain(session.getSessionDomain());
		sessionManager.getSessionCookieConfig().setPath(session.getSessionPath());
		sessionManager.getSessionCookieConfig().setMaxAge(session.getSessionMaxAge());
		sessionManager.setRefreshCookieAge(session.getSessionAgeInSeconds());
		sessionIdManager.setWorkerName(session.getSessionWorkerName());
		sessionManager.setSessionIdManager(sessionIdManager);
		SessionHandler sessionHandler = new SessionHandler(sessionManager);
		context.setSessionHandler(sessionHandler);
		server.setSessionIdManager(sessionIdManager);
	}

	protected String guid() {
		String guid = UUID.randomUUID().toString();
		return System.currentTimeMillis() + "_" + guid.substring(0, 8) + guid.substring(9, 13) + guid.substring(14, 18) + guid.substring(19, 23) + guid.substring(24);
	}
}