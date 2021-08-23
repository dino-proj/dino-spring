package com.botbrain.dino.utils.flan.function.imp;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;

@Description(value = "${name}(), get the time in millis", usage = "${name}() return 1464972773776")
public class FNNow extends Base {

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		return String.valueOf(System.currentTimeMillis());
	}
}
