// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.utils;

import org.apache.commons.lang3.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody LU
 * @date 2022-03-07 21:58:04
 */

@Slf4j
public class IpUtils {

  private IpUtils() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * 获取IP地址
   *
   * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
   * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
   */
  public static String getIpAddr(HttpServletRequest request) {
    String ip = null;
    try {
      ip = request.getHeader("x-forwarded-for");
      ip = StringUtils.getIfEmpty(ip, () -> request.getHeader("Proxy-Client-IP"));
      ip = StringUtils.getIfEmpty(ip, () -> request.getHeader("WL-Proxy-Client-IP"));
      ip = StringUtils.getIfEmpty(ip, () -> request.getHeader("HTTP_CLIENT_IP"));
      ip = StringUtils.getIfEmpty(ip, () -> request.getHeader("HTTP_X_FORWARDED_FOR"));
      ip = StringUtils.getIfEmpty(ip, request::getRemoteAddr);
    } catch (Exception e) {
      log.error("get ip error occurred ", e);
    }

    //使用代理，则获取第一个IP地址

    if (StringUtils.isNotEmpty(ip)) {
      ip = StringUtils.split(ip, ',')[0];
    }

    return ip;
  }

}
