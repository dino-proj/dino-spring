package com.botbrain.dino.utils.flan.function.imp;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.BuildException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;
import com.botbrain.dino.utils.flan.function.Function;
import com.botbrain.dino.utils.flan.function.Struct;

@Description(value = "map struct used to map value to another", usage = "'1'?['1':'abc', _:456] return abc")
public class StructMap implements Struct {

	private Function defaultExpresion;
	private Map<String, Function> mapExpresion = new HashMap<String, Function>();

	@Override
	public void setup(String... args) {
		// Do nothing
	}

	public void setDefault(Function exp) {
		this.defaultExpresion = exp;
	}

	public void addCase(String key, Function exp) {
		if (mapExpresion.containsKey(key)) {
			throw new BuildException("map key:'" + key + "' exists.");
		}
		mapExpresion.put(key, exp);
	}

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		String key = val;
		if (key == null) {
			key = "";
		}
		Function f = mapExpresion.get(key);
		if (f != null) {
			return f.eval(context, val);
		} else if (defaultExpresion != null) {
			return defaultExpresion.eval(context, val);
		} else {
			return val;
		}
	}

	@Override
	public void cleanup() {
		mapExpresion.clear();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, Function> nv : mapExpresion.entrySet()) {
			sb.append("'").append(nv.getKey()).append("':").append(nv.getValue().toString()).append(',');
		}
		if (defaultExpresion != null) {
			sb.append("_:").append(defaultExpresion);
		} else {
			sb.deleteCharAt(sb.length() - 1);
		}
		return "[" + sb.toString() + "]";
	}

}
