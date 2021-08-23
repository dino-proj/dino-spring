package com.botbrain.dino.utils.flan.function.imp;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;

@Description(value = "${name}(varName), get var from context", usage = "${name}('var1') return presetted var1 ")
public class FNVarGet extends BaseOneArg {

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		Object obj = context.getParam("__var:" + arg1);
		return obj == null ? null : obj.toString();
	}

}
