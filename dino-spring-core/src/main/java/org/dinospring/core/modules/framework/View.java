package org.dinospring.core.modules.framework;

import lombok.Data;

@Data
public abstract class View {
  private String id;
  private String title;
  private String template;
  private String previewImage;

  public abstract String getPageType();
}
