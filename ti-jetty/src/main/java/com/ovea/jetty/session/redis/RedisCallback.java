package com.ovea.jetty.session.redis;

import redis.clients.jedis.Jedis;

interface RedisCallback<V> {
	V execute(Jedis jedis);
}