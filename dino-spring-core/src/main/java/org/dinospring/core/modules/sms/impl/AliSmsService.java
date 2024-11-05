// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.sms.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.dinospring.core.modules.sms.config.SmsModuleProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.teaopenapi.models.Config;
import com.google.gson.Gson;

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
      Object templateParams, String signName) {
    signName = StringUtils.defaultIfBlank(signName, this.smsModuleProperties.getAli().getSignName());
    return this.doSendTemplateSms(mobiles, templateId, templateParams, signName);
  }

  @Override
  public boolean sendSms(Collection<String> mobiles, String message) {
    throw new UnsupportedOperationException("Unimplemented method 'sendSms'");
  }

  private Collection<String> doSendTemplateSms(Collection<String> mobiles, String templateId,
      Object templateParams, String signName) {

    var failedMobiles = new ArrayList<String>();
    var aliSmsProperties = this.smsModuleProperties.getAli();

    // 实例化一个认证对象，入参需要传入阿里云账户密钥对accessKeyId，accessKeySecret
    var config = new Config()
        // 设置endpoint
        .setEndpoint(StringUtils.defaultIfBlank(aliSmsProperties.getEndpoint(), "dysmsapi.aliyuncs.com"))
        // 配置 AccessKey ID
        .setAccessKeyId(aliSmsProperties.getAccessKeyId())
        // 配置 AccessKey Secret，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
        .setAccessKeySecret(aliSmsProperties.getAccessKeySecret());

    // 实例化一个客户端构造器
    Client client;
    try {
      client = new Client(config);

      // 实例化一个请求Sms对象
      var smsRequest = new SendSmsRequest();
      smsRequest.setPhoneNumbers(StringUtils.join(mobiles, ","));
      smsRequest.setSignName(signName);
      smsRequest.setTemplateCode(templateId);
      smsRequest.setTemplateParam(paramToJsonStr(templateParams));

      // 设置extendCode
      if (StringUtils.isNotBlank(aliSmsProperties.getExtendCode())) {
        smsRequest.setSmsUpExtendCode(aliSmsProperties.getExtendCode());
      }

      // 获取响应对象
      var sendSmsResponse = client.sendSms(smsRequest);

      if (sendSmsResponse.getStatusCode() != 0) {
        log.error("短信发送失败: {}", sendSmsResponse);
        failedMobiles.addAll(mobiles);
      }
    } catch (Exception e) {
      log.error("短信发送失败", e);
      failedMobiles.addAll(mobiles);
    }

    return failedMobiles;
  }

  private String paramToJsonStr(Object params) {
    // 如果是Collection或array, 则转为Index-Value的Map

    if (params instanceof Collection) {

      var paramsMap = new HashMap<String, Object>();
      for (var i = 0; i < ((Collection<?>) params).size(); i++) {
        paramsMap.put(String.valueOf(i + 1), ((Collection<?>) params).toArray()[i]);
      }
      return gson.toJson(paramsMap);
    } else if (params.getClass().isArray()) {
      var paramsMap = new HashMap<String, Object>();
      for (var i = 0; i < ((Object[]) params).length; i++) {
        paramsMap.put(String.valueOf(i + 1), ((Object[]) params)[i]);
      }
      return gson.toJson(paramsMap);
    } else {
      return gson.toJson(params);
    }
  }
}
