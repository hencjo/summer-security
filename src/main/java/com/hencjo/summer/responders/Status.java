package com.hencjo.summer.responders;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.hencjo.summer.api.Responder;

public final class Status implements Responder {
	private final int status;

	public Status(int status) {
		this.status = status;
	}

	@Override
	public ContinueOrRespond respond(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setStatus(status);
		return ContinueOrRespond.STOP;
	}

	@Override
	public String describer() {
		return "Status \"" + status + "\"";
	}
}