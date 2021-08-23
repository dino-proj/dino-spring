package com.botbrain.dino.utils.flan.function.imp;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;

@Description(value = "the const function used to hold const value.", usage = "do not used directly, using String or Int expression")
public class FNConst extends BaseOneArg {

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		return arg1;
	}

	public String value() {
		return arg1;
	}

}
