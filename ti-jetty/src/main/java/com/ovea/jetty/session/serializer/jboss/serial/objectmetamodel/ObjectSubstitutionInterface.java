package com.ovea.jetty.session.serializer.jboss.serial.objectmetamodel;

import java.io.IOException;

public interface ObjectSubstitutionInterface {
	public Object replaceObject(Object obj) throws IOException;
}
