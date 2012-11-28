package com.hencjo.summer;

import com.hencjo.summer.api.Responder;

public final class RuleAuthorizer {
	final Rule rule;
	final Responder authorizer;

	public RuleAuthorizer(Rule rule, Responder authorizer) {
		this.rule = rule;
		this.authorizer = authorizer;
	}

	public String describer() {
		return rule.describer() + ".then(" + authorizer.describer() + ")";
	}
}
