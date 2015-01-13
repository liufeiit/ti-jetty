package com.ovea.jetty.session.serializer;

import com.ovea.jetty.session.Serializer;

public abstract class SerializerSkeleton implements Serializer {
	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}

	@Override
	public String serialize(Object o) {
		return o == null ? null : String.valueOf(o);
	}
}
