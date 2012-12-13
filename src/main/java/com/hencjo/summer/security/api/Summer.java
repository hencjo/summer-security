package com.hencjo.summer.security.api;

import com.hencjo.summer.security.SummerFilterDelegate;
import com.hencjo.summer.security.SummerRule;
import com.hencjo.summer.security.requests.AnyPath;
import com.hencjo.summer.security.requests.Header;
import com.hencjo.summer.security.requests.PathBeginsWith;
import com.hencjo.summer.security.requests.PathEquals;
import com.hencjo.summer.security.responders.Allow;
import com.hencjo.summer.security.responders.Redirect;
import com.hencjo.summer.security.responders.Status;
import com.hencjo.summer.security.rules.WhenRule;

public final class Summer {
	
	public static SummerFilterDelegate summer(SummerRule ... rules) {
		return new SummerFilterDelegate(Loggers.noop(), rules);
	}

	public static SummerFilterDelegate summer(SummerLogger logger, SummerRule ... summerRules) {
		return new SummerFilterDelegate(logger, summerRules);
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
