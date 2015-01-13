package com.ovea.jetty.session.serializer.jboss.serial.references;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public abstract class PersistentReference {
	public static final int REFERENCE_WEAK = 1;
	public static final int REFERENCE_SOFT = 2;

	private WeakReference classReference;
	private Reference referencedObject;
	private int referenceType = 0;

	/**
	 * @param clazz
	 *            The clazz being used on this object (where we will do
	 *            reflection operations)
	 * @param referencedObject
	 *            The reflection object being used
	 * @param referenceType
	 *            if REFERENCE_WEAK will use a WeakReference, and if
	 *            REFERENCE_SOFT will use a SoftReference for referencedObject
	 */
	public PersistentReference(Class clazz, Object referencedObject, int referenceType) {
		this.referenceType = referenceType;
		if (clazz != null) {
			classReference = new WeakReference(clazz);
		}
		buildReference(referencedObject);
	}

	/**
	 * Checks the reference but doesn't perform rebuild if empty
	 */
	protected Object internalGet() {
		if (referencedObject == null)
			return null;

		return referencedObject.get();

	}

	public Object get() {
		if (referencedObject == null)
			return null;

		Object returnValue = referencedObject.get();
		if (returnValue == null) {
			try {
				// Return ths value straight from the rebuild, to guarantee the
				// value is not destroyed if a GC happens during the rebuild
				// reference
				return rebuildReference();
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}

		return returnValue;
	}

	public abstract Object rebuildReference() throws Exception;

	/**
	 * This method should be called from a synchronized block
	 */
	protected void buildReference(Object obj) {
		if (obj == null) {
			referencedObject = null;
		} else {
			if (referenceType == REFERENCE_WEAK) {
				referencedObject = new WeakReference(obj);
			} else {
				referencedObject = new SoftReference(obj);
			}
		}
	}

	public Class getMappedClass() {
		if (classReference == null)
			return null;
		Class returnClass = (Class) classReference.get();
		if (returnClass == null) {
			throw new RuntimeException("Class was already unloaded");
		}
		return (Class) returnClass;
	}

}
