package com.hencjo.summer.security.api;

import java.util.Optional;
import java.util.function.Function;

@Deprecated
public interface Authenticator {
	boolean authenticate(String username, String password);

	default <T> CredentialsAuthenticator<T> asAuthenticator2(Function<String, T> ft) {
		return c -> authenticate(c.username, c.password) ? Optional.of(ft.apply(c.username)) : Optional.empty();
	}
}