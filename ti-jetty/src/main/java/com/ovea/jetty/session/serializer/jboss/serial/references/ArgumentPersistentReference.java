package com.ovea.jetty.session.serializer.jboss.serial.references;

import java.lang.ref.WeakReference;

public abstract class ArgumentPersistentReference extends PersistentReference {
	public ArgumentPersistentReference(Class clazz, Object referencedObject, int referenceType) {
		super(clazz, referencedObject, referenceType);
	}

	WeakReference[] arguments;

	public void setArguments(Class[] parguments) {
		this.arguments = new WeakReference[parguments.length];
		for (int i = 0; i < arguments.length; i++) {
			this.arguments[i] = new WeakReference(parguments[i]);
		}
	}

	public Class[] getArguments() {
		if (arguments == null) {
			return null;
		} else {
			Class argumentsReturn[] = new Class[arguments.length];
			for (int i = 0; i < arguments.length; i++) {
				argumentsReturn[i] = (Class) arguments[i].get();
			}
			return argumentsReturn;
		}
	}

}
