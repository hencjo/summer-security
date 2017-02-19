package com.hencjo.summer.security;

import org.junit.Test;

import java.time.Instant;
import java.time.Period;
import java.time.ZoneId;

import static org.junit.Assert.assertEquals;

public class CookiesTest {
	@Test
	public void setCookieContents() {
		assertEquals("name=the-user;Expires=Thu, 01-Jan-1970 01:00:00 GMT;Path=/", Cookies.cookie(Instant.EPOCH, "name", "/", "the-user", 3600));
		assertEquals("name=another-user;Expires=Thu, 01-Jan-1970 01:00:00 GMT;Path=/", Cookies.cookie(Instant.EPOCH, "name", "/", "another-user", 3600));
	}

	@Test
	public void setCookieName() {
		assertEquals("name=admin;Expires=Thu, 01-Jan-1970 01:00:00 GMT;Path=/", Cookies.cookie(Instant.EPOCH, "name", "/", "admin", 3600));
		assertEquals("other-name=admin;Expires=Thu, 01-Jan-1970 01:00:00 GMT;Path=/", Cookies.cookie(Instant.EPOCH, "other-name", "/", "admin", 3600));
	}

	@Test
	public void setCookieExpires() {
		assertEquals("name=user;Expires=Thu, 01-Jan-1970 01:00:00 GMT;Path=/", Cookies.cookie(Instant.EPOCH, "name", "/", "user", 3600));
		assertEquals("name=user;Expires=Thu, 01-Jan-1970 01:00:03 GMT;Path=/", Cookies.cookie(Instant.EPOCH.plusMillis(3000), "name", "/", "user", 3600));
		assertEquals("name=user;Expires=Thu, 01-Jan-1970 02:00:00 GMT;Path=/", Cookies.cookie(Instant.EPOCH.plusMillis(3600000), "name", "/", "user", 3600));

		Instant gmt = Instant.EPOCH.atZone(ZoneId.of("GMT")).plus(Period.ofYears(90)).toInstant();
		assertEquals("name=user;Expires=Thu, 01-Jan-2060 01:00:00 GMT;Path=/", Cookies.cookie(gmt, "name", "/", "user", 3600));
		assertEquals("name=user;Expires=Mon, 17-Dec-2029 00:00:00 GMT;Path=/", Cookies.cookie(Instant.EPOCH, "name", "/", "user", 60 * 365 * 24 * 3600));
	}
	
	@Test
	public void removeName() {
		assertEquals("name=deleted;Expires=Thu, 01-Jan-1970 00:00:00 GMT;Path=/", Cookies.removeCookie("name", "/"));
		assertEquals("other-name=deleted;Expires=Thu, 01-Jan-1970 00:00:00 GMT;Path=/", Cookies.removeCookie("other-name", "/"));
	}
}
