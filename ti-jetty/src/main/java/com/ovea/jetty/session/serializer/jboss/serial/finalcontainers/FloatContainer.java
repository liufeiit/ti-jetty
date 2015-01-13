package com.ovea.jetty.session.serializer.jboss.serial.finalcontainers;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Field;

public class FloatContainer extends FinalContainer {
	float value;

	public FloatContainer(float value) {
		this.value = value;
	}

	public FloatContainer() {
	}

	public float getValue() {
		return value;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		final FloatContainer that = (FloatContainer) o;

		if (Float.compare(that.value, value) != 0)
			return false;

		return true;
	}

	public int hashCode() {
		return value != +0.0f ? Float.floatToIntBits(value) : 0;
	}

	public void writeMyself(DataOutput output) throws IOException {
		output.writeFloat(value);
	}

	public void readMyself(DataInput input) throws IOException {
		value = input.readFloat();
	}

	public void setPrimitive(Object obj, Field field) throws IllegalAccessException {
		field.setFloat(obj, value);
	}

}
