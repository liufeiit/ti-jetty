package me.jetty.ti.redis;

/**
 * Redis实现的连接工厂接口定义类。
 * 
 * @author 刘飞
 * 
 * @version 1.0.0
 * @since 2015年2月5日 上午10:49:57
 */
public interface RedisConnectionFactory {
	/**
	 * 获得Redis连接
	 * @return
	 */
	RedisConnection getConnection();
	
	void destroy() throws Exception;
}
