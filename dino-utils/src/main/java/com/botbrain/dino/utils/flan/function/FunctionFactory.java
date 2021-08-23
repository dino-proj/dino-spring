package com.botbrain.dino.utils.flan.function;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import com.botbrain.dino.utils.flan.function.imp.FNBase64Decode;
import com.botbrain.dino.utils.flan.function.imp.FNBase64Encode;
import com.botbrain.dino.utils.flan.function.imp.FNBase64EncodeUrlSafe;
import com.botbrain.dino.utils.flan.function.imp.FNBePrefix;
import com.botbrain.dino.utils.flan.function.imp.FNBreak;
import com.botbrain.dino.utils.flan.function.imp.FNCity;
import com.botbrain.dino.utils.flan.function.imp.FNConst;
import com.botbrain.dino.utils.flan.function.imp.FNDefault;
import com.botbrain.dino.utils.flan.function.imp.FNGUID;
import com.botbrain.dino.utils.flan.function.imp.FNHttpCookie;
import com.botbrain.dino.utils.flan.function.imp.FNHttpCookieSet;
import com.botbrain.dino.utils.flan.function.imp.FNHttpHeader;
import com.botbrain.dino.utils.flan.function.imp.FNHttpParam;
import com.botbrain.dino.utils.flan.function.imp.FNHttpPathInfo;
import com.botbrain.dino.utils.flan.function.imp.FNIP;
import com.botbrain.dino.utils.flan.function.imp.FNIPRemote;
import com.botbrain.dino.utils.flan.function.imp.FNIncrease;
import com.botbrain.dino.utils.flan.function.imp.FNNotEmpty;
import com.botbrain.dino.utils.flan.function.imp.FNNow;
import com.botbrain.dino.utils.flan.function.imp.FNNowFormatted;
import com.botbrain.dino.utils.flan.function.imp.FNNowNano;
import com.botbrain.dino.utils.flan.function.imp.FNProp;
import com.botbrain.dino.utils.flan.function.imp.FNProvince;
import com.botbrain.dino.utils.flan.function.imp.FNReferer;
import com.botbrain.dino.utils.flan.function.imp.FNReqMethod;
import com.botbrain.dino.utils.flan.function.imp.FNReqProtocol;
import com.botbrain.dino.utils.flan.function.imp.FNReqUri;
import com.botbrain.dino.utils.flan.function.imp.FNToLowerCase;
import com.botbrain.dino.utils.flan.function.imp.FNUAToProperty;
import com.botbrain.dino.utils.flan.function.imp.FNUrlHost;
import com.botbrain.dino.utils.flan.function.imp.FNUrlParam;
import com.botbrain.dino.utils.flan.function.imp.FNUrlPath;
import com.botbrain.dino.utils.flan.function.imp.FNUserAgent;
import com.botbrain.dino.utils.flan.function.imp.FNVarGet;
import com.botbrain.dino.utils.flan.function.imp.FNVarSet;
import com.botbrain.dino.utils.flan.function.imp.OPerCallchain;
import com.botbrain.dino.utils.flan.function.imp.OPerJoin;
import com.botbrain.dino.utils.flan.function.imp.OPerOr;

public class FunctionFactory {
	public static final String FUNC_CONST = "CONST";
	public static final String FUNC_MAP = "MAP";

	private Map<String, FunctionMetaInfo> functions = new HashMap<String, FunctionMetaInfo>();
	private Map<String, Class<? extends Oper>> ops = new HashMap<String, Class<? extends Oper>>();

	public static FunctionFactory emptyFactory() {
		return new FunctionFactory();
	}

	public static FunctionFactory buildinFactory() {
		FunctionFactory f = new FunctionFactory();
		f.registryBuildins();
		return f;
	}

	private FunctionFactory() {
	};

