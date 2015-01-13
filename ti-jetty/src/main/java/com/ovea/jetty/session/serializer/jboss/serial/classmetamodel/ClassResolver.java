package com.ovea.jetty.session.serializer.jboss.serial.classmetamodel;

public interface ClassResolver {

	public Class resolveClass(String name) throws ClassNotFoundException;

}
