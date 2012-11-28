package com.hencjo.summer;

public interface Authenticator {
	boolean authenticate(String username, String password);
}
