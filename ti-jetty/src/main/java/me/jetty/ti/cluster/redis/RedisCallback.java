package me.jetty.ti.cluster.redis;

import redis.clients.jedis.Jedis;

/**
 * 
 * @author john.liu E-mail:fei.liu@yeepay.com
 * @version 1.0.0
 * @since 2015年1月16日 下午2:22:08
 */
public interface RedisCallback<T> {
	T doInRedis(Jedis jedis) throws Throwable;
}