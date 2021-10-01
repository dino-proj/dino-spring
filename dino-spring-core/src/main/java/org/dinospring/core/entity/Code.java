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

package org.dinospring.core.entity;

import java.util.Arrays;

public interface Code {
  enum STATUS {
    OK(0, "ok"), DELETED(1, "deleted"),
    //审核相关
    CHECKING(10, "checking"), ACCEPTED(0, "accepted"), DENNIED(12, "dennied"),
    //锁住或者过期
    LOCKED(99, "locked"), EXPERIED(100, "experied");

    int id;
    String name;

    private STATUS(int id, String name) {
      this.id = id;
      this.name = name;
    }

    public int getId() {
      return id;
    }

    public String getName() {
      return this.name;
    }

    public boolean eq(Integer id) {
      if (id == null) {
        return false;
      }
      return this.id == id;
    }

    public boolean eq(String name) {
      if (name == null) {
        return false;
      }
      return this.name.equalsIgnoreCase(name);
    }

    public static STATUS of(int id) {
      return Arrays.stream(STATUS.values()).filter(v -> v.eq(id)).findFirst().orElse(null);
    }

    public static STATUS of(String name) {
      return Arrays.stream(STATUS.values()).filter(v -> v.eq(name)).findFirst().orElse(null);
    }

    @Override
    public String toString() {
      return this.name().toLowerCase();
    }
  }

  enum CHECK {
    ING(0, "checking"), ACCEPT(1, "accept"), DENNIED(2, "dennied");

    int id;
    String name;

    private CHECK(int id, String name) {
      this.id = id;
      this.name = name;
    }

    public int getId() {
      return id;
    }

    public String getName() {
      return this.name;
    }

    public static CHECK of(int id) {
      return Arrays.stream(CHECK.values()).filter(v -> v.id == id).findFirst().orElse(null);
    }

    public static CHECK of(String name) {
      return Arrays.stream(CHECK.values()).filter(v -> v.eq(name)).findFirst().orElse(null);
    }

    public boolean eq(Integer id) {
      if (id == null) {
        return false;
      }
      return id.equals(this.id);
    }

    public boolean eq(String name) {
      if (name == null) {
        return false;
      }
      return this.name.equalsIgnoreCase(name);
    }

    @Override
    public String toString() {
      return this.name().toLowerCase();
    }
  }
}
