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
