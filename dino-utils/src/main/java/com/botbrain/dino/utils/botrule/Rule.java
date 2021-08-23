package com.botbrain.dino.utils.botrule;

public abstract class Rule {
  private String id;

  protected Rule(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

}
