package com.ovea.jetty.session.serializer.jboss.serial.references;

import java.lang.reflect.Constructor;

public class ConstructorPersistentReference extends ArgumentPersistentReference {

	public ConstructorPersistentReference(Class clazz, Object referencedObject, int referenceType) {
		super(clazz, referencedObject, referenceType);
		this.setArguments(((Constructor) referencedObject).getParameterTypes());
	}

	public synchronized Object rebuildReference() throws Exception {
		// A reference to guarantee the value is not being GCed during while the
		// value is being rebuilt
		Object returnValue = null;
		if ((returnValue = internalGet()) != null)
			return returnValue;

		Constructor constructor = getMappedClass().getConstructor(getArguments());
		constructor.setAccessible(true);
		buildReference(constructor);
		return constructor;
	}

}
