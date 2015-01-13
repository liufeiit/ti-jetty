package com.ovea.jetty.session.redis;

interface JedisExecutor {
	<V> V execute(RedisCallback<V> cb);
}