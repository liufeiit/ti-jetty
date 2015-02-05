package me.jetty.ti.utils;

import java.util.UUID;

/**
 * @author 刘飞
 * 
 * @version 1.0.0
 * @since 2015年2月5日 上午11:43:32
 */
public class GuidUtils {
	
	public static String guid() {
		String uuid = UUID.randomUUID().toString();
		String guid = System.currentTimeMillis() + uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid.substring(19, 23) + uuid.substring(24);
		return guid;
	}
}