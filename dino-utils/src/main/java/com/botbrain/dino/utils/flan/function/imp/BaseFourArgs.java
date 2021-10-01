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

import com.botbrain.dino.utils.flan.function.Function;

public abstract class BaseFourArgs implements Function {
	protected String arg1;
	protected String arg2;
	protected String arg3;
	protected String arg4;
	protected static String msg = "need just four arguments!";

	@Override
	public void setup(String... args) {
		isTrue(args.length == 4, msg);
		arg1 = args[0];
		arg2 = args[1];
		arg3 = args[2];
		arg4 = args[3];
	}

	@Override
	public void cleanup() {
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "('" + arg1 + "','" + arg2 + "','" + arg3 + "','" + arg4 + "')";
	}
}
