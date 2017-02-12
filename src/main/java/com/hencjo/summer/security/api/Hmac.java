package com.hencjo.summer.security.api;

import java.security.GeneralSecurityException;

public interface Hmac {
	byte[] hmac(byte[] bytes) throws GeneralSecurityException;
}
