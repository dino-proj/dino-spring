package com.botbrain.dino.utils.ip;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

/**
 * IPv4和地域映射解析类，将IPv4地址，映射到一个地理位置，地理位置信息包含在{@link Region}中。
 *
 * @author tuuboo
 *
 */
@Slf4j
public class IpArea {

  private Region[][] regionArray = new Region[256][];

  /**
   * @param ipdataFilePath
   *            ip文件路径。
   * @throws IOException
   *             io异常
   */
  public IpArea(String ipdataFilePath) throws IOException {
    loadIp(ipdataFilePath);
  }

  /**
   * @param reader
   *            ip数据Reader。
   * @throws IOException
   *             io异常
   */
  public IpArea(Reader reader) throws IOException {
    loadIp(reader, false);
  }

  /**
   * @param in
   *            ip数据输入流。
   * @throws IOException
   *             io异常
   */
  public IpArea(InputStream in) throws IOException {
    loadIp(new InputStreamReader(in), false);
  }

  /**
   * @param ipdataFilePath
   *            ip文件路径。
   * @param isAll
   *            是否加载全部国家
   * @throws IOException
   *             io异常
   */
  public IpArea(String ipdataFilePath, boolean isAll) throws IOException {
    loadIp(ipdataFilePath, isAll);
  }

  /**
   * @param reader
   *            ip数据Reader。
   * @param isAll
   *            是否加载全部国家
   * @throws IOException
   *             io异常
   */
  public IpArea(Reader reader, boolean isAll) throws IOException {
    loadIp(reader, isAll);
  }

  /**
   * @param in
   *            ip数据输入流。
   * @param isAll
   *            是否加载全部国家
   * @throws IOException
   *             io异常
   */
  public IpArea(InputStream in, boolean isAll) throws IOException {
    log.info("load ip >>>>");
    loadIp(new InputStreamReader(in), isAll);
  }

  /**
   * 判断ip的正则匹配式
   */
  private Pattern isIpPattern;

  private void loadIp(String ipdata) throws IOException {
    try {
      loadIp(new FileReader(ipdata), false);
    } catch (FileNotFoundException e) {
      throw new IOException("ipdata file:[" + ipdata + "] not found");
    }
  }

  private void loadIp(String ipdata, boolean isAll) throws IOException {
    try {
      loadIp(new FileReader(ipdata), isAll);
    } catch (FileNotFoundException e) {
      throw new IOException("ipdata file:[" + ipdata + "] not found");
    }
  }

  private void loadIp(Reader reader, boolean isAll) throws IOException {
    Map<Integer, List<Region>> regionMap = new HashMap<>();
    BufferedReader br = new BufferedReader(reader);

    br.lines().forEach(line -> {
      String[] arr = line.split(",", -1);
      // 223.245.183.59,223.245.183.59,3757422395,3757422395,1156340000,1156340000,1156340000,1156340100,,
      if (arr.length != 3) {
        return;
      }

      int firstSeg = Integer.parseInt(arr[0].split(Pattern.quote("."))[0]);

      int endSeg = Integer.parseInt(arr[1].split(Pattern.quote("."))[0]);

      int startip = this.ipToInt(arr[0]);
      int endip = this.ipToInt(arr[1]);
      String regioncode = arr[2];

      Region region = new Region(startip, endip, regioncode);
      if (!isAll && !Region.CHINA_PATTERN.matcher(region.getRealCountryId()).matches()) {
        return;
      }

      if (firstSeg != endSeg) {

        for (int seg = endSeg; seg > firstSeg; seg--) {
          if (seg != endSeg) {
            endip = this.ipToInt(seg + ".255.255.255");
          }
          putIntoMapList(regionMap, seg, new Region(ipToInt(seg + ".0.0.0"), endip, regioncode));
        }

        region = new Region(startip, ipToInt(firstSeg + ".255.255.255"), regioncode);
      }

      putIntoMapList(regionMap, firstSeg, region);
    });
    for (Entry<Integer, List<Region>> region : regionMap.entrySet()) {
      Region[] regarr = region.getValue().toArray(new Region[0]);
      Arrays.sort(regarr);
      regionArray[region.getKey()] = regarr;
      // ip段个数
    }

  }

