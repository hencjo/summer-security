package com.hencjo.summer.security.encryption;

import com.hencjo.summer.security.api.DataEncryption;

import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class AESEncryption implements DataEncryption {
	private final SecureRandom secureRandom = new SecureRandom();
	private final SecretKeySpec key;

	public AESEncryption(byte[] key) {
		this.key = new SecretKeySpec(key, "AES");
	}

	@Override
	public Encoding encode(byte[] bytes) throws GeneralSecurityException {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, iv(secureRandom, cipher.getBlockSize()));
		return new Encoding(cipher.doFinal(bytes), cipher.getIV());
	}

	@Override
	public byte[] decode(byte[] encrypted, byte[] iv) throws GeneralSecurityException {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
		return cipher.doFinal(encrypted);
	}

	private static IvParameterSpec iv(SecureRandom secureRandom, int blockSize) {
		byte[] iv = new byte[blockSize];
		secureRandom.nextBytes(iv);
		return new IvParameterSpec(iv);
	}
}