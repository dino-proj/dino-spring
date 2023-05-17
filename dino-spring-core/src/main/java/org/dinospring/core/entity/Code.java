// Copyright 2021 dinodev.cn
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

/**
 *
 * @author tuuboo
 */

public interface Code {
  /**
   * 状态值
   * @return
   */
  int getId();

  /**
   * 状态名
   * @return
   */
  String getName();

  enum STATUS implements Code {
    //正常
    OK(0, "ok"), DELETED(1, "deleted"),
    //锁住或者过期
    LOCKED(99, "locked"), EXPERIED(100, "experied");

    int id;
    String name;

    private STATUS(int id, String name) {
      this.id = id;
      this.name = name;
    }

    @Override
    public int getId() {
      return id;
    }

    @Override
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

  /**
   * 审核
   */
  enum CHECK implements Code {
    //审核中
    CHECKING(0, "checking"),
    //审核通过
    ACCEPTED(1, "accepted"),
    //审核未通过
    DENNIED(2, "dennied");

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

  /**
   * 任务状态
   */
  enum TASK implements Code {

    //任务进度
    INIT(1, "init"), RUNNING(2, "running"), SUCCEED(0, "succeed"), FAILD(3, "faild"), TIMEOUT(4, "timeout");

    int id;
    String name;

    private TASK(int id, String name) {
      this.id = id;
      this.name = name;
    }

    public int getId() {
      return id;
    }

    public String getName() {
      return this.name;
    }

    public static TASK of(int id) {
      return Arrays.stream(TASK.values()).filter(v -> v.id == id).findFirst().orElse(null);
    }

    public static TASK of(String name) {
      return Arrays.stream(TASK.values()).filter(v -> v.eq(name)).findFirst().orElse(null);
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