  private <K, V> void putIntoMapList(Map<K, List<V>> mapList, K key, V value) {
    List<V> vList = mapList.get(key);
    if (vList == null) {
      vList = new ArrayList<>();
    }
    vList.add(value);
    mapList.put(key, vList);
  }

  private static final Comparator<Region> COMPARATOR = (o1, o2) -> {
    // o2为search,o2<o1返回1
    if (o2.getStartip() < o1.getStartip()) {
      return 1;
    } else if (o2.getEndip() > o1.getEndip()) {
      return -1;
    } else {
      return 0;
    }
  };

  /**
   * 将IP地址映射成地域信息。
   *
   * @param ip
   *            ip地址，ipv4格式，如：10.103.23.61
   * @return 如果无法映射，则返回null
   */
  public Region getRegion(String ip) {

    // 对 ip 进行正则判断
    Matcher m = isIpPattern.matcher(ip);
    if (!m.matches()) {
      return null;
    }

    String[] strs = ip.split("\\.");
    if (strs.length != 4) {
      return null;
    }
    int ipInt = this.ipToInt(strs);
    Region search = new Region(ipInt);

    int firstSeg = Integer.parseInt(strs[0]);
    if (firstSeg > 255) {
      return null;
    }

    Region[] regionArr = regionArray[firstSeg];
    if (regionArr == null) {
      return null;
    }

    int idx = Arrays.binarySearch(regionArr, search, COMPARATOR);
    if (idx >= 0) {
      return regionArr[idx];
    } else {
      return null;
    }
  }

  private int ipToInt(String ip) {
    String[] strs = ip.split("\\.");
    return ipToInt(strs);
  }

  private int ipToInt(String[] strs) {
    return (parseInt(strs[1]) << 16) | (parseInt(strs[2]) << 8) | (parseInt(strs[3]));
  }

  private int parseInt(Object strData) {
    if (strData == null || strData.toString().length() == 0) {
      return 0;
    } else {
      try {
        return Integer.parseInt(strData.toString());
      } catch (Exception e) {
        return 0;
      }
    }
  }

  /**
   * 地域Region信息，地域码是符合国家行政区划编码的，包含香港，澳门，台湾等。
   *
   * @author tuuboo
   *
   */
  public static class Region implements Comparable<Region> {

    private static final String CHINA_COUNTRY_CODE = "1156";
    private static final String HONGKONG_COUNTRY_CODE = "1344";
    private static final String AOMEN_COUNTRY_CODE = "1446";
    private static final String TAIWAN_COUNTRY_CODE = "1158";

    private static final String HONGKONG_PROVINCE_ID = "81";
    private static final String AOMEN_PROVINCE_ID = "82";
    private static final String TAIWAN_PROVINCE_ID = "71";

    private static final Pattern CHINA_PATTERN = Pattern.compile(
        CHINA_COUNTRY_CODE + "|" + HONGKONG_COUNTRY_CODE + "|" + TAIWAN_COUNTRY_CODE + "|" + AOMEN_COUNTRY_CODE);

    private static final String[] ZHI_XIA_SHI_IDS = { "11", "31", "12", "50" };

    protected Region(int startip, int endip, String regioncode) {
      setStartip(startip);
      setEndip(endip);
      setRegioncode(regioncode);
    }

    protected Region(int startip) {
      setStartip(startip);
      setEndip(startip);
      setRegioncode("");
    }

    /**
     * 根据起始IP段顺序排序.
     *
     * @param o
     *            other one
     */
    @Override
    public int compareTo(Region o) {
      return this.getStartip() < o.getStartip() ? -1 : 1;
    }

    /**
     *
     * @return 得到所属国家的代码，例如：1156代表中国（港澳台不映射）
     */
    public String getRealCountryId() {
      return regioncode.substring(0, 4);
    }

