// Copyright 2021 dinodev.cn
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

package org.dinospring.core.modules.message.sms.impl;

import java.util.List;

import org.dinospring.core.modules.message.sms.SmsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 *
 * @author tuuboo
 */

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
