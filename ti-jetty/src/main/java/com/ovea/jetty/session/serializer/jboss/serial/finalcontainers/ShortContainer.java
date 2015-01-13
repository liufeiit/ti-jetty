package com.ovea.jetty.session.serializer.jboss.serial.finalcontainers;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Field;

public class ShortContainer extends FinalContainer {
	short value;

	public ShortContainer(short value) {
		this.value = value;
	}

	public ShortContainer() {
	}

	public short getValue() {
		return value;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		final ShortContainer that = (ShortContainer) o;

		if (value != that.value)
			return false;

		return true;
	}

	public int hashCode() {
		return (int) value;
	}

	public void writeMyself(DataOutput output) throws IOException {
		output.writeShort(value);
	}

	public void readMyself(DataInput input) throws IOException {
		value = input.readShort();
	}

	public void setPrimitive(Object obj, Field field) throws IllegalAccessException {
		field.setShort(obj, value);
	}
}
