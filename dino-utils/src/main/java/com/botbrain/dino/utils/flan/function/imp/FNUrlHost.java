package com.botbrain.dino.utils.flan.function.imp;

import java.net.MalformedURLException;
import java.net.URL;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;

@Description(value = "${name}(), extract host from the url", usage = "'http://www.example.com/'?${name}() return 'www.example.com' ")
public class FNUrlHost extends Base {

	@Override
	public String eval(Context context, String val) throws AbandonedException {

		if (val == null || val.length() == 0) {
			return val;
		}
		try {
			URL u = new URL(val);
			return u.getHost();
		} catch (MalformedURLException e) {
			// Do nothing
		}
		return "";
	}

}
