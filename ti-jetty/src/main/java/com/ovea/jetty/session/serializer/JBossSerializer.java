package com.ovea.jetty.session.serializer;

import java.io.InputStream;
import java.io.OutputStream;

import com.ovea.jetty.session.serializer.jboss.serial.io.JBossObjectInputStream;
import com.ovea.jetty.session.serializer.jboss.serial.io.JBossObjectOutputStream;

public final class JBossSerializer extends BinarySerializer {
	@Override
	protected void write(OutputStream out, Object o) throws Exception {
		JBossObjectOutputStream oos = new JBossObjectOutputStream(out);
		oos.writeObject(o);
	}

	@Override
	protected Object read(InputStream is) throws Exception {
		JBossObjectInputStream ois = new JBossObjectInputStream(is);
		return ois.readObject();
	}
}
