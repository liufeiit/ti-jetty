package me.srv.ti.ns;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import me.srv.ti.jx.XPath;
import me.srv.ti.jx.XRoot;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.xml.DocumentContainer;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

/**
 * 
 * @author fei.liu E-mail:fei.liu@andpay.me
 * 
 * @version 1.0.0
 * @since 2015年1月30日 下午1:12:13
 */
public class XNsRegistry implements NsRegistry {

	protected final Logger log = Log.getLogger(getClass());

	@Override
	public <T> T newInstance(Class<T> xclass) {
		if (xclass == null) {
			return null;
		}

		T bean = null;
		try {
			bean = xclass.newInstance();
		} catch (Exception e) {
			log.info("Init Bean Error.", e);
		}

		if (bean == null) {
			return null;
		}

		XRoot xroot = xclass.getAnnotation(XRoot.class);
		if (xroot == null) {
			log.info(xclass.getName() + " 没有发现XPath配置[" + XRoot.class.getName() + "].");
			return null;
		}
		String root = xroot.value();
		log.debug("读取XPath配置信息 " + root);
		JXPathContext context = null;
		try {
			context = JXPathContext.newContext(new DocumentContainer(new File(root).toURI().toURL()));
		} catch (Exception e) {
			log.info("读取XPath配置信息异常.", e);
			return null;
		}
		PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(xclass);
		for (PropertyDescriptor propertyDescriptor : pds) {
			XPath xpath = null;
			String name = propertyDescriptor.getName();
			Method method = propertyDescriptor.getWriteMethod();
			if (method == null) {
				continue;
			}
			if (!StringUtils.startsWith(method.getName(), SET_PREFIX)) {
				continue;
			}
			xpath = method.getAnnotation(XPath.class);
			if (xpath == null) {
				Field field = findField(xclass, name);
				if (field == null) {
					continue;
				}
				xpath = field.getAnnotation(XPath.class);
			}
			if (xpath == null) {
				log.info("没有为[" + name + "]配置XPath信息.");
				continue;
			}
			String path = xpath.value();
			log.debug("设置属性值:" + path);
			try {
				BeanUtils.setProperty(bean, name, context.getValue(path));
			} catch (Exception e) {
				log.info("Invoking Method[" + method + "] Error.", e);
			}
		}
		return bean;
	}

	public static Field findField(Class<?> clazz, String name) {
		Class<?> searchType = clazz;
		while (!Object.class.equals(searchType) && searchType != null) {
			Field[] fields = searchType.getDeclaredFields();
			for (Field field : fields) {
				if (name == null || name.equals(field.getName())) {
					return field;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}
}