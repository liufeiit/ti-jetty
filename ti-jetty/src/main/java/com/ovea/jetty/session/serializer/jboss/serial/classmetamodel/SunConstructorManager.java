package com.ovea.jetty.session.serializer.jboss.serial.classmetamodel;

import sun.reflect.ReflectionFactory;

import java.io.Externalizable;
import java.io.Serializable;
import java.lang.reflect.Constructor;
public class SunConstructorManager extends ConstructorManager {
	static boolean supported = true;

	static {
		try {
			reflectionFactory = ReflectionFactory.getReflectionFactory();
		} catch (Throwable e) {
			e.printStackTrace();
			supported = false;
		}
	}

	static ReflectionFactory reflectionFactory;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.serial.classmetamodel.ConstructorManager#getConstructor(java
	 * .lang.Class)
	 */
	public Constructor getConstructor(Class clazz) throws SecurityException, NoSuchMethodException {
		if (clazz.isInterface()) {
			throw new NoSuchMethodException("Can't create a constructor for a pure interface");
		} else if (!Serializable.class.isAssignableFrom(clazz)) {
			Constructor constr = clazz.getDeclaredConstructor(EMPTY_CLASS_ARRY);
			constr.setAccessible(true);
			return constr;
		} else if (Externalizable.class.isAssignableFrom(clazz)) {
			Constructor constr = clazz.getConstructor(EMPTY_CLASS_ARRY);
			constr.setAccessible(true);
			return constr;
		} else {
			Class currentClass = clazz;
			while (Serializable.class.isAssignableFrom(currentClass)) {
				currentClass = currentClass.getSuperclass();
			}
			Constructor constr = currentClass.getDeclaredConstructor(EMPTY_CLASS_ARRY);
			constr.setAccessible(true);

			// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6220682
			Constructor newConstructor = reflectionFactory.newConstructorForSerialization(clazz, constr);
			newConstructor.setAccessible(true);

			return newConstructor;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.serial.classmetamodel.ConstructorManager#isSupported()
	 */
	public boolean isSupported() {
		return supported;
	}

}
