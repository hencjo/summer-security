package com.hencjo.summer.security.api;

import java.util.Optional;

public interface Authenticator2 {
	Optional<String> authenticate(String username, String password);
}
