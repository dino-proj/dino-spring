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

package com.botbrain.dino.utils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacUtils;

import lombok.experimental.UtilityClass;

/**
 * Copyright：botBrain.ai
 * Author: SongXiaoGuang
 * Date: 2018/3/13.
 * Description:
 */
@UtilityClass
public class EncryptUtil {

    /**
     * md5 salt
     * @param source
     * @param salt
     * @return
     */
    public static String md5Salt(String source, String salt) {
        try {
            Key secretKey = new SecretKeySpec(salt.getBytes(StandardCharsets.UTF_8), "HmacMD5");
            Mac mac = Mac.getInstance("HmacMD5");
            mac.init(secretKey);
            return DigestUtils.md5Hex(HmacUtils.updateHmac(mac, source).doFinal());
        } catch (Exception e) {
            e.printStackTrace();
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
            Mac hmacSha256 = Mac.getInstance("HmacSHA256");
            byte[] keyBytes = userSecretKey.getBytes(StandardCharsets.UTF_8);
            hmacSha256.init(new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256"));
            String encryptStr = DigestUtils.md5Hex(hmacSha256.doFinal((uid + timestamp + osKey).getBytes("UTF-8")));
            return Base64.getEncoder().encodeToString(encryptStr.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean checkUserSign(String osKey, long timestamp, String uid, String userSecretKey, String sign) {
        try {
            sign = URLDecoder.decode(sign, StandardCharsets.UTF_8);
            Mac hmacSha256 = Mac.getInstance("HmacSHA256");
            byte[] keyBytes = userSecretKey.getBytes(StandardCharsets.UTF_8);
            hmacSha256.init(new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256"));
            String encryptStr = DigestUtils
                    .md5Hex(hmacSha256.doFinal((uid + timestamp + osKey).getBytes(StandardCharsets.UTF_8)));
            return sign.equals(Base64.getEncoder().encodeToString(encryptStr.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
