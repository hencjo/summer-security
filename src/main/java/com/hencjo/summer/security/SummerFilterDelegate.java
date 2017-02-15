package com.hencjo.summer.security;

import com.hencjo.summer.security.api.Responder;
import com.hencjo.summer.security.api.SummerLogger;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class SummerFilterDelegate {
	private final SummerLogger logger;
	private final SummerRule[] summerRules;
	
	public SummerFilterDelegate(SummerLogger logger, SummerRule ... summerRules) {
		this.logger = logger;
		this.summerRules = summerRules;
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws Exception {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		for (SummerRule sr : summerRules) {
			logger.debug("Applying " + sr.describer() + " to " + pretty(req));
			if (!sr.rule.matches(req)) {
				continue;
			}
			logger.debug(sr.describer() + " to " + pretty(req) + " ==> MATCH!");
			
			if (sr.authorizer.respond(req, res) == Responder.ContinueOrRespond.RESPOND) {
				logger.debug(" ==> STOPPED");
				return;
			}
			
			logger.debug(" ==> ALLOWED");
			filterChain.doFilter(request, response);
			return;
		}
		
		res.sendError(500, "Reached Autumn. Please end Summer Security's configuration with a deny-all to avoid this.");
	}

	private static String pretty(HttpServletRequest req) {
		return req.getMethod().toUpperCase() + " " + req.getRequestURI();
	}
}
