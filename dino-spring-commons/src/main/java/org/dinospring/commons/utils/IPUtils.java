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

package org.dinospring.commons.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author tuuboo
 * @date 2022-03-07 21:58:04
 */

@Slf4j
public class IPUtils {

  private IPUtils() {
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
