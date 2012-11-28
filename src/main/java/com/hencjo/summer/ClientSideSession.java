package com.hencjo.summer;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.hencjo.summer.api.RequestMatcher;
import com.hencjo.summer.encryption.ClientSideEncryption;
import com.hencjo.summer.utils.Base64;
import com.hencjo.summer.utils.Charsets;

public class ClientSideSession {
	private final String sessionCookie;
	private final ClientSideEncryption encryption;
	private final Base64 base64 = new Base64();
	private final Cookies cookies = new Cookies();

	public ClientSideSession(ClientSideEncryption encryption, String sessionCookie) {
		this.encryption = encryption;
		this.sessionCookie = sessionCookie;
	}
	
	public String hasClientSideSession(HttpServletRequest request) {
		List<Cookie> cs = cookiesWithName(request, sessionCookie);
		if (cs.isEmpty()) return null;
		for (Cookie c : cs) {
			byte[] decode = encryption.decode(base64.decode(c.getValue()));
			if (decode == null) continue;
			try {
				return new String(decode, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	private static List<Cookie> cookiesWithName(HttpServletRequest request, String cookieName) {
		List<Cookie> matching = new LinkedList<>();
		for (Cookie cookie : request.getCookies()) {
			if (!cookieName.equals(cookie.getName())) continue;
			matching.add(cookie);
		}
		return matching;
	}

	public RequestMatcher exists() {
		return new RequestMatcher() {
			@Override
			public boolean matches(HttpServletRequest request) {
				String loggedInUsername = hasClientSideSession(request);
				if (loggedInUsername != null) {
					SummerContextImpl.setAuthenticatedAs(loggedInUsername);
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
				response.addHeader("Set-Cookie", cookies.setCookie(System.currentTimeMillis(), sessionCookie, request.getContextPath(), base64.encode(encryption.encode(username.getBytes(Charsets.utf8))), 3600));
			}
			
			@Override
			public void stopSession(HttpServletRequest request, HttpServletResponse response) {
				response.addHeader("Set-Cookie", cookies.removeCookie(sessionCookie, request.getContextPath()));
			}
		};
	}
}
