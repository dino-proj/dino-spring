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
