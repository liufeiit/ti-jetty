package me.jetty.ti.utils;

import java.net.URL;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0.0
 * @since 2015年4月19日 上午11:33:22
 */
public class ResourceUtils {

	public static URL getResource(String name) {
		return ResourceUtils.class.getResource(name);
	}
}