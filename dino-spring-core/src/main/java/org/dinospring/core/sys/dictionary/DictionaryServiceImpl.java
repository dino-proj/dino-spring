package org.dinospring.core.sys.dictionary;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.dinospring.core.annotion.BindDict.DictFilds;
import org.dinospring.core.service.impl.ServiceBase;
import org.dinospring.core.sys.dictionary.Dictionary.DictItem;
import org.dinospring.data.dao.CURDRepositoryBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Primary
@Service("dictionaryService")
@Slf4j
public class DictionaryServiceImpl extends ServiceBase<Dictionary, Long> implements DictionaryService {

  @Autowired
  private DictionaryRepository dictionaryRepository;

  @Autowired
  private DictItemRepository dictItemRepository;

  @Override
  public CURDRepositoryBase<Dictionary, Long> getRepository() {
    return dictionaryRepository;
  }

  @Override
  public List<DictItem> getKeyValueList(String key) {
    return dictItemRepository.listDictItems(key);
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
    return removeById(id);
  }

}
