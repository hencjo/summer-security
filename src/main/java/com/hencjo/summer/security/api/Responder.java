package com.hencjo.summer.security.api;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Responder {
	public static enum ContinueOrRespond { RESPOND, CONTINUE; }

	public ContinueOrRespond respond(HttpServletRequest request, HttpServletResponse response) throws IOException;
	String describer();
}
