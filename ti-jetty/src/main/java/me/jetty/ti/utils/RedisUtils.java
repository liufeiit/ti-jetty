package me.jetty.ti.utils;

import me.jetty.ti.etc.Redis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author 刘飞
 * 
 * @version 1.0.0
 * @since 2015年2月5日 上午11:03:23
 */
public class RedisUtils {

	public static JedisPool createRedisConnectionPool(Redis redis) {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxActive(redis.getMaxActive());
		poolConfig.setMinIdle(redis.getMinIdle());
		poolConfig.setMaxIdle(redis.getMaxIdle());
		poolConfig.setMaxWait(redis.getMaxWait());
		JedisPool pool = new JedisPool(poolConfig, redis.getHost(), redis.getPort(), redis.getTimeout());
		return pool;
	}
}