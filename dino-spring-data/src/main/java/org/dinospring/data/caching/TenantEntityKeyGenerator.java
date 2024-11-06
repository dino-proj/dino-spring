package org.dinospring.data.caching;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;

/**
 *
 * @author Cody Lu
 * @date 2022-06-29 15:18:36
 */

public class TenantEntityKeyGenerator implements KeyGenerator {

  @Override
  public Object generate(Object target, Method method, Object... params) {
    // TODO Auto-generated method stub
    return null;
  }

}
