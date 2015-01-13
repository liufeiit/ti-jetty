package com.ovea.jetty.session.serializer.jboss.serial.classmetamodel;

import com.ovea.jetty.session.serializer.jboss.serial.objectmetamodel.ObjectsCache;
import com.ovea.jetty.session.serializer.jboss.serial.objectmetamodel.ObjectsCache.JBossSeralizationInputInterface;

import java.io.IOException;

public interface ClassDescriptorStrategy {
	void writeClassDescription(Object obj, ClassMetaData metaData, ObjectsCache cache, int description) throws IOException;

	StreamingClass readClassDescription(ObjectsCache cache, JBossSeralizationInputInterface input, ClassResolver classResolver, String className) throws IOException;
}
