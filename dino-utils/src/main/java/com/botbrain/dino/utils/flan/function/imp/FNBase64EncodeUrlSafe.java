package com.botbrain.dino.utils.flan.function.imp;

import static org.apache.commons.lang3.Validate.isTrue;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;

@Description(value = "${name} is used to encode plain string to base64, using URL-safe variation of the base64 algorithm", usage = "${name}([charset]), the charset is optional, 'UTF-8' as default.")
public class FNBase64EncodeUrlSafe extends Base {

	private Charset charSet;

	@Override
	public void setup(String... args) {
		isTrue(args.length <= 1, "need one or less argument!");
		if (args.length == 0) {
			charSet = StandardCharsets.UTF_8;
		}
		charSet = Charset.forName(args[0]);
	}

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		if (val == null || val.length() == 0) {
			return val;
		}
		return Base64.getUrlEncoder().encodeToString(val.getBytes(charSet));
	}
}
