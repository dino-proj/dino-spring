package org.dinospring.core.modules.message.sms;

import java.util.List;

public interface SmsService {

  /**
   * 发送短信验证码
   * @param mobile 下发手机号码，采用 E.164 标准，格式为+[国家或地区码][手机号]，例如：+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号。默认使用+86
   * @param captcha 短信验证码
   * @param signName 短信签名内容
   * @return 发送成功返回true，否则返回false
   */
  boolean sendSmsCaptcha(String mobile, String captcha, String signName);

  /**
   * 
   * @param templateId 短信模板的ID
   * @param signName 短信签名内容
   * @param templateParams 短信模板参数
   * @param mobiloes 下发手机号码，采用 E.164 标准，格式为+[国家或地区码][手机号]，例如：+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号。默认使用+86
   * @return 发送成功返回true，否则返回false
   */
  boolean sendSms(String templateId, String signName, List<String> templateParams, String... mobiloes);
}
