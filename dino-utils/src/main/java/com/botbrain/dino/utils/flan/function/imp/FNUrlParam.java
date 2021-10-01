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

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;

import org.apache.commons.lang3.StringUtils;

@Description(value = "${name}(paramName, ...), extract param from the url", usage = "'http://host/abc?a=123&b=456'?${name}('a') return '123' ")
public class FNUrlParam extends BaseManyArgs {

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		if (val == null || val.length() == 0) {
			return val;
		}
		try {
			URL u = new URL(val);
			String q = u.getQuery();
			if (StringUtils.isEmpty(q)) {
				return null;
			}
			Map<String, String[]> params = getParamsMap(q, "utf-8");
			for (String arg : args) {
				String[] paramValue = params.get(arg);
				if (paramValue != null && StringUtils.isNotEmpty(paramValue[0])) {
					return paramValue[0];
				}

			}
		} catch (MalformedURLException e) {
			// Do nothing
		}
		return "";
	}

	private static Map<String, String[]> getParamsMap(String queryString, String enc) {
		Map<String, String[]> paramsMap = new HashMap<>();
		if (queryString == null || queryString.length() == 0) {
			return paramsMap;
		}
		int ampersandIndex;
		int lastAmpersandIndex = 0;
		String subStr;
		String param;
		String value;
		String[] paramPair;
		String[] values;
		String[] newValues;
		do {
			ampersandIndex = queryString.indexOf('&', lastAmpersandIndex) + 1;
			if (ampersandIndex > 0) {
				subStr = queryString.substring(lastAmpersandIndex, ampersandIndex - 1);
				lastAmpersandIndex = ampersandIndex;
			} else {
				subStr = queryString.substring(lastAmpersandIndex);
			}
			paramPair = subStr.split("=");
			param = paramPair[0];
			value = paramPair.length == 1 ? "" : paramPair[1];
			try {
				value = URLDecoder.decode(value, enc);
			} catch (UnsupportedEncodingException ignored) {
				// Do nothing
			}
			if (paramsMap.containsKey(param)) {
				values = paramsMap.get(param);
				int len = values.length;
				newValues = new String[len + 1];
				System.arraycopy(values, 0, newValues, 0, len);
				newValues[len] = value;
			} else {
				newValues = new String[] { value };
			}
			paramsMap.put(param, newValues);
		} while (ampersandIndex > 0);

		return paramsMap;
	}
}
