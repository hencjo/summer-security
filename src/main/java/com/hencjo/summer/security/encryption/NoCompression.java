package com.hencjo.summer.security.encryption;

import com.hencjo.summer.security.api.Compression;

public class NoCompression implements Compression {
	@Override
	public byte[] compress(byte[] x) { return x; }

	@Override
	public byte[] uncompress(byte[] x) {
		return x;
	}
}
