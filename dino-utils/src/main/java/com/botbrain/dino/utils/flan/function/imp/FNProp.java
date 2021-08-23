package com.botbrain.dino.utils.flan.function.imp;

import java.util.Objects;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

@Description(value = "${name}(propName), get property from object", usage = "${name}('title'), eq 'object.getTile()'")
public class FNProp extends BaseOneArg {
	private String method;

	@Override
	public void setup(String... args) {
		super.setup(args);
		method = "get" + StringUtils.capitalize(arg1);
	}

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		Object obj = context.arg(0);
		try {
			return Objects.toString(MethodUtils.invokeMethod(obj, method));
		} catch (Exception e) {
			// DO Nothing
		}
		return "";
	}

}
