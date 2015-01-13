package com.ovea.jetty.session.serializer.jboss.serial.io;

import com.ovea.jetty.session.serializer.jboss.serial.objectmetamodel.DataContainer;
import com.ovea.jetty.session.serializer.jboss.serial.objectmetamodel.DataContainerConstants;
import com.ovea.jetty.session.serializer.jboss.serial.util.StringUtilBuffer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.OutputStream;

public class JBossObjectOutputStreamSharedTree extends JBossObjectOutputStream {
	protected ObjectOutput objectOutput;
	DataContainer dataContainer;

	public JBossObjectOutputStreamSharedTree(OutputStream output, boolean checkSerializableClass, StringUtilBuffer buffer) throws IOException {
		super(output, checkSerializableClass, buffer);
	}

	public JBossObjectOutputStreamSharedTree(OutputStream output, boolean checkSerializableClass) throws IOException {
		super(output, checkSerializableClass);
	}

	public JBossObjectOutputStreamSharedTree(OutputStream output, StringUtilBuffer buffer) throws IOException {
		super(output, buffer);
	}

	public JBossObjectOutputStreamSharedTree(OutputStream output) throws IOException {
		super(output);
	}

	protected void writeObjectOverride(Object obj) throws IOException {
		checkOutput();
		objectOutput.writeObject(obj);
	}

	public void reset() throws IOException {
		checkOutput();
		objectOutput.writeByte(DataContainerConstants.RESET);
		dataContainer.getCache().reset();
	}

	private void checkOutput() throws IOException {
		if (objectOutput == null) {
			dataContainer = new DataContainer(null, this.getSubstitutionInterface(), checkSerializableClass, buffer, classDescriptorStrategy, objectDescriptorStrategy);
			if (output instanceof DataOutputStream) {
				dataOutput = (DataOutputStream) output;
			} else {
				dataOutput = new DataOutputStream(output);
			}

			objectOutput = dataContainer.getDirectOutput(this.dataOutput);
		}
	}

}
