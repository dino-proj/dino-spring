// Copyright 2022 dinospring.cn
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

package org.dinospring.core.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 将filter的异常重新抛出，让其被全局异常处理器捕获
 * @author tuuboo
 * @date 2022-05-06 19:46:56
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class ErrorRethrowController implements ErrorController {

  @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public void handleError(HttpServletRequest request) throws Throwable {
    if (request.getAttribute("javax.servlet.error.exception") != null) {
      throw (Throwable) request.getAttribute("javax.servlet.error.exception");
    }
  }
}
