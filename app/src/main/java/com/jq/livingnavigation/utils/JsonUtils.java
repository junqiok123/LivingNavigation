package com.jq.livingnavigation.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Json转换工具类
 * 
 * @author yb
 * 
 */
public class JsonUtils {

	private static Gson gson;

	static {
		gson = new Gson();
	}

	/**
	 * json字符串转单个简单对象
	 * 
	 * @param jsonStr
	 * @param t
	 *            Order.class
	 * @return
	 * @throws Exception
	 */
	public static <T> T getObject(String jsonStr, Class<T> t) throws Exception {
		return gson.fromJson(jsonStr, t);
	}

	/**
	 * json字符串转单个复杂对象
	 * 
	 * @param jsonStr
	 * @param type
	 *            new TypeToken<JsonResult<Order>>() { }.getType()
	 * @return
	 * @throws Exception
	 */
	public static <T> T getObject(String jsonStr, Type type) {
		return gson.fromJson(jsonStr, type);
	}

	public static String toString(Object t) {
		return gson.toJson(t);
	}

}
