package com.botbrain.dino.utils.flan.function.imp;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;
import com.botbrain.dino.utils.flan.function.Function;

@Description(value = "${name} join operator", usage = "'a'${name}'b' get 'ab'")
public class OPerJoin extends BaseOPer {

	public OPerJoin() {
		super("JOIN", true);
	}

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		StringBuilder ret = new StringBuilder();
		for (Function fn : fns) {
			ret.append(fn.eval(context, val));
		}
		return ret.toString();
	}

}
