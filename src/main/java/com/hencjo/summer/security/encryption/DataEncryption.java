package com.hencjo.summer.security.encryption;

public interface DataEncryption {
	Encoding encode(byte[] bytes);
	byte[] decode(byte[] encrypted, byte[] iv);

	default byte[] decode(Encoding e) { return decode(e.data, e.iv); }

	final class Encoding {
		public final byte[] data;
		public final byte[] iv;

		public Encoding(byte[] data, byte[] iv) {
			this.data = data;
			this.iv = iv;
		}
	}
}
