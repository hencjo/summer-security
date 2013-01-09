package com.hencjo.summer.security;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.hencjo.summer.security.api.*;
import com.hencjo.summer.security.utils.HttpServletRequests;

public final class FormBasedLogin {
	private final SummerAuthenticatedUser summerAuthenticatedUser = new SummerAuthenticatedUser();
	
	private final SummerLogger summerLogger;
	private final Authenticator authenticator;
	private final SessionWriter sessionWriter;

	private final String loginUrl;
	private final String logoutUrl;
	private final String usernameParameter;
	private final String passwordParameter;
	private final Responder loggedOut;
	private final Responder loginFailure;
	private final Responder loginSuccess;

	
	public FormBasedLogin(SummerLogger summerLogger, Authenticator authenticator, SessionWriter sessionWriter, String loginUrl, String logoutUrl, String usernameParameter, String passwordParameter, Responder loggedOut, Responder loginFailure, Responder loginSuccess) {
		this.summerLogger = summerLogger;
		this.authenticator = authenticator;
		this.sessionWriter = sessionWriter;
		this.loginUrl = loginUrl;
		this.logoutUrl = logoutUrl;
		this.usernameParameter = usernameParameter;
		this.passwordParameter = passwordParameter;
		this.loggedOut = loggedOut;
		this.loginFailure = loginFailure;
		this.loginSuccess = loginSuccess;
	}
	
	public RequestMatcher loginRequest() {
		return new RequestMatcher() {
			@Override
			public boolean matches(HttpServletRequest request) {
				return ("POST".equals(request.getMethod()) && HttpServletRequests.path(request).equals(loginUrl));
			}
			
			@Override
			public String describer() {
				return "FormBasedLoginRequest";
			}
		};
	}
	
	public RequestMatcher logoutRequest() {
		return new RequestMatcher() {
			@Override
			public boolean matches(HttpServletRequest request) {
				return HttpServletRequests.path(request).equals(logoutUrl);
			}
			
			@Override
			public String describer() {
				return "FormBasedLogoutRequest";
			}
		};
	}

	public Responder performLoginRequest() {
		return new Responder() {
			@Override
			public ContinueOrRespond respond(HttpServletRequest request, HttpServletResponse response) throws IOException {
				String username = request.getParameter(usernameParameter);
				String password = request.getParameter(passwordParameter);

				if (username == null || password == null) {
					loginFailure.respond(request, response);
					return ContinueOrRespond.RESPOND;
				}

				if (!authenticator.authenticate(username, password)) {
					summerLogger.info("FormBasedLogin. Authentication failed for user \"" + username + "\".");
					loginFailure.respond(request, response);
					return ContinueOrRespond.RESPOND;
				}

				summerAuthenticatedUser.set(request, username);
				sessionWriter.startSession(request, response, username);
				loginSuccess.respond(request, response);
				summerLogger.info("FormBasedLogin. Authentication successful for user \"" + username + "\".");
				return ContinueOrRespond.RESPOND;
			}


			@Override
			public String describer() {
				return "FormBasedLogin.PerformLoginRequest";
			}
		};
	}
	
	public Responder performLogoutRequest() {
		return new Responder() {
			@Override
			public ContinueOrRespond respond(HttpServletRequest request, HttpServletResponse response) throws IOException {
				summerLogger.info("FormBasedLogin. Logout");
				sessionWriter.stopSession(request, response);
				loggedOut.respond(request, response);
				return ContinueOrRespond.RESPOND;
			}

			@Override
			public String describer() {
				return "FormBasedLogin.PerformLogoutRequest";
			}
		};
	}
}
