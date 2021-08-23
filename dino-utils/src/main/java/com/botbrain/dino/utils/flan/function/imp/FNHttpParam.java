package com.botbrain.dino.utils.flan.function.imp;

import javax.servlet.http.HttpServletRequest;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;

import org.apache.commons.lang3.StringUtils;

@Description(value = "${name}(paramName, ...), read a value from request params", usage = "${name}('param', 'paramnew'), read a param named 'param' or 'paramnew' from request")
public class FNHttpParam extends BaseManyArgs {

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		HttpServletRequest req = context.arg(0, HttpServletRequest.class);
		for (String arg : args) {
			String str = req.getParameter(arg);
			if (StringUtils.isNotEmpty(str)) {
				return str;
			}
		}
		return null;
	}

}
