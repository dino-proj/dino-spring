// Copyright 2021 dinospring.cn
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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
