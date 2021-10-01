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

import static org.apache.commons.lang3.Validate.notNull;

import java.util.Arrays;

import com.botbrain.dino.utils.flan.function.Function;
import com.botbrain.dino.utils.flan.function.Oper;

public abstract class BaseOPer extends BaseContainner implements Oper {
	private String op;
	private boolean expandME = false;

	protected BaseOPer(String op) {
		this.op = op;
	}

	protected BaseOPer(String op, boolean expandME) {
		this.op = op;
		this.expandME = expandME;
	}

	@Override
	public void addHead(Function fn) {
		notNull(fn, "function param should not be null");
		if (expandME && fn.getClass() == this.getClass()) {
			Function[] thefuns = ((BaseOPer) fn).getAll();
			for (int i = thefuns.length - 1; i >= 0; i--) {
				super.addHead(thefuns[i]);
			}
		} else {
			super.addHead(fn);
		}
	}

	@Override
	public void addTail(Function fn) {
		notNull(fn, "function param should not be null");
		if (expandME && fn.getClass() == this.getClass()) {
			for (Function f : ((BaseOPer) fn).getAll()) {
				super.addTail(f);
			}
		} else {
			super.addTail(fn);
		}
	}

	@Override
	public String getOpName() {
		return op;
	}

	@Override
	public String toString() {
		return this.op + Arrays.toString(fns);
	}

}
