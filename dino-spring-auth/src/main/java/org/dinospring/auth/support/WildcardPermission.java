// Copyright 2022 dinodev.cn
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
package org.dinospring.auth.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.dinospring.auth.Permission;

/**
 *
 * @author tuuboo
 * @date 2022-04-07 03:02:53
 */
public class WildcardPermission implements Permission, Serializable {

  private static final long serialVersionUID = -8984100818079079079L;

  protected static final String WILDCARD_TOKEN = "*";
  protected static final String PART_DIVIDER_TOKEN = ":";
  protected static final String SUBPART_DIVIDER_TOKEN = ",";

  private List<Set<String>> parts;

  public WildcardPermission() {
    this.parts = new ArrayList<Set<String>>();
  }

  public WildcardPermission(@Nonnull String permPattern) {
    setParts(permPattern);
  }

  protected void setParts(String permPattern) {
    this.parts = new ArrayList<>();
    var partStr = StringUtils.split(StringUtils.trim(permPattern), PART_DIVIDER_TOKEN);
    for (var part : partStr) {
      var subPartStr = StringUtils.split(part, SUBPART_DIVIDER_TOKEN);
      var subParts = Set.of(subPartStr);
      parts.add(subParts);
    }
  }

  protected List<Set<String>> getParts() {
    return this.parts;
  }

  @Override
  public boolean implies(Permission permission) {
    if (!(permission instanceof WildcardPermission)) {
      return false;
    }
    var otherParts = ((WildcardPermission) permission).getParts();

    int i = 0;
    for (Set<String> otherPart : otherParts) {
      // If this permission has less parts than the other permission, everything after the number of parts contained
      // in this permission is automatically implied, so return true
      if (getParts().size() - 1 < i) {
        return true;
      } else {
        Set<String> part = getParts().get(i);
        if (!part.contains(WILDCARD_TOKEN) && !part.containsAll(otherPart)) {
          return false;
        }
        i++;
      }
    }

    // If this permission has more parts than the other parts, only imply it if all of the other parts are wildcards
    for (; i < getParts().size(); i++) {
      Set<String> part = getParts().get(i);
      if (!part.contains(WILDCARD_TOKEN)) {
        return false;
      }
    }

    return true;
  }

  @Override
  public String toString() {
    StringBuilder buffer = new StringBuilder();
    for (Set<String> part : parts) {
      if (buffer.length() > 0) {
        buffer.append(PART_DIVIDER_TOKEN);
      }
      Iterator<String> partIt = part.iterator();
      while (partIt.hasNext()) {
        buffer.append(partIt.next());
        if (partIt.hasNext()) {
          buffer.append(SUBPART_DIVIDER_TOKEN);
        }
      }
    }
    return buffer.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof WildcardPermission) {
      WildcardPermission wp = (WildcardPermission) o;
      return parts.equals(wp.parts);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return parts.hashCode();
  }

  /**
   * 创建一个新的WildcardPermission对象
   * @param permissionStr 权限字符串，格式为："resource:action" || "path1:parth2:action1,action2"
   * @return
   */
  public static WildcardPermission of(String permissionStr) {
    Objects.requireNonNull(permissionStr);
    return new WildcardPermission(permissionStr);
  }

  /**
   * 创建一个新的WildcardPermission对象
   * @param resource 权限资源, 如："user"，"client:user"
   * @param actions 权限动作，如："create"，"create,update"
   * @return
   */
  public static WildcardPermission of(String resource, String... actions) {
    Objects.requireNonNull(resource);
    Objects.requireNonNull(actions);
    var permissionStr = resource + PART_DIVIDER_TOKEN + StringUtils.join(actions, SUBPART_DIVIDER_TOKEN);
    return new WildcardPermission(permissionStr);
  }

  /**
   * 创建一个新的WildcardPermission对象
   * @param resource 权限资源, 如："user"，"client:user"
   * @param actions 权限动作，如："create"，"create,update"
   * @return
   */
  public static WildcardPermission of(String resource, Collection<String> actions) {
    Objects.requireNonNull(resource);
    Objects.requireNonNull(actions);
    var permissionStr = resource + PART_DIVIDER_TOKEN + StringUtils.join(actions, SUBPART_DIVIDER_TOKEN);
    return new WildcardPermission(permissionStr);
  }

}