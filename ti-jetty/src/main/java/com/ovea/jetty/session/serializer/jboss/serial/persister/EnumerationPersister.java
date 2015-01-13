package com.ovea.jetty.session.serializer.jboss.serial.persister;

import com.ovea.jetty.session.serializer.jboss.serial.classmetamodel.ClassMetaData;
import com.ovea.jetty.session.serializer.jboss.serial.classmetamodel.ClassMetamodelFactory;
import com.ovea.jetty.session.serializer.jboss.serial.classmetamodel.StreamingClass;
import com.ovea.jetty.session.serializer.jboss.serial.objectmetamodel.ObjectSubstitutionInterface;
import com.ovea.jetty.session.serializer.jboss.serial.objectmetamodel.ObjectsCache;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class EnumerationPersister implements Persister {

	byte id;

	static Class enumClass;

	static {
		try {
			enumClass = Class.forName("java.lang.Enum");
		} catch (Throwable e) {
		}
	}

	public byte getId() {
		return id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	/**
	 * @todo is it needed to get another metadata here. need to verify
	 */
	public void writeData(ClassMetaData metaData, ObjectOutput out, Object obj, ObjectSubstitutionInterface substitution) throws IOException {
		Enum aEnum = (Enum) obj;
		out.writeUTF(aEnum.getDeclaringClass().getName());
		out.writeUTF(aEnum.name());
	}

	/**
	 * @todo is it needed to get another metadata here. need to verify
	 */
	public Object readData(ClassLoader loader, StreamingClass streaming, ClassMetaData nonUsedmetaData, int referenceId, ObjectsCache cache, ObjectInput input, ObjectSubstitutionInterface substitution)
			throws IOException {
		String instanceName = null;
		Class enumClass = null;
		String classEnum = input.readUTF();
		try {
			ClassMetaData enummetaData = ClassMetamodelFactory.getClassMetaData(classEnum, cache.getClassResolver(), loader, true);
			enumClass = enummetaData.getClazz();
			instanceName = input.readUTF();
			Object enumInstance = Enum.valueOf(enumClass, instanceName);
			if (enumInstance != null) {
				cache.putObjectInCacheRead(referenceId, enumInstance);
				return enumInstance;
			} else {
				throw new IOException("Enumeration " + instanceName + " not found at Enum Class " + enumClass);
			}
		} catch (Exception e) {
			IOException ioException = new IOException(e.getMessage());
			ioException.initCause(e);
			throw ioException;
		}
	}

	public boolean canPersist(Object obj) {
		if (enumClass != null) {
			return (enumClass.isAssignableFrom(obj.getClass()));
		} else {
			return false;
		}
	}

}
