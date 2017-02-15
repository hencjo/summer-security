package com.hencjo.summer.security;

import com.hencjo.summer.security.api.Compression;
import com.hencjo.summer.security.api.DataEncryption;
import com.hencjo.summer.security.api.Hmac;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SecureCookies {
	private final Tid current;
	private final Map<String, Tid> tids;

	public SecureCookies(Tid currentTid, Collection<Tid> deprecatedTids) {
		HashMap<String, Tid> tids = new HashMap<>();
		tids.put(currentTid.tid, currentTid);
		for (Tid tid : deprecatedTids) tids.put(tid.tid, tid);

		this.current = currentTid;
		this.tids = Collections.unmodifiableMap(tids);
	}

	public Optional<String> decodeAsString(String rawCookieValue, Duration maxAge) throws GeneralSecurityException, IOException {
		return value(tids, rawCookieValue, maxAge).map(x -> new String(x, StandardCharsets.UTF_8));
	}

	public Optional<byte[]> decode(String rawCookieValue, Duration maxAge) throws GeneralSecurityException, IOException {
		return value(tids, rawCookieValue, maxAge);
	}

	public String encode(String value) throws IOException, GeneralSecurityException {
		return encode(current, value.getBytes(StandardCharsets.UTF_8), Instant.now());
	}

	public String encode(byte [] value) throws IOException, GeneralSecurityException {
		return encode(current, value, Instant.now());
	}

	private static Optional<byte[]> value(Map<String, Tid> tids, String value, Duration maxAge) throws GeneralSecurityException, IOException {
		String[] split = value.split("\\|");
		if (split.length != 5)
			return Optional.empty();

		String eData = split[0];
		String eAtime = split[1];
		String eTid = split[2];
		String eIv = split[3];
		String eAuthTag = split[4];

		Tid tid = tids.get(new String(decode(eTid), StandardCharsets.UTF_8));
		if (tid == null)
			return Optional.empty();

		if (!eAuthTag(box(eData, eAtime, eTid, eIv), tid.hmac).equals(eAuthTag))
			return Optional.empty();

		Instant atime = Instant.ofEpochSecond(new BigInteger(new String(decode(eAtime), StandardCharsets.UTF_8), 16).longValue());

		if (atime.plus(maxAge).isBefore(Instant.now()))
			return Optional.empty();

		byte[] data = decode(eData);
		byte[] iv = decode(eIv);
		return Optional.of(tid.compression.uncompress(tid.encryption.decode(data, iv)));
	}

	private static String encode(Tid tid, byte[] data, Instant atime) throws IOException, GeneralSecurityException {
		DataEncryption.Encoding encode = tid.encryption.encode(tid.compression.compress(data));
		String message = box(
			base64(encode.data),
			base64(BigInteger.valueOf(atime.getEpochSecond()).toString(16)),
			base64(tid.tid),
			base64(encode.iv)
		);
		return box(message, eAuthTag(message, tid.hmac));
	}

	private static String box(String ... vs) {
		return Stream.of(vs).collect(Collectors.joining("|"));
	}

	private static String eAuthTag(String message, Hmac hmac) throws GeneralSecurityException {
		return base64(hmac.hmac(message.getBytes(StandardCharsets.UTF_8)));
	}

	private static String base64(byte[] x) {
		return java.util.Base64.getUrlEncoder().encodeToString(x);
	}

	private static String base64(String x) {
		return base64(x.getBytes(StandardCharsets.UTF_8));
	}

	private static byte[] decode(String x) {
		return java.util.Base64.getUrlDecoder().decode(x);
	}

	public static final class Tid {
		public final String tid;
		public final DataEncryption encryption;
		public final Hmac hmac;
		public final Compression compression;

		public Tid(
			String tid,
			DataEncryption encryption,
			Hmac hmac,
			Compression compression
		) {
			this.tid = tid;
			this.encryption = encryption;
			this.hmac = hmac;
			this.compression = compression;
		}
	}
}