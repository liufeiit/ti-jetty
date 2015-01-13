package com.ovea.jetty.session.serializer;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public final class JdkSerializer extends BinarySerializer {
	@Override
	protected void write(OutputStream out, Object o) throws Exception {
		ObjectOutputStream oos = new ObjectOutputStream(out);
		oos.writeObject(o);
	}

	@Override
	protected Object read(InputStream is) throws Exception {
		ObjectInputStream ois = new ObjectInputStream(is);
		return ois.readObject();
	}
}
