package org.dinospring.core.sys.tenant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.dinospring.commons.sys.Tenant;
import org.dinospring.core.vo.VoImplBase;

/**
 *
 * @author tuuboo
 * @date 2022-03-01 18:37:07
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class TenantVo extends VoImplBase<String> implements Tenant {

  @Schema(description = "租户名称")
  String name;

  @Schema(description = "租户简称")
  String shortName;

  @Schema(description = "租户LOGO")
  String iconUrl;

  @Schema(description = "租户自定义域名，如果用户配置了域名，则用其自己的域名")
  String customDomain;

  @Schema(description = "租户秘钥，请不要随便显示")
  String secretKey;

  @Schema(description = "租户编码")
  private String code;

  @Override
  public String getSecretKey() {
    return StringUtils.overlay(secretKey, "****", 4, secretKey.length() - 4);
  }

}
