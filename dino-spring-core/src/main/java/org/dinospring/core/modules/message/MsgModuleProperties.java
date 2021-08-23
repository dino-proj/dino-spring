package org.dinospring.core.modules.message;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "dinospring.module.msg")
public class MsgModuleProperties {

  @Data
  public static class SmsProperties {
    /**
     * 短信厂商
     */
    private SmsVendor vendor;
  }

  public enum SmsVendor {
    ALI, TENCENT;

    @Override
    public String toString() {
      return name().toLowerCase();
    }
  }
}
