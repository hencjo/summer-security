package com.hencjo.summer.security.api;

import java.util.Optional;

@Deprecated
public interface Authenticator {
	boolean authenticate(String username, String password);

	default Authenticator2 asAuthenticator2() {
		return (username, password) -> authenticate(username, password) ? Optional.of(username) : Optional.empty();
	}
}