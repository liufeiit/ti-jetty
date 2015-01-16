package me.jetty.ti.redis;

import java.util.Set;

public interface RedisTemplate {
	
	<T> T execute(RedisCallback<T> action);
	
	/**
	 * 设置Value
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	String setValue(String key, String value);

	/**
	 * 获得Value
	 * 
	 * @return
	 */
	String getValue(String key);

	/**
	 * 删除Value
	 * 
	 * @param key
	 */
	Long removeValue(String key);

	/**
	 * 设置KeyValue的过期时间（秒）
	 * 
	 * @param key
	 * @param seconds
	 * @return
	 */
	Long expire(String key, int seconds);

	/**
	 * 获得KeyValue的生存周期（秒）
	 * 
	 * @param key
	 * @return
	 */
	Long ttl(String key);

	/**
	 * 是否存在KeyValue
	 * 
	 * @param key
	 * @return
	 */
	boolean exists(String key);

	/**
	 * 获得Key模式对应的Key列表
	 * 
	 * @param kayPattern
	 * @return
	 */
	Set<String> keys(String kayPattern);

	/**
	 * 删除Key模式对应的数据
	 * 
	 * @param keyPattern
	 * @return
	 */
	long removeValues(String keyPattern);

	/**
	 * 左弹出list值
	 * 
	 * @param list
	 * @return
	 */
	String lpop(String list);

	/**
	 * 左预读list值
	 * 
	 * @param list
	 * @return
	 */
	String lpeek(String list);

	/**
	 * 右压入list值
	 * 
	 * @param list
	 * @param value
	 */
	void rpush(String list, String value);

	/**
	 * 右弹出list值
	 * 
	 * @param timeout
	 * @param lists
	 * @return
	 */
	String blpop(int timeout, String... lists);

	/**
	 * 获得并且设置key对应的值
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	String getSet(String key, String value);
}
