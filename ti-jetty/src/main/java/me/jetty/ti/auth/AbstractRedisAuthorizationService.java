package me.jetty.ti.auth;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import me.jetty.ti.auth.request.LoginRequest;
import me.jetty.ti.auth.response.LoginResponse;
import me.jetty.ti.redis.DefaultRedisConnectionFactory;
import me.jetty.ti.redis.RedisCallback;
import me.jetty.ti.redis.RedisConnectionFactory;
import me.jetty.ti.redis.RedisTemplate;
import me.jetty.ti.utils.GuidUtils;
import me.jetty.ti.utils.ProfileHolder;
import me.jetty.ti.utils.RedisUtils;

/**
 * 
 * @author 刘飞
 * 
 * @version 1.0.0
 * @since 2015年2月5日 上午11:28:14
 */
public abstract class AbstractRedisAuthorizationService {
	
	protected final Logger log = Log.getLogger(getClass());
	
	protected RedisTemplate redisTemplate;

	public AbstractRedisAuthorizationService() {
		super();
		JedisPool redisConnectionPool = RedisUtils.createRedisConnectionPool(ProfileHolder.getProfile().getRedis());
		RedisConnectionFactory connectionFactory = new DefaultRedisConnectionFactory(redisConnectionPool);
		redisTemplate = new RedisTemplate(connectionFactory);
	}

	protected LoginResponse login(final LoginRequest request) {
		String open_id = redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(Jedis jedis) throws Throwable {
				String appId = request.getApp_id();
				String secretId = request.getSecret_id();
				String openId = jedis.hget(appId, secretId);
				if(StringUtils.isNotBlank(openId)) {
					return openId;
				}
				openId = GuidUtils.guid();
				jedis.hset(appId, secretId, openId);
				return openId;
			}
		});
		return null;
	}
}