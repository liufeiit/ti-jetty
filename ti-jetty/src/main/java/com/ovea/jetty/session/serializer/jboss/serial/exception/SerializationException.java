package com.ovea.jetty.session.serializer.jboss.serial.exception;

import java.io.IOException;

public class SerializationException extends IOException {
	private static final long serialVersionUID = 1L;

	public SerializationException(String message, Exception source) {
		this(message);
		this.initCause(source);
	}

	public SerializationException(String message) {
		super(message);
	}

	public SerializationException(Exception ex) {
		this(ex.getMessage(), ex);
	}
}
