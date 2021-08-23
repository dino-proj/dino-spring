package com.botbrain.dino.utils.flan.function.imp;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;

@Description(value = "${name}(varName), set a var into context", usage = "'varvalue'?${name}('var1') set 'var1'='varvalue' into context ")
public class FNVarSet extends BaseOneArg {

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		context.setParam("__var:" + arg1, val);
		return val;
	}

}
