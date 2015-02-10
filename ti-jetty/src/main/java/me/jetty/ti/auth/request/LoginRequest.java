package me.jetty.ti.auth.request;

import me.jetty.ti.auth.Alias;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @author 刘飞
 * 
 * @version 1.0.0
 * @since 2015年2月5日 上午11:29:17
 */
@XStreamAlias(Alias.Login_Request)
public class LoginRequest {

	/**
	 * 应用ID
	 */
	@XStreamAlias(Alias.APP_ID)
	private String app_id;

	/**
	 * 应用系统用户ID
	 */
	@XStreamAlias(Alias.SECRET_ID)
	private String secret_id;

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public String getSecret_id() {
		return secret_id;
	}

	public void setSecret_id(String secret_id) {
		this.secret_id = secret_id;
	}
}