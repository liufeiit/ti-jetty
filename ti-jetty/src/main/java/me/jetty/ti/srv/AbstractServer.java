package me.jetty.ti.srv;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import me.jetty.ti.etc.Connector;
import me.jetty.ti.etc.ContextMapping;
import me.jetty.ti.etc.Profile;
import me.jetty.ti.etc.Redis;
import me.jetty.ti.etc.Session;
import me.jetty.ti.etc.SslConnector;
import me.jetty.ti.utils.StreamUtils;
import me.jetty.ti.utils.XmlUtils;

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

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.ovea.jetty.session.redis.RedisSessionIdManager;
import com.ovea.jetty.session.redis.RedisSessionManager;
import com.ovea.jetty.session.serializer.JsonSerializer;

/**
 * 
 * @author fei.liu E-mail:fei.liu@andpay.me
 * 
 * @version 1.0.0
 * @since 2015年1月30日 下午5:25:57
 */
public abstract class AbstractServer implements Server {

	protected final static Logger log = Log.getLogger(JettyServer.class);

	protected AtomicBoolean started = new AtomicBoolean(false);

	protected Profile profile;

	protected Map<String, String> contextMapping = new HashMap<String, String>();

	public AbstractServer() {
		super();
		try {
			profile = XmlUtils.toObj(Profile.class, StreamUtils.copyToString(new FileInputStream("../etc/profile.xml"), Charset.forName("UTF-8")), "server");
		} catch (IOException e) {
			log.info("Reading Jetty Profile Error.", e);
			System.exit(-1);
		}
		log.info("Profile " + profile);
		List<ContextMapping> mappings = profile.getMappings();
		if (mappings != null && !mappings.isEmpty()) {
			for (ContextMapping mapping : mappings) {
				contextMapping.put(mapping.getPath(), mapping.getTo());
			}
		}
		init();
	}

	@Override
	public final void start() throws Exception {
		if (started.compareAndSet(true, true) && isStarted()) {
			log.warn("Jetty Server has been started.");
			return;
		}
		start0();
	}

	@Override
	public final void stop() throws Exception {
		if (!isStarted()) {
			return;
		}
		started.set(false);
		stop0();
	}

	protected abstract void start0() throws Exception;

	protected abstract void stop0() throws Exception;

	@Override
	public boolean isStarted() {
		return started.get();
	}

	protected void init() {
		if (!Temp_Directory.exists()) {
			Temp_Directory.mkdirs();
		}
		if (!Log_Directory.exists()) {
			Log_Directory.mkdirs();
		}
		if (!Apps_Directory.exists()) {
			Apps_Directory.mkdirs();
		}
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

	protected void setLogHandler(WebApp context) {
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
	}

	protected void setSessionHandler(org.eclipse.jetty.server.Server server, WebApp context) {
		Session session = profile.getSession();
		AbstractSessionManager sessionManager;
		AbstractSessionIdManager sessionIdManager;
		if (profile.isRedisSessionEnable()) {
			JedisPool pool = createRedisConnectionPool();
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

	protected JedisPool createRedisConnectionPool() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		Redis redis = profile.getRedis();
		poolConfig.setMaxActive(redis.getMaxActive());
		poolConfig.setMinIdle(redis.getMinIdle());
		poolConfig.setMaxIdle(redis.getMaxIdle());
		poolConfig.setMaxWait(redis.getMaxWait());
		JedisPool pool = new JedisPool(poolConfig, redis.getHost(), redis.getPort(), redis.getTimeout());
		return pool;
	}

	protected String guid() {
		String guid = UUID.randomUUID().toString();
		return System.currentTimeMillis() + "_" + guid.substring(0, 8) + guid.substring(9, 13) + guid.substring(14, 18) + guid.substring(19, 23) + guid.substring(24);
	}
}