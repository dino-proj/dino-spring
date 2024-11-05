// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.login;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.dinospring.commons.exception.BusinessException;
import org.dinospring.commons.request.PostBody;
import org.dinospring.commons.response.Response;
import org.dinospring.commons.response.Status;
import org.dinospring.commons.sys.User;
import org.dinospring.commons.utils.Assert;
import org.dinospring.commons.validation.constraints.Mobile;
import org.dinospring.core.annotion.param.ParamJsonBody;
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
 * @author Cody LU
 */

public interface LoginByMobile<U extends User<K>, K extends Serializable>
    extends LoginControllerBase<U, K> {

  /**
   * 登录Service
   * @return
   */
  LoginServiceBase<U, K> loginService();

  /**
   * 用手机短信验证码登录
   * @param req
   * @return
   */
  @Operation(summary = "用短信验证码登陆")
  @ParamJsonBody(example = "{\"mobile\":\"13800138000\", \"captcha\":\"1234\"}")
  @PostMapping("/mobile")
  default Response<LoginAuth<U, K>> byMobile(@RequestBody PostBody<MobileLoginBody> req) {

    String mobile = req.getBody().mobile;
    String captcha = req.getBody().captcha;

    //验证手机和验证码
    Assert.isTrue(this.loginService().verifySmsCaptcha(mobile, captcha), Status.CODE.FAIL_LOGIN.withMsg("验证码错误"));
    //查询用户
    U user = this.loginService().findUserByMobile(mobile).orElse(null);

    //如果用户为空，且是开放注册，则自动注册新用户
    if (user == null) {
      throw BusinessException.of(Status.CODE.FAIL_USER_NOT_EXIST);
    }
    return Response.success(this.loginService().loginAuth(user, req.getPlt(), req.getGuid()));
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
   * @param mobile
   * @param ts
   * @param sign
   * @param signName
   * @return
   */
  @Operation(summary = "发送验证码")
  @GetMapping("/captcha/sms")
  default Response<Boolean> sendSmsCaptcha(
      @RequestParam String mobile,
      @RequestParam(value = "_nonce", required = false) String ts,
      @RequestParam(required = false) String sign,
      @RequestParam(value = "sign_name", required = false) String signName) {

    try {
      if (checkSign(mobile, ts, sign)) {
        return Response.success(this.loginService().sendSmsCaptcha(mobile));
      } else {
        return Response.success(true);
      }
    } catch (Exception e) {
      this.log().error("error when send sms captcha", e);
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