	private void registryBuildins() {
		registryOper("OR", OPerOr.class);
		registryOper("+", OPerJoin.class);
		registryOper("?", OPerCallchain.class);

		registry(FUNC_CONST, FNConst.class);

		registry("const", FNConst.class);
		registry("default", FNDefault.class);
		registry("break", FNBreak.class);
		registry("as_var", FNVarSet.class);
		registry("var", FNVarGet.class);
		registry("guid", FNGUID.class);
		registry("incr", FNIncrease.class);
		registry("prefix", FNBePrefix.class);

		registry("now", FNNow.class);
		registry("now_nano", FNNowNano.class);
		registry("now_fmt", FNNowFormatted.class);
		registry("ip", FNIP.class);
		registry("ip_remote", FNIPRemote.class);
		registry("method", FNReqMethod.class);
		registry("uri", FNReqUri.class);
		registry("protocol", FNReqProtocol.class);
		registry("not_empty", FNNotEmpty.class);
		registry("base64_decode", FNBase64Decode.class);
		registry("base64_encode", FNBase64Encode.class);
		registry("base64_encode_urlsafe", FNBase64EncodeUrlSafe.class);
		registry("url_host", FNUrlHost.class);
		registry("url_path", FNUrlPath.class);
		registry("url_param", FNUrlParam.class);
		registry("param", FNHttpParam.class);
		registry("prop", FNProp.class);
		registry("header", FNHttpHeader.class);
		registry("cookie", FNHttpCookie.class);
		registry("set_cookie", FNHttpCookieSet.class);
		registry("useragent", FNUserAgent.class);
		registry("referer", FNReferer.class);
		registry("uaprop", FNUAToProperty.class);
		registry("province", FNProvince.class);
		registry("city", FNCity.class);
		registry("path_info", FNHttpPathInfo.class);
		registry("lower_case", FNToLowerCase.class);
	}

	/**
	 * 注册一个函数
	 * 
	 * @param fnName
	 *            ，函数的名字，如果名字已经被注册，则抛出异常IllegalArgumentException
	 * @param fnCls
	 *            ，函数的Class类
	 */
	public <T extends Function> void registry(String fnName, Class<T> fnCls) {
		notBlank(fnName, "fnName should not be empty");
		notNull(fnCls, "fnCls should not be null");
		isTrue(!Modifier.isAbstract(fnCls.getModifiers()), "fnCls:[%s] should not be abstract", fnCls.getName());
		isTrue(Modifier.isPublic(fnCls.getModifiers()), "fnCls:[%s] should be public", fnCls.getName());
		if (functions.containsKey(fnName)) {
			throw new IllegalArgumentException(
					String.format("fnName:[%s] has registried as class:[%s]", fnName, functions.get(fnName).getFnClassName()));
		}
		functions.put(fnName, new FunctionMetaInfo(fnName, fnCls));
	}

	/**
	 * 注册一个操作符
	 * 
	 * @param opName
	 *            操作符的名字，操作符会转成大写，如果名字已经被注册，则抛出异常IllegalArgumentException
	 * @param opCLs
	 *            操作符的Class类
	 */
	private <T extends Oper> void registryOper(String opName, Class<T> opCLs) {
		notBlank(opName, "opName should not be empty");
		notNull(opCLs, "opCls should not be null");
		isTrue(!Modifier.isAbstract(opCLs.getModifiers()), "opCls:[%s] should not be abstract", opCLs.getName());
		isTrue(Modifier.isPublic(opCLs.getModifiers()), "opCls:[%s] should be public", opCLs.getName());
		if (ops.containsKey(opName)) {
			throw new IllegalArgumentException(
					String.format("opName:[%s] has registried as class:[%s]", opName, ops.get(opName.toUpperCase()).getName()));
		}

		ops.put(opName.toUpperCase(), opCLs);
	}

	/**
	 * 获取一个操作符
	 * @param name 操作符的名字
	 * @return 操作符类型
	 */
	public Class<? extends Oper> getOper(String name) {
		return ops.get(name);
	}

	/**
	 * 获取一个函数
	 * @param name 函数名字
	 * @return 函数类型
	 */
	public Class<? extends Function> getFunction(String name) {
		FunctionMetaInfo fm = functions.get(name);
		return fm == null ? null : fm.getFnClass();
	}

	/**
	 * 获取函数Meta信息
	 * @param name 函数名字
	 * @return Meta信息
	 */
	public FunctionMetaInfo getFunctionMetaInfo(String name) {
		return functions.get(name);
	}
}
