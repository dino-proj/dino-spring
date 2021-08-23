package com.botbrain.dino.utils.flan.function.imp;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;

@Description(value = "${name}(), check the value if it's empty", usage = "''?${name}() throw AbandonedException; 'abc'?${name}() YES")
public class FNNotEmpty extends Base {

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		if (val == null || val.length() == 0) {
			throw new AbandonedException("is empty");
		}
		return val;
	}

}
