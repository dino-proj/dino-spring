package org.dinospring.core.sys.dictionary;

import java.util.List;

import org.dinospring.core.sys.dictionary.Dictionary.DictItem;
import org.dinospring.data.dao.CURDRepositoryBase;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DictItemRepository extends CURDRepositoryBase<DictItem, Long> {

  @Query("FROM DictItem where key=:key ORDER BY orderNum")
  List<DictItem> listDictItems(@Param("key") String key);
}
