package com.ovea.jetty.session;

public interface Serializer {

	void start();

	void stop();

	String serialize(Object o) throws SerializerException;

	<T> T deserialize(String o, Class<T> targetType) throws SerializerException;
}
