package com.ovea.jetty.session.serializer.jboss.serial.finalcontainers;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Field;

public class ByteContainer extends FinalContainer {
	byte value;

	public ByteContainer(byte value) {
		this.value = value;
	}

	public ByteContainer() {
	}

	public byte getValue() {
		return value;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		final ByteContainer that = (ByteContainer) o;

		if (value != that.value)
			return false;

		return true;
	}

	public int hashCode() {
		return (int) value;
	}

	public void writeMyself(DataOutput output) throws IOException {
		output.write(value);
	}

	public void readMyself(DataInput input) throws IOException {
		value = input.readByte();
	}

	public void setPrimitive(Object obj, Field field) throws IllegalAccessException {
		field.setByte(obj, value);
	}

}
