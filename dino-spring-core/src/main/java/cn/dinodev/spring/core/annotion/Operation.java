// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.annotion;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 权限-操作 类型定义
 * @author Cody Lu
 */
@Getter
@AllArgsConstructor
public class Operation {

  /**
   * 操作权限编码
   */
  private final String code;

  /**
   * 操作权限说明
   */
  private final String label;

  /**
   * 操作权限类型 - 查看列表
   */
  public static final Operation OP_LOGIN = new Operation("login", "登录系统");

  /**
   * 操作权限类型 - 查看列表
   */
  public static final Operation OP_LOGOUT = new Operation("logout", "退出登录");

  /**
   * 操作权限类型 - 查看列表
   */
  public static final Operation OP_LIST = new Operation("list", "查看列表");

  /**
   * 操作权限类型 - 查看详情
   */
  public static final Operation OP_DETAIL = new Operation("detail", "查看详情");

  /**
   * 操作权限类型 - 添加
   */
  public static final Operation OP_CREATE = new Operation("create", "创建");

  /**
   * 操作权限类型 - 更新
   */
  public static final Operation OP_UPDATE = new Operation("update", "更新");

  /**
   * 操作权限类型 - 删除
   */
  public static final Operation OP_DELETE = new Operation("delete", "删除");

  /**
   * 操作权限类型 - 批量删除
   */
  public static final Operation OP_BATCH_DELETE = new Operation("batchDelete", "批量删除");

  /**
   * 操作权限类型 - 导入
   */
  public static final Operation OP_IMPORT = new Operation("import", "导入");

  /**
   * 操作权限类型 - 导出
   */
  public static final Operation OP_EXPORT = new Operation("export", "导出");
}
