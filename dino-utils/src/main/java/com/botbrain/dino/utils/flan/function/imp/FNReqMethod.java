package com.botbrain.dino.utils.flan.function.imp;

import javax.servlet.http.HttpServletRequest;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;

@Description(value = "${name}(), get the request method", usage = "${name}() return GET/POST/PUT...")
public class FNReqMethod extends Base {

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		HttpServletRequest req = context.arg(0, HttpServletRequest.class);
		return req.getMethod();
	}

}
