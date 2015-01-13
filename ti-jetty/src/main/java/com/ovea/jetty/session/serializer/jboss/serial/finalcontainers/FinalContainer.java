package com.ovea.jetty.session.serializer.jboss.serial.finalcontainers;

import com.ovea.jetty.session.serializer.jboss.serial.objectmetamodel.DataExport;

import java.lang.reflect.Field;

public abstract class FinalContainer extends DataExport {
	public abstract void setPrimitive(Object obj, Field field) throws IllegalAccessException;
}
