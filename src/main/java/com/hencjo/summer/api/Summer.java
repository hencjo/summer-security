package com.hencjo.summer.api;

import com.hencjo.summer.RuleAuthorizer;
import com.hencjo.summer.SummerFilterDelegate;
import com.hencjo.summer.requests.AnyPath;
import com.hencjo.summer.requests.Header;
import com.hencjo.summer.requests.PathBeginsWith;
import com.hencjo.summer.requests.PathEquals;
import com.hencjo.summer.responders.Allow;
import com.hencjo.summer.responders.Redirect;
import com.hencjo.summer.responders.Status;
import com.hencjo.summer.rules.WhenRule;

public final class Summer {
	
	public static SummerFilterDelegate summer(RuleAuthorizer ... rules) {
		return new SummerFilterDelegate(rules);
	}
	
	// REQUEST MATCHERS
	
	public static Header header(final String header) {
		return new Header(header);
	}
	
	public static WhenRule when(RequestMatcher rule) {
		return new WhenRule(rule);
	}
	
	public static WhenRule otherwise() {
		return when(any());
	}
	
	public static PathBeginsWith pathBeginsWith(String beginsWith) {
		return new PathBeginsWith(beginsWith);
	}
	
	public static PathEquals pathEquals(String equals) {
		return new PathEquals(equals);
	}
	
	public static AnyPath any() { 
		return new AnyPath();
	}
	
	// RESPONDERS
	
	public static Responder status(final int status) {
		return new Status(status);
	}
	
	/**
	 * @param where URL to redirect to. Relative to Context Path.
	 * @return
	 */
	public static Responder redirect(final String where) {
		return new Redirect(where);
	}

	public static Responder allow() {
		return new Allow();
	}
	
	public static Responder deny() { 
		return new Status(403);
	}
}
