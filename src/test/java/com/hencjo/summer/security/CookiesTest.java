package com.hencjo.summer.security;

import static org.junit.Assert.*;
import org.junit.Test;

public class CookiesTest {
	@Test
	public void setCookieContents() {
		Cookies cookies = new Cookies();
		assertEquals("name=the-user;Expires=Thu, 01-Jan-1970 01:00:00 GMT;Path=/", cookies.cookie(0, "name", "/", "the-user", 3600));
		assertEquals("name=another-user;Expires=Thu, 01-Jan-1970 01:00:00 GMT;Path=/", cookies.cookie(0, "name", "/", "another-user", 3600));
	}

	@Test
	public void setCookieName() {
		Cookies cookies = new Cookies();
		assertEquals("name=admin;Expires=Thu, 01-Jan-1970 01:00:00 GMT;Path=/", cookies.cookie(0, "name", "/", "admin", 3600));
		assertEquals("other-name=admin;Expires=Thu, 01-Jan-1970 01:00:00 GMT;Path=/", cookies.cookie(0, "other-name", "/", "admin", 3600));
	}
	
	@Test
	public void setCookieExpires() {
		Cookies cookies = new Cookies();
		assertEquals("name=user;Expires=Thu, 01-Jan-1970 01:00:00 GMT;Path=/", cookies.cookie(0, "name", "/", "user", 3600));
		assertEquals("name=user;Expires=Thu, 01-Jan-1970 01:00:03 GMT;Path=/", cookies.cookie(3000, "name", "/", "user", 3600));
		assertEquals("name=user;Expires=Thu, 01-Jan-1970 02:00:00 GMT;Path=/", cookies.cookie(3600000, "name", "/", "user", 3600));
	}
	
	@Test
	public void removeName() {
		Cookies cookies = new Cookies();
		assertEquals("name=deleted;Expires=Thu, 01-Jan-1970 00:00:00 GMT;Path=/", cookies.removeCookie("name", "/"));
		assertEquals("other-name=deleted;Expires=Thu, 01-Jan-1970 00:00:00 GMT;Path=/", cookies.removeCookie("other-name", "/"));
	}
}
