package com.ovea.jetty.session.serializer.jboss.serial.classmetamodel;

import java.lang.reflect.Field;

public abstract class FieldsManager {

	/**
	 * We need to test if Reflection could be used to change final fields or
	 * not. In case negative we will use UnsafeFieldsManager and this class will
	 * be used to execute this test
	 */
	private static class InternalFinalFieldTestClass {
		final int x = 0;
	}

	private static FieldsManager fieldsManager;

	static {
		if (UnsafeFieldsManager.isSupported()) {
			fieldsManager = new UnsafeFieldsManager();
		} else {
			try {
				Field fieldX = InternalFinalFieldTestClass.class.getDeclaredField("x");
				fieldX.setAccessible(true);

				InternalFinalFieldTestClass fieldTest = new InternalFinalFieldTestClass();
				fieldX.setInt(fieldTest, 33);

				fieldsManager = new ReflectionFieldsManager();

			} catch (Throwable e) {
			}
		}
		if (fieldsManager == null) {
			System.err.println("Couldn't set FieldsManager, JBoss Serialization can't work properly on this VM");
		}

	}

	public static FieldsManager getFieldsManager() {
		return fieldsManager;
	}

	public abstract void fillMetadata(ClassMetadataField field);

	public abstract void setInt(Object obj, ClassMetadataField field, int value);

	public abstract int getInt(Object obj, ClassMetadataField field);

	public abstract void setByte(Object obj, ClassMetadataField field, byte value);

	public abstract byte getByte(Object obj, ClassMetadataField field);

	public abstract void setLong(Object obj, ClassMetadataField field, long value);

	public abstract long getLong(Object obj, ClassMetadataField field);

	public abstract void setFloat(Object obj, ClassMetadataField field, float value);

	public abstract float getFloat(Object obj, ClassMetadataField field);

	public abstract void setDouble(Object obj, ClassMetadataField field, double value);

	public abstract double getDouble(Object obj, ClassMetadataField field);

	public abstract void setShort(Object obj, ClassMetadataField field, short value);

	public abstract short getShort(Object obj, ClassMetadataField field);

	public abstract void setCharacter(Object obj, ClassMetadataField field, char value);

	public abstract char getCharacter(Object obj, ClassMetadataField field);

	public abstract void setBoolean(Object obj, ClassMetadataField field, boolean value);

	public abstract boolean getBoolean(Object obj, ClassMetadataField field);

	public abstract void setObject(Object obj, ClassMetadataField field, Object value);

	public abstract Object getObject(Object obj, ClassMetadataField field);

}
