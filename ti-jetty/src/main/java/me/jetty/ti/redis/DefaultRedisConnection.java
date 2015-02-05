package me.jetty.ti.redis;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 缺省Redis连接实现类。
 * 
 * @author sea.bao
 */
public class DefaultRedisConnection implements RedisConnection {
	private final Logger logger = Log.getLogger(getClass());

	/**
	 * Jedis对象
	 */
	private Jedis jedis;

	/**
	 * Jedis池对象
	 */
	private JedisPool pool;

	public DefaultRedisConnection(JedisPool pool) {
		this.pool = pool;
		jedis = this.pool.getResource();
	}

	public Jedis getJedis() {
		return jedis;
	}

	public void close() {
		try {
			pool.returnResource(jedis);
		} catch (Throwable e) {
			logger.warn("Return jedis resource meet error.", e);
		}
	}

	public void closeBroken() {
		try {
			pool.returnBrokenResource(jedis);
		} catch (Throwable e) {
			logger.warn("Return jedis resource meet error.", e);
		}
	}

	public void forceClose() {
		try {
			jedis.disconnect();
		} catch (Throwable e) {
			logger.warn("Jedis disconnect error.", e);
		}
	}
}
