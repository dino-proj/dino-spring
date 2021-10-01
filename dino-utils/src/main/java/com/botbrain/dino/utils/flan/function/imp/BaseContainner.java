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
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.botbrain.dino.utils.flan.function.Function;
import com.botbrain.dino.utils.flan.function.FunctionContainner;

import org.apache.commons.lang3.ArrayUtils;

public abstract class BaseContainner implements FunctionContainner {

	protected Function[] fns = new Function[0];

	@Override
	public void setup(String... args) {
	}

	@Override
	public void cleanup() {
	}

	@Override
	public void addHead(Function fn) {
		notNull(fn, "function param should not be null");
		fns = ArrayUtils.addAll(new Function[] { fn }, fns);
	}

	@Override
	public void addTail(Function fn) {
		notNull(fn, "function param should not be null");
		fns = ArrayUtils.add(fns, fn);
	}

	@Override
	public Function[] getAll() {
		return ArrayUtils.clone(fns);
	}

	@Override
	public Iterator<Function> iterator() {
		return new Iterator<Function>() {
			private int i = -1;

			@Override
			public boolean hasNext() {
				return i + 1 < fns.length;
			}

			@Override
			public Function next() {
				i++;
				if (i >= fns.length) {
					throw new NoSuchElementException();
				}
				return fns[i];
			}

			@Override
			public void remove() {
				fns = ArrayUtils.remove(fns, i);
				i--;
			}
		};
	}

	@Override
	public int length() {
		return fns.length;
	}

	@Override
	public Function get(int i) {
		return fns[i];
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + Arrays.toString(fns);
	}
}
