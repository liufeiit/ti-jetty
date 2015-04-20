package me.jetty.ti.auth;

import me.jetty.ti.auth.request.LoginRequest;
import me.jetty.ti.auth.request.PrivilegedRequest;
import me.jetty.ti.auth.response.LoginResponse;
import me.jetty.ti.auth.response.PrivilegedResponse;

/**
 * 用户授权服务.
 * 
 * @author 刘飞
 * 
 * @version 1.0.0
 * @since 2015年2月5日 上午10:38:18
 */
public interface AuthenticationProvider {

	/**
	 * 用户登录
	 */
	LoginResponse doLogin(LoginRequest request);
	
	/**
	 * 用户身份校验
	 */
	PrivilegedResponse doPrivileged(PrivilegedRequest request);
}