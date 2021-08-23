package com.botbrain.dino.utils.flan.function.imp;

import javax.servlet.http.HttpServletRequest;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;

@Description(value = "${name}(header), get header of http request", usage = "${name}('Reffer'), read a header named 'userid' from request")
public class FNHttpHeader extends BaseOneArg {

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		HttpServletRequest req = context.arg(0, HttpServletRequest.class);
		return req.getHeader(arg1);
	}

}
