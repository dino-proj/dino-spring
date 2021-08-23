package com.botbrain.dino.utils.flan.function.imp;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;
import com.botbrain.dino.utils.flan.function.Function;

@Description(value = "${name} call chain", usage = "")
public class OPerCallchain extends BaseOPer {

	public OPerCallchain() {
		super("CALLCHAIN", true);
	}

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		String preVal = val;
		String ret = null;
		for (Function func : fns) {
			ret = func.eval(context, preVal);
			preVal = ret;
		}
		return ret;
	}

}
