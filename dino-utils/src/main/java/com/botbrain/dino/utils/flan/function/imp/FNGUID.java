package com.botbrain.dino.utils.flan.function.imp;

import static org.apache.commons.lang3.Validate.isTrue;
import java.util.UUID;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;

@Description(value = "${name}(len), gen a guid by System.currentTimeMillis() + randomstring[len -13]", usage = "example: ${name}('16')?set_cookie('__guid', 'domain.com', '20001'), gen a guid and write to the client")
public class FNGUID extends BaseOneArg {
	private int len;

	@Override
	public void setup(String... args) {
		super.setup(args);
		len = Integer.parseInt(arg1);
		isTrue(len >= 13, "the length of guid must greater or eq than 13");
	}

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		String value;
		if (len <= 13) {
			value = String.valueOf(System.currentTimeMillis());
		} else {
			value = System.currentTimeMillis() + UUID.randomUUID().toString();
		}
		return value;
	}
}
