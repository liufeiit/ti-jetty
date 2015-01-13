package com.ovea.jetty.session.serializer.jboss.serial.util;

public class StringUtilBuffer {

	/* A way to pass an integer as a parameter to a method */
	public static class Position {
		int pos;
		long size;

		public Position reset() {
			pos = 0;
			size = 0;
			return this;
		}
	}

	Position position = new Position();

	public char charBuffer[];
	public byte byteBuffer[];

	public void resizeCharBuffer(int newSize) {
		if (newSize <= charBuffer.length) {
			throw new RuntimeException("New buffer can't be smaller");
		}
		char[] newCharBuffer = new char[newSize];
		for (int i = 0; i < charBuffer.length; i++) {
			newCharBuffer[i] = charBuffer[i];
		}
		charBuffer = newCharBuffer;
	}

	public void resizeByteBuffer(int newSize) {
		if (newSize <= byteBuffer.length) {
			throw new RuntimeException("New buffer can't be smaller");
		}
		byte[] newByteBuffer = new byte[newSize];
		for (int i = 0; i < byteBuffer.length; i++) {
			newByteBuffer[i] = byteBuffer[i];
		}
		byteBuffer = newByteBuffer;
	}

	public StringUtilBuffer() {
		this(1024, 1024);
	}

	public StringUtilBuffer(int sizeChar, int sizeByte) {
		charBuffer = new char[sizeChar];
		byteBuffer = new byte[sizeByte];
	}

}
