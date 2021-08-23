package com.botbrain.dino.utils.flan.function.imp;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;
import com.botbrain.dino.utils.flan.function.Function;

@Description(value = "${name} or operator", usage = "'' ${name} 'b' get 'b'")
public class OPerOr extends BaseOPer {

	public OPerOr() {
		super("OR", true);
	}

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		String ret = "";
		for (Function fn : fns) {
			ret = fn.eval(context, val);
			if (ret != null && ret.length() > 0) {
				break;
			}
		}

		return ret;
	}

}
