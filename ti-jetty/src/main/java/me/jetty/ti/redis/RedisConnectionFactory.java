package me.jetty.ti.redis;

public interface RedisConnectionFactory {
	/**
	 * 获得Redis连接
	 * 
	 * @return
	 */
	RedisConnection getConnection();
}
