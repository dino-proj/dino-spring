package com.botbrain.dino.utils.flan.function;

public interface FunctionContainner extends Function, Iterable<Function> {

	public void addHead(Function left);

	public void addTail(Function right);

	public Function[] getAll();

	public int length();

	public Function get(int i);
}
