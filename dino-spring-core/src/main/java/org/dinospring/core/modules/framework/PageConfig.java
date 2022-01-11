package org.dinospring.core.modules.framework;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import org.dinospring.commons.annotion.AnnotionedJsonTypeIdResolver;

import java.io.Serializable;

/**
 *
 * @author tuuboo
 */

@JsonTypeInfo(use = Id.CUSTOM, include = As.PROPERTY, visible = true, property = "@t")
@JsonTypeIdResolver(AnnotionedJsonTypeIdResolver.class)
public interface PageConfig extends Serializable {


  /**
   * 入参处理
   */
  default void processReq() {

  }

  /**
   * 出参处理
   */
  default void processVo() {

  }
}
