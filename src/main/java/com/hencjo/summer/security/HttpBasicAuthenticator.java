package com.hencjo.summer.security;

import com.hencjo.summer.security.api.CredentialsAuthenticator;
import com.hencjo.summer.security.api.CredentialsAuthenticator.Credentials;
import com.hencjo.summer.security.api.RequestMatcher;
import com.hencjo.summer.security.api.Responder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public final class HttpBasicAuthenticator {
	private final CredentialsAuthenticator authenticator;
	private final String realm;

	public HttpBasicAuthenticator(CredentialsAuthenticator authenticator, String realm) {
		if (realm == null) throw new IllegalArgumentException("Realm may not be null.");
		if (realm.contains("\"")) throw new IllegalArgumentException("Realm may not contain quotes (\")");
		this.authenticator = authenticator;
		this.realm = realm;
	}

	public RequestMatcher authorizes() {
		return new RequestMatcher() {
			@Override
			public boolean matches(HttpServletRequest request) {
				return hasAuthentication(request).isPresent();
			}
			
			@Override
			public String describer() {
				return "HttpBasic";
			}
		};
	}

	public Optional<String> hasAuthentication(HttpServletRequest request) {
		Optional<Credentials> credentials = credentials(request);
		if (!credentials.isPresent()) return Optional.empty();
		return authenticator.authenticate(credentials.get());
	}

	public static Optional<Credentials> credentials(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");
		if (authorization == null) return Optional.empty();
		if (!authorization.startsWith("Basic")) return Optional.empty();
		String usernameColonPassword = new String(java.util.Base64.getDecoder().decode(authorization.substring(6)), StandardCharsets.UTF_8);
		String[] split = usernameColonPassword.split(":");
		if (split.length != 2) return Optional.empty();
		String username = split[0];
		String password = split[1];
		return Optional.of(new Credentials(username, password));
	}

	public Responder wwwAuthenticate() {
		return new Responder() {
			@Override
			public ContinueOrRespond respond(HttpServletRequest request, HttpServletResponse response) throws IOException {
				response.setHeader("WWW-Authenticate", "Basic realm=\""+realm+"\"");
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				return ContinueOrRespond.RESPOND;
			}

			@Override
			public String describer() {
				return "WWW-Authenticate: Basic realm=\"" + realm + "\"";
			}
		};
	}
}
