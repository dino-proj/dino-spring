package com.botbrain.dino.utils.flan.function;

import com.botbrain.dino.utils.flan.AbandonedException;

public interface Function {
	public void setup(String... args);

	public String eval(Context context, String val) throws AbandonedException;

	public void cleanup();
}
