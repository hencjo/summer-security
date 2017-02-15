package com.hencjo.summer.security.api;

public class Authenticators {

	public static Authenticator allowEveryoneAuthenticator() {
		return (username, password) -> true;
	}

}
