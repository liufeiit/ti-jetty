package me.jetty.ti.redis;

import redis.clients.jedis.Jedis;

public interface RedisConnection {
	/**
	 * 获得Jedis对象
	 * 
	 * @return
	 */
	Jedis getJedis();

	/**
	 * 关闭正常连接
	 */
	void close();

	/**
	 * 关闭异常连接
	 */
	void closeBroken();

	/**
	 * 强制关闭连接
	 */
	void forceClose();
}