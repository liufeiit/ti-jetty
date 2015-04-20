package me.jetty.ti.auth;

/**
 * 用户授权服务.
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0.0
 * @since 2015年4月20日 下午9:48:57
 */
public interface XmlAuthenticationProvider {

	/**
	 * 用户登录
	 */
	String doLogin(String loginRequest);
	
	/**
	 * 用户身份校验
	 */
	String doPrivileged(String privilegedRequest);
}