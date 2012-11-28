package com.hencjo.summer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class Cookies {
	private static final ThreadLocal<SimpleDateFormat> HTTP_DATE = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss zzz", Locale.US);
			simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
			return simpleDateFormat;
		};
	};
	
	public String setCookie(long time, String name, String path, String contents, int expiresInSeconds) {
		Date date = new Date(time + (expiresInSeconds * 1000));
		return new StringBuilder()
			.append(name).append("=")
			.append(contents)
			.append(";Expires=").append(HTTP_DATE.get().format(date))
			.append(";Path=").append(path)
			.toString();
	}
	
	public String removeCookie(String name, String path) {
		return new StringBuilder()
			.append(name).append("=deleted;Expires=Thu, 01-Jan-1970 00:00:00 GMT")
			.append(";Path=").append(path)
			.toString();
	}
}
