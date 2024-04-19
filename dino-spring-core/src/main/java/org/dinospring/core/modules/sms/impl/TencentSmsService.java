// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.sms.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.dinospring.core.modules.sms.config.SmsModuleProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody LU
 */

@Slf4j
@Service
@ConditionalOnProperty(prefix = SmsModuleProperties.PREFIX, name = "vendor", havingValue = "tencent")
public class TencentSmsService extends SmsServiceBase {

  @Autowired
  private SmsModuleProperties smsModuleProperties;

  @Override
  public Collection<String> sendTemplateSms(Collection<String> mobiles, String templateId,
      Collection<String> templateParams, @Nullable String signName) {
    signName = StringUtils.defaultIfBlank(signName, this.smsModuleProperties.getTencent().getSignName());
    return this.doSendTemplateSms(mobiles, templateId, templateParams, signName);
  }

  @Override
  public boolean sendSms(Collection<String> mobiles, String message) {
    throw new UnsupportedOperationException("Unimplemented method 'sendSms'");
  }

  private Collection<String> doSendTemplateSms(Collection<String> mobiles, String templateId,
      Collection<String> templateParams, String signName) {

    var failedMobiles = new ArrayList<String>();
    var tencentSmsProperties = this.smsModuleProperties.getTencent();
    // CAM密匙查询: https://console.tencentcloud.com/cam/capi
    var cred = new Credential(tencentSmsProperties.getSecretId(), tencentSmsProperties.getSecretKey());

    // 实例化一个http选项
    var httpProfile = new HttpProfile();
    if (StringUtils.isNotBlank(tencentSmsProperties.getEndpoint())) {
      httpProfile.setEndpoint(tencentSmsProperties.getEndpoint());
    }

    // 实例化一个客户端配置对象
    var clientProfile = new ClientProfile();
    clientProfile.setHttpProfile(httpProfile);

    // 实例化sms client对象
    var client = new SmsClient(cred, tencentSmsProperties.getRegion(), clientProfile);

    // 实例化一个请求对象
    var req = new SendSmsRequest();
    req.setSmsSdkAppId(tencentSmsProperties.getAppId());
    req.setSignName(signName);
    req.setTemplateId(templateId);
    // 设置发送者ID
    var senderid = tencentSmsProperties.getSenderId();
    if (StringUtils.isNotBlank(senderid)) {
      req.setSenderId(senderid);
    }
    // 设置短信码号扩展号
    var extendCode = tencentSmsProperties.getExtendCode();
    if (StringUtils.isNotBlank(extendCode)) {
      req.setExtendCode(extendCode);
    }

    // 设置发送手机号
    req.setPhoneNumberSet(mobiles.toArray(new String[mobiles.size()]));
    req.setTemplateParamSet(templateParams.toArray(new String[templateParams.size()]));
    try {
      // 发送短信
      var resp = client.SendSms(req);
      var status = resp.getSendStatusSet();
      if (status != null && status.length > 0) {
        for (var s : status) {
          if (s.getCode() != "Ok") {
            log.error("Failed to send sms to {}, code: {}, message: {}", s.getPhoneNumber(), s.getCode(),
                s.getMessage());
            failedMobiles.add(s.getPhoneNumber());
          }
        }
      }
    } catch (TencentCloudSDKException e) {
      log.error("Failed to send sms", e);
      failedMobiles.addAll(mobiles);
    }

    return failedMobiles;
  }

}
