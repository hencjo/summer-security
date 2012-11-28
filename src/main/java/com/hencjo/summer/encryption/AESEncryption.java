package com.hencjo.summer.encryption;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import com.hencjo.summer.utils.Base64;
import com.hencjo.summer.utils.Charsets;

public final class AESEncryption implements ClientSideEncryption {
	private final SecretKeySpec key;
	private final IvParameterSpec iv;
  
	public AESEncryption(byte[] key, byte[] iv) {
		this.key = new SecretKeySpec(key, "AES");
		this.iv = new IvParameterSpec(iv);
	}
	
	@Override
	public byte[] encode(byte[] bytes) {
		 try {
       Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
       cipher.init(Cipher.ENCRYPT_MODE, key, iv);
       return cipher.doFinal(bytes);
   } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
  	 throw new RuntimeException(e);
   }
	}

	@Override
	public byte[] decode(byte[] encrypted) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, iv);
			return cipher.doFinal(encrypted);
		} catch (IllegalBlockSizeException e) {
			return null;
		} catch ( BadPaddingException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) {
		AESEncryption clientSideEncryption = new AESEncryption(new Base64().decode("RE2zSPlxVph5Fm/tgQd60g=="), new byte[16]);
		
//		byte[] generateKey2 = clientSideEncryption.generateKey2();
//		System.out.println("key: " + Base64.encode(generateKey2));
		
		String in = "hello";
		byte[] encode = clientSideEncryption.encode(in.getBytes(Charsets.utf8));
		
		String decode = new String(clientSideEncryption.decode(new Base64().decode("4tt8i/Ff1Q1kzQHTkUoKQg==")), Charsets.utf8);
		System.out.println("Encoding: " + in);
		System.out.println(" => " + new String(encode, Charsets.utf8));
		System.out.println("And back: " + decode);
	}
}
