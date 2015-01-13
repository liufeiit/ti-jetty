package com.ovea.jetty.session.serializer.jboss.serial.classmetamodel;

import com.ovea.jetty.session.serializer.jboss.serial.references.FieldPersistentReference;
import com.ovea.jetty.session.serializer.jboss.serial.util.HashStringUtil;

import java.lang.reflect.Field;

public class ClassMetadataField {
	public ClassMetadataField(Field field) {
		this.setField(field);
		this.setFieldName(field.getName());
		this.shaHash = HashStringUtil.hashName(field.getType().getName() + "$" + field.getName());
		this.setObject(!ClassMetamodelFactory.isImmutable(field.getType()));
	}

	String fieldName;

	FieldPersistentReference field;

	/**
	 * Used only by {@link UnsafeFieldsManager}
	 */
	long unsafeKey;

	boolean isObject;

	long shaHash;

	/**
	 * Order the field appears on the slot
	 */
	short order;

	/**
	 * @return Returns the field.
	 */
	public Field getField() {
		return (Field) field.get();
	}

	/**
	 * @param field
	 *            The field to set.
	 */
	public void setField(Field afield) {
		this.field = new FieldPersistentReference(afield, ClassMetaData.REFERENCE_TYPE_IN_USE);
	}

	/**
	 * @return Returns the fieldName.
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @param fieldName
	 *            The fieldName to set.
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return Returns the isObject.
	 */
	public boolean isObject() {
		return isObject;
	}

	/**
	 * @param isObject
	 *            The isObject to set.
	 */
	public void setObject(boolean isObject) {
		this.isObject = isObject;
	}

	public long getUnsafeKey() {
		return unsafeKey;
	}

	public void setUnsafeKey(long unsafeKey) {
		this.unsafeKey = unsafeKey;
	}

	public long getShaHash() {
		return shaHash;
	}

	public void setShaHash(long shaHash) {
		this.shaHash = shaHash;
	}

	public short getOrder() {
		return order;
	}

	public void setOrder(short order) {
		this.order = order;
	}

}
