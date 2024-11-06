// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.auth;

/**
 * 内置资源操作类型
 * @author Cody Lu
 * @date 2022-04-07 01:26:30
 */

public interface Operations {

  static final String CREATE = ":create";
  static final String READ = ":read";
  static final String UPDATE = ":update";
  static final String DELETE = ":delete";
  static final String EXECUTE = ":execute";
  static final String LIST = ":list";
  static final String LIST_ALL = ":listAll";
  static final String SEARCH = ":search";
  static final String IMPORT = ":import";
  static final String EXPORT = ":export";
  static final String UPLOAD = ":upload";
  static final String DOWNLOAD = ":download";

  static final String CREATE_ACTION = "create";
  static final String READ_ACTION = "read";
  static final String UPDATE_ACTION = "update";
  static final String DELETE_ACTION = "delete";
  static final String EXECUTE_ACTION = "execute";
  static final String LIST_ACTION = "list";
  static final String LIST_ALL_ACTION = "listAll";
  static final String SEARCH_ACTION = "search";
  static final String IMPORT_ACTION = "import";
  static final String EXPORT_ACTION = "export";
  static final String UPLOAD_ACTION = "upload";
  static final String DOWNLOAD_ACTION = "download";

  static final String CREATE_NAME = "创建";
  static final String READ_NAME = "读取";
  static final String UPDATE_NAME = "更新";
  static final String DELETE_NAME = "删除";
  static final String EXECUTE_NAME = "执行";
  static final String LIST_NAME = "列表";
  static final String LIST_ALL_NAME = "全部列表";
  static final String SEARCH_NAME = "搜索";
  static final String IMPORT_NAME = "导入";
  static final String EXPORT_NAME = "导出";
  static final String UPLOAD_NAME = "上传";
  static final String DOWNLOAD_NAME = "下载";
}
