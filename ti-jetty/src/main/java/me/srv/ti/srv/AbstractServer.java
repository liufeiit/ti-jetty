package me.srv.ti.srv;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import me.srv.ti.etc.JettyProfile;
import me.srv.ti.ns.NsRegistry;

import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.session.AbstractSessionIdManager;
import org.eclipse.jetty.server.session.AbstractSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

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

	protected JettyProfile profile;

	public AbstractServer() {
		super();
		init();
		profile = NsRegistry.DEFAULT_NS_REGISTRY.newInstance(JettyProfile.class);
		log.info("JettyProfile " + profile);
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
		if (!App_Directory.exists()) {
			App_Directory.mkdirs();
		}
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
		JedisPool pool = createRedisConnectionPool();
		AbstractSessionManager sessionManager = new RedisSessionManager(pool, new JsonSerializer());
		((RedisSessionManager) sessionManager).setSaveInterval(profile.getSessionSaveInterval());
		AbstractSessionIdManager sessionIdManager = new RedisSessionIdManager(server, pool);
		((RedisSessionIdManager) sessionIdManager).setScavengerInterval(profile.getSessionScavengerInterval());

		sessionManager.getSessionCookieConfig().setDomain(profile.getSessionDomain());
		sessionManager.getSessionCookieConfig().setPath(profile.getSessionPath());
		sessionManager.getSessionCookieConfig().setMaxAge(profile.getSessionMaxAge());
		sessionManager.setRefreshCookieAge(profile.getSessionAgeInSeconds());
		sessionIdManager.setWorkerName(profile.getSessionWorkerName());

		sessionManager.setSessionIdManager(sessionIdManager);
		SessionHandler sessionHandler = new SessionHandler(sessionManager);
		context.setSessionHandler(sessionHandler);
		server.setSessionIdManager(sessionIdManager);
	}

	protected JedisPool createRedisConnectionPool() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxActive(profile.getRedisMaxActive());
		poolConfig.setMinIdle(profile.getRedisMinIdle());
		poolConfig.setMaxIdle(profile.getRedisMaxIdle());
		poolConfig.setMaxWait(profile.getRedisMaxWait());
		JedisPool pool = new JedisPool(poolConfig, profile.getRedisHost(), profile.getRedisPort(), profile.getRedisTimeout());
		return pool;
	}

	protected String guid() {
		String guid = UUID.randomUUID().toString();
		return System.currentTimeMillis() + "_" + guid.substring(0, 8) + guid.substring(9, 13) + guid.substring(14, 18) + guid.substring(19, 23) + guid.substring(24);
	}
}