package com.ovea.jetty.session.serializer.jboss.serial.persister;

import com.ovea.jetty.session.serializer.jboss.serial.classmetamodel.ClassMetaData;
import com.ovea.jetty.session.serializer.jboss.serial.classmetamodel.StreamingClass;
import com.ovea.jetty.session.serializer.jboss.serial.exception.SerializationException;
import com.ovea.jetty.session.serializer.jboss.serial.objectmetamodel.ObjectSubstitutionInterface;
import com.ovea.jetty.session.serializer.jboss.serial.objectmetamodel.ObjectsCache;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

public class ProxyPersister implements Persister {
	private byte id;

	public byte getId() {
		return id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	public void writeData(ClassMetaData metaData, ObjectOutput output, Object obj, ObjectSubstitutionInterface substitution) throws IOException {
		Object handler = Proxy.getInvocationHandler(obj);

		output.writeObject(handler);
		output.writeObject(obj.getClass());
	}

	public Object readData(ClassLoader loader, StreamingClass streaming, ClassMetaData metaData, int referenceId, ObjectsCache cache, ObjectInput input, ObjectSubstitutionInterface substitution)
			throws IOException {

		try {
			Object handler = input.readObject();
			Class proxy = (Class) input.readObject();
			Constructor constructor = proxy.getConstructor(new Class[] { InvocationHandler.class });
			Object obj = constructor.newInstance(new Object[] { handler });
			cache.putObjectInCacheRead(referenceId, obj);
			return obj;
		} catch (ClassNotFoundException e) {
			throw new SerializationException(e);
		} catch (NoSuchMethodException e) {
			throw new SerializationException(e);
		} catch (IllegalAccessException e) {
			throw new SerializationException(e);
		} catch (InstantiationException e) {
			throw new SerializationException(e);
		} catch (InvocationTargetException e) {
			throw new SerializationException(e);
		}
	}

	public boolean canPersist(Object obj) {
		// not implemented
		return false;
	}

}
