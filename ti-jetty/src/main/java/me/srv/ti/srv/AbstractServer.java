package me.srv.ti.srv;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import me.srv.ti.etc.JettyProfile;
import me.srv.ti.ns.NsRegistry;

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

	protected JedisPool createRedisConnectionPool(JettyProfile profile) {
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