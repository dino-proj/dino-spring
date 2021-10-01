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
