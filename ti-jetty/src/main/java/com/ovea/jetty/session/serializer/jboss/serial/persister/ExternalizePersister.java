package com.ovea.jetty.session.serializer.jboss.serial.persister;

import com.ovea.jetty.session.serializer.jboss.serial.classmetamodel.ClassMetaData;
import com.ovea.jetty.session.serializer.jboss.serial.classmetamodel.StreamingClass;
import com.ovea.jetty.session.serializer.jboss.serial.exception.SerializationException;
import com.ovea.jetty.session.serializer.jboss.serial.objectmetamodel.ObjectSubstitutionInterface;
import com.ovea.jetty.session.serializer.jboss.serial.objectmetamodel.ObjectsCache;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class ExternalizePersister implements Persister {
	byte id;

	public byte getId() {
		return id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	public void writeData(ClassMetaData metaData, ObjectOutput out, Object obj, ObjectSubstitutionInterface substitution) throws IOException {
		((Externalizable) obj).writeExternal(out);
	}

	public Object readData(ClassLoader loader, StreamingClass streaming, ClassMetaData metaData, int referenceId, ObjectsCache cache, ObjectInput input, ObjectSubstitutionInterface substitution)
			throws IOException {

		Object obj = metaData.newInstance();
		cache.putObjectInCacheRead(referenceId, obj);

		try {
			((Externalizable) obj).readExternal(input);
		} catch (ClassNotFoundException e) {
			throw new SerializationException(e);
		}

		return obj;
	}

	public boolean canPersist(Object obj) {
		return false;
	}
}
