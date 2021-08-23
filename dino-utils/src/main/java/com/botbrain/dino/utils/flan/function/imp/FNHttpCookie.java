package com.botbrain.dino.utils.flan.function.imp;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;

import org.apache.commons.lang3.StringUtils;

@Description(value = "${name}(cookieName, ...), read a value from cookie", usage = "${name}('userid', 'useridnew'), read a cookie named 'userid' or 'useridnew' from request")
public class FNHttpCookie extends BaseManyArgs {

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		for (String arg : args) {
			String str = getCookieByName(context.arg(0, HttpServletRequest.class), arg);
			if (StringUtils.isNotEmpty(str)) {
				return str;
			}
		}
		return null;
	}

	private final String getCookieByName(HttpServletRequest req, String name) {
		if (req.getCookies() == null) {
			return null;
		}
		for (Cookie ck : req.getCookies()) {
			if (ck.getName().equals(name)) {
				return ck.getValue();
			}
		}
		return null;
	}

}
