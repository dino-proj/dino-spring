package com.botbrain.dino.utils.flan.function.imp;

import com.botbrain.dino.utils.flan.function.Function;

public abstract class Base implements Function {

	@Override
	public void setup(String... args) {
	}

	@Override
	public void cleanup() {
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
