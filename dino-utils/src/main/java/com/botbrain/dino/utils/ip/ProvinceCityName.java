package com.botbrain.dino.utils.ip;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import lombok.extern.slf4j.Slf4j;

/**
 * 提供根据国家、省份、城市编码获取对应的名称.
 * 
 * @author tuuboo
 * 
 */
@Slf4j
public class ProvinceCityName {

	private Map<String, String> provinceNameMap;
	private Map<String, String> cityNameMap;
	private Map<String, String> countryNameMap;

	/**
	 * 加载国家名称映射文件， 数据中每行一条映射，用逗号或制表符做分割，第一个字段为id，第二个字段为名字，后面的字段忽略.
	 * 
	 * @param filePath
	 *            文件的路径
	 * @return 是否加载成功
	 * @throws IOException
	 */
	public boolean loadCountryMap(String filePath) throws IOException {
		return loadCountryMap(new FileReader(filePath));
	}

	/**
	 * 加载国家名称映射文件， 数据中每行一条映射，用逗号或制表符做分割，第一个字段为id，第二个字段为名字，后面的字段忽略.
	 * 
	 * @param reader
	 *            input reader
	 * 
	 * @return 是否加载成功
	 * @throws IOException
	 */
	public boolean loadCountryMap(Reader reader) throws IOException {
		countryNameMap = loadData(reader);
		return countryNameMap != null;
	}

	/**
	 * 加载国家名称映射文件， 数据中每行一条映射，用逗号或制表符做分割，第一个字段为id，第二个字段为名字，后面的字段忽略.
	 * 
	 * @param input
	 *            InputStream
	 * 
	 * @return 是否加载成功
	 * @throws IOException
	 */
	public boolean loadCountryMap(InputStream input) throws IOException {
		InputStreamReader reader = new InputStreamReader(input);
		countryNameMap = loadData(reader);
		return countryNameMap != null;
	}

	/**
	 * 加载省份名称映射文件， 数据中每行一条映射，用逗号或制表符做分割，第一个字段为id，第二个字段为名字，后面的字段忽略.
	 * 
	 * @param filePath
	 *            文件的路径
	 * @return 是否加载成功
	 * @throws IOException
	 */
	public boolean loadProvinceMap(String filePath) throws IOException {
		return loadProvinceMap(new FileReader(filePath));
	}

	/**
	 * 加载省份名称映射文件， 数据中每行一条映射，用逗号或制表符做分割，第一个字段为id，第二个字段为名字，后面的字段忽略.
	 * 
	 * @param reader
	 *            input reader
	 * 
	 * @return 是否加载成功
	 * @throws IOException
	 */
	public boolean loadProvinceMap(Reader reader) throws IOException {
		provinceNameMap = loadData(reader);
		return provinceNameMap != null;
	}

	/**
	 * 加载省份名称映射文件， 数据中每行一条映射，用逗号或制表符做分割，第一个字段为id，第二个字段为名字，后面的字段忽略.
	 * 
	 * @param input
	 *            InputStream
	 * 
	 * @return 是否加载成功
	 * @throws IOException
	 */
	public boolean loadProvinceMap(InputStream input) throws IOException {
		InputStreamReader reader = new InputStreamReader(input);
		provinceNameMap = loadData(reader);
		return provinceNameMap != null;
	}

	/**
	 * 加载城市名称映射数据， 数据中每行一条映射，用逗号或制表符做分割，第一个字段为id，第二个字段为名字，后面的字段忽略.
	 * 
	 * @param filePath
	 *            城市映射数据文件的路径
	 * @return 是否加载成功
	 * @throws IOException
	 */
	public boolean loadCityMap(String filePath) throws IOException {
		return loadCityMap(new FileReader(filePath));
	}

	/**
	 * 加载城市名称映射数据， 数据中每行一条映射，用逗号或制表符做分割，第一个字段为id，第二个字段为名字，后面的字段忽略.
	 * 
	 * @param reader
	 *            省份映射数据Reader
	 * @return 是否加载成功
	 * @throws IOException
	 */
	public boolean loadCityMap(Reader reader) throws IOException {
		cityNameMap = loadData(reader);
		return cityNameMap != null;
	}

	/**
	 * 加载省份名称映射数据， 数据中每行一条映射，用逗号或制表符做分割，第一个字段为id，第二个字段为名字，后面的字段忽略.
	 * 
	 * @param input
	 *            城市映射数据
	 * @return 是否加载成功
	 * @throws IOException
	 */
	public boolean loadCityMap(InputStream input) throws IOException {
		InputStreamReader reader = new InputStreamReader(input);
		cityNameMap = loadData(reader);
		return cityNameMap != null;
	}

