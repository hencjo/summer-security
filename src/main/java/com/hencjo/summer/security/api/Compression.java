package com.hencjo.summer.security.api;

import java.io.IOException;

public interface Compression {
	byte[] compress(byte[] x) throws IOException;
	byte[] uncompress(byte[] x) throws IOException;
}
