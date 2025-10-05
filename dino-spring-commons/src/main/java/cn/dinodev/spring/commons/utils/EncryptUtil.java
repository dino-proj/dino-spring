// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.utils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacUtils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 加密工具类，提供数据加密解密的便捷方法
 *
 * @author Cody Lu
 * @since 2022-03-31
 */

@Slf4j
@UtilityClass
public class EncryptUtil {

  private static final String HMAC_SHA256 = "HmacSHA256";
  private static final String HMAC_MD5 = "HmacMD5";

  /**
   * md5 salt
   * @param source
   * @param salt
   * @return
   */
  public static String md5Salt(String source, String salt) {
    try {
      Key secretKey = new SecretKeySpec(salt.getBytes(StandardCharsets.UTF_8), HMAC_MD5);
      Mac mac = Mac.getInstance(HMAC_MD5);
      mac.init(secretKey);
      return DigestUtils.md5Hex(HmacUtils.updateHmac(mac, source).doFinal());
    } catch (Exception e) {
      log.error("MD5 salt encryption failed", e);
    }
    return null;
  }

  /**
   * 生成用户签名
   * @param osKey
   * @param timestamp
   * @param uid
   * @param userSecretKey
   * @return
   */
  public static String genUserSign(String osKey, long timestamp, String uid, String userSecretKey) {
    try {
      Mac hmacSha256 = Mac.getInstance(HMAC_SHA256);
      byte[] keyBytes = userSecretKey.getBytes(StandardCharsets.UTF_8);
      hmacSha256.init(new SecretKeySpec(keyBytes, 0, keyBytes.length, HMAC_SHA256));
      String encryptStr = DigestUtils.md5Hex(hmacSha256.doFinal((uid + timestamp + osKey).getBytes("UTF-8")));
      return Base64.getEncoder().encodeToString(encryptStr.getBytes());
    } catch (Exception e) {
      log.error("User sign generation failed", e);
      return null;
    }
  }

  /**
   * 校验用户签名
   * @param osKey 操作系统密钥
   * @param timestamp 时间戳
   * @param uid 用户ID
   * @param userSecretKey 用户密钥
   * @param sign 签名
   * @return 校验结果
   */
  public static boolean checkUserSign(String osKey, long timestamp, String uid, String userSecretKey, String sign) {
    try {
      sign = URLDecoder.decode(sign, StandardCharsets.UTF_8);
      Mac hmacSha256 = Mac.getInstance(HMAC_SHA256);
      byte[] keyBytes = userSecretKey.getBytes(StandardCharsets.UTF_8);
      hmacSha256.init(new SecretKeySpec(keyBytes, 0, keyBytes.length, HMAC_SHA256));
      String encryptStr = DigestUtils
          .md5Hex(hmacSha256.doFinal((uid + timestamp + osKey).getBytes(StandardCharsets.UTF_8)));
      return sign.equals(Base64.getEncoder().encodeToString(encryptStr.getBytes()));
    } catch (Exception e) {
      log.error("User sign check failed", e);
    }
    return false;
  }

}
