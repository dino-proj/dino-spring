// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.response.encrypt;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/**
 * 响应数据加密自动配置
 * @author Cody Lu
 * @date 2025-09-17 18:56:46
 */

@Configuration
@EnableConfigurationProperties(ResponseEncryptProperties.class)
@Slf4j
public class ResponseEncryptAutoConfig {

  @Bean
  @ConditionalOnProperty(prefix = ResponseEncryptProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
  public ResponseDataEncryptor responseDataEncryptor(ResponseEncryptProperties props) {
    // 打印日志，方便调试
    log.debug("---->> response-encrypt: enabled， aesKey length: {}",
        props.getAesKey() == null ? 0 : props.getAesKey().length());

    // 初始化默认的加密器, 这里可以根据配置选择不同的加密实现
    return new ResponseDataAesEncryptor(props.getAesKey());
  }

}
