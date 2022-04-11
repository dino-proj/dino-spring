// Copyright 2022 dinospring.cn
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.dinospring.auth.AuthzChecker;
import org.dinospring.auth.annotation.CheckAuthz;
import org.dinospring.auth.annotation.Logic;
import org.dinospring.auth.session.AuthSession;
import org.springframework.beans.factory.BeanFactory;

/**
 * 自定义Bean权限校验器
 * @author tuuboo
 * @date 2022-04-09 19:28:01
 */

public class AuthzCheckerCustomBean extends AbstractAuthzChecker<CheckAuthz, List<Pair<AuthzChecker[], Logic>>> {

  private final BeanFactory beanFactory;

  public AuthzCheckerCustomBean(BeanFactory beanFactory) {
    super(CheckAuthz.class);
    this.beanFactory = beanFactory;
  }

  @Override
  protected List<Pair<AuthzChecker[], Logic>> getMethodInvocationMeta(MethodInvocation mi,
      Collection<CheckAuthz> annosInClass,
      Collection<CheckAuthz> annosInMethod) {

    return Stream.concat(annosInClass.stream(), annosInMethod.stream())
        .map(t -> makeAnnoPredicate(t)).collect(
            Collectors.toList());
  }

  @Override
  protected boolean isPermmited(AuthSession session, MethodInvocation mi,
      List<Pair<AuthzChecker[], Logic>> predicates) {

    for (var predicate : predicates) {
      var logic = predicate.getRight();
      var beans = predicate.getLeft();
      var result = true;
      if (logic == Logic.ALL) {
        for (var bean : beans) {
          if (!bean.isPermmited(session, mi)) {
            result = false;
            break;
          }
        }
      } else {
        for (var bean : beans) {
          if (bean.isPermmited(session, mi)) {
            break;
          }
        }
        result = false;
      }
      if (!result) {
        return false;
      }
    }
    return true;
  }

  private Pair<AuthzChecker[], Logic> makeAnnoPredicate(CheckAuthz anno) {
    var beans = Arrays.asList(anno.value()).stream().filter(StringUtils::isNotBlank)
        .collect(Collectors.toList());
    var beanClass = anno.beanClass();
    return Pair.of(getBeans(beanClass, beans), anno.logic());
  }

  private AuthzChecker[] getBeans(Class<? extends AuthzChecker>[] beanClass, List<String> beanName) {
    var beans = new ArrayList<AuthzChecker>();
    for (var clazz : beanClass) {
      var bean = beanFactory.getBean(clazz);
      if (Objects.isNull(bean)) {
        distinctAdd(beans, bean);
      } else {
        distinctAdd(beans, bean);
      }

    }
    for (var name : beanName) {
      var bean = beanFactory.getBean(name, AuthzChecker.class);
      if (Objects.isNull(bean)) {
        throw new IllegalArgumentException("bean not found:" + name);
      }
      distinctAdd(beans, bean);
    }

    return beans.toArray(new AuthzChecker[beans.size()]);
  }

  private static <T> void distinctAdd(List<T> list, T el) {
    for (var e : list) {
      if (e == el) {
        return;
      }
    }
    list.add(el);
  }

}
