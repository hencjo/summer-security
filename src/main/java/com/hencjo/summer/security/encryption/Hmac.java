package com.hencjo.summer.security.encryption;

import java.security.GeneralSecurityException;

public interface Hmac {
	byte[] hmac(byte[] bytes) throws GeneralSecurityException;
}
