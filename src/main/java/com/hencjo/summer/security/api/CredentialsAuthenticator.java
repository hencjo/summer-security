package com.hencjo.summer.security.api;

import java.util.Optional;

public interface CredentialsAuthenticator<T> {
	Optional<T> authenticate(Credentials credentials);

	final class Credentials {
		public final String username;
		public final String password;

		public Credentials(String username, String password) {
			this.username = username;
			this.password = password;
		}
	}
}
