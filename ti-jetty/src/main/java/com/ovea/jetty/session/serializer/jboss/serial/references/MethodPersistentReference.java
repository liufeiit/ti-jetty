package com.ovea.jetty.session.serializer.jboss.serial.references;

import java.lang.reflect.Method;

public class MethodPersistentReference extends ArgumentPersistentReference {
	public MethodPersistentReference(Method method, int referenceType) {
		super(method != null ? method.getDeclaringClass() : null, method, referenceType);
		if (method != null) {
			this.name = method.getName();
			setArguments(method.getParameterTypes());
		}
	}

	String name;

	public synchronized Object rebuildReference() throws Exception {
		// A reference to guarantee the value is not being GCed during while the
		// value is being rebuilt
		Object returnValue = null;
		if ((returnValue = internalGet()) != null)
			return returnValue;

		Method aMethod = getMappedClass().getDeclaredMethod(name, getArguments());
		aMethod.setAccessible(true);
		buildReference(aMethod);
		return aMethod;
	}

	public Method getMethod() {
		return (Method) get();
	}

}
