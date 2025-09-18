// Copyright 2025 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.response.encrypt;

import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.dinodev.spring.commons.response.Response;
import lombok.extern.slf4j.Slf4j;

/**
 * 响应数据加密处理,基于注解方式
 * @author Cody Lu
 * @date 2025-09-17 18:45:20
 */

@ControllerAdvice(annotations = RestController.class)
@ConditionalOnProperty(prefix = ResponseEncryptProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@Slf4j
public class ResponseEncryptAdvice implements ResponseBodyAdvice<Object> {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ResponseDataEncryptor responseDataEncryptor;

  private static final TypeReference<Map<String, Object>> MAP_TYPE_REF = new TypeReference<>() {
  };

  public ResponseEncryptAdvice() {
    log.info("---->> response-encrypt: ResponseEncryptAdvice loaded.");
  }

  @Override
  public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    var isSupport = returnType.getMethod() != null &&
    // returnType是Response类型或者其子类
        Response.class.isAssignableFrom(returnType.getParameterType()) &&
        (returnType.getMethod().isAnnotationPresent(ResponseEncrypt.class));

    // 打印日志，方便调试
    if (log.isDebugEnabled()) {
      log.debug("---->>> response-encrypt: {}, method: {}, returnType: {}",
          isSupport, returnType.getMethod().getName(), returnType.getParameterType().getName());
    }

    return isSupport;
  }

  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType,
      MediaType selectedContentType,
      Class<? extends HttpMessageConverter<?>> selectedConverterType,
      ServerHttpRequest request, ServerHttpResponse response) {

    if (body instanceof Response<?> responseObj) {

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
          var jsonData = objectMapper.writeValueAsBytes(data);
          // 这里进行加密操作，假设encryptData是一个加密方法
          String encryptedData = responseDataEncryptor.encryptData(jsonData);

          var omCopy = objectMapper.copy();
          omCopy.addMixIn(Response.class, ResponseMixin.class);
          var exPropMap = omCopy.convertValue(responseObj, MAP_TYPE_REF);

          return new ResponseWithEncryptedData(responseObj, encryptedData, exPropMap);
        } catch (Exception e) {
          throw new RuntimeException("Failed to encrypt response data", e);
        }

      }
    }

    return body;
  }

  private static interface ResponseMixin {
    @JsonIgnore
    String getCode();

    @JsonIgnore
    String getMsg();

    @JsonIgnore
    Object getData();

    @JsonIgnore
    Long getCost();
  }

  /**
   * 响应数据加密后的包装类, 将response的data字段替换为加密后的字符串，并将其他字段保留，放到extensions字段中，在json序列化时，将extensions字段展开
   * @author Cody Lu
   * @date 2025-09-17 19:20:45
   */
  private static class ResponseWithEncryptedData extends Response<String> {

    private Map<String, Object> extensions;

    public ResponseWithEncryptedData(Response<?> originalResponse, String encryptedData,
        Map<String, Object> extensions) {
      super(originalResponse.getCode(), originalResponse.getMsg());
      this.setData(encryptedData);
      this.setCost(originalResponse.getCost());
      this.extensions = extensions;
    }

    @JsonAnyGetter
    public Map<String, Object> getExtensions() {
      return extensions;
    }

    @JsonProperty("_enc")
    public boolean getEncrypted() {
      return true;
    }

  }
}