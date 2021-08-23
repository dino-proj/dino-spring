package com.botbrain.dino.utils.flan.function.imp;

import static org.apache.commons.lang3.Validate.isTrue;

import com.botbrain.dino.utils.flan.function.Function;

public abstract class BaseOneArg implements Function {
	protected String arg1;
	protected static String msg = "need just one argument!";

	@Override
	public void setup(String... args) {
		isTrue(args.length == 1, msg);
		arg1 = args[0];
	}

	@Override
	public void cleanup() {
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "('" + arg1 + "')";
	}
}
