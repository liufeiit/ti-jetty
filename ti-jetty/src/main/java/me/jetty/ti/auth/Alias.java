package me.jetty.ti.auth;

/**
 * 
 * @author 刘飞
 * 
 * @version 1.0.0
 * @since 2015年2月5日 下午3:19:32
 */
public interface Alias {
	String Login_Request = "login-request";
	String Privileged_Request = "privileged-request";
	
	String Login_Response = "login-response";
	String Privileged_Response = "privileged-response";
	
	String APP_ID = "app_id";
	String SECRET_ID = "secret_id";
	String OPEN_ID = "open_id";
	String ACCESS_TOKEN = "access_token";
	String EXPIRES_IN = "expires_in";
}