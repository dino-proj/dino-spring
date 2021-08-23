package org.dinospring.commons.context;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.dinospring.commons.sys.Tenant;
import org.dinospring.commons.sys.User;
import org.springframework.context.ApplicationContext;
import org.springframework.data.util.CastUtils;

public class DinoContextThreadLocalImpl implements DinoContext {
  private static final ThreadLocal<Map<String, Object>> resources = new InheritableThreadLocalMap<>();
  private static final String KEY_CURRENT_TENANT = DinoContextThreadLocalImpl.class.getName() + "_CURRENT_TENANT";
  private static final String KEY_CURRENT_USER = DinoContextThreadLocalImpl.class.getName() + "_CURRENT_USER";

  private static ApplicationContext applicationContext;

  public static Tenant getCurrentTenant() {
    return CastUtils.cast(MapUtils.getObject(resources.get(), KEY_CURRENT_TENANT));
  }

  public static void setCurrentTenant(Tenant tenant) {
    ensureResourcesInitialized();
    if (tenant == null) {
      resources.get().remove(KEY_CURRENT_TENANT);
    } else {
      resources.get().put(KEY_CURRENT_TENANT, tenant);
    }
  }

  public static void remove() {
    resources.remove();
  }

  public static <T extends User<?>> T getCurrentUser() {
    return CastUtils.cast(MapUtils.getObject(resources.get(), KEY_CURRENT_USER));
  }

  public static <T extends User<?>> void setCurrentUser(T user) {
    ensureResourcesInitialized();
    if (user == null) {
      resources.get().remove(KEY_CURRENT_USER);
    } else {
      resources.get().put(KEY_CURRENT_USER, user);
    }
  }

  public static void setApplicationContext(ApplicationContext context) {
    applicationContext = context;
  }

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  private static void ensureResourcesInitialized() {
    if (resources.get() == null) {
      resources.set(new HashMap<>());
    }
  }

  private static final class InheritableThreadLocalMap<T extends Map<String, Object>>
      extends InheritableThreadLocal<T> {

    /**
     * @param parentValue the parent value, a HashMap as defined in the {@link #initialValue()} method.
     * @return the HashMap to be used by any parent-spawned child threads (a clone of the parent HashMap).
     */
    @Override
    protected T childValue(T parentValue) {
      if (parentValue != null) {
        return ObjectUtils.clone(parentValue);
      } else {
        return null;
      }
    }
  }

  @Override
  public <K extends Serializable> User<K> currentUser() {
    return getCurrentUser();
  }

  @Override
  public Tenant currentTenant() {
    return getCurrentTenant();
  }

  @Override
  public <K extends Serializable> void currentUser(User<K> user) {
    setCurrentUser(user);
  }

  @Override
  public void currentTenant(Tenant tenant) {
    setCurrentTenant(tenant);
  }

}