    /**
     *
     * @return 得到所属国家的代码，例如：1156代表中国（港澳台映射为中国）
     */
    public String getCountryId() {
      if (Region.CHINA_PATTERN.matcher(this.getRealCountryId()).matches()) {
        return "1156";
      } else {
        return this.getRealCountryId();
      }
    }

    /**
     *
     * @return 得到所属城市的代码，例如：110000代表北京，440100代表广州，990000代表未知.
     */
    public String getCityId() {
      if (this.isHaiWai()) {
        return "900000";
      } else {
        if (AOMEN_COUNTRY_CODE.equals(this.getRealCountryId())) {
          return AOMEN_PROVINCE_ID + "0000";
        } else if (this.getRealCountryId().equals(HONGKONG_COUNTRY_CODE)) {
          return HONGKONG_PROVINCE_ID + "0000";
        } else if (this.getRealCountryId().equals(TAIWAN_COUNTRY_CODE)) {
          return TAIWAN_PROVINCE_ID + "0000";
        } else {
          String city = regioncode.substring(4);
          if (city.equals("000000")) {
            return "990000";
          } else {
            return city;
          }
        }
      }
    }

    /**
     *
     * @return 得到所属省份的代码，例如：11代表北京，37代表山东,99代表未知.
     */
    public String getProvinceId() {
      if (this.isHaiWai()) {
        return "90";
      } else {
        if (this.getRealCountryId().equals(AOMEN_COUNTRY_CODE)) {
          return AOMEN_PROVINCE_ID;
        } else if (this.getRealCountryId().equals(HONGKONG_COUNTRY_CODE)) {
          return HONGKONG_PROVINCE_ID;
        } else if (this.getRealCountryId().equals(TAIWAN_COUNTRY_CODE)) {
          return TAIWAN_PROVINCE_ID;
        } else {
          String province = regioncode.substring(4, 6);
          if (province.equals("00")) {
            return "99";
          } else {
            return province;
          }
        }
      }
    }

    /**
     * 是否是直辖市，直辖市包括北京市、上海市、天津市、重庆市。
     *
     * @return true:直辖市
     */
    public boolean isZhiXiaShi() {
      if (!this.isHaiWai()) {
        for (String zxs : ZHI_XIA_SHI_IDS) {
          if (zxs.equals(this.getProvinceId())) {
            return true;
          }
        }
      }
      return false;
    }

    /**
     * 是否是海外，中国(包括港澳台）,之外的国家返回true
     *
     * @return true:海外
     */
    public boolean isHaiWai() {
      return !(Region.CHINA_PATTERN.matcher(this.getRealCountryId()).matches());
    }

    private int startip;
    private int endip;
    private String regioncode;

    /**
     *
     * @return 起始IP段长整数。
     */
    protected int getStartip() {
      return startip;
    }

    protected void setStartip(int startip) {
      this.startip = startip;
    }

    /**
     *
     * @return 截止IP段长整数
     */
    protected int getEndip() {
      return endip;
    }

    protected void setEndip(int endip) {
      this.endip = endip;
    }

    /**
     * IP段地区编码。
     *
     * @return
     */
    public String getRegioncode() {
      return regioncode;
    }

    protected void setRegioncode(String regioncode) {
      this.regioncode = regioncode;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      char sep = ';';
      sb.append("country:").append(this.getCountryId()).append(sep);
      sb.append("province:").append(this.getProvinceId()).append(sep);
      sb.append("city:").append(this.getCityId()).append(sep);
      sb.append("startip:").append(this.startip).append(sep);
      sb.append("endip:").append(this.endip);
      return sb.toString();
    }
  }

  /**
   * 测试用。
   *
   * @param args
   *            参数
   * @throws IOException
   *             io异常
   */
  public static void main(String[] args) throws IOException {
    String testData = "1.2.2.0,1.2.2.255,1004110000\n" + "1.2.3.0,1.2.3.255,1036000000\n"
        + "1.2.4.0,1.2.7.255,1158350000";
    IpArea ia = new IpArea(new StringReader(testData), false);
    log.info(String.valueOf(ia.getRegion("1.2.2.3")));
    log.info(String.valueOf(ia.getRegion("1.2.5.255")));
  }
}
