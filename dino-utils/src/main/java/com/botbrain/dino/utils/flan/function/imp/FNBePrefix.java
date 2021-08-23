package com.botbrain.dino.utils.flan.function.imp;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;

@Description(value = "${name}(prefix) is used to make string start with a prefix.", usage = "express1()?${name}('/'), if the result value of 'express1()' is not start with '/'. add it.")
public class FNBePrefix extends BaseOneArg {

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		if (val == null || val.length() == 0) {
			return arg1;
		}
		return val.startsWith(arg1) ? val : arg1 + val;
	}

}
