package com.botbrain.dino.utils.flan.function.imp;

import static org.apache.commons.lang3.Validate.isTrue;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;

import eu.bitwalker.useragentutils.UserAgent;

public class FNUAToProperty extends BaseOneArg {
	protected String userAgent;
	protected static String msg = "need just one argument!";

	@Override
	public void setup(String... args) {
		isTrue(args.length == 1, msg);
		arg1 = args[0];
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "('" + arg1 + "')";
	}

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		if (val == null || "".equals(val)) {
			return "";
		}
		UserAgent ua = null;
		try {
			ua = UserAgent.parseUserAgentString(val);
			if (ua == null) {
				return "";
			} else {
				if (arg1.equals("browser")) {
					return ua.getBrowser().getName();
				} else if (arg1.equals("browserVersion")) {
					return ua.getBrowserVersion().getVersion();
				} else {
					return "";
				}
			}
		} catch (Exception e) {
			return "";
		}
	}
}
