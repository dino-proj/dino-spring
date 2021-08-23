package com.botbrain.dino.utils.flan.function.imp;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;

@Description(value = "${name}(), send 302 response", usage = "'http://www.example.com/'?${name}() redirect to 'http://www.example.com/' ")
public class FNResp302 extends Base {

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		HttpServletResponse res = context.arg(1, HttpServletResponse.class);
		try {
			res.sendRedirect(val);
		} catch (IOException e) {
			// Do nothing
		}
		return val;
	}

}
