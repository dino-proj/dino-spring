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
import java.util.UUID;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;

@Description(value = "${name}(len), gen a guid by System.currentTimeMillis() + randomstring[len -13]", usage = "example: ${name}('16')?set_cookie('__guid', 'domain.com', '20001'), gen a guid and write to the client")
public class FNGUID extends BaseOneArg {
	private int len;

	@Override
	public void setup(String... args) {
		super.setup(args);
		len = Integer.parseInt(arg1);
		isTrue(len >= 13, "the length of guid must greater or eq than 13");
	}

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		String value;
		if (len <= 13) {
			value = String.valueOf(System.currentTimeMillis());
		} else {
			value = System.currentTimeMillis() + UUID.randomUUID().toString();
		}
		return value;
	}
}
