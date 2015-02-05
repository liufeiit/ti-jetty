package me.jetty.ti.utils;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import com.thoughtworks.xstream.XStream;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0.0
 * @since 2015年2月4日 上午12:11:24
 */
public class XmlUtils {

	protected static final Logger log = Log.getLogger(XmlUtils.class);
	
	public static <T> String toXML(T object, String alias) {
		try {
			XStream xstream = new XStream();
			xstream.autodetectAnnotations(true);
			xstream.alias(alias, object.getClass());
			return xstream.toXML(object);
		} catch (Exception e) {
			log.warn("XStream Parse to XML Error.", e);
		}
		return StringUtils.EMPTY;
	}
	
	public static <T> T toObj(Class<T> clazz, String xml, String alias) {
		try {
			XStream xstream = new XStream();
			xstream.autodetectAnnotations(true);
			xstream.alias(alias, clazz);
			return clazz.cast(xstream.fromXML(xml));
		} catch (Exception e) {
			log.warn("XStream Parse to Obj Error.", e);
		}
		return null;
	}
}
