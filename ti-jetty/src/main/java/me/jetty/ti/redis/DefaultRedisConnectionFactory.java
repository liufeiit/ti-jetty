package me.jetty.ti.redis;

import redis.clients.jedis.JedisPool;

public class DefaultRedisConnectionFactory implements RedisConnectionFactory {

	/**
	 * jedis池
	 */
	private JedisPool pool;

	public DefaultRedisConnectionFactory(JedisPool pool) {
		super();
		this.pool = pool;
	}

	public void destroy() throws Exception {
		pool.destroy();
	}

	public RedisConnection getConnection() {
		return new DefaultRedisConnection(pool);
	}
}