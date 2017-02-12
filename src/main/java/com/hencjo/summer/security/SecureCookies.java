package com.hencjo.summer.security;

import com.hencjo.summer.security.api.Compression;
import com.hencjo.summer.security.api.DataEncryption;
import com.hencjo.summer.security.api.Hmac;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class SecureCookies {
	private final DataEncryption encryption;
	private final Hmac hmac;
	private final Compression compression;
	private final int scsExpirationSeconds;

	public SecureCookies(DataEncryption encryption, Hmac hmac, Compression compression, int scsExpirationSeconds) {
		this.encryption = encryption;
		this.hmac = hmac;
		this.compression = compression;
		this.scsExpirationSeconds = scsExpirationSeconds;
	}

	public Optional<String> cookieValue(Cookie c) {
		try {
			return ec(c);
		} catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	private Optional<String> ec(Cookie c) throws GeneralSecurityException, IOException {
		String[] split = c.getValue().split("\\|");
		if (split.length != 5)
			return Optional.empty();

		String eData = split[0];
		String eAtime = split[1];
		String eTid = split[2];
		String eIv = split[3];
		String eAuthTag = split[4];

		if (!eAuthTag(eData, eAtime, eTid, eIv, hmac).equals(eAuthTag))
			return Optional.empty();

		byte[] data = decode(eData);
		byte[] iv = decode(eIv);
		Instant atime = Instant.ofEpochSecond(new BigInteger(new String(decode(eAtime), StandardCharsets.UTF_8), 16).longValue());

		if (atime.plus(scsExpirationSeconds, ChronoUnit.SECONDS).isBefore(Instant.now()))
			return Optional.empty();

		return Optional.of(new String(compression.uncompress(encryption.decode(data, iv)), StandardCharsets.UTF_8));
	}

	public void setCookie(
		HttpServletRequest request,
		HttpServletResponse response,
		String name,
		String value,
		int expiresInSeconds
	) {
		try {
			Cookies.setCookie(response,
				Cookies.cookie(
					System.currentTimeMillis(),
					name,
					request.getContextPath(),
					payload(value, Instant.now()),
					expiresInSeconds
				)
			);
		} catch (IOException | GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
	}

	public void removeCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		Cookies.setCookie(response, Cookies.removeCookie(name, request.getContextPath()));
	}

	private String payload(String data, Instant atime) throws IOException, GeneralSecurityException {
		DataEncryption.Encoding encode = encryption.encode(compression.compress(data.getBytes(StandardCharsets.UTF_8)));
		String eData = base64(encode.data);
		String eAtime = base64(BigInteger.valueOf(atime.getEpochSecond()).toString(16));
		String eTid = "";
		String eIV = base64(encode.iv);
		String eAuthTag = eAuthTag(eData, eAtime, eTid, eIV, hmac);
		return eData + '|' + eAtime + '|' + eTid + '|' + eIV + '|' + eAuthTag;
	}

	private static String eAuthTag(String eData, String eAtime, String eTid, String eIv, Hmac hmac) {
		try {
			String message = eData + '|' + eAtime + '|' + eTid + '|' + eIv;
			return base64(hmac.hmac(message.getBytes(StandardCharsets.UTF_8)));
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
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
}