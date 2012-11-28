package com.hencjo.summer.responders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.hencjo.summer.api.Responder;

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