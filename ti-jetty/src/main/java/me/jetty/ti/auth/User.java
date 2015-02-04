package me.jetty.ti.auth;

import java.io.Serializable;

/**
 * 
 * @author 刘飞
 * 
 * @version 1.0.0
 * @since 2015年2月4日 下午10:37:57
 */
public class User implements Serializable {

	private static final long serialVersionUID = -3418598147153414291L;

	/**
	 * 应用ID
	 */
	private String appId;
	
	/**
	 * 用于绑定应用的用户ID
	 */
	private String openId;
	
	/**
	 * 访问令牌
	 */
	private String token;
}