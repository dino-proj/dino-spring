package org.dinospring.core.sys.dictionary;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.dinospring.core.annotion.BindDict.DictFilds;
import org.dinospring.core.service.Service;
import org.dinospring.core.sys.dictionary.Dictionary.DictItem;

public interface DictionaryService extends Service<Dictionary, Long> {

    /***
     * 获取对应类型的键值对
     * @param key 字典键值
     * @param field 要获取的字典字段名
     * @return
     */
    List<DictItem> getKeyValueList(String key);

    /**
    * 添加字典定义及其子项
    * @param dictVO
    * @return
    */
    default boolean addDictTree(DictionaryVO dictVO) {
        return createDictAndChildren(dictVO);
    }

    /**
     * 将dict的某个属性，绑定到vo中
     * @param <V> vo的类型
     * @param voList vo的列表
     * @param key 字典的key
     * @param field 字典中要绑定的值
     * @param dictItemNamePropertyOfVO vo中字典name值的属性名
     * @param voPropertyName vo中接收值的属性名
     */
    <V> void bindItemProperty(List<V> voList, String key, DictFilds field, String dictItemNamePropertyOfVO,
            String voPropertyName);

    /**
     * 将dict的某个属性，绑定到vo中
     * @param <V> vo的类型
     * @param voList vo的列表
     * @param key 字典的key
     * @param field 字典中要绑定的值
     * @param dictItemNameGetterOfVO vo中字典name值的获取方法
     * @param voSetter vo中接收值的Setter方法
     */
    <V> void bindItemProperty(List<V> voList, String key, DictFilds field, Function<V, String> dictItemNameGetterOfVO,
            BiConsumer<V, String> voSetter);

    /**
     * 添加字典定义及其子项
     * @param dictVO
     * @return
     */
    boolean createDictAndChildren(DictionaryVO dictVO);

    /**
     * 更新字典定义及其子项
     * @param dictVO
     * @return
     */
    boolean updateDictAndChildren(DictionaryVO dictVO);

    /**
     * 删除字典定义及其子项
     * @param id
     * @return
     */
    boolean deleteDictAndChildren(Long id);

}
