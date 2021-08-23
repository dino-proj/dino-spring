package org.dinospring.core.modules.framework;

public enum PageType {
  FUNCTION(1, "FUNCTION"), LIST(2, "LIST"), CUSTOM(3, "CUSTOM");

  private int id;
  private String type;

  private PageType(int id, String type) {
    this.id = id;
    this.type = type;
  }

  public int getId() {
    return id;
  }

  public String getType() {
    return type;
  }

  @Override
  public String toString() {
    return type;
  }
}