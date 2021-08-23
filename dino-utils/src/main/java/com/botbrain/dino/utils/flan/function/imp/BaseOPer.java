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
