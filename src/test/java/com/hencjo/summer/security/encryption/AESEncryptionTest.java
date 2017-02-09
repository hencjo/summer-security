package com.hencjo.summer.security.encryption;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class AESEncryptionTest {
	@Test
	public void test() {
		AESEncryption clientSideEncryption = new AESEncryption(AESEncryption.sha256hash("very-secret-key"));

		String payload = "hello";
		byte[] payloadBytes = payload.getBytes(StandardCharsets.UTF_8);
		ClientSideEncryption.Encoding e = clientSideEncryption.encode(payloadBytes);
		byte[] decodedBytes = clientSideEncryption.decode(e);
		String decoded = new String(decodedBytes, StandardCharsets.UTF_8);
		assertEquals(payload, decoded);
	}
}