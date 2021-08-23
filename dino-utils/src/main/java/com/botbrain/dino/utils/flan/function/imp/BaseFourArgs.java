package com.botbrain.dino.utils.flan.function.imp;

import static org.apache.commons.lang3.Validate.isTrue;

import com.botbrain.dino.utils.flan.function.Function;

public abstract class BaseFourArgs implements Function {
	protected String arg1;
	protected String arg2;
	protected String arg3;
	protected String arg4;
	protected static String msg = "need just four arguments!";

	@Override
	public void setup(String... args) {
		isTrue(args.length == 4, msg);
		arg1 = args[0];
		arg2 = args[1];
		arg3 = args[2];
		arg4 = args[3];
	}

	@Override
	public void cleanup() {
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "('" + arg1 + "','" + arg2 + "','" + arg3 + "','" + arg4 + "')";
	}
}
