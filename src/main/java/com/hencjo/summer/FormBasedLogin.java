package com.hencjo.summer;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.hencjo.summer.api.RequestMatcher;
import com.hencjo.summer.api.Responder;

public class FormBasedLogin {
	private final Authenticator authenticator;
	private final SessionWriter sessionWriter;

	private final String loginUrl;
	private final String logoutUrl;
	private final String usernameParameter;
	private final String passwordParameter;
	private final Responder loggedOut;
	private final Responder loginFailure;
	private final Responder loginSuccess;
	
	public FormBasedLogin(Authenticator authenticator, SessionWriter sessionWriter, String loginUrl, String logoutUrl, String usernameParameter, String passwordParameter, Responder loggedOut, Responder loginFailure, Responder loginSuccess) {
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
				return ("POST".equals(request.getMethod()) && loginUrl.equals(request.getPathInfo()));
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
				return (logoutUrl.equals(request.getPathInfo()));
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
					return ContinueOrRespond.STOP;
				}

				System.out.println("Check credentials: " + username + "/" + password);
				if (!authenticator.authenticate(username, password)) {
					loginFailure.respond(request, response);
					return ContinueOrRespond.STOP;
				}

				SummerContextImpl.setAuthenticatedAs(username);
				sessionWriter.startSession(request, response, username);
				loginSuccess.respond(request, response);
				System.out.println("Credentials check out!");
				return ContinueOrRespond.STOP;
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
				System.out.println("Logout");
				sessionWriter.stopSession(request, response);
				loggedOut.respond(request, response);
				return ContinueOrRespond.STOP;
			}

			@Override
			public String describer() {
				return "FormBasedLogin.PerformLogoutRequest";
			}
		};
	}
}
