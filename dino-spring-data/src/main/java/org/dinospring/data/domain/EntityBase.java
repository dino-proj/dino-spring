package org.dinospring.data.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Entity基础父类
 * @author mazc@dibo.ltd
 * @version v2.0
 * @date 2018/12/27
 */
@Data
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
public abstract class EntityBase<K extends Serializable> implements Serializable {
  /**
   * 默认主键字段id，类型为Long型自增，转json时转换为String
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "myIdGenerator")
  @GenericGenerator(name = "myIdGenerator", strategy = "org.dinospring.data.domain.IdGenerator")
  @Schema(description = "ID")
  private K id;

  /**
   * 默认逻辑删除标记，is_deleted=0有效
   */
  @Column(name = "status", nullable = false)
  @Schema(description = "0-正常， 1-删除")
  private Integer status = 0;

  /**
   * 默认记录创建时间字段，新建时由数据库赋值
   */
  @CreatedDate
  @Column(name = "create_at", updatable = false, nullable = false)
  @Schema(description = "创建时间")
  private Date createAt;

  @CreatedBy
  @Column(name = "create_by", updatable = false, length = 32, nullable = true)
  @Schema(description = "创建者用户ID")
  private String createBy;

  @LastModifiedDate
  @Column(name = "update_at", updatable = true, nullable = false)
  @Schema(description = "最后更新时间")
  private Date updateAt;

  @LastModifiedBy
  @Column(name = "update_by", updatable = true, length = 32, nullable = true)
  @Schema(description = "最后更新更新的用户ID")
  private String updateBy;
}
