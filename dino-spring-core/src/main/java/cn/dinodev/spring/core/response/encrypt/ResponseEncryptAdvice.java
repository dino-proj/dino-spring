// Copyright 2025 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.response.encrypt;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.dinodev.spring.commons.response.Response;

/**
 * 响应数据加密处理,基于注解方式
 * @author Cody Lu
 * @date 2025-09-17 18:45:20
 */

@ControllerAdvice(annotations = RestController.class)
@ConditionalOnBean(ResponseDataEncryptor.class)
public class ResponseEncryptAdvice implements ResponseBodyAdvice<Object> {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ResponseDataEncryptor responseDataEncryptor;

  @Override
  public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    return returnType.getMethod() != null &&
    // returnType是Response类型或者其子类
        Response.class.isAssignableFrom(returnType.getParameterType()) &&
        (returnType.getMethod().isAnnotationPresent(ResponseEncrypt.class));
  }

  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType,
      MediaType selectedContentType,
      Class<? extends HttpMessageConverter<?>> selectedConverterType,
      ServerHttpRequest request, ServerHttpResponse response) {

    if (body instanceof Response) {
      @SuppressWarnings("unchecked")
      Response<Object> responseObj = (Response<Object>) body;

      // 检查是否需要加密
      ResponseEncrypt responseEncrypt = returnType.getMethod().getAnnotation(ResponseEncrypt.class);
      boolean shouldEncrypt = responseEncrypt != null;

      if (shouldEncrypt) {
        // 这里ObjectMapper对data字段进行加密，并将加密后的数据设置回data字段
        var data = responseObj.getData();
        if (Objects.isNull(data)) {
          return body;
        }

        try {
          String jsonData = objectMapper.writeValueAsString(data);
          // 这里进行加密操作，假设encryptData是一个加密方法
          String encryptedData = responseDataEncryptor.encryptData(jsonData);
          responseObj.setData(encryptedData);
          return responseObj;
        } catch (Exception e) {
          throw new RuntimeException("Failed to encrypt response data", e);
        }

      }
    }

    return body;
  }
}