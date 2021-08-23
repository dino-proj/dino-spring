package com.botbrain.dino.utils.flan.function.imp;

import static org.apache.commons.lang3.Validate.isTrue;
import java.util.Date;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;

import org.apache.commons.lang3.time.FastDateFormat;

@Description(value = "${name}(fmt), get the time in formatted, ftm:default is 'yyyy-MM-dd hh:mm:ss'", usage = "${name}() return 2015-03-24 10:10:10")
public class FNNowFormatted extends Base {

	private FastDateFormat fmt;
	private static FastDateFormat fmtyyyymmddHHMMssz = FastDateFormat.getInstance("yyyy-MM-dd hh:mm:ss");

	@Override
	public void setup(String... args) {
		isTrue(args.length <= 1, "need one or less argument!");
		if (args.length == 0) {
			fmt = fmtyyyymmddHHMMssz;
		}
		fmt = FastDateFormat.getInstance(args[0]);
	}

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		return fmt.format(new Date());
	}
}
