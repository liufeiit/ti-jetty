package me.srv.ti.ns;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import me.srv.ti.jx.XPath;
import me.srv.ti.jx.XRoot;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.xml.DocumentContainer;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ReflectionUtils;

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
		
		XRoot xroot = xclass.getAnnotation(XRoot.class);
		if (xroot == null) {
			log.info(xclass.getName() + " 没有发现XPath配置[" + XRoot.class.getName() + "].");
			return null;
		}
		String root = xroot.value();
		log.debug("读取XPath配置信息 " + root);
		JXPathContext context = null;
		try {
			Resource resource = new FileSystemResource(root);
			context = JXPathContext.newContext(new DocumentContainer(resource.getURL()));
		} catch (Exception e) {
			log.info("读取XPath配置信息异常.", e);
			return null;
		}
		T bean = BeanUtils.instantiateClass(xclass);
		PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(xclass);
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
				Field field = ReflectionUtils.findField(xclass, name);
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
				org.apache.commons.beanutils.BeanUtils.setProperty(bean, name, context.getValue(path));
			} catch (Exception e) {
				log.info("Invoking Method[" + method + "] Error.", e);
			}
		}
		return bean;
	}
}