// Copyright 2025 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.response.encrypt;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import jakarta.annotation.Nullable;

/**
 * Aes加密器
 * @author Cody Lu
 * @date 2025-09-17 18:18:02
 */

public class ResponseDataAesEncryptor implements ResponseDataEncryptor {

  private final SecretKeySpec keySpec;

  /**
   * 构造 AES 响应数据加密器
   *
   * @param aesKey AES 密钥，长度必须为 16、24 或 32 个字符
   * @throws IllegalArgumentException 如果密钥为空或长度不符合要求
   */
  public ResponseDataAesEncryptor(String aesKey) {
    // 检查配置的aesKey是否有效，以及长度是否正确，16, 24, 32
    if (aesKey == null) {
      throw new IllegalArgumentException("Invalid aesKey for response data encryptor, must be 16, 24 or 32 characters");
    }
    int length = aesKey.getBytes(java.nio.charset.StandardCharsets.UTF_8).length;
    if (length != 16 && length != 24 && length != 32) {
      throw new IllegalArgumentException(
          "Invalid aesKey length for response data encryptor, must be 16, 24 or 32 bytes");
    }

    this.keySpec = new SecretKeySpec(aesKey.getBytes(java.nio.charset.StandardCharsets.UTF_8), "AES");
  }

  @Override
  public String encryptData(@Nullable byte[] plainBytes) {
    // 实现AES加密逻辑，使用aesKey和aesIv进行加密
    if (plainBytes == null) {
      return null;
    }
    byte[] aesIv = new byte[16]; // 16字节 for AES
    new SecureRandom().nextBytes(aesIv);

    try {
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      IvParameterSpec ivSpec = new IvParameterSpec(aesIv);
      cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
      byte[] encrypted = cipher.doFinal(plainBytes);
      // 将iv和加密数据一起返回，方便解密时使用
      byte[] combined = new byte[aesIv.length + encrypted.length];
      System.arraycopy(aesIv, 0, combined, 0, aesIv.length);
      System.arraycopy(encrypted, 0, combined, aesIv.length, encrypted.length);
      // 使用Base64编码返回字符串
      return Base64.encodeBase64String(combined);
    } catch (Exception e) {
      throw new IllegalStateException("AES encryption failed", e);
    }

  }
}
