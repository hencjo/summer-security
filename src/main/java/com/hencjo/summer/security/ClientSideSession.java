package com.hencjo.summer.security;

import com.hencjo.summer.security.api.RequestMatcher;
import com.hencjo.summer.security.encryption.DataEncryption;
import com.hencjo.summer.security.encryption.Hmac;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class ClientSideSession {
	private final SummerAuthenticatedUser summerAuthenticatedUser = new SummerAuthenticatedUser();
	private final String sessionCookie;
	private final DataEncryption encryption;
	private final int expiresInSeconds;
	private final Hmac hmac;

	public ClientSideSession(
		DataEncryption encryption,
		Hmac hmac, String sessionCookie,
		int expiresInSeconds
	) {
		this.encryption = encryption;
		this.sessionCookie = sessionCookie;
		this.expiresInSeconds = expiresInSeconds;
		this.hmac = hmac;
	}
	
	private String hasClientSideSession(HttpServletRequest request)  {
		List<Cookie> cs = cookiesWithName(request, sessionCookie);
		if (cs.isEmpty()) return null;
		for (Cookie c : cs) return examineCookie(c);
		return null;
	}

	private String examineCookie(Cookie c) {
		String[] split = c.getValue().split("\\|");
		if (split.length != 5)
			return null;

		String eData = split[0];
		String eAtime = split[1];
		String eTid = split[2];
		String eIv = split[3];
		String eAuthTag = split[4];

		if (!eAuthTag(eData, eAtime, eTid, eIv, hmac).equals(eAuthTag))
			return null;

		byte[] data = decode(eData);
		byte[] iv = decode(eIv);
		Instant atime = Instant.ofEpochSecond(new BigInteger(new String(decode(eAtime), StandardCharsets.UTF_8), 16).longValue());

		if (atime.plus(expiresInSeconds, ChronoUnit.SECONDS).isBefore(Instant.now()))
			return null;

		return new String(encryption.decode(data, iv), StandardCharsets.UTF_8);
	}

	private static List<Cookie> cookiesWithName(HttpServletRequest request, String cookieName) {
		return Arrays.stream(request.getCookies())
			.filter(x -> cookieName.equals(x.getName()))
			.collect(Collectors.toList());
	}

	public RequestMatcher exists() {
		return new RequestMatcher() {
			@Override
			public boolean matches(HttpServletRequest request) {
				String loggedInUsername = hasClientSideSession(request);
				if (loggedInUsername != null) {
					summerAuthenticatedUser.set(request, loggedInUsername);
					return true;
				}
				return false;
			}
			
			@Override
			public String describer() {
				return "ClientSideSession";
			}
		};
	}
	
	public SessionWriter sessionWriter() {
		return new SessionWriter() {
			@Override
			public void startSession(HttpServletRequest request, HttpServletResponse response, String username) {
				response.addHeader("Set-Cookie", Cookies.cookie(
					System.currentTimeMillis(),
					sessionCookie,
					request.getContextPath(),
					payload(username, Instant.now()),
					expiresInSeconds
				));
			}
			
			@Override
			public void stopSession(HttpServletRequest request, HttpServletResponse response) {
				String username = hasClientSideSession(request);
				if (username == null) return;
				response.addHeader("Set-Cookie", Cookies.cookie(
					System.currentTimeMillis(),
					sessionCookie,
					request.getContextPath(),
					payload(username, Instant.ofEpochSecond(0)),
					expiresInSeconds
				));
			}
		};
	}

	private String payload(String data, Instant atime) {
		DataEncryption.Encoding encode = encryption.encode(data.getBytes(StandardCharsets.UTF_8));
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

	private static String base64(byte[] iv) {
		return java.util.Base64.getEncoder().encodeToString(iv);
	}

	private static String base64(String s) {
		return Base64.encode(s.getBytes(StandardCharsets.UTF_8));
	}

	private static byte[] decode(String eData) {
		return java.util.Base64.getDecoder().decode(eData);
	}
}