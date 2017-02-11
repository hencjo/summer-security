package com.hencjo.summer.security.api;

import java.util.Optional;

@Deprecated
public interface Authenticator {
	boolean authenticate(String username, String password);

	default CredentialsAuthenticator asAuthenticator2() {
		return c -> authenticate(c.username, c.password) ? Optional.of(c.username) : Optional.empty();
	}
}