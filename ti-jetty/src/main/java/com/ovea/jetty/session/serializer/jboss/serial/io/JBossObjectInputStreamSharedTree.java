package com.ovea.jetty.session.serializer.jboss.serial.io;

import com.ovea.jetty.session.serializer.jboss.serial.objectmetamodel.DataContainer;
import com.ovea.jetty.session.serializer.jboss.serial.util.StringUtilBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;

public class JBossObjectInputStreamSharedTree extends JBossObjectInputStream {

	ObjectInput input = null;
	DataContainer container = null;

	public JBossObjectInputStreamSharedTree(InputStream is, ClassLoader loader, StringUtilBuffer buffer) throws IOException {
		super(is, loader, buffer);
	}

	public JBossObjectInputStreamSharedTree(InputStream is, ClassLoader loader) throws IOException {
		super(is, loader);
	}

	public JBossObjectInputStreamSharedTree(InputStream is, StringUtilBuffer buffer) throws IOException {
		super(is, buffer);
	}

	public JBossObjectInputStreamSharedTree(InputStream is) throws IOException {
		super(is);
	}

	public Object readObjectOverride() throws IOException, ClassNotFoundException {
		if (input == null) {
			container = new DataContainer(classLoader, getSubstitutionInterface(), false, buffer, classDescriptorStrategy, objectDescriptorStrategy);
			container.setClassResolver(resolver);
			input = container.getDirectInput(dis);
		}
		return input.readObject();
	}

	public ClassLoader getClassLoader() {
		return super.getClassLoader();
	}

	public void setClassLoader(ClassLoader classLoader) {
		if (container != null) {
			container.getCache().setLoader(classLoader);
		}
		super.setClassLoader(classLoader);
	}

}
