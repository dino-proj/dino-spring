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

    
        return null;
  }


}
