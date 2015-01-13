package com.ovea.jetty.session.serializer.jboss.serial.finalcontainers;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Field;

public class IntegerContainer extends FinalContainer {
	int value;

	public IntegerContainer(int value) {
		this.value = value;
	}

	public IntegerContainer() {
	}

	public int getValue() {
		return value;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		final IntegerContainer that = (IntegerContainer) o;

		if (value != that.value)
			return false;

		return true;
	}

	public int hashCode() {
		return value;
	}

	public void writeMyself(DataOutput output) throws IOException {
		output.writeInt(value);
	}

	public void readMyself(DataInput input) throws IOException {
		value = input.readInt();
	}

	public void setPrimitive(Object obj, Field field) throws IllegalAccessException {
		field.setInt(obj, value);
	}

}
