package com.ovea.jetty.session.serializer.jboss.serial.finalcontainers;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Field;

public class LongContainer extends FinalContainer {
	long value;

	public LongContainer(long value) {
		this.value = value;
	}

	public LongContainer() {
	}

	public long getValue() {
		return value;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		final LongContainer that = (LongContainer) o;

		if (value != that.value)
			return false;

		return true;
	}

	public int hashCode() {
		return (int) (value ^ (value >>> 32));
	}

	public void writeMyself(DataOutput output) throws IOException {
		output.writeLong(value);
	}

	public void readMyself(DataInput input) throws IOException {
		value = input.readLong();
	}

	public void setPrimitive(Object obj, Field field) throws IllegalAccessException {
		field.setLong(obj, value);
	}

}
