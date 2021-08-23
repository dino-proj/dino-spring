package com.botbrain.dino.utils.ip;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;

/**
 * 加载IP相关数据。
 * 
 * @author tuuboo
 * @since JDK1.5
 * 
 */
public final class IpDataUtils {
	private IpDataUtils() {
	}

	private static IpArea singleIpArea = null;
	private static ProvinceCityName singleProvinceCytyName = null;

	static {
		try {
			makeIpArea(true);
			makeProvinceCytyName();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param isAll
	 *            是否加载全部国家
	 * @return ip地址区域映射
	 * @throws IOException
	 *             加载文件时可能会出现找不到文件的可能
	 */
	public static void makeIpArea(boolean isAll) throws IOException {
		InputStream file = IpDataUtils.class.getResourceAsStream("/ip-data/committee_ipdata");
		IpArea tmp = makeIpArea(file, isAll);
		singleIpArea = tmp;
	}

	/**
	 * @return ip地址区域映射
	 * @throws IOException
	 *             加载文件时可能会出现找不到文件的可能
	 */
	public static void makeProvinceCytyName() throws IOException {
		ProvinceCityName pcName = new ProvinceCityName();
		pcName.loadCityMap(IpDataUtils.class.getResourceAsStream("/ip-data/city_name"));
		pcName.loadProvinceMap(IpDataUtils.class.getResourceAsStream("/ip-data/province_name"));
		pcName.loadCountryMap(IpDataUtils.class.getResourceAsStream("/ip-data/country_name"));
		singleProvinceCytyName = pcName;
	}

	/**
	 * Ip数据路径
	 * @param isAll
	 *            是否加载全部国家
	 * @return ip地址区域映射
	 * @throws IOException
	 *             加载文件时可能会出现找不到文件的可能
	 */
	public static IpArea makeIpArea(InputStream inputStream, boolean isAll) throws IOException {
		return new IpArea(inputStream, isAll);
	}

	public static String getCityName(String ip) {
		IpArea.Region region = singleIpArea.getRegion(ip);
		if (region == null || StringUtils.isBlank(region.getProvinceId())) {
			return null;
		} else {
			return singleProvinceCytyName.getCityOrProvinceName(region.getCityId());
		}
	}

	public static String getProvinceName(String ip) {
		IpArea.Region region = singleIpArea.getRegion(ip);
		if (region == null || StringUtils.isBlank(region.getProvinceId())) {
			return null;
		} else {
			return singleProvinceCytyName.getProvinceName(region.getProvinceId());
		}
	}
}
