package com.hencjo.summer.api;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Responder {
	public static enum ContinueOrRespond { STOP, CONTINUE; }

	public ContinueOrRespond respond(HttpServletRequest request, HttpServletResponse response) throws IOException;
	String describer();
}
