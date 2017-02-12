package com.hencjo.summer.security.api;

import java.security.GeneralSecurityException;

public interface DataEncryption {
	Encoding encode(byte[] bytes) throws GeneralSecurityException;
	byte[] decode(byte[] encrypted, byte[] iv) throws GeneralSecurityException;

	default byte[] decode(Encoding e) throws GeneralSecurityException { return decode(e.data, e.iv); }

	final class Encoding {
		public final byte[] data;
		public final byte[] iv;

		public Encoding(byte[] data, byte[] iv) {
			this.data = data;
			this.iv = iv;
		}
	}
}
