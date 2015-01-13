package com.ovea.jetty.session.redis;

import com.ovea.jetty.session.SessionIdManagerSkeleton;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.TransactionBlock;
import redis.clients.jedis.exceptions.JedisException;

import javax.naming.InitialContext;
import java.util.LinkedList;
import java.util.List;

public final class RedisSessionIdManager extends SessionIdManagerSkeleton {

	final static Logger LOG = Log.getLogger("com.ovea.jetty.session");

	private static final Long ZERO = 0L;
	private static final String REDIS_SESSIONS_KEY = "jetty-sessions";
	static final String REDIS_SESSION_KEY = "jetty-session-";

	private final JedisExecutor jedisExecutor;

	public RedisSessionIdManager(Server server, JedisPool jedisPool) {
		super(server);
		this.jedisExecutor = new PooledJedisExecutor(jedisPool);
	}

	public RedisSessionIdManager(Server server, final String jndiName) {
		super(server);
		this.jedisExecutor = new JedisExecutor() {
			JedisExecutor delegate;

			@Override
			public <V> V execute(RedisCallback<V> cb) {
				if (delegate == null) {
					try {
						InitialContext ic = new InitialContext();
						JedisPool jedisPool = (JedisPool) ic.lookup(jndiName);
						delegate = new PooledJedisExecutor(jedisPool);
					} catch (Exception e) {
						throw new IllegalStateException("Unable to find instance of " + JedisExecutor.class.getName() + " in JNDI location " + jndiName + " : " + e.getMessage(), e);
					}
				}
				return delegate.execute(cb);
			}
		};
	}

	@Override
	protected void deleteClusterId(final String clusterId) {
		jedisExecutor.execute(new RedisCallback<Object>() {
			@Override
			public Object execute(Jedis jedis) {
				return jedis.srem(REDIS_SESSIONS_KEY, clusterId);
			}
		});
	}

	@Override
	protected void storeClusterId(final String clusterId) {
		jedisExecutor.execute(new RedisCallback<Object>() {
			@Override
			public Object execute(Jedis jedis) {
				return jedis.sadd(REDIS_SESSIONS_KEY, clusterId);
			}
		});
	}

	@Override
	protected boolean hasClusterId(final String clusterId) {
		return jedisExecutor.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean execute(Jedis jedis) {
				return jedis.sismember(REDIS_SESSIONS_KEY, clusterId);
			}
		});
	}

	@Override
	protected List<String> scavenge(final List<String> clusterIds) {
		List<String> expired = new LinkedList<String>();
		List<Object> status = jedisExecutor.execute(new RedisCallback<List<Object>>() {
			@Override
			public List<Object> execute(Jedis jedis) {
				return jedis.multi(new TransactionBlock() {
					@Override
					public void execute() throws JedisException {
						for (String clusterId : clusterIds) {
							exists(REDIS_SESSION_KEY + clusterId);
						}
					}
				});
			}
		});
		for (int i = 0; i < status.size(); i++)
			if (ZERO.equals(status.get(i)))
				expired.add(clusterIds.get(i));
		if (LOG.isDebugEnabled() && !expired.isEmpty())
			LOG.debug("[RedisSessionIdManager] Scavenger found {} sessions to expire: {}", expired.size(), expired);
		return expired;
	}

}
