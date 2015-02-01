package me.srv.ti.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import me.srv.ti.err.ErrorCode;
import me.srv.ti.err.SrvException;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0.0
 * @since 2015年1月30日 下午11:19:41
 */
public class BeanUtils {

	public static final ClassLoader defaultLoader = BeanUtils.class.getClassLoader();

	private static Method DEFINE_CLASS;
	private static final ProtectionDomain PROTECTION_DOMAIN;

	static {
		PROTECTION_DOMAIN = AccessController.doPrivileged(new PrivilegedAction<ProtectionDomain>() {
			public ProtectionDomain run() {
				return BeanUtils.class.getProtectionDomain();
			}
		});
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			public Object run() {
				try {
					Class<?> loader = Class.forName("java.lang.ClassLoader");
					DEFINE_CLASS = loader.getDeclaredMethod("defineClass", new Class[] { String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class });
					DEFINE_CLASS.setAccessible(true);
				} catch (ClassNotFoundException e) {
					throw new SrvException(ErrorCode.Srv_Bean_Resolver_Error, "Init defineClass Method Error.", e);
				} catch (NoSuchMethodException e) {
					throw new SrvException(ErrorCode.Srv_Bean_Resolver_Error, "Init defineClass Method Error.", e);
				}
				return null;
			}
		});
	}

	public static PropertyDescriptor[] getPropertyDescriptors(final Class<?> type) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(type, Object.class);
			PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
			List<PropertyDescriptor> properties = new ArrayList<PropertyDescriptor>(props.length);
			for (int i = 0; i < props.length; i++) {
				PropertyDescriptor pd = props[i];
				if (pd.getReadMethod() != null && pd.getWriteMethod() != null) {
					properties.add(pd);
				}
			}
			return properties.toArray(new PropertyDescriptor[properties.size()]);
		} catch (IntrospectionException e) {
			throw new SrvException(ErrorCode.Srv_Bean_Resolver_Error, "Introspector Type " + type + " Error.", e);
		}
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

	public static Method findDeclaredMethod(final Class<?> type, final String methodName, final Class<?>[] parameterTypes) throws NoSuchMethodException {
		Class<?> cl = type;
		while (cl != null) {
			try {
				return cl.getDeclaredMethod(methodName, parameterTypes);
			} catch (NoSuchMethodException e) {
				cl = cl.getSuperclass();
			}
		}
		throw new NoSuchMethodException(methodName);
	}

	public static Class<?> defineClass(String className, byte[] b, ClassLoader loader) throws Exception {
		Object[] args = new Object[] { className, b, new Integer(0), new Integer(b.length), PROTECTION_DOMAIN };
		return (Class<?>) DEFINE_CLASS.invoke(loader, args);
	}

	public static Object newInstance(String className, ClassLoader cl) throws Throwable {
		if (className == null)
			throw new IllegalArgumentException("Null class name");
		if (cl == null)
			throw new IllegalArgumentException("Null classloader");
		Class<?> clazz = Class.forName(className, false, cl);
		try {
			return clazz.newInstance();
		} catch (Throwable t) {
			throw new SrvException(ErrorCode.Srv_Bean_Resolver_Error, "newInstance Type " + className + " Error.", t);
		}
	}

	public static Object newInstance(String className) throws Throwable {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		return newInstance(className, cl);
	}
}