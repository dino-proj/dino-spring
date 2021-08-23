package org.dinospring.core.annotion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import lombok.RequiredArgsConstructor;

/**
 * 绑定字典注解
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface BindDict {

    /***
     * 绑定数据字典Key
     * @return
     */
    String key();

    /***
     * 数据字典项取值字段
     * @return
     */
    DictFilds field() default DictFilds.VALUE;

    /**
     * 数据字典KEY的优先查询值，默认是租户级别
     * @return
     */
    Scope scope() default Scope.TENANT;

    @RequiredArgsConstructor
    public enum DictFilds {
        NAME("item_name"), VALUE("item_value"), ICON("item_icon");

        private final String fieldName;

        @Override
        public String toString() {
            return fieldName;
        }
    }

    public enum Scope {
        SYSTEM, TENANT, TENENT_PREFER
    }

}
