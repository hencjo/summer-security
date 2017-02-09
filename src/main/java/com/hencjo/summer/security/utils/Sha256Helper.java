package com.hencjo.summer.security.utils;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

public class Sha256Helper {
	public static byte[] sha256hash(String key) {
		try {
			return MessageDigest.getInstance("SHA-256")
				.digest(key.getBytes(StandardCharsets.UTF_8));
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
	}
}
