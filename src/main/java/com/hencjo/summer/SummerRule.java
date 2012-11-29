package com.hencjo.summer;

import com.hencjo.summer.api.Responder;

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
