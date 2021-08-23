package com.botbrain.dino.utils.flan.function.imp;

import javax.servlet.http.HttpServletRequest;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.ip.IpDataUtils;

public class FNProvince extends Base {
	@Override
	public String eval(Context context, String val) throws AbandonedException {
		HttpServletRequest req = context.arg(0, HttpServletRequest.class);
		return getProvince(req);
	}

	private final String getProvince(HttpServletRequest request) {
		return getProvinceFromIp(getIp(request));
	}

	private final String getIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0) {
			ip = request.getRemoteAddr();
		}
		return formatIp(ip);
	}

	private final String formatIp(String ip) {
		if (ip == null)
			return null;
		int x = 0;
		if ((x = ip.indexOf(",")) > -1)
			ip = ip.substring(x + 1).trim();
		return ip;
	}

	private final String getProvinceFromIp(String ip) {
		return IpDataUtils.getProvinceName(ip);
	}

}
