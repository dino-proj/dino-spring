// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.login.tenanted;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import cn.dinodev.spring.commons.exception.BusinessException;
import cn.dinodev.spring.commons.request.PostBody;
import cn.dinodev.spring.commons.response.Response;
import cn.dinodev.spring.commons.response.Status;
import cn.dinodev.spring.commons.sys.Tenant;
import cn.dinodev.spring.commons.sys.User;
import cn.dinodev.spring.commons.utils.Assert;
import cn.dinodev.spring.commons.validation.constraints.Mobile;
import cn.dinodev.spring.core.annotion.param.ParamJsonBody;
import cn.dinodev.spring.core.annotion.param.ParamTenant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author Cody Lu
 */

public interface LoginByMobileTenanted<U extends User<K>, K extends Serializable>
    extends LoginControllerBaseTenanted<U, K> {

  /**
   * 用手机短信验证码登录
   * @param tenant
   * @param req
   * @return
   */
  @Operation(summary = "用短信验证码登陆")
  @ParamTenant
  @ParamJsonBody(example = "{\"mobile\":\"13800138000\", \"captcha\":\"1234\"}")
  @PostMapping("/mobile")
  default Response<LoginAuthTenanted<U, K>> byMobile(Tenant tenant, @RequestBody PostBody<MobileLoginBody> req) {

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
    return Response.success(loginService().loginAuth(tenant, user, req.getPlt(), req.getGuid()));
  }

  @Data
  public static class MobileLoginBody {
    @Schema(description = "用户手机号", requiredMode = RequiredMode.REQUIRED)
    @Size(min = 13)
    @NotBlank
    @Mobile
    private String mobile;

    @NotBlank
    @Schema(description = "手机验证码", requiredMode = RequiredMode.REQUIRED)
    private String captcha;
  }

  /**
   * 发送验证码
   * @param tenant
   * @param mobile
   * @param ts
   * @param sign
   * @param signName
   * @return
   */
  @Operation(summary = "发送验证码")
  @ParamTenant
  @GetMapping("/captcha/sms")
  default Response<Boolean> sendSmsCaptcha(Tenant tenant,
      @RequestParam String mobile, //
      @RequestParam(value = "_nonce", required = false) String ts,
      @RequestParam(required = false) String sign,
      @RequestParam(value = "sign_name", required = false) String signName) {

    Assert.notNull(tenant, Status.CODE.FAIL_TENANT_NOT_EXIST);

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
