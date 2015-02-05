package me.jetty.ti.redis;

import redis.clients.jedis.Jedis;

/**
 * Redis连接接口定义类。
 * 
 * @author 刘飞
 * 
 * @version 1.0.0
 * @since 2015年2月5日 上午10:50:07
 */
public interface RedisConnection {
	/**
	 * 获得Jedis对象
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