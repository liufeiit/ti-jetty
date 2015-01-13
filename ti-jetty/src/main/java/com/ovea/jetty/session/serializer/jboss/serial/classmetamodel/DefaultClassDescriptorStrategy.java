package com.ovea.jetty.session.serializer.jboss.serial.classmetamodel;

import com.ovea.jetty.session.serializer.jboss.serial.objectmetamodel.DataContainerConstants;
import com.ovea.jetty.session.serializer.jboss.serial.objectmetamodel.ObjectsCache;
import com.ovea.jetty.session.serializer.jboss.serial.objectmetamodel.ObjectsCache.JBossSeralizationInputInterface;

import java.io.IOException;

public class DefaultClassDescriptorStrategy implements ClassDescriptorStrategy {
	public void writeClassDescription(Object obj, ClassMetaData metaData, ObjectsCache cache, int description) throws IOException {
		writeClassDescription(obj, metaData, cache, description, true);
	}

	public void writeClassDescription(Object obj, ClassMetaData metaData, ObjectsCache cache, int description, boolean writeClassDescription) throws IOException {
		ObjectsCache.JBossSeralizationOutputInterface outputParent = cache.getOutput();
		int cacheId = cache.findIdInCacheWrite(metaData, false);
		if (cacheId == 0) {
			cacheId = cache.putObjectInCacheWrite(metaData, false);
			outputParent.writeByte(DataContainerConstants.NEWDEF);
			outputParent.addObjectReference(cacheId);
			if (writeClassDescription) {
				outputParent.writeUTF(metaData.getClassName());
			}
			StreamingClass.saveStream(metaData, outputParent);
		} else {
			outputParent.writeByte(DataContainerConstants.OBJECTREF);
			outputParent.addObjectReference(cacheId);
		}
	}

	public StreamingClass readClassDescription(ObjectsCache cache, JBossSeralizationInputInterface input, ClassResolver classResolver, String className) throws IOException {
		return readClassDescription(cache, input, classResolver, className, null);
	}

	public StreamingClass readClassDescription(ObjectsCache cache, JBossSeralizationInputInterface input, ClassResolver classResolver, String className, Class clazz) throws IOException {
		byte defClass = input.readByte();
		StreamingClass streamingClass = null;
		if (defClass == DataContainerConstants.NEWDEF) {
			int referenceId = input.readObjectReference();
			if (className == null) {
				className = input.readUTF();
			}

			streamingClass = StreamingClass.readStream(input, classResolver, cache.getLoader(), className);
			cache.putObjectInCacheRead(referenceId, streamingClass);
		} else {
			int referenceId = input.readObjectReference();
			streamingClass = (StreamingClass) cache.findObjectInCacheRead(referenceId);
			if (streamingClass == null) {
				throw new IOException("Didn't find StreamingClass circular refernce id=" + referenceId);
			}
		}

		return streamingClass;
	}
}
