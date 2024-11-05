// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.validation.constraints;

/**
 * 国家
 * @author Cody LU
 * @date 2022-04-01 00:31:01
 */

public enum Country {
  //中国
  CHINA("86", "中国"),
  TAIWAN("886", "中国台湾"),
  HONGKONG("852", "中国香港"),
  MACAO("853", "中国澳门"),
  USA("1", "美国"),
  UK("44", "英国"),
  AUSTRALIA("61", "澳大利亚"),
  CANADA("1", "加拿大"),
  INDIA("91", "印度"),
  JAPAN("81", "日本"),
  KOREA("82", "韩国"),
  SOUTH_KOREA("82", "韩国"),
  VIETNAM("84", "越南"),
  THAILAND("66", "泰国"),
  PHILIPPINES("63", "菲律宾"),
  BRITAIN("44", "英国"),
  FRANCE("33", "法国"),
  GERMANY("49", "德国"),
  ITALY("39", "意大利"),
  NETHERLANDS("31", "荷兰"),
  SPAIN("34", "西班牙"),
  SWITZERLAND("41", "瑞士"),
  SWEDEN("46", "瑞典"),
  NORWAY("47", "挪威"),
  DENMARK("45", "丹麦"),
  FINLAND("358", "芬兰"),
  POLAND("48", "波兰"),
  CZECH_REPUBLIC("420", "捷克"),
  SLOVAKIA("421", "斯洛伐克"),
  SLOVENIA("386", "斯洛文尼亚"),
  HUNGARY("36", "匈牙利"),
  ROMANIA("40", "罗马尼亚"),
  CROATIA("385", "克罗地亚"),
  BULGARIA("359", "保加利亚");

  private String code;
  private String name;

  Country(String code, String name) {
    this.code = code;
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public String getCountry() {
    return name;
  }
}
