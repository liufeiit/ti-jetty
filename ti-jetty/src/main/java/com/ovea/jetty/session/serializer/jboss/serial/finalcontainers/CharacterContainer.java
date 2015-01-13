package com.ovea.jetty.session.serializer.jboss.serial.finalcontainers;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Field;

public class CharacterContainer extends FinalContainer {
	char value;

	public CharacterContainer(char value) {
		this.value = value;
	}

	public CharacterContainer() {
	}

	public char getValue() {
		return value;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		final CharacterContainer that = (CharacterContainer) o;

		if (value != that.value)
			return false;

		return true;
	}

	public int hashCode() {
		return (int) value;
	}

	public void writeMyself(DataOutput output) throws IOException {
		output.writeChar(value);
	}

	public void readMyself(DataInput input) throws IOException {
		value = input.readChar();
	}

	public void setPrimitive(Object obj, Field field) throws IllegalAccessException {
		field.setChar(obj, value);
	}
}
