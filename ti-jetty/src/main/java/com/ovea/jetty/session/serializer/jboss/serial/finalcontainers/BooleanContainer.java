package com.ovea.jetty.session.serializer.jboss.serial.finalcontainers;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Field;

public class BooleanContainer extends FinalContainer {
	static private BooleanContainer TRUE = new BooleanContainer(true);
	static private BooleanContainer FALSE = new BooleanContainer(false);

	boolean value;

	static public BooleanContainer valueOf(boolean value) {
		return value ? TRUE : FALSE;
	}

	private BooleanContainer(boolean value) {
		this.value = value;
	}

	public boolean getValue() {
		return value;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		final BooleanContainer that = (BooleanContainer) o;

		if (value != that.value)
			return false;

		return true;
	}

	public int hashCode() {
		return (value ? 1 : 0);
	}

	public void writeMyself(DataOutput output) throws IOException {
		output.writeBoolean(value);
	}

	public void readMyself(DataInput input) throws IOException {
		value = input.readBoolean();
	}

	public void setPrimitive(Object obj, Field field) throws IllegalAccessException {
		field.setBoolean(obj, value);
	}

}
