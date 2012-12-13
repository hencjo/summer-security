package com.hencjo.summer.security.utils;

public final class Base64 {
	public String encode(byte[] buf) {
		return javax.xml.bind.DatatypeConverter.printBase64Binary(buf);
	}

	public byte[] decode(String s) {
		return javax.xml.bind.DatatypeConverter.parseBase64Binary(s);
	}
}
