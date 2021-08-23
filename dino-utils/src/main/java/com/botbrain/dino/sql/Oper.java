package com.botbrain.dino.sql;

public enum Oper {
  EQ("="), GT(">"), LT("<"), NE("<>"), GTE(">="), LTE("<="), LIKE("LIKE"), NOT_LIKE("NOT LIKE"), IN("IN"),
  NOT_IN("NOT_IN"), IS_NULL("IS NULL"), IS_NOT_NULL("IS NOT NULL"), BETWEEN("BETWEEN ? AND ?"), EXISTS("EXISTS");

  private String op;

  /**
   * 
   * @param op the oprator
   */
  private Oper(String op) {
    this.op = op;
  }

  public String getOp() {
    return op;
  }

  @Override
  public String toString() {
    return this.op;
  }
}
