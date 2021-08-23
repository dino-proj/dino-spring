package org.dinospring.core.sys.dictionary;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DictionaryVO extends Dictionary {
  private static final long serialVersionUID = 5882152649072986950L;

  private List<Dictionary> children;
}
