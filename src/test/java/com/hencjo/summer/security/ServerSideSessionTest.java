package com.hencjo.summer.security;

import static org.mockito.Mockito.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Test;

public class ServerSideSessionTest {
	@Test
	public void testSessionWriterStopSessionRemovesJSESSIONCookie() throws Exception {
		givenCookiesAssertThatJSESSIONCookieIsRemoved(new Cookie[] {
					cookie("JSESSIONID", "/", null)
				}, new String[] { 
					"JSESSIONID=deleted;Expires=Thu, 01-Jan-1970 00:00:00 GMT;Path=/" 
				});
	}
	
	@Test
	public void forOtherPathsThanSlash() throws Exception {
		givenCookiesAssertThatJSESSIONCookieIsRemoved(new Cookie[] {
				cookie("JSESSIONID", "/path/", null)
			}, new String[] { 
				"JSESSIONID=deleted;Expires=Thu, 01-Jan-1970 00:00:00 GMT;Path=/path/" 
		});
		
		givenCookiesAssertThatJSESSIONCookieIsRemoved(new Cookie[] {
				cookie("JSESSIONID", "/other", null)
			}, new String[] { 
				"JSESSIONID=deleted;Expires=Thu, 01-Jan-1970 00:00:00 GMT;Path=/other" 
		});
	}
	
	@Test
	public void ifMoreThanOneJSESSIONCookieBothShouldBeRemoved() throws Exception {
		givenCookiesAssertThatJSESSIONCookieIsRemoved(new Cookie[] {
				cookie("JSESSIONID", "/", null), 
				cookie("JSESSIONID", "/two", null)
			}, new String[] { 
				"JSESSIONID=deleted;Expires=Thu, 01-Jan-1970 00:00:00 GMT;Path=/", 
				"JSESSIONID=deleted;Expires=Thu, 01-Jan-1970 00:00:00 GMT;Path=/two" 
			});
	}
	
	@Test
	public void onlyJSESSIONIDShouldBeRemoved() throws Exception {
		givenCookiesAssertThatJSESSIONCookieIsRemoved(new Cookie[] {
				cookie("JSESSIONID", "/two", null), 
				cookie("ANOTHER", "/two", null)
			}, new String[] { 
				"JSESSIONID=deleted;Expires=Thu, 01-Jan-1970 00:00:00 GMT;Path=/two" 
			});
	}

	private static void givenCookiesAssertThatJSESSIONCookieIsRemoved(Cookie[] cookies, String[] expected) throws Exception {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		HttpSession session = mock(HttpSession.class);
		when(request.getCookies()).thenReturn(cookies);
		when(request.getSession(true)).thenReturn(session);
		
		ServerSideSession sss = new ServerSideSession("attribute");
		sss.sessionWriter().stopSession(request, response);
		
		verify(session, times(1)).invalidate();
		for (String e : expected) {
			verify(response, times(1)).setHeader("Set-Cookie", e);
		}
		verifyNoMoreInteractions(response);
	}

	private static Cookie cookie(String name, String path, String value) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath(path);
		return cookie;
	}
}
