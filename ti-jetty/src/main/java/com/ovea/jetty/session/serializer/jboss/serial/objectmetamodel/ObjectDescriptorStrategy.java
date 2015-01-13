package com.ovea.jetty.session.serializer.jboss.serial.objectmetamodel;

import com.ovea.jetty.session.serializer.jboss.serial.classmetamodel.ClassMetaData;
import com.ovea.jetty.session.serializer.jboss.serial.classmetamodel.StreamingClass;
import com.ovea.jetty.session.serializer.jboss.serial.objectmetamodel.ObjectsCache.JBossSeralizationInputInterface;
import com.ovea.jetty.session.serializer.jboss.serial.objectmetamodel.ObjectsCache.JBossSeralizationOutputInterface;

import java.io.IOException;

public interface ObjectDescriptorStrategy {
	boolean writeObjectSpecialCase(JBossSeralizationOutputInterface output, ObjectsCache cache, Object obj) throws IOException;

	boolean writeDuplicateObject(JBossSeralizationOutputInterface output, ObjectsCache cache, Object obj, ClassMetaData metaData) throws IOException;

	Object replaceObjectByClass(ObjectsCache cache, Object obj, ClassMetaData metaData) throws IOException;

	Object replaceObjectByStream(ObjectsCache cache, Object obj, ClassMetaData metaData) throws IOException;

	boolean doneReplacing(ObjectsCache cache, Object newObject, Object oldObject, ClassMetaData oldMetaData) throws IOException;

	void writeObject(JBossSeralizationOutputInterface output, ObjectsCache cache, ClassMetaData metadata, Object obj) throws IOException;

	Object readObjectSpecialCase(JBossSeralizationInputInterface input, ObjectsCache cache, byte byteIdentify) throws IOException;

	Object readObject(JBossSeralizationInputInterface input, ObjectsCache cache, StreamingClass streamingClass, int reference) throws IOException;
}
