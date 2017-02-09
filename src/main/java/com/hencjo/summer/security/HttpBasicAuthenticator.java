package com.hencjo.summer.security;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.hencjo.summer.security.api.Authenticator;
import com.hencjo.summer.security.api.Authenticator2;
import com.hencjo.summer.security.api.RequestMatcher;
import com.hencjo.summer.security.api.Responder;
import com.hencjo.summer.security.utils.Base64;
import com.hencjo.summer.security.utils.Charsets;

public final class HttpBasicAuthenticator<T> {
	private final Authenticator2 authenticator;
	private final SummerAuthenticatedUser summerAuthenticatedUser = new SummerAuthenticatedUser();
	private final Base64 base64 = new Base64();
	private final String realm;

	public HttpBasicAuthenticator(Authenticator2 authenticator, String realm) {
		if (realm == null) throw new IllegalArgumentException("Realm may not be null.");
		if (realm.contains("\"")) throw new IllegalArgumentException("Realm may not contain quotes (\")");
		this.authenticator = authenticator;
		this.realm = realm;
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
				Optional<String> authenticate = authenticator.authenticate(username, password);
				if (!authenticate.isPresent()) return false;
				summerAuthenticatedUser.set(request, authenticate.get());
				return true;
			}
			
			@Override
			public String describer() {
				return "HttpBasic";
			}
		};
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
