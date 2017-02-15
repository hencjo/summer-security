package com.hencjo.summer.security;

import com.hencjo.summer.security.api.AttributeEncoding;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

public class Attributes {
	public static final AttributeEncoding<String> StringAttribute = new AttributeEncoding<String>() {
		@Override
		public byte[] toBytes(String s) {
			return s.getBytes(StandardCharsets.UTF_8);
		}

		@Override
		public String fromBytes(byte[] bytes) {
			return new String(bytes, StandardCharsets.UTF_8);
		}
	};

	public static <A,B> AttributeEncoding<B> wrap(AttributeEncoding<A> wrapped, Function<A, B> fab, Function<B, A> fba) {
		return new AttributeEncoding<B>() {
			@Override
			public byte[] toBytes(B b) {
				return wrapped.toBytes(fba.apply(b));
			}

			@Override
			public B fromBytes(byte[] bytes) {
				return fab.apply(wrapped.fromBytes(bytes));
			}
		};
	}
}
