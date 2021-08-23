package com.botbrain.dino.utils.flan.function.imp;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Function;

import org.apache.commons.lang3.StringUtils;

public class FNToLowerCase implements Function {

	@Override
	public void setup(String... args) {
		// Do nothing
	}

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		if (StringUtils.isNotBlank(val)) {
			val = val.toLowerCase();
		}
		return val;
	}

	@Override
	public void cleanup() {
		// Do nothing
	}

}
