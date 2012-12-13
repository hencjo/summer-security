package com.hencjo.summer.security.responders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.hencjo.summer.security.api.Responder;

public final class Allow implements Responder {
	@Override
	public Responder.ContinueOrRespond respond(HttpServletRequest request, HttpServletResponse response) {
		return Responder.ContinueOrRespond.CONTINUE;
	}

	@Override
	public String describer() {
		return "Allow";
	}
}