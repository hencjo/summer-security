package com.hencjo.summer.security;

import com.hencjo.summer.security.api.Responder;

public final class SummerRule {
	final Rule rule;
	final Responder authorizer;

	public SummerRule(Rule rule, Responder authorizer) {
		this.rule = rule;
		this.authorizer = authorizer;
	}

	public String describer() {
		return rule.describer() + ".then(" + authorizer.describer() + ")";
	}
}
