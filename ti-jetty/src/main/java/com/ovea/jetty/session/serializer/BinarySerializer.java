package com.ovea.jetty.session.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.eclipse.jetty.util.B64Code;

import com.ovea.jetty.session.SerializerException;

public abstract class BinarySerializer extends SerializerSkeleton {

	private boolean gzip = true;

	public void setGzip(boolean gzip) {
		this.gzip = gzip;
	}

	public boolean isGzip() {
		return gzip;
	}

	@Override
	public final String serialize(Object o) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			if (gzip) {
				GZIPOutputStream gout = new GZIPOutputStream(baos);
				write(gout, o);
				gout.finish();
				gout.close();
			} else {
				write(baos, o);
			}
			return String.valueOf(B64Code.encode(baos.toByteArray(), false));
		} catch (Exception e) {
			throw new SerializerException("Error serializing " + o + " : " + e.getMessage(), e);
		}
	}

	@Override
	public final <T> T deserialize(String o, Class<T> targetType) throws SerializerException {
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(B64Code.decode(o));
			return targetType.cast(read(gzip ? new GZIPInputStream(bais) : bais));
		} catch (Exception e) {
			throw new SerializerException("Error deserializing " + o + " : " + e.getMessage(), e);
		}
	}

	protected abstract void write(OutputStream out, Object o) throws Exception;

	protected abstract Object read(InputStream is) throws Exception;
}
