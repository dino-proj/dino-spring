// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.sys.dictionary.impl;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.dinospring.core.annotion.BindDict.DictFilds;
import org.dinospring.core.service.impl.ServiceBase;
import org.dinospring.core.sys.dictionary.DictItemEntity;
import org.dinospring.core.sys.dictionary.DictionaryEntity;
import org.dinospring.core.sys.dictionary.DictionaryRepository;
import org.dinospring.core.sys.dictionary.DictionaryService;
import org.dinospring.core.sys.dictionary.DictionaryVO;
import org.dinospring.data.dao.CrudRepositoryBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Cody LU
 */

@Service("dictionaryService")
public class DictionaryServiceImpl extends ServiceBase<DictionaryEntity, Long> implements DictionaryService {

  @Autowired
  private DictionaryRepository dictionaryRepository;

  @Override
  public CrudRepositoryBase<DictionaryEntity, Long> repository() {
    return dictionaryRepository;
  }

  @Override
  public List<DictItemEntity> getKeyValueList(String key) {
    return dictionaryRepository.listDictItems(key);
  }

  @Override
  public <V> void bindItemProperty(List<V> voList, String key, DictFilds field, String dictItemNamePropertyOfVO,
      String voPropertyName) {
    // TODO Auto-generated method stub

  }

  @Override
  public <V> void bindItemProperty(List<V> voList, String key, DictFilds field,
      Function<V, String> dictItemNameGetterOfVO, BiConsumer<V, String> voSetter) {
    // TODO Auto-generated method stub

  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean createDictAndChildren(DictionaryVO dictVO) {

    return true;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean updateDictAndChildren(DictionaryVO dictVO) {

    return true;
  }

  @Override
  public boolean deleteDictAndChildren(Long id) {
    var dict = getById(id);
    // 如果不为null，则删除其子项和自身
    if (dict != null) {
      // 删除子项
      dictionaryRepository.deleteDictItems(dict.getKey());
      // 删除自身
      return removeById(id);
    }
    return false;
  }

}
