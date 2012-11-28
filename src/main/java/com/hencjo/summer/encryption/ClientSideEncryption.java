package com.hencjo.summer.encryption;

public interface ClientSideEncryption {
	byte[] encode(byte[] bytes);
	byte[] decode(byte[] encrypted);
}
