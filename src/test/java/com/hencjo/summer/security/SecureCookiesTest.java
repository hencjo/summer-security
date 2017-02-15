package com.hencjo.summer.security;

import com.hencjo.summer.security.SecureCookies.Tid;
import com.hencjo.summer.security.api.DataEncryption;
import com.hencjo.summer.security.api.Hmac;
import com.hencjo.summer.security.encryption.NoCompression;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.logging.MemoryHandler;

import static org.junit.Assert.*;

public class SecureCookiesTest {
	private final SecureCookies cut = new SecureCookies(
		new Tid("v2", new IdEncryption(), new IdHmac(), new NoCompression()),
		Arrays.asList(
			new Tid("v1", new IdEncryption(), new IdHmac(), new NoCompression())
		)
	);

	@Test
	public void encodeDecodeString() throws Exception {
		String data = "meow";
		assertEquals(data, cut.decodeAsString(cut.encode(data), Duration.of(1, ChronoUnit.HOURS)).get());
	}

	@Test
	public void encodeDecodeBytes() throws Exception {
		byte[] data = "meow".getBytes(StandardCharsets.UTF_8);
		assertArrayEquals(data, cut.decode(cut.encode(data), Duration.of(1, ChronoUnit.HOURS)).get());
	}

	@Test
	public void expiry() throws Exception {
		assertEquals(Optional.empty(), cut.decodeAsString(cut.encode(""), Duration.of(-1, ChronoUnit.HOURS)));
	}

	@Test
	public void testDecodeUsingOldTid() throws GeneralSecurityException, IOException {
		String v1 = "bWVvdw==|NThhNGExNDA=|djE=|aXY=|c2lnbmF0dXJl"; //'meow' encoded using tid:v1
		Optional<String> s = cut.decodeAsString(v1, Duration.of(1, ChronoUnit.HOURS));
		assertEquals("meow", s.get());
	}

	static final class IdEncryption implements DataEncryption {
		@Override
		public Encoding encode(byte[] bytes) throws GeneralSecurityException {
			return new Encoding(bytes, "iv".getBytes(StandardCharsets.UTF_8));
		}

		@Override
		public byte[] decode(byte[] encrypted, byte[] iv) throws GeneralSecurityException {
			return encrypted;
		}
	}

	static final class IdHmac implements Hmac {
		@Override
		public byte[] hmac(byte[] bytes) throws GeneralSecurityException {
			return "signature".getBytes(StandardCharsets.UTF_8);
		}
	}
}