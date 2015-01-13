package com.ovea.jetty.session;

public final class SerializerException extends RuntimeException {
	private static final long serialVersionUID = 1669218829475607626L;

	public SerializerException(Throwable cause) {
		super(cause.getMessage(), cause);
	}

	public SerializerException(String message) {
		super(message);
	}

	public SerializerException(String message, Throwable cause) {
		super(message, cause);
	}
}
