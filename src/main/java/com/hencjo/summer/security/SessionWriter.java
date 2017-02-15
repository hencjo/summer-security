package com.hencjo.summer.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SessionWriter {
	void startSession(HttpServletRequest request, HttpServletResponse response, String username) throws Exception;
	void stopSession(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
