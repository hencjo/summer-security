package com.hencjo.summer.security.encryption;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class Sha256Hmac implements Hmac {
	private final byte[] key;

	public Sha256Hmac(byte[] key) {
		this.key = key;
	}

	@Override
	public byte[] hmac(byte[] bytes) throws NoSuchAlgorithmException, InvalidKeyException {
		Key sk = new SecretKeySpec(key, "HmacSHA256");
		Mac mac = Mac.getInstance(sk.getAlgorithm());
		mac.init(sk);
		return mac.doFinal(bytes);
	}
}
