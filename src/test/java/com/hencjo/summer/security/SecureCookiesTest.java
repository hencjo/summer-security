package com.hencjo.summer.security;

import com.hencjo.summer.security.SecureCookies.Tid;
import com.hencjo.summer.security.api.DataEncryption;
import com.hencjo.summer.security.api.Hmac;
import com.hencjo.summer.security.encryption.NoCompression;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SecureCookiesTest {
	private final Tid tidV1 = new Tid("v1", new IdEncryption(), new IdHmac(), new NoCompression());
	private final Tid tidV2 = new Tid("v2", new IdEncryption(), new IdHmac(), new NoCompression());

	private final SecureCookies cutV1 = new SecureCookies(
		tidV1,
		Collections.emptyList()
	);

	private final SecureCookies cutV2 = new SecureCookies(
		tidV2,
		Collections.singletonList(tidV1)
	);

	@Test
	public void encodeDecodeString() throws Exception {
		String data = "meow";
		assertEquals(data, cutV2.decodeAsString(cutV2.encode(data), 3600).get());
	}

	@Test
	public void encodeDecodeBytes() throws Exception {
		byte[] data = "meow".getBytes(StandardCharsets.UTF_8);
		assertArrayEquals(data, cutV2.decode(cutV2.encode(data), 3600).get());
	}

	@Test
	public void expiry() throws Exception {
		assertEquals(Optional.empty(), cutV2.decodeAsString(cutV2.encode(""), -3600));
	}

	@Test
	public void testDecodeUsingOldTid() throws GeneralSecurityException, IOException {
		String v1 = cutV1.encode("meow");
		assertEquals("meow", cutV2.decodeAsString(v1, 3600).get());
	}

	private static final class IdEncryption implements DataEncryption {
		@Override
		public Encoding encode(byte[] bytes) throws GeneralSecurityException {
			return new Encoding(bytes, "iv".getBytes(StandardCharsets.UTF_8));
		}

		@Override
		public byte[] decode(byte[] encrypted, byte[] iv) throws GeneralSecurityException {
			return encrypted;
		}
	}

	private static final class IdHmac implements Hmac {
		@Override
		public byte[] hmac(byte[] bytes) throws GeneralSecurityException {
			return "signature".getBytes(StandardCharsets.UTF_8);
		}
	}
}