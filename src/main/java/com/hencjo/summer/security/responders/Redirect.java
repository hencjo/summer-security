package com.hencjo.summer.security.responders;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.hencjo.summer.security.api.Responder;

public final class Redirect implements Responder {
	private final String where;

	public Redirect(String where) {
		this.where = where;
	}

	@Override
	public ContinueOrRespond respond(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.sendRedirect(request.getContextPath() + where);
		return ContinueOrRespond.STOP;
	}

	@Override
	public String describer() {
		return "Redirect \"" + where + "\"";
	}
}