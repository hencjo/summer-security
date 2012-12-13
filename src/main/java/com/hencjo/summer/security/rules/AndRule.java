package com.hencjo.summer.security.rules;

import javax.servlet.http.HttpServletRequest;
import com.hencjo.summer.security.Rule;
import com.hencjo.summer.security.SummerRule;
import com.hencjo.summer.security.api.Responder;
import com.hencjo.summer.security.responders.Allow;
import com.hencjo.summer.security.responders.Status;

public final class AndRule implements Rule {
	private final Rule left;
	private final Rule right;
	
	public AndRule(Rule left, Rule right) {
		this.left = left;
		this.right = right;
	}
	
	public SummerRule then(Responder a) {
		return new SummerRule(this, a);
	}
	
	public Rule and(Rule r) {
		return new AndRule(this, r);
	}

	@Override
	public boolean matches(HttpServletRequest request) {
		return left.matches(request) && right.matches(request);
	}
	
	@Override
	public String describer() {
		return "And(" + left.describer() + "," + right.describer() + ")";
	}
	
	public SummerRule thenAllow() {
		return new SummerRule(this, new Allow());
	}
	
	public SummerRule thenDeny() {
		return new SummerRule(this, new Status(403));
	}
}