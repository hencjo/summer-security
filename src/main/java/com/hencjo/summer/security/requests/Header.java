package com.hencjo.summer.security.requests;

import javax.servlet.http.HttpServletRequest;
import com.hencjo.summer.security.api.RequestMatcher;

public final class Header {
	private final String header;

	public Header(String header) {
		this.header = header;
	}

	public RequestMatcher equals(final String value) {
		return new RequestMatcher() {
			@Override
			public boolean matches(HttpServletRequest request) {
				String h = request.getHeader(header);
				return ((h != null)?h:"").equals(value);
			}
			
			@Override
			public String describer() {
				return "Header \"" + header + "\" equals \"" + value + "\"";
			}
		};
	}
}
