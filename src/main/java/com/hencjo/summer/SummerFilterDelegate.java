package com.hencjo.summer;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.hencjo.summer.api.Responder;

public final class SummerFilterDelegate {
	private final SummerRule[] summerRules;
	
	public SummerFilterDelegate(SummerRule ... summerRules) {
		this.summerRules = summerRules;
	}
	
	public void doFilter(ServletRequest sreq, ServletResponse sres, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) sreq;
		HttpServletResponse response = (HttpServletResponse) sres;
		
		for (SummerRule sr : summerRules) {
			System.out.println("Applying " + sr.describer() + " to " + pretty(request));
			
			if (!sr.rule.matches(request)) continue;
			System.out.println(" ==> MATCH!");
			
			if (sr.authorizer.respond(request, response) == Responder.ContinueOrRespond.STOP) {
				System.out.println(" ==> STOPPED");
				return;
			}
			
			System.out.println(" ==> ALLOWED");
			filterChain.doFilter(sreq, sres);
			return;
		}
		
		response.sendError(500, "Reached Autumn. Please end Summer Security's configuration with a deny-all to avoid this.");
	}

	private static String pretty(HttpServletRequest req) {
		return new StringBuilder()
			.append(req.getMethod().toUpperCase()).append(" ").append(req.getRequestURI())
			.toString();
	}
}
