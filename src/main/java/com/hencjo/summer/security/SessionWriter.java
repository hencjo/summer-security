package com.hencjo.summer.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SessionWriter<T> {
	void startSession(HttpServletRequest request, HttpServletResponse response, T t) throws Exception;
	void stopSession(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
