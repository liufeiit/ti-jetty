package com.ovea.jetty.session.serializer.jboss.serial.classmetamodel;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class StreamingClass {
	public StreamingClass(Class clazz) throws IOException {
		metadata = ClassMetamodelFactory.getClassMetaData(clazz, false);
	}

	public StreamingClass(ClassMetaData clazz) throws IOException {
		metadata = clazz;
	}

	ClassMetaData metadata;

	/**
	 * Position the fields used by this StreamingClass on Read. When a
	 * StreamingClass is read from a Streaming, an older version could have a
	 * different order on fields. This array is used to manage that
	 */
	private short[][] keyFields;

	public static void saveStream(ClassMetaData metadata, ObjectOutput out) throws IOException {
		ClassMetaDataSlot slots[] = metadata.getSlots();
		out.writeShort(slots.length);
		for (int slotNR = 0; slotNR < slots.length; slotNR++) {
			out.writeLong(slots[slotNR].getShaHash());
			ClassMetadataField[] fields = slots[slotNR].getFields();
			out.writeShort(fields.length);
			for (int fieldNR = 0; fieldNR < fields.length; fieldNR++) {
				out.writeLong(fields[fieldNR].getShaHash());
			}
		}
	}

	public static StreamingClass readStream(ObjectInput inp, ClassResolver resolver, ClassLoader loader, String className) throws IOException {
		ClassMetaData metadata = ClassMetamodelFactory.getClassMetaData(className, resolver, loader, false);
		StreamingClass streamClass = new StreamingClass(metadata);

		// number of slots on streaming
		short slotsNrOnStreaming = inp.readShort();
		ClassMetaDataSlot slots[] = metadata.getSlots();
		if (slotsNrOnStreaming != slots.length) {
			throw new IOException("The hierarchy of " + className + " is different in your current classPath");
		}

		short[][] keyfields = new short[slots.length][];

		for (int slotIndex = 0; slotIndex < slots.length; slotIndex++) {
			long shaSlotHash = inp.readLong();
			if (slots[slotIndex].getShaHash() != shaSlotHash) {
				throw new IOException("The hierarchy of " + className + " is different in your current classPath");
			}
			short numberofFields = inp.readShort();
			keyfields[slotIndex] = new short[numberofFields];

			ClassMetadataField fields[] = slots[slotIndex].getFields();
			if (numberofFields > fields.length) {
				throw new IOException("Current classpath has lesser fields on " + className + " than its original version");
			}
			for (short fieldIndex = 0; fieldIndex < fields.length; fieldIndex++) {
				long hashfield = inp.readLong();
				ClassMetadataField fieldOnHash = slots[slotIndex].getField(hashfield);
				if (fieldOnHash == null) {
					throw new IOException("Field hash " + fieldOnHash + " is not available on current classPath for class " + className);
				}
				keyfields[slotIndex][fieldIndex] = fieldOnHash.getOrder();
			}
		}

		streamClass.keyFields = keyfields;

		return streamClass;
	}

	public short[][] getKeyFields() {
		return keyFields;
	}

	public void setKeyFields(short[][] keyFields) {
		this.keyFields = keyFields;
	}

	public ClassMetaData getMetadata() {
		return metadata;
	}

	public void setMetadata(ClassMetaData metadata) {
		this.metadata = metadata;
	}

}
