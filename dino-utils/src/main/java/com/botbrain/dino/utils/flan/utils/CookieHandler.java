package com.botbrain.dino.utils.flan.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class CookieHandler {

	public final static String P3P_VALUE = "CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"";

	// 获取cookie值
	public static String getCookieByName(Cookie[] cookies, String name) {
		for (Cookie ck : cookies) {
			if (ck.getName().equals(name)) {
				return ck.getValue();
			}
		}
		return null;
	}

	public static Cookie getCookie(Cookie[] cookies, String name) {
		for (Cookie ck : cookies) {
			if (ck.getName().equals(name)) {
				return ck;
			}
		}
		return null;
	}

	public static Cookie addCookie(String value, HttpServletResponse res, String key, String domain, int maxAge) {
		Cookie c = CookieHandler.getNewCookie(key, domain, maxAge, value);
		res.addCookie(c);
		res.addHeader("P3P", P3P_VALUE);
		return c;
	}

	// 创建cookie
	public static Cookie getNewCookie(String key, String domain, int maxAge, String value) {
		Cookie c = new Cookie(key, value);
		c.setDomain(domain);
		c.setPath("/");
		c.setMaxAge(maxAge);
		return c;
	}

}
