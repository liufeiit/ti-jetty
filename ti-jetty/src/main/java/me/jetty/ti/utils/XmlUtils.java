package me.jetty.ti.utils;

import com.thoughtworks.xstream.XStream;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0.0
 * @since 2015年2月4日 上午12:11:24
 */
public class XmlUtils {
	
	public static <T> String toXML(T object, String alias) {
		XStream xstream = new XStream();
		xstream.autodetectAnnotations(true);
		xstream.alias(alias, object.getClass());
		return xstream.toXML(object);
	}
	
	public static <T> T toObj(Class<T> clazz, String xml, String alias) {
		XStream xstream = new XStream();
		xstream.autodetectAnnotations(true);
		xstream.alias(alias, clazz);
		return clazz.cast(xstream.fromXML(xml));
	}
}
