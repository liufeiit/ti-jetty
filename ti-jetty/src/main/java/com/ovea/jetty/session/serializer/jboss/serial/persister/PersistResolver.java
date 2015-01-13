package com.ovea.jetty.session.serializer.jboss.serial.persister;

import com.ovea.jetty.session.serializer.jboss.serial.classmetamodel.ClassMetaData;

public class PersistResolver {
	static Persister arrayPersister = new ArrayPersister();
	static ExternalizePersister externalizePersister = new ExternalizePersister();
	static RegularObjectPersister defaultPersister = new RegularObjectPersister();
	static ClassReferencePersister classReferencePersister = new ClassReferencePersister();
	static ProxyPersister proxyPersister = new ProxyPersister();
	static Persister enumPersister = null;

	static {
		try {
			Class enumClass = Class.forName("java.lang.Enum");
			if (enumClass != null) {
				try {
					enumPersister = (Persister) PersistResolver.class.getClassLoader().loadClass("com.ovea.jetty.session.serializer.jboss.serial.persister.EnumerationPersister").newInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Throwable e) {

		}
	}

	static {
		defaultPersister.setId((byte) 1);
		arrayPersister.setId((byte) 2);
		externalizePersister.setId((byte) 3);
		classReferencePersister.setId((byte) 5);
		proxyPersister.setId((byte) 6);
		if (enumPersister != null) {
			enumPersister.setId((byte) 7);
		}
	}

	public static Persister resolvePersister(byte id) {
		switch (id) {
		case 1:
			return defaultPersister;
		case 2:
			return arrayPersister;
		case 3:
			return externalizePersister;
		case 4:
			throw new RuntimeException("This persister is not valid any more");
		case 5:
			return classReferencePersister;
		case 6:
			return proxyPersister;
		case 7:
			if (enumPersister == null) {
				throw new RuntimeException("This current VM doesn't support Enumerations");
			}
			return enumPersister;
		default:
			return defaultPersister;
		}
	}

	public static Persister resolvePersister(Object objToBeSerialized, ClassMetaData metaData) {
		if (metaData.isArray() && (!(objToBeSerialized instanceof Class))) {
			return arrayPersister;
		} else if (objToBeSerialized instanceof Class || metaData.getClazz() == Class.class) {
			return classReferencePersister;
		} else if (metaData.isExternalizable()) {
			return externalizePersister;
		}
		if (metaData.isProxy()) {
			return proxyPersister;
		} else if (enumPersister != null) {
			if (enumPersister.canPersist(objToBeSerialized)) {
				return enumPersister;
			} else {
				return defaultPersister;
			}
		} else {
			return defaultPersister;
		}
	}
}
