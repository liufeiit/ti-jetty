package com.ovea.jetty.session.serializer.jboss.serial.finalcontainers;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Field;

public class DoubleContainer extends FinalContainer {
	double value;

	public DoubleContainer(double value) {
		this.value = value;
	}

	public DoubleContainer() {
	}

	public double getValue() {
		return value;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		final DoubleContainer that = (DoubleContainer) o;

		if (Double.compare(that.value, value) != 0)
			return false;

		return true;
	}

	public int hashCode() {
		final long temp = value != +0.0d ? Double.doubleToLongBits(value) : 0L;
		return (int) (temp ^ (temp >>> 32));
	}

	public void writeMyself(DataOutput output) throws IOException {
		output.writeDouble(value);
	}

	public void readMyself(DataInput input) throws IOException {
		value = input.readDouble();
	}

	public void setPrimitive(Object obj, Field field) throws IllegalAccessException {
		field.setDouble(obj, value);
	}

}
