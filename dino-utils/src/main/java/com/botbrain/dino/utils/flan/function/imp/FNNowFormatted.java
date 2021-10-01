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
import java.util.Date;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;

import org.apache.commons.lang3.time.FastDateFormat;

@Description(value = "${name}(fmt), get the time in formatted, ftm:default is 'yyyy-MM-dd hh:mm:ss'", usage = "${name}() return 2015-03-24 10:10:10")
public class FNNowFormatted extends Base {

	private FastDateFormat fmt;
	private static FastDateFormat fmtyyyymmddHHMMssz = FastDateFormat.getInstance("yyyy-MM-dd hh:mm:ss");

	@Override
	public void setup(String... args) {
		isTrue(args.length <= 1, "need one or less argument!");
		if (args.length == 0) {
			fmt = fmtyyyymmddHHMMssz;
		}
		fmt = FastDateFormat.getInstance(args[0]);
	}

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		return fmt.format(new Date());
	}
}
