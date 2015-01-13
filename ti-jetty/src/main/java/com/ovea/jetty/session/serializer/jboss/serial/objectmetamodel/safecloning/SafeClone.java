package com.ovea.jetty.session.serializer.jboss.serial.objectmetamodel.safecloning;

public interface SafeClone {
	public boolean isSafeToReuse(Object obj);
}
