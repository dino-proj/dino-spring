// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.sms.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.stream.Streams;
import org.apache.commons.text.StringTokenizer;
import org.dinospring.core.modules.sms.config.SmsModuleProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.google.gson.Gson;

import darabonba.core.client.ClientOverrideConfiguration;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody LU
 */

@Slf4j
@Service
@ConditionalOnProperty(prefix = SmsModuleProperties.PREFIX, name = "vendor", havingValue = "ali")
public class AliSmsService extends SmsServiceBase {
  @Autowired
  private SmsModuleProperties smsModuleProperties;

  @Autowired
  private Gson gson;

  @Override
  public Collection<String> sendTemplateSms(Collection<String> mobiles, String templateId,
      Collection<String> templateParams, String signName) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'sendTemplateSms'");
  }

  @Override
  public boolean sendSms(Collection<String> mobiles, String message) {
    throw new UnsupportedOperationException("Unimplemented method 'sendSms'");
  }

  private Collection<String> doSendTemplateSms(Collection<String> mobiles, String templateId,
      Collection<String> templateParams, String signName) {

    var failedMobiles = new ArrayList<String>();
    var aliSmsProperties = this.smsModuleProperties.getAli();

    // 实例化一个认证对象，入参需要传入阿里云账户密钥对accessKeyId，accessKeySecret
    var cred = Credential.builder().accessKeyId(aliSmsProperties.getAccessKeyId())
        .accessKeySecret(aliSmsProperties.getAccessKeySecret()).build();

    var credentialProvider = StaticCredentialProvider.create(cred);

    // 实例化一个客户端构造器
    var clientBuilder = AsyncClient.builder();
    clientBuilder.credentialsProvider(credentialProvider);
    clientBuilder.region(aliSmsProperties.getRegion());
    if (StringUtils.isNotBlank(aliSmsProperties.getEndpoint())) {
      clientBuilder.overrideConfiguration(
          ClientOverrideConfiguration.create().setEndpointOverride(aliSmsProperties.getEndpoint()));
    }

    // 实例化一个请求Sms对象
    var smsRequestBuilder = SendSmsRequest.builder();
    smsRequestBuilder.s.setSmsSdkAppId(aliSmsProperties.getAppId());

    smsRequestBuilder.phoneNumbers(StringUtils.join(mobiles, ","));
    smsRequestBuilder.templateCode(templateId);
    smsRequestBuilder.templateParam(templateParams.toArray(new String[templateParams.size()]));
    smsRequestBuilder.signName(signName);

    // 设置extendCode
    if (StringUtils.isNotBlank(aliSmsProperties.getExtendCode())) {
      smsRequestBuilder.smsUpExtendCode(aliSmsProperties.getExtendCode());
    }

    try (AsyncClient client = clientBuilder.build()) {

    }

  }

  private String paramToIndexedJsonStr(String[] params) {
    if (params == null || params.length == 0) {
      return "";
    }
    var paramsMap = Arrays.stream(params).collect(Collectors.toMap(String::valueOf, String::valueOf));
    gson.toJson(params)
    StringBuilder jsonStringBuilder = new StringBuilder();
    jsonStringBuilder.append('{');
    for(int idx = 0; idx < params.length; idx++) {
      jsonStringBuilder.append("\"").append(idx).append("\":\"").append(StringTokenizer. params[idx]).append("\",");
    }
  }

}
