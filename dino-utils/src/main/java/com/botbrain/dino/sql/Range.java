package com.botbrain.dino.sql;

import java.io.Serializable;

public interface Range<T extends Serializable> extends Serializable {
  T getBegin();

  T getEnd();
}
