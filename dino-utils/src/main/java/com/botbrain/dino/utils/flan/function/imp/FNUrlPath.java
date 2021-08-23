package com.botbrain.dino.utils.flan.function.imp;

import java.net.MalformedURLException;
import java.net.URL;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;

@Description(value = "${name}(), extract path from the url", usage = "'http://www.example.com/abc.html'?${name}() return '/abc.html' ")
public class FNUrlPath extends Base {

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		if (val == null || val.length() == 0) {
			return val;
		}
		try {
			URL u = new URL(val);
			StringBuilder sb = new StringBuilder(val.length());
			String p = u.getPath();
			if (p == null || p.length() == 0) {
				sb.append("/");
			} else {
				sb.append(p);
			}
			String q = u.getQuery();
			if (q != null) {
				sb.append("?").append(q);
			}
			return sb.toString();
		} catch (MalformedURLException e) {
			// Do nothing
		}
		return "";
	}

}
