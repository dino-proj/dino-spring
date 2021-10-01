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
