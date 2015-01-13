package com.ovea.jetty.session.serializer.jboss.serial.classmetamodel;

import java.lang.reflect.Constructor;

public class DefaultConstructorManager extends ConstructorManager {

	public Constructor getConstructor(Class clazz) throws SecurityException, NoSuchMethodException {
		Constructor constr = clazz.getDeclaredConstructor(EMPTY_CLASS_ARRY);
		constr.setAccessible(true);
		return constr;
	}

	public boolean isSupported() {
		return true;
	}
}
