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