	private Map<String, String> loadData(Reader reader) throws IOException {
		Map<String, String> map = new HashMap<>();
		BufferedReader br = null;
		br = new BufferedReader(reader);
		String line = null;
		while ((line = br.readLine()) != null) {
			StringTokenizer itr = new StringTokenizer(line, "\t");
			if (itr.countTokens() < 2) {
				itr = new StringTokenizer(line, ",");
			}

			if (itr.countTokens() >= 2) {
				map.put(itr.nextToken(), itr.nextToken());
			}
		}

		return map;
	}

	/**
	 * 根据国家的id，返回国家的名字.
	 * 
	 * @param nId
	 *            国家的ID号,4位数字,如1156为中国
	 * @return 国家名字，如果查不到，则返回null
	 */
	public String getCountryName(String nId) {
		return countryNameMap.get(nId);
	}

	/**
	 * 根据省份的id，返回省份的名字.
	 * 
	 * @param pId
	 *            省份的ID号,2位数字,如10为北京
	 * @return 省份名字，如果查不到，则返回null
	 */
	public String getProvinceName(String pId) {
		return provinceNameMap.get(pId);
	}

	/**
	 * 根据城市的id，返回其所属省份的名字，取城市ID的前两位作为省份id.
	 * 
	 * @param cId
	 *            城市的ID,4位,取前两位查找省份名字
	 * @return 省份名字，如果查不到，则返回null
	 */
	public String getProvinceNameByCityId(String cId) {
		return provinceNameMap.get(cId.substring(0, 2));
	}

	/**
	 * 根据城市的ID，返回城市的名字。
	 * 
	 * @param cId
	 *            城市的id,如3713为临沂
	 * @return 城市名字，如果查不到，则返回null
	 */
	public String getCityName(String cId) {
		return cityNameMap.get(cId);
	}

	/**
	 * 获取城市的全名，在城市的前面会加上省份的信息，例如371300结果是“山东省临沂市”.
	 * 
	 * @param cId
	 *            城市ID
	 * @return 如果城市没有名字，则返回省的名字
	 */
	public String getFullName(String cId) {
		String pName = getProvinceNameByCityId(cId);
		String cName = getCityName(cId);
		if (cName != null) {
			return (pName == null) ? cName : pName + " " + cName;
		} else {
			return pName;
		}
	}

	/**
	 * 获取城市的名字，如果没有名字，则返回所属省份的名字，例如110000 则返回北京市 主要用于处理直辖市的情况.
	 * 
	 * @param cId
	 *            城市ID
	 * @return
	 */
	public String getCityOrProvinceName(String cId) {
		String cName = getCityName(cId);
		return cName == null ? getProvinceNameByCityId(cId) : cName;
	}

	/**
	 * @param args 命令行参数
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String cityData = "370100\t济南市\t37\n520100\t贵阳市\t52\n360100\t南昌市\t36\n360700\t赣州市\t36\n360400\t九江市\t36\n"
				+ "152200\t兴安盟\t15\n150600\t鄂尔多斯市\t15\n150800\t巴彦淖尔市\t15\n150300\t乌海市\t15\n150900\t乌兰察布市\t15\n"
				+ "152500\t锡林郭勒盟\t15\n150100\t呼和浩特市\t15\n150400\t赤峰市\t15\n150500\t通辽市\t15\n150200\t包头市\t15\n"
				+ "150700\t呼伦贝尔市\t15\n371300\t临沂市\t37";
		String provinceData = "11\t北京市\n31\t上海市\n12\t天津市\n50\t重庆市\n52\t贵州省\n36\t江西省\n15\t内蒙古\n"
				+ "42\t湖北省\n43\t湖南省\n22\t吉林省\n35\t福建省\n37\t山东省\n45\t广西\n44\t广东省\n51\t四川省\n"
				+ "34\t安徽省\n62\t甘肃省\n71\t台湾省\n81\t香港\n32\t江苏省\n21\t辽宁省\n14\t山西省\n46\t海南省\n"
				+ "41\t河南省\n61\t陕西省\n13\t河北省\n23\t黑龙江省\n33\t浙江省\n65\t新疆\n53\t云南省\n63\t青海省\n64\t宁夏\n82\t澳门\n54\t西藏\n";

		String countryData = "1004\t阿富汗\n1008\t阿尔巴尼亚\n1010\t南极洲\n1012\t阿尔及利亚\n1016\t美属萨摩亚\n1020\t安道尔";

		ProvinceCityName pcName = new ProvinceCityName();
		pcName.loadCityMap(new StringReader(cityData));
		pcName.loadProvinceMap(new StringReader(provinceData));
		pcName.loadCountryMap(new StringReader(countryData));

		String[] testIds = { "110000", "150100", "371300" };
		for (String id : testIds) {
			log.info("{}, {}, {}, {}", id, pcName.getProvinceNameByCityId(id), pcName.getCityName(id),
					pcName.getFullName(id));
		}

		testIds = new String[] { "1008", "1016", "1156" };
		for (String id : testIds) {
			log.info(id + ":" + pcName.getCountryName(id));
		}

	}

}
