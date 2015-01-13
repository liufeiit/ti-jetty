package com.ovea.jetty.session.serializer.jboss.serial.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

public class MarshalledObject implements Serializable {
	private byte[] bytes;
	private int hash;

	static final long serialVersionUID = -1433248532959364465L;

	public MarshalledObject() {
	}

	public MarshalledObject(Object obj) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JBossObjectOutputStream mvos = new JBossObjectOutputStream(baos);
		mvos.writeObject(obj);
		mvos.flush();
		bytes = baos.toByteArray();
		mvos.close();
		hash = 0;
		for (int i = 0; i < bytes.length; i++) {
			hash += bytes[i];
		}
	}

	public Object get() throws IOException, ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		JBossObjectInputStream ois = new JBossObjectInputStream(bais);
		try {
			return ois.readObject();
		} finally {
			ois.close();
			bais.close();
		}
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		final MarshalledObject that = (MarshalledObject) o;

		if (!Arrays.equals(bytes, that.bytes))
			return false;

		return true;
	}

	public int hashCode() {
		return hash;
	}

}
