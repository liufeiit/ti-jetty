package com.ovea.jetty.session.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

class PooledJedisExecutor implements JedisExecutor {
	
	private final JedisPool jedisPool;

	PooledJedisExecutor(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	@Override
	public <V> V execute(RedisCallback<V> cb) {
		Jedis jedis = jedisPool.getResource();
		try {
			return cb.execute(jedis);
		} catch (JedisException e) {
			jedisPool.returnBrokenResource(jedis);
			throw e;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

}
