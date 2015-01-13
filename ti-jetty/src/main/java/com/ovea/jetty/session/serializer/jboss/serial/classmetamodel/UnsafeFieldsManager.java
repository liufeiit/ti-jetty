package com.ovea.jetty.session.serializer.jboss.serial.classmetamodel;

import sun.misc.Unsafe;

import java.io.ObjectStreamClass;
import java.lang.reflect.Field;

public class UnsafeFieldsManager extends FieldsManager {
	UnsafeFieldsManager() {
	}

	static Unsafe unsafe;

	public static boolean isSupported() {
		return unsafe != null;
	}

	static {
		try {
			Class[] classes = ObjectStreamClass.class.getDeclaredClasses();
			for (int i = 0; i < classes.length; i++) {
				if (classes[i].getName().equals("java.io.ObjectStreamClass$FieldReflector")) {
					Field unsafeField = classes[i].getDeclaredField("unsafe");
					unsafeField.setAccessible(true);

					unsafe = (Unsafe) unsafeField.get(null);
					break;
				}
			}
		} catch (Throwable e) {
			// e.printStackTrace()
		}
	}

	public void fillMetadata(ClassMetadataField field) {
		if (field.getField() != null) {
			field.setUnsafeKey(unsafe.objectFieldOffset(field.getField()));
		}
	}

	public void setInt(Object obj, ClassMetadataField field, int value) {
		unsafe.putInt(obj, field.getUnsafeKey(), value);
	}

	public int getInt(Object obj, ClassMetadataField field) {
		return unsafe.getInt(obj, field.getUnsafeKey());
	}

	public void setByte(Object obj, ClassMetadataField field, byte value) {
		unsafe.putByte(obj, field.getUnsafeKey(), value);
	}

	public byte getByte(Object obj, ClassMetadataField field) {
		return unsafe.getByte(obj, field.getUnsafeKey());
	}

	public void setLong(Object obj, ClassMetadataField field, long value) {
		unsafe.putLong(obj, field.getUnsafeKey(), value);
	}

	public long getLong(Object obj, ClassMetadataField field) {
		return unsafe.getLong(obj, field.getUnsafeKey());
	}

	public void setFloat(Object obj, ClassMetadataField field, float value) {
		unsafe.putFloat(obj, field.getUnsafeKey(), value);
	}

	public float getFloat(Object obj, ClassMetadataField field) {
		return unsafe.getFloat(obj, field.getUnsafeKey());
	}

	public void setDouble(Object obj, ClassMetadataField field, double value) {
		unsafe.putDouble(obj, field.getUnsafeKey(), value);
	}

	public double getDouble(Object obj, ClassMetadataField field) {
		return unsafe.getDouble(obj, field.getUnsafeKey());
	}

	public void setShort(Object obj, ClassMetadataField field, short value) {
		unsafe.putShort(obj, field.getUnsafeKey(), value);
	}

	public short getShort(Object obj, ClassMetadataField field) {
		return unsafe.getShort(obj, field.getUnsafeKey());
	}

	public void setCharacter(Object obj, ClassMetadataField field, char value) {
		unsafe.putChar(obj, field.getUnsafeKey(), value);
	}

	public char getCharacter(Object obj, ClassMetadataField field) {
		return unsafe.getChar(obj, field.getUnsafeKey());
	}

	public void setBoolean(Object obj, ClassMetadataField field, boolean value) {
		unsafe.putBoolean(obj, field.getUnsafeKey(), value);
	}

	public boolean getBoolean(Object obj, ClassMetadataField field) {
		return unsafe.getBoolean(obj, field.getUnsafeKey());
	}

	public void setObject(Object obj, ClassMetadataField field, Object value) {
		unsafe.putObject(obj, field.getUnsafeKey(), value);
	}

	public Object getObject(Object obj, ClassMetadataField field) {
		return unsafe.getObject(obj, field.getUnsafeKey());
	}

}
