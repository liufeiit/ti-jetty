package me.jetty.ti.redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

public class DefaultRedisConnectionFactory implements RedisConnectionFactory {
	/**
	 * 缺省连接缓存尺寸
	 */
	public static final int DEFAULT_CONNECTION_CACHE_SIZE = 2;

	/**
	 * 缺省连接最大数量
	 */
	public static final int DEFAULT_CONNECTION_MAX_SIZE = 5;

	/**
	 * jedis池
	 */
	private JedisPool pool;

	/**
	 * Redis主机
	 */
	private String host = "127.0.0.1";

	/**
	 * Redis端口
	 */
	private int port = Protocol.DEFAULT_PORT;

	/**
	 * 超时（毫秒）
	 */
	private long timeout = Protocol.DEFAULT_TIMEOUT;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 数据库
	 */
	private int database = Protocol.DEFAULT_DATABASE;

	/**
	 * 连接缓存尺寸
	 */
	private int connectionCacheSize = DEFAULT_CONNECTION_CACHE_SIZE;

	/**
	 * 连接最大数量
	 */
	private int connectionMaxSize = DEFAULT_CONNECTION_MAX_SIZE;

	public RedisConnectionFactory init() {
		if (connectionMaxSize < connectionCacheSize) {
			connectionMaxSize = connectionCacheSize;
		}
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxActive(connectionMaxSize);
		poolConfig.setMinIdle(connectionCacheSize);
		poolConfig.setMaxIdle(connectionMaxSize);
		poolConfig.setMaxWait((int) timeout);
		pool = new JedisPool(poolConfig, host, port, (int) timeout, password, database);
		return this;
	}

	public void destroy() throws Exception {
		pool.destroy();
	}

	public RedisConnection getConnection() {
		return new DefaultRedisConnection(pool);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getDatabase() {
		return database;
	}

	public void setDatabase(int database) {
		this.database = database;
	}

	public int getConnectionCacheSize() {
		return connectionCacheSize;
	}

	public void setConnectionCacheSize(int connectionCacheSize) {
		this.connectionCacheSize = connectionCacheSize;
	}

	public int getConnectionMaxSize() {
		return connectionMaxSize;
	}

	public void setConnectionMaxSize(int connectionMaxSize) {
		this.connectionMaxSize = connectionMaxSize;
	}
}