package com.botbrain.dino.utils.flan.function.imp;

import javax.servlet.http.HttpServletRequest;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;

@Description(value = "${name}(), get the useragent of the request", usage = "${name}() return 'Mozilla/5.0 (Windows NT 6.3; WOW64) ....' ")
public class FNUserAgent extends Base {
	@Override
	public String eval(Context context, String val) throws AbandonedException {
		HttpServletRequest req = context.arg(0, HttpServletRequest.class);
		return req.getHeader("User-Agent");
	}
}
