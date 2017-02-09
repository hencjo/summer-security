package com.hencjo.summer.security.encryption;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class AESEncryption implements ClientSideEncryption {
	private final SecureRandom secureRandom = new SecureRandom();
	private final SecretKeySpec key;

	public AESEncryption(byte[] key) {
		this.key = new SecretKeySpec(key, "AES");
	}

	@Override
	public Encoding encode(byte[] bytes) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, iv(secureRandom, cipher.getBlockSize()));
			return new Encoding(cipher.doFinal(bytes), cipher.getIV());
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte[] decode(byte[] encrypted, byte[] iv) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
			return cipher.doFinal(encrypted);
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
	}

	private static IvParameterSpec iv(SecureRandom secureRandom, int blockSize) {
		byte[] iv = new byte[blockSize];
		secureRandom.nextBytes(iv);
		return new IvParameterSpec(iv);
	}

}