package me.jetty.ti.auth;

/**
 * @author 刘飞
 * 
 * @version 1.0.0
 * @since 2015年2月5日 上午10:45:47
 */
public class RedisAuthorizationServiceAdapter extends AbstractRedisAuthorizationService implements AuthorizationService {

	@Override
	public String login(String secretId) {
		return null;
	}

	@Override
	public boolean check(String secretJson) {
		return false;
	}
}