package com.hencjo.summer.api;

public class NoopLogger implements SummerLogger {
	@Override
	public void debug(String string) {
	}

	@Override
	public void info(String string) {
	}
}