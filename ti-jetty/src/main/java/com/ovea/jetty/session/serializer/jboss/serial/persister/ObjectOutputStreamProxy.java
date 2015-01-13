package com.ovea.jetty.session.serializer.jboss.serial.persister;

import com.ovea.jetty.session.serializer.jboss.serial.classmetamodel.ClassMetaDataSlot;
import com.ovea.jetty.session.serializer.jboss.serial.exception.SerializationException;
import com.ovea.jetty.session.serializer.jboss.serial.objectmetamodel.FieldsContainer;
import com.ovea.jetty.session.serializer.jboss.serial.objectmetamodel.ObjectSubstitutionInterface;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;

public class ObjectOutputStreamProxy extends ObjectOutputStream {

	Object currentObj;
	ClassMetaDataSlot currentMetaClass;
	ObjectSubstitutionInterface currentSubstitution;
	FieldsContainer currentContainer = null;

	ObjectOutput bout;

	public ObjectOutputStreamProxy(ObjectOutput output, Object currentObj, ClassMetaDataSlot currentMetaClass, ObjectSubstitutionInterface currentSubstitution) throws IOException {
		super();
		this.bout = output;
		this.currentObj = currentObj;
		this.currentMetaClass = currentMetaClass;
		this.currentSubstitution = currentSubstitution;
	}

	protected void writeObjectOverride(Object obj) throws IOException {
		bout.writeObject(obj);
	}

	public void writeUnshared(Object obj) throws IOException {
		writeObjectOverride(obj);
	}

	public void defaultWriteObject() throws IOException {
		writeFields();
	}

	public void writeFields() throws IOException {
		if (currentContainer != null) {
			currentContainer.writeMyself(this);
			currentContainer = null;
		} else {
			RegularObjectPersister.writeSlotWithFields(currentMetaClass, bout, currentObj, currentSubstitution);
		}
	}

	public void reset() throws IOException {
	}

	protected void writeStreamHeader() throws IOException {
	}

	protected void writeClassDescriptor(ObjectStreamClass desc) throws IOException {
	}

	/**
	 * Writes a byte. This method will block until the byte is actually written.
	 *
	 * @param val
	 *            the byte to be written to the stream
	 * @throws java.io.IOException
	 *             If an I/O error has occurred.
	 */
	public void write(int val) throws IOException {
		bout.write(val);
	}

	/**
	 * Writes an array of bytes. This method will block until the bytes are
	 * actually written.
	 *
	 * @param buf
	 *            the data to be written
	 * @throws java.io.IOException
	 *             If an I/O error has occurred.
	 */
	public void write(byte[] buf) throws IOException {
		bout.write(buf);
	}

	public void write(byte[] buf, int off, int len) throws IOException {
		if (buf == null) {
			throw new SerializationException("buf can't be null");
		}
		bout.write(buf, off, len);
	}

	/**
	 * Flushes the stream. This will write any buffered output bytes and flush
	 * through to the underlying stream.
	 *
	 * @throws java.io.IOException
	 *             If an I/O error has occurred.
	 */
	public void flush() throws IOException {
		bout.flush();
	}

	protected void drain() throws IOException {
		// bout.drain();
	}

	public void close() throws IOException {
		flush();
		bout.close();
	}

	public void writeBoolean(boolean val) throws IOException {
		bout.writeBoolean(val);
	}

	public void writeByte(int val) throws IOException {
		bout.writeByte(val);
	}

	public void writeShort(int val) throws IOException {
		bout.writeShort(val);
	}

	public void writeChar(int val) throws IOException {
		bout.writeChar(val);
	}

	public void writeInt(int val) throws IOException {
		bout.writeInt(val);
	}

	public void writeLong(long val) throws IOException {
		bout.writeLong(val);
	}

	public void writeFloat(float val) throws IOException {
		bout.writeFloat(val);
	}

	public void writeDouble(double val) throws IOException {
		bout.writeDouble(val);
	}

	public void writeBytes(String str) throws IOException {
		bout.writeBytes(str);
	}

	public void writeChars(String str) throws IOException {
		bout.writeChars(str);
	}

	public void writeUTF(String str) throws IOException {
		bout.writeUTF(str);
	}

	public PutField putFields() throws IOException {
		currentContainer = new FieldsContainer(currentMetaClass);
		return currentContainer.createPut();
	}

}
