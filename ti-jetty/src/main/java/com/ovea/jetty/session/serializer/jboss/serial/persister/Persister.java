package com.ovea.jetty.session.serializer.jboss.serial.persister;

import com.ovea.jetty.session.serializer.jboss.serial.classmetamodel.ClassMetaData;
import com.ovea.jetty.session.serializer.jboss.serial.classmetamodel.StreamingClass;
import com.ovea.jetty.session.serializer.jboss.serial.objectmetamodel.ObjectSubstitutionInterface;
import com.ovea.jetty.session.serializer.jboss.serial.objectmetamodel.ObjectsCache;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public interface Persister {
	/**
	 * You need to always return what was sent by setId. This is to enable
	 * Streaming to discover what Persister to use
	 */
	public byte getId();

	public void setId(byte id);

	public void writeData(ClassMetaData metaData, ObjectOutput out, Object obj, ObjectSubstitutionInterface substitution) throws IOException;

	/**
	 * @param loader
	 * @param metaData
	 * @param referenceId
	 * @param cache
	 *            It's the persister job to assign the cache with a created
	 *            object, right after its creation, as if in case of circular
	 *            references those references are respected.
	 * @param input
	 * @param substitution
	 * @return
	 * @throws java.io.IOException
	 */
	public Object readData(ClassLoader loader, StreamingClass streaming, ClassMetaData metaData, int referenceId, ObjectsCache cache, ObjectInput input, ObjectSubstitutionInterface substitution)
			throws IOException;

	/**
	 * Ask the persister if the persister can handle this object
	 */
	public boolean canPersist(Object obj);
}
