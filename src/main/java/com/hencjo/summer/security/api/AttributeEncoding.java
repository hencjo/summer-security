package com.hencjo.summer.security.api;

public interface AttributeEncoding<T> {
	byte[] toBytes(T t);
	T fromBytes(byte[] bytes);
}
