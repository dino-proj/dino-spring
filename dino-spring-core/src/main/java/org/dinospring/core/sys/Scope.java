package org.dinospring.core.sys;

import java.io.Serializable;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;

/**
 * scope 接口，用于定义scope范围
 */
public interface Scope extends Serializable {
  /** 
   * scope 的名字
   * @return
   */
  String getName();

  /**
   * scope 顺序
   * @return
   */
  int getOrder();

  /**
   * 比自己优先级低的Scopes
   * @param includeThis 是否包含本Scope
   * @return 比自己小或等于自己排序的Scopes
   */
  Scope[] lowerScopes(boolean includeThis);

  /**
   * 比自己优先级高的Scopes
   * @param includeThis 是否包含本Scope
   * @return 比自己大或等于自己排序的Scopes
   */
  Scope[] higherScopes(boolean includeThis);

  @AllArgsConstructor
  enum DEFAULT implements Scope {
    SYS("sys", 0), TENANT("tenant", 1), PAGE("page", 2), USER("user", 3);

    private static final Scope[] EMPTY_ARRAY = new Scope[0];

    private String name;
    private int order;

    @Override
    public String getName() {
      return name;
    }

    @Override
    public int getOrder() {
      return order;
    }

    @Override
    public Scope[] lowerScopes(boolean includeThis) {
      return Arrays.stream(DEFAULT.values())
          .filter(s -> s.getOrder() < this.getOrder() || (includeThis && s.getOrder() == this.getOrder()))
          .collect(Collectors.toList()).toArray(EMPTY_ARRAY);
    }

    @Override
    public Scope[] higherScopes(boolean includeThis) {
      return Arrays.stream(DEFAULT.values())
          .filter(s -> s.getOrder() > this.getOrder() || (includeThis && s.getOrder() == this.getOrder()))
          .collect(Collectors.toList()).toArray(EMPTY_ARRAY);
    }

    @Override
    public String toString() {
      return name;
    }

    public static Function<String, Scope> provider() {
      return DEFAULT::of;
    }

    public static Scope of(String name) {
      return DEFAULT.valueOf(name.toUpperCase());
    }

  }
}
