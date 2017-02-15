package com.hencjo.summer.security;

import static org.junit.Assert.*;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;

public class CookiesTest {
	@Test
	public void setCookieContents() {
		assertEquals("name=the-user;Expires=Thu, 01-Jan-1970 01:00:00 GMT;Path=/", Cookies.cookie(Instant.EPOCH, "name", "/", "the-user", Duration.ofSeconds(3600)));
		assertEquals("name=another-user;Expires=Thu, 01-Jan-1970 01:00:00 GMT;Path=/", Cookies.cookie(Instant.EPOCH, "name", "/", "another-user", Duration.ofSeconds(3600)));
	}

	@Test
	public void setCookieName() {
		assertEquals("name=admin;Expires=Thu, 01-Jan-1970 01:00:00 GMT;Path=/", Cookies.cookie(Instant.EPOCH, "name", "/", "admin", Duration.ofSeconds(3600)));
		assertEquals("other-name=admin;Expires=Thu, 01-Jan-1970 01:00:00 GMT;Path=/", Cookies.cookie(Instant.EPOCH, "other-name", "/", "admin", Duration.ofSeconds(3600)));
	}
	
	@Test
	public void setCookieExpires() {
		assertEquals("name=user;Expires=Thu, 01-Jan-1970 01:00:00 GMT;Path=/", Cookies.cookie(Instant.EPOCH, "name", "/", "user", Duration.ofSeconds(3600)));
		assertEquals("name=user;Expires=Thu, 01-Jan-1970 01:00:03 GMT;Path=/", Cookies.cookie(Instant.EPOCH.plusMillis(3000), "name", "/", "user", Duration.ofSeconds(3600)));
		assertEquals("name=user;Expires=Thu, 01-Jan-1970 02:00:00 GMT;Path=/", Cookies.cookie(Instant.EPOCH.plusMillis(3600000), "name", "/", "user", Duration.ofSeconds(3600)));
	}
	
	@Test
	public void removeName() {
		assertEquals("name=deleted;Expires=Thu, 01-Jan-1970 00:00:00 GMT;Path=/", Cookies.removeCookie("name", "/"));
		assertEquals("other-name=deleted;Expires=Thu, 01-Jan-1970 00:00:00 GMT;Path=/", Cookies.removeCookie("other-name", "/"));
	}
}
