package me.jetty.ti.auth;

import me.jetty.ti.auth.request.PrivilegedRequest;
import me.jetty.ti.auth.request.LoginRequest;
import me.jetty.ti.auth.response.PrivilegedResponse;
import me.jetty.ti.auth.response.LoginResponse;
import me.jetty.ti.redis.DefaultRedisConnectionFactory;
import me.jetty.ti.redis.RedisCallback;
import me.jetty.ti.redis.RedisConnectionFactory;
import me.jetty.ti.redis.RedisTemplate;
import me.jetty.ti.utils.CryptoUtils;
import me.jetty.ti.utils.GuidUtils;
import me.jetty.ti.utils.ProfileHolder;
import me.jetty.ti.utils.RedisUtils;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 用户授权服务.
 * 
 * @author 刘飞
 * 
 * @version 1.0.0
 * @since 2015年2月5日 上午11:28:14
 */
public class RedisAuthenticationProvider implements AuthenticationProvider {

	protected final Logger log = Log.getLogger(getClass());

	protected RedisTemplate redisTemplate;

	public RedisAuthenticationProvider() {
		super();
		JedisPool redisConnectionPool = RedisUtils.createRedisConnectionPool();
		RedisConnectionFactory connectionFactory = new DefaultRedisConnectionFactory(redisConnectionPool);
		redisTemplate = new RedisTemplate(connectionFactory);
	}

	public LoginResponse doLogin(LoginRequest request) {
		final String appId = request.getApp_id();
		final String secretId = request.getSecret_id();
		final String open_id = redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(Jedis jedis) throws Throwable {
				String openId = GuidUtils.guid();
				// 将应用系统的用户ID关联到open_id
				jedis.hset(appId, openId, secretId);
				return openId;
			}
		});
		return redisTemplate.execute(new RedisCallback<LoginResponse>() {
			@Override
			public LoginResponse doInRedis(Jedis jedis) throws Throwable {
				String access_token = CryptoUtils.sign(open_id, secretId);
				int expires_in = ProfileHolder.getProfile().getTokenExpiresInSec();
				// 设置access_token的过期时间
				jedis.setex(open_id, expires_in, access_token);
				return new LoginResponse(appId, open_id, access_token, expires_in);
			}
		}, LoginResponse.DEFAULT_RESPONSE);
	}

	public PrivilegedResponse doPrivileged(PrivilegedRequest request) {
		final String appId = request.getApp_id();
		final String open_id = request.getOpen_id();
		final String access_token = request.getAccess_token();
		return redisTemplate.execute(new RedisCallback<PrivilegedResponse>() {
			@Override
			public PrivilegedResponse doInRedis(Jedis jedis) throws Throwable {
				String _access_token = jedis.get(open_id);
				if (StringUtils.isEmpty(_access_token)) {
					return PrivilegedResponse.DEFAULT_RESPONSE;
				}
				if (!StringUtils.equals(_access_token, access_token)) {
					return PrivilegedResponse.DEFAULT_RESPONSE;
				}
				String secretId = jedis.hget(appId, open_id);
				if (StringUtils.isEmpty(secretId)) {
					return PrivilegedResponse.DEFAULT_RESPONSE;
				}
				return new PrivilegedResponse(secretId);
			}
		}, PrivilegedResponse.DEFAULT_RESPONSE);
	}
}