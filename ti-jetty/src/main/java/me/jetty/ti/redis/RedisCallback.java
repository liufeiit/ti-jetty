package me.jetty.ti.redis;

import redis.clients.jedis.Jedis;

/**
 * @author 刘飞
 * 
 * @version 1.0.0
 * @since 2015年2月5日 上午10:54:50
 */
public interface RedisCallback<T> {
	T doInRedis(Jedis jedis) throws Throwable;
}