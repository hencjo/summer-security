package com.hencjo.summer.api;

public class Authenticators {

	public static Authenticator allowEveryoneAuthenticator() {
		return new Authenticator() {
			@Override
			public boolean authenticate(String username, String password) {
				return true;
			}
		};
	}

}
