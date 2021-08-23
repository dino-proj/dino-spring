package com.botbrain.dino.utils.flan.function.imp;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;

@Description(value = "${name}(), get the nano time", usage = "${name}() return 14649727737761234")
public class FNNowNano extends Base {

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		return String.valueOf(System.nanoTime());
	}
}
