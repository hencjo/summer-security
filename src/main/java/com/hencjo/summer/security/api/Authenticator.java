package com.hencjo.summer.security.api;

public interface Authenticator {
	boolean authenticate(String username, String password);
}
