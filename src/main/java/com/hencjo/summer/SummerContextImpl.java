package com.hencjo.summer;

public class SummerContextImpl implements SummerContext {
	public static final ThreadLocal<String> authenticatedAs = new ThreadLocal<String>() {
		@Override
		protected String initialValue() {
			return null;
		};
	};
	
	public static void setAuthenticatedAs(String username) {
		SummerContextImpl.authenticatedAs.set(username);
	}

	@Override
	public String getAuthenticatedAs() {
		return SummerContextImpl.authenticatedAs.get();
	}
}
