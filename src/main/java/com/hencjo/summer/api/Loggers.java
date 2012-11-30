package com.hencjo.summer.api;

public class Loggers {
	public static SummerLogger noop() { return new NoopLogger(); }

	public static SummerLogger full() {
		return new SummerLogger() {
			
			@Override
			public void info(String string) {
				System.out.println(string);
			}
			
			@Override
			public void debug(String string) {
				System.out.println(string);
			}
		};
	}
}
