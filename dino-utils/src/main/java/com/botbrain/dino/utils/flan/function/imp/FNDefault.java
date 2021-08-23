package com.botbrain.dino.utils.flan.function.imp;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;

@Description(value = "${name}(defaultValue) is used to set defaultValue if the preValue is empty.", usage = "express1()?${name}('the defaultValue'), if the result value of 'express1()' is empty. the default value is instead.")
public class FNDefault extends BaseOneArg {

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		if (val == null || val.length() == 0) {
			return arg1;
		}
		return val;
	}

}
