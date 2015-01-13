package com.ovea.jetty.session.serializer.jboss.serial.classmetamodel;

import com.ovea.jetty.session.serializer.jboss.serial.util.ClassMetaConsts;

import java.lang.reflect.Constructor;

public abstract class ConstructorManager implements ClassMetaConsts {
	public abstract Constructor getConstructor(Class clazz) throws SecurityException, NoSuchMethodException;

	public abstract boolean isSupported();
}
