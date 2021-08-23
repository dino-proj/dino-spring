package com.botbrain.dino.utils.flan.function;

public class FunctionMetaInfo {
	private String name;
	private String description;
	private String usage;
	private Class<? extends Function> fnClass;

	public FunctionMetaInfo(String name, Class<? extends Function> fnClass) {
		this.name = name;
		this.fnClass = fnClass;

	}

	public FunctionMetaInfo(String name, Class<? extends Function> fnClass, String description, String usage) {
		super();
		this.name = name;
		this.description = description;
		this.usage = usage;
		this.fnClass = fnClass;
	}

	public Class<? extends Function> getFnClass() {
		return fnClass;
	}

	public String getFnClassName() {
		return fnClass.getName();
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getUsage() {
		return usage;
	}

}
