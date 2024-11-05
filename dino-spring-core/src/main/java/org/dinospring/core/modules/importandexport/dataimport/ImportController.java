// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.importandexport.dataimport;

import java.io.IOException;

import org.dinospring.commons.response.Response;
import org.dinospring.commons.utils.TypeUtils;
import org.dinospring.core.annotion.param.ParamTenant;
import org.dinospring.core.modules.importandexport.handler.DataImportHandler;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;

/**
 * @author JL
 * @param <T>
 * @Date: 2021/9/30
 */
public interface ImportController<S extends DataImportHandler<T>, T> {

  /**
   * 数据存储接口
   *
   * @return
   */
  S importHandler();

  /**
   * 数据模型Class类
   * @return
   */
  default Class<T> modelClass() {
    return TypeUtils.getGenericParamClass(this, ImportController.class, 1);
  }

  /**
   * 文件导入
   * @param file 文件流
   * @return
   * @throws IOException
   */
  @Operation(summary = "数据导入")
  @ParamTenant
  @PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  default Response<Boolean> dataImport(@RequestParam("file") MultipartFile file) throws IOException {
    DataImport.doRead(file.getInputStream(), modelClass(), e -> importHandler().importData(e));
    return Response.success();
  }

  /**
   * 文件导入
   * @param pathName 文件目录
   * @return
   * @throws IOException
   */
  default Response<Boolean> dataImport(String pathName) throws IOException {
    DataImport.doRead(pathName, modelClass(), e -> importHandler().importData(e));
    return Response.success();
  }

}
