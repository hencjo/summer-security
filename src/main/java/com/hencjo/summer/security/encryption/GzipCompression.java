package com.hencjo.summer.security.encryption;

import com.hencjo.summer.security.api.Compression;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipCompression implements Compression {
	@Override
	public byte[] compress(byte[] x) throws IOException{
		try(
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			GZIPOutputStream gzos = new GZIPOutputStream(baos)
		) {
			gzos.write(x);
			return baos.toByteArray();
		}
	}

	@Override
	public byte[] uncompress(byte[] x) throws IOException {
		try(GZIPInputStream gzis = new GZIPInputStream(new ByteArrayInputStream(x))) {
			return allBytes(gzis);
		}
	}

	private static byte[] allBytes(InputStream is) throws IOException {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			byte[] buffer = new byte[1024];
			while (true) {
				int r = is.read(buffer);
				if (r == -1) break;
				out.write(buffer, 0, r);
			}
			return out.toByteArray();
		}
	}
}