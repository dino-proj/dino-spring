package com.botbrain.dino.utils.flan.function.imp;

import static org.apache.commons.lang3.Validate.isTrue;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;

import org.apache.commons.lang3.StringUtils;

@Description(value = "${name}(n), n: default 1, increase the value by n", usage = "5?${name}(10), return 15")
public class FNIncrease extends Base {
	private int step;

	@Override
	public void setup(String... args) {
		isTrue(args.length <= 1, "need one or less argument!");
		if (args.length == 0) {
			step = 1;
		}
		step = Integer.parseInt(args[0]);
	}

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		if (StringUtils.isEmpty(val) || !StringUtils.isNumeric(val)) {
			return "1";
		}
		long preval = Long.valueOf(val);
		return String.valueOf(preval + step);
	}
}
