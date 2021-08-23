package com.botbrain.dino.utils.flan.function;

import java.util.HashMap;

public class Context {
	private Object[] args;
	private HashMap<String, Object> params = new HashMap<>();

	public Context(Object... args) {
		this.args = args;
	}

	/**
	 * 
	 * @param <T>
	 * @param idx 索引
	 * @param cls 类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T arg(int idx, Class<T> cls) {
		return (T) args[idx];
	}

	public Object arg(int idx) {
		return args[idx];
	}

	/**
	 * 
	 * @param <T>
	 * @param name 名字
	 * @param cls 类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getParam(String name, Class<T> cls) {
		return (T) params.get(name);
	}

	public Object getParam(String name) {
		return params.get(name);
	}

	public void setParam(String name, Object val) {
		params.put(name, val);
	}
}
