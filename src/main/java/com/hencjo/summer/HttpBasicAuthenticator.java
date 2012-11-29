package com.hencjo.summer;

import javax.servlet.http.HttpServletRequest;
import com.hencjo.summer.api.Authenticator;
import com.hencjo.summer.api.RequestMatcher;
import com.hencjo.summer.utils.Base64;
import com.hencjo.summer.utils.Charsets;

public final class HttpBasicAuthenticator {
	private final Authenticator authenticator;
	private final Base64 base64 = new Base64();

	public HttpBasicAuthenticator(Authenticator authenticator) {
		this.authenticator = authenticator;
	}

	public RequestMatcher authorizes() {
		return new RequestMatcher() {
			@Override
			public boolean matches(HttpServletRequest request) {
				String authorization = request.getHeader("Authorization");
				if (authorization == null) return false;
				if (!authorization.startsWith("Basic")) return false;
				String usernameColonPassword = new String(base64.decode(authorization.substring(6)), Charsets.UTF8);
				String[] split = usernameColonPassword.split(":");
				if (split.length != 2) return false;
				String username = split[0];
				String password = split[1];
				if (authenticator.authenticate(username, password)) {
					SummerContextImpl.setAuthenticatedAs(username);
					return true;
				}
				return false;
			}
			
			@Override
			public String describer() {
				return "HttpBasic";
			}
		};
	}
}
