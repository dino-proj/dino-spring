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

package org.dinospring.commons.context;

import java.io.Serializable;

import org.dinospring.commons.sys.Tenant;
import org.dinospring.commons.sys.User;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

/**
 *
 * @author Cody LU
 */
public class ContextHelper {

  public static final ContextHelper INST = new ContextHelper();

  private static DinoContext context;

  private static ApplicationContext applicationContext;

  private ContextHelper() {
  }

  public static void setApplicationContext(ApplicationContext context) {
    applicationContext = context;
  }

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  public static void setDinoContext(DinoContext dinoAppContext) {
    context = dinoAppContext;
  }

  public static DinoContext getDinoContext() {
    return context;
  }

  public static String currentTenantId() {
    Assert.notNull(context, "DinoContext bean not found");
    return context.currentTenant() == null ? null : context.currentTenant().getId();
  }

  public static Tenant currentTenant() {
    Assert.notNull(context, "DinoContext bean not found");
    return context.currentTenant();
  }

  public static <K extends Serializable> User<K> currentUser() {
    Assert.notNull(context, "DinoContext bean not found");
    return context.currentUser();
  }

  /**
   * 查找Bean
   * @param <T>
   * @param requiredType Bean的类型
   * @return 找不到返回{@code null}
   */
  public static <T> T findBean(Class<T> requiredType) {
    return applicationContext.getBean(requiredType);
  }

  /**
   * 查找Bean
   * @param <T>
   * @param name Bean的名字
   * @param requiredType Bean的类型
   * @return 找不到返回{@code null}
   */
  public static <T> T findBean(String name, Class<T> requiredType) {
    return applicationContext.getBean(name, requiredType);
  }

}
