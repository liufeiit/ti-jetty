package com.ovea.jetty.session.serializer.jboss.serial.references;

import java.lang.reflect.Field;

public class FieldPersistentReference extends PersistentReference {
	public FieldPersistentReference(Field field, int referenceType) {
		super(field != null ? field.getDeclaringClass() : null, field, referenceType);
		this.name = field.getName();
	}

	String name;

	public synchronized Object rebuildReference() throws Exception {
		// A reference to guarantee the value is not being GCed during while the
		// value is being rebuilt
		Object returnValue = null;
		if ((returnValue = internalGet()) != null)
			return returnValue;

		Field field = getMappedClass().getDeclaredField(name);
		field.setAccessible(true);
		buildReference(field);
		return field;
	}

	public Field getField() {
		return (Field) get();
	}
}
