package com.botbrain.dino.utils.flan.function.imp;

import javax.servlet.http.HttpServletRequest;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;

@Description(value = "${name}(), get the reffer of http request", usage = "${name}() return 'http://...'")
public class FNReferer extends Base {

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		HttpServletRequest req = context.arg(0, HttpServletRequest.class);
		return req.getHeader("Referer");
	}

}
