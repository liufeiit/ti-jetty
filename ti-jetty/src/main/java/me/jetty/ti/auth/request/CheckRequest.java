package me.jetty.ti.auth.request;

import me.jetty.ti.auth.Alias;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @author 刘飞
 * 
 * @version 1.0.0
 * @since 2015年2月5日 下午3:01:07
 */
@XStreamAlias(Alias.Check_Request)
public class CheckRequest {
	
	/**
	 * 应用ID
	 */
	@XStreamAlias(Alias.APP_ID)
	private String app_id;

	/**
	 * 用于绑定应用系统用户开放ID
	 */
	@XStreamAlias(Alias.OPEN_ID)
	private String open_id;

	/**
	 * 访问凭证令牌
	 */
	@XStreamAlias(Alias.ACCESS_TOKEN)
	private String access_token;

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public String getOpen_id() {
		return open_id;
	}

	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
}