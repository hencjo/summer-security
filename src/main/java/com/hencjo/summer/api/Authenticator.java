package com.hencjo.summer.api;

public interface Authenticator {
	boolean authenticate(String username, String password);
}
