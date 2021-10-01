// Copyright 2021 dinospring.cn
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.botbrain.dino.utils.flan.function.imp;

import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;
import com.botbrain.dino.utils.flan.utils.CookieHandler;

import org.apache.commons.lang3.StringUtils;

@Description(value = "${name}(cookieName, domain, maxAge), write the preValue as cookie named 'cookieName' on 'domain'", usage = "example1: '1'?${name}('flag', 'example.com', '')")
public class FNHttpCookieSet extends BaseThreeArgs {
	private class TimeCal {
		int v;

		TimeCal(int v) {
			this.v = v;
		}

		public int time() {
			return v;
		}
	}

	private class TimeCalToday extends TimeCal {
		TimeCalToday() {
			super(0);
		}

		@Override
		public int time() {
			Calendar now = Calendar.getInstance();
			return (23 - now.get(Calendar.HOUR_OF_DAY)) * 60 * 60 + (59 - now.get(Calendar.MINUTE)) * 60 + 60
					- now.get(Calendar.SECOND);
		}
	}

	private TimeCal cal;
	private boolean autoDomain = false;

	@Override
	public void setup(String... args) {
		super.setup(args);
		autoDomain = "auto".equalsIgnoreCase(arg2);
		if (StringUtils.isNumeric(arg3)) {
			cal = new TimeCal(Integer.parseInt(arg3));
		} else if ("today".equalsIgnoreCase(arg3)) {
			cal = new TimeCalToday();
		} else if ("session".equalsIgnoreCase(arg3)) {
			cal = new TimeCal(-1);
		} else if ("del".equalsIgnoreCase(arg3) || "delete".equalsIgnoreCase(arg3)) {
			cal = new TimeCal(0);
		} else {
			int n = Integer.parseInt(arg3.substring(0, arg3.length() - 1));
			switch (arg3.charAt(arg3.length() - 1)) {
				case 'h':
				case 'H':
					cal = new TimeCal(n * 60 * 60);
					break;
				case 'd':
				case 'D':
					cal = new TimeCal(n * 24 * 60 * 60);
					break;
				case 'm':
				case 'M':
					cal = new TimeCal(n * 30 * 24 * 60 * 60);
					break;
				case 'y':
				case 'Y':
					cal = new TimeCal(n * 360 * 24 * 60 * 60);
					break;
				default:
					throw new IllegalArgumentException("age param not valid:" + arg3);
			}
		}
	}

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		HttpServletRequest req = context.arg(0, HttpServletRequest.class);
		HttpServletResponse res = context.arg(1, HttpServletResponse.class);
		if (autoDomain) {
			CookieHandler.addCookie(val, res, arg1, req.getHeader("Host"), cal.time());
		} else {
			CookieHandler.addCookie(val, res, arg1, arg2, cal.time());
		}
		return val;
	}

}
