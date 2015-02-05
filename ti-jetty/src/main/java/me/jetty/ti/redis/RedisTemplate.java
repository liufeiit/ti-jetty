package me.jetty.ti.redis;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

/**
 * @author 刘飞
 * 
 * @version 1.0.0
 * @since 2015年2月5日 上午10:55:21
 */
public final class RedisTemplate {
	
	private final Logger log = Log.getLogger(RedisTemplate.class);

	private RedisConnectionFactory connectionFactory;

	public RedisTemplate(RedisConnectionFactory connectionFactory) {
		super();
		this.connectionFactory = connectionFactory;
	}

	public <T> T execute(RedisCallback<T> action) {
		return execute(action, null);
	}
	
	public <T> T execute(RedisCallback<T> action, T defaultValue) {
		RedisConnection conn = connectionFactory.getConnection();
		try {
			return action.doInRedis(conn.getJedis());
		} catch (Throwable ex) {
			conn.closeBroken();
			log.warn(ex.getMessage(), ex);
		} finally {
			conn.close();
		}
		return defaultValue;
	}
}