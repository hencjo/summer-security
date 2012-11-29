package com.hencjo.summer.rules;

import javax.servlet.http.HttpServletRequest;
import com.hencjo.summer.Rule;
import com.hencjo.summer.SummerRule;
import com.hencjo.summer.api.RequestMatcher;
import com.hencjo.summer.api.Responder;
import com.hencjo.summer.responders.Allow;
import com.hencjo.summer.responders.Status;

public final class WhenRule implements Rule {
	private final RequestMatcher requestMatcher;
	
	public WhenRule(RequestMatcher requestMatcher) {
		this.requestMatcher = requestMatcher;
	}
	
	public SummerRule then(Responder responder) {
		return new SummerRule(this, responder);
	}
	
	public Rule and(Rule responder) {
		return new AndRule(this, responder);
	}

	@Override
	public boolean matches(HttpServletRequest request) {
		return requestMatcher.matches(request);
	}
	
	@Override
	public String describer() {
		return "When(" + requestMatcher.describer() + ")";
	}

	public SummerRule thenAllow() {
		return new SummerRule(this, new Allow());
	}

	public SummerRule thenDeny() {
		return new SummerRule(this, new Status(403));
	}
}