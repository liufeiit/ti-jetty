package me.jetty.ti.cluster.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import redis.clients.jedis.Jedis;

public class DefaultRedisTemplate implements RedisTemplate {
	/**
	 * 数据库索引
	 */
	private Integer dbIndex;

	/**
	 * Redis连接工厂
	 */
	private RedisConnectionFactory connectionFactory;

	/**
	 * 连接池
	 */
	private Map<Long, RedisConnection> connections = new ConcurrentHashMap<Long, RedisConnection>();

	protected interface RedisCallback<T> {
		T doInRedis(Jedis jedis) throws Throwable;
	}

	protected <T> T execute(RedisCallback<T> action) {
		RedisConnection conn = connectionFactory.getConnection();
		try {
			connections.put(Thread.currentThread().getId(), conn);
			Jedis jedis = conn.getJedis();
			if (dbIndex != null) {
				jedis.select(dbIndex);
			}
			T retObj = action.doInRedis(jedis);
			conn.close();
			return retObj;
		} catch (Throwable ex) {
			conn.closeBroken();
			throw new RuntimeException(ex.getMessage(), ex);
		} finally {
			connections.remove(Thread.currentThread().getId());
		}
	}

	public void closeConnections(Set<Long> consumerThreadIds) {
		for (long tid : consumerThreadIds) {
			RedisConnection conn = connections.remove(tid);
			if (conn != null) {
				try {
					conn.forceClose();
				} catch (Throwable e) {
				}
			}
		}
	}

	public String setValue(final String key, final String value) {
		return execute(new RedisCallback<String>() {
			public String doInRedis(Jedis jedis) throws Throwable {
				return jedis.set(key, value);
			}
		});
	}

	public String getValue(final String key) {
		return execute(new RedisCallback<String>() {
			public String doInRedis(Jedis jedis) throws Throwable {
				return jedis.get(key);
			}
		});
	}

	public Long removeValue(final String key) {
		return execute(new RedisCallback<Long>() {
			public Long doInRedis(Jedis jedis) throws Throwable {
				return jedis.del(key);
			}
		});
	}

	public Long expire(final String key, final int seconds) {
		return execute(new RedisCallback<Long>() {
			public Long doInRedis(Jedis jedis) throws Throwable {
				return jedis.expire(key, seconds);
			}
		});
	}

	public Long ttl(final String key) {
		return execute(new RedisCallback<Long>() {
			public Long doInRedis(Jedis jedis) throws Throwable {
				return jedis.ttl(key);
			}
		});
	}

	public boolean exists(final String key) {
		return execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(Jedis jedis) throws Throwable {
				return jedis.exists(key);
			}
		});
	}

	public Set<String> keys(final String kayPattern) {
		return execute(new RedisCallback<Set<String>>() {
			public Set<String> doInRedis(Jedis jedis) throws Throwable {
				return jedis.keys(kayPattern);
			}
		});
	}

	public long removeValues(final String keyPattern) {
		return execute(new RedisCallback<Long>() {
			public Long doInRedis(Jedis jedis) throws Throwable {
				Set<String> keys = jedis.keys(keyPattern);
				for (String key : keys) {
					jedis.del(key);
				}

				return (long) keys.size();
			}
		});
	}

	public String lpop(final String list) {
		return execute(new RedisCallback<String>() {
			public String doInRedis(Jedis jedis) throws Throwable {
				return jedis.lpop(list);
			}
		});
	}

	public String lpeek(final String list) {
		return execute(new RedisCallback<String>() {
			public String doInRedis(Jedis jedis) throws Throwable {
				List<String> values = jedis.lrange(list, 0, 0);
				if (values.isEmpty()) {
					return null;
				} else {
					return values.get(0);
				}
			}
		});
	}

	public void rpush(final String list, final String value) {
		execute(new RedisCallback<String>() {
			public String doInRedis(Jedis jedis) throws Throwable {
				jedis.rpush(list, value);
				return null;
			}
		});
	}

	public String blpop(final int timeout, final String... lists) {
		return execute(new RedisCallback<String>() {
			public String doInRedis(Jedis jedis) throws Throwable {
				List<String> values = jedis.blpop(timeout, lists);
				if (values == null) {
					return null;
				}

				if (values.size() < 2) {
					return null;
				}

				return values.get(1);
			}
		});
	}

	public String getSet(final String key, final String value) {
		return execute(new RedisCallback<String>() {
			public String doInRedis(Jedis jedis) throws Throwable {
				return jedis.getSet(key, value);
			}
		});
	}

	public void flushDb() {
		execute(new RedisCallback<String>() {
			public String doInRedis(Jedis jedis) throws Throwable {
				return jedis.flushDB();
			}
		});
	}

	public Integer getDbIndex() {
		return dbIndex;
	}

	public void setDbIndex(Integer dbIndex) {
		this.dbIndex = dbIndex;
	}

	public RedisConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

}
