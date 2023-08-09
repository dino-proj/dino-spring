// Copyright 2021 dinodev.cn
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.dinospring.core.modules.importandexport.dataexport;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

import org.dinospring.commons.response.Response;
import org.dinospring.commons.utils.TypeUtils;
import org.dinospring.core.annotion.param.ParamPageable;
import org.dinospring.core.annotion.param.ParamSort;
import org.dinospring.core.annotion.param.ParamTenant;
import org.dinospring.core.modules.importandexport.handler.DataExportHandler;
import org.springframework.web.bind.annotation.PostMapping;

import io.swagger.v3.oas.annotations.Operation;

/**
 * @author JL
 * @Date: 2021/9/30
 */
public interface ExportController<S extends DataExportHandler<E>, E> {

  /**
   * 数据存储接口
   *
   * @return
   */
  S exportHandler();

  /**
   * 文件导出
   * @param httpServletResponse
   * @return
   * @throws IOException
   */
  @Operation(summary = "导出")
  @ParamTenant
  @ParamPageable
  @ParamSort
  @PostMapping("export")
  default Response<Boolean> dataExport(HttpServletResponse httpServletResponse) throws IOException {
    DataExport.simpleWrite(httpServletResponse.getOutputStream(),
        TypeUtils.getGenericParamClass(this, DataExportHandler.class, 1), exportHandler());
    return Response.success();
  }

  /**
   * 文件导出
   * @param pathName 文件目录
   * @return
   * @throws IOException
   */
  default Response<Boolean> dataExport(String pathName) throws IOException {
    DataExport.simpleWrite(pathName, TypeUtils.getGenericParamClass(this, DataExportHandler.class, 1),
        exportHandler());
    return Response.success();
  }

}
