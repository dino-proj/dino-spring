// Copyright 2021 dinospring.cn
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

package org.dinospring.core.modules.login;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.dinospring.commons.exception.BusinessException;
import org.dinospring.commons.request.PostBody;
import org.dinospring.commons.response.Response;
import org.dinospring.commons.response.Status;
import org.dinospring.commons.sys.Tenant;
import org.dinospring.commons.sys.User;
import org.dinospring.commons.utils.Assert;
import org.dinospring.core.annotion.param.ParamJsonBody;
import org.dinospring.core.annotion.param.ParamTenant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author tuuboo
 */

public interface LoginByMobile<U extends User<K>, K extends Serializable>
    extends LoginControllerBase<U, K> {

  /**
       * 用手机短信验证码登录
       * @param tenantId
       * @param req
       * @return
       */
  @Operation(summary = "用短信验证码登陆")
  @ParamTenant
  @ParamJsonBody(example = "{\"mobile\":\"13800138000\", \"captcha\":\"1234\"}")
  @PostMapping("/mobile")
  default Response<LoginAuth<U, K>> byMobile(@PathVariable("tenant_id") String tenantId, //
      @RequestBody PostBody<MobileLoginBody> req) {

    if (Tenant.isSys(tenantId)) {
      return Response.fail(Status.CODE.FAIL_TENANT_NOT_EXIST);
    }
    var tenant = tenantService().getById(tenantId, Tenant.class);
    Assert.notNull(tenant, Status.CODE.FAIL_TENANT_NOT_EXIST);

    String mobile = req.getBody().mobile;
    String captcha = req.getBody().captcha;

    //验证手机和验证码
    Assert.isTrue(loginService().verifySmsCaptcha(mobile, captcha), Status.CODE.FAIL_LOGIN.withMsg("验证码错误"));
    //查询用户
    U user = loginService().findUserByMobile(tenant.getId(), mobile).orElse(null);

    //如果用户为空，且是开放注册，则自动注册新用户
    if (user == null) {
      throw BusinessException.of(Status.CODE.FAIL_USER_NOT_EXIST);
    }
    return Response.success(loginService().loginAuth(tenant,
        tenantService().projection(loginService().userClass(), user), req.getPlt(), req.getGuid()));
  }

  @Data
  public static class MobileLoginBody {
    @Schema(description = "用户手机号", required = true)
    @Size(min = 13)
    private String mobile;

    @NotBlank
    @Schema(description = "手机验证码", required = true)
    private String captcha;
  }

  /**
   * 发送验证码
   * @param tenantId
   * @param mobile
   * @param ts
   * @param sign
   * @param signName
   * @return
   */
  @Operation(summary = "发送验证码")
  @ParamTenant
  @GetMapping("/captcha/sms")
  default Response<Boolean> sendSmsCaptcha(@PathVariable("tenant_id") String tenantId,
      @RequestParam(value = "mobile") String mobile, @RequestParam(value = "_nonce", required = false) String ts,
      @RequestParam(value = "sign", required = false) String sign,
      @RequestParam(value = "sign_name", required = false) String signName) {

    try {
      if (checkSign(mobile, ts, sign)) {
        return Response.success(loginService().sendSmsCaptcha(mobile));
      } else {
        return Response.success(true);
      }
    } catch (Exception e) {
      log().error("error when send sms captcha", e);
      return Response.success(false);
    }
  }

  /**
   * checkSign
   * @param mobile
   * @param ts
   * @param sign
   * @return
   * @throws NoSuchAlgorithmException
   */
  private static boolean checkSign(String mobile, String ts, String sign) throws NoSuchAlgorithmException {
    if (StringUtils.isAnyBlank(mobile, ts, sign) || !StringUtils.isNumeric(ts)) {
      return false;
    }
    return sign.equalsIgnoreCase(sign(mobile, ts));
  }

  /**
   * sign
   * @param str1
   * @param str2
   * @return
   * @throws NoSuchAlgorithmException
   */
  private static String sign(String str1, String str2) throws NoSuchAlgorithmException {
    char[] c2 = str2.toCharArray();
    ArrayUtils.reverse(c2);
    int[] chs = (String.valueOf(c2) + str1).chars().toArray();
    int len = chs.length;
    int tmp = 3 * 3 * 3;
    for (int i = 0; i < chs.length; i++) {
      chs[i] = (chs[tmp % len] << (tmp % ((tmp & 5) + 1))) + tmp;
      tmp = chs[i];
    }
    StringBuilder sb = new StringBuilder();
    for (int i : chs) {
      sb.append(i);
    }
    return Hex.encodeHexString(MessageDigest.getInstance("MD5").digest(sb.toString().getBytes()));
  }

}
