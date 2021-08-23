package org.dinospring.commons.context;

import java.io.Serializable;

import org.dinospring.commons.sys.Tenant;
import org.dinospring.commons.sys.User;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

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

  public static <T> T findBean(Class<T> cls) {
    return applicationContext.getBean(cls);
  }

}
