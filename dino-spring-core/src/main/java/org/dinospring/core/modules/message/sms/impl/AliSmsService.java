package org.dinospring.core.modules.message.sms.impl;

import java.util.List;

import org.dinospring.core.modules.message.sms.SmsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "dinospring.module.msg.sms", name = "vendor", havingValue = "ali")
public class AliSmsService implements SmsService {

  @Override
  public boolean sendSmsCaptcha(String mobile, String captcha, String signName) {
    return false;
  }

  @Override
  public boolean sendSms(String templateId, String signName, List<String> templateParams, String... mobiloes) {
    return false;
  }

}
