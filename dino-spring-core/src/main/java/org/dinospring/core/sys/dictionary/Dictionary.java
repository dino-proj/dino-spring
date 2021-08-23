package org.dinospring.core.sys.dictionary;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.dinospring.data.domain.TenantableEntityBase;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 * 数据字典实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@FieldNameConstants
@Entity
@Table(name = "sys_dictionary", indexes = @Index(name = "idx_sys_dictionary__tenant_key", columnList = "tenant_id,key"))
public class Dictionary extends TenantableEntityBase<Long> {

  /***
   * 数据字典键值
   */
  @NotNull(message = "数据字典j键值不能为空！")
  @Size(max = 50, message = "数据字典键值长度超长！")
  private String key;

  @OneToMany(fetch = FetchType.EAGER)
  private List<DictItem> items = new ArrayList<>();

  /***
   * 备注信息
   */
  @Size(max = 200, message = "数据字典备注长度超长！")
  private String description;

  /***
   * 是否为系统预置（预置不可删除）
   */
  private boolean deletable = false;

  /***
   * 是否可编辑
   */
  private boolean editable = false;

  @Data
  @Entity(name = "DictItem")
  @Table(name = "sys_dictionary_item", indexes = @Index(name = "idx_sys_dictionary__tenant_key_itemvalue", columnList = "tenant_id,key,itemValue"))
  public static class DictItem extends TenantableEntityBase<Long> {

    @Column
    private String key;
    /***
     * 数据字典项的显示名称
     */
    @NotNull(message = "数据字典项名称不能为空！")
    @Size(max = 100, message = "数据字典项名称长度不能大于100！")
    private String itemName;

    /***
     * 数据字典项的存储值（编码）
     */
    @Size(max = 100, message = "数据字典项编码长度不能大于100！")
    private String itemValue;

    /***
    * 数据字典项的存储值（编码）
    */
    @Size(max = 100, message = "字典对应的图标")
    private String itemIcon;

    /***
     * 排序号
     */
    private Integer orderNum;
  }
}
