package com.ovea.jetty.session.serializer.jboss.serial.objectmetamodel;

public interface DataContainerConstants {
	public static final byte CLASSDEF = 1;
	public static final byte OBJECTDEF = 2;
	public static final byte OBJECTREF = 3;
	public static final byte STRING = 4;
	public static final byte DOUBLE = 5;
	public static final byte INTEGER = 6;
	public static final byte LONG = 7;
	public static final byte SHORT = 8;
	public static final byte BYTE = 9;
	public static final byte FLOAT = 10;
	public static final byte CHARACTER = 11;
	public static final byte BOOLEAN = 12;
	public static final byte BYTEARRAY = 13;

	public static final byte DOUBLEOBJ = 25;
	public static final byte INTEGEROBJ = 26;
	public static final byte LONGOBJ = 27;
	public static final byte SHORTOBJ = 28;
	public static final byte BYTEOBJ = 29;
	public static final byte FLOATOBJ = 30;
	public static final byte CHARACTEROBJ = 31;
	public static final byte BOOLEANOBJ = 32;

	public static final byte IMMUTABLE_OBJREF = 40;

	public static final byte SMARTCLONE_DEF = 50;
	public static final byte NEWDEF = 51;

	public static final byte RESET = 60;

	public static final byte NULLREF = 99;

	public static byte[] openSign = new byte[] { (byte) 'j', (byte) 'b', (byte) 's', (byte) '1', (byte) '{' };
	public static byte[] closeSign = new byte[] { (byte) 'j', (byte) 'b', (byte) 's', (byte) '1', (byte) '}' };

}
