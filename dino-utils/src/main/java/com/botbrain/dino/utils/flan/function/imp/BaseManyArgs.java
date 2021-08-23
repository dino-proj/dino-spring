package com.botbrain.dino.utils.flan.function.imp;

import static org.apache.commons.lang3.Validate.isTrue;

import com.botbrain.dino.utils.flan.function.Function;

import org.apache.commons.lang3.StringUtils;

public abstract class BaseManyArgs implements Function {
	protected String[] args;
	protected static String msg = "need at lest one argument!";

	@Override
	public void setup(String... args) {
		isTrue(args.length > 0, msg);
		this.args = args;
	}

	@Override
	public void cleanup() {
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "('" + StringUtils.join(args, ", ") + "')";
	}
}
