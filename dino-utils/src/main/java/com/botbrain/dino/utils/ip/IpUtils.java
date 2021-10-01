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

package com.botbrain.dino.utils.ip;

public class IpUtils {

	/**
	 * 私有IP: A类 10.0.0.0 ~ 10.255.255.255 B类 172.16.0.0 ~ 172.31.255.255 C类
	 * 192.168.0.0 ~ 192.168.255.255 以及127这个网段是环回地址
	 * 
	 * @param ip
	 * @return
	 */
	private static long innerIpRange[] = new long[] { ipv4toNum("10.0.0.0"), ipv4toNum("10.255.255.255"),
			ipv4toNum("172.16.0.0"), ipv4toNum("172.31.255.255"), ipv4toNum("192.168.0.0"), ipv4toNum("192.168.255.255"),
			ipv4toNum("127.0.0.1"), ipv4toNum("127.0.0.1") };

	private IpUtils() {
	}

	public static boolean isInner(String ip) {
		long lip = ipv4toNum(ip);
		if (lip == 0) {
			return true;
		}
		return (lip >= innerIpRange[0] && lip <= innerIpRange[1]) || (lip >= innerIpRange[2] && lip <= innerIpRange[3])
				|| (lip >= innerIpRange[4] && lip <= innerIpRange[5]) || (lip >= innerIpRange[6] && lip <= innerIpRange[7]);
	}

	public static boolean isOuter(String ip) {
		return !isInner(ip);
	}

	// 把IP地址转换为Long型数字
	public static long ipv4toNum(String ipAddress) {
		String[] ip = ipAddress.split(".");
		if (ip.length != 4) {
			return 0;
		}

		try {
			long first = Integer.valueOf(ip[0]) << 24;
			long second = Integer.valueOf(ip[1]) << 16;
			long three = Integer.valueOf(ip[2]) << 8;
			long four = Integer.valueOf(ip[3]);
			return first + second + three + four;
		} catch (NumberFormatException e) {
			return 0;
		}

	}

}
