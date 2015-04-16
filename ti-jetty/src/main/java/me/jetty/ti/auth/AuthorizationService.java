package me.jetty.ti.auth;

/**
 * 用户授权服务.
 * 
 * @author 刘飞
 * 
 * @version 1.0.0
 * @since 2015年2月5日 上午10:38:18
 */
public interface AuthorizationService {

	/**
	 * 用户登录
	 */
	String doLogin(String secretId);
	
	/**
	 * 用户身份校验
	 */
	String doPrivileged(String secretXml);
}