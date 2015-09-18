package com.jq.livingnavigation.utils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Http工具类
 * 
 * @author yb
 * 
 */
public class HttpUtils {

	/**
	 * 默认连接超时时间
	 */
	public static final int DEFAULT_CON_TIME = 10 * 1000;
	/**
	 * 默认请求超时时间
	 */
	public static final int DEFAULT_SO_TIME = 20 * 1000;

	/**
	 * doGet请求
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 *             请求链接失败
	 * @throws ServerException
	 *             返回服务错误
	 */
	public synchronized static String doGet(String url) throws IOException,
			ServerException {
		return doGet(url, null);
	}

	/**
	 * doGet请求
	 * 
	 * @param url
	 * @param parms
	 *            参数map
	 * @return
	 * @throws IOException
	 *             请求链接失败
	 * @throws ServerException
	 *             返回服务错误
	 */
	public synchronized static String doGet(String url,
			Map<String, String> parms) throws IOException, ServerException {
		HttpClient client = getHttpClient(DEFAULT_CON_TIME, DEFAULT_SO_TIME);
		StringBuffer sb = new StringBuffer(url).append("?");
		if (parms != null && !parms.isEmpty()) {
			for (Entry<String, String> entry : parms.entrySet()) {
				sb.append(entry.getKey()).append("=").append(entry.getValue())
						.append("&");
			}
		}
		HttpGet httpGet = new HttpGet(sb.toString().substring(0,
				sb.length() - 1));
		HttpResponse response = client.execute(httpGet);
		int code = response.getStatusLine().getStatusCode();
		if (HttpStatus.SC_OK == code) {
			String result = EntityUtils.toString(response.getEntity(), "UTF-8");
			return result;
		} else {
			throw new ServerException("http response code:" + code);
		}
	}

	/**
	 * doPost请求
	 * 
	 * @param url
	 * @param parms
	 *            参数map
	 * @return
	 * @throws IOException
	 *             请求链接失败
	 * @throws ServerException
	 *             返回服务错误
	 */
	public synchronized static String doPost(String url,
			Map<String, String> parms) throws IOException, ServerException {
		HttpClient client = getHttpClient(DEFAULT_CON_TIME, DEFAULT_SO_TIME);
		HttpPost httpPost = new HttpPost(url);
		// httpPost.setHeader("content-type", "application/json");
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		for (Entry<String, String> p : parms.entrySet()) {
			parameters.add(new BasicNameValuePair(p.getKey(), p.getValue()));
		}
		httpPost.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
		HttpResponse response = client.execute(httpPost);
		int code = response.getStatusLine().getStatusCode();
		if (HttpStatus.SC_OK == code) {
			String result = EntityUtils.toString(response.getEntity(), "UTF-8");
			return result;
		} else {
			throw new ServerException("http response code:" + code);
		}
	}

	/**
	 * httpClient
	 * 
	 * @param connectionTime
	 *            连接超时
	 * @param soTime
	 *            请求超时
	 * @return
	 */
	public synchronized static HttpClient getHttpClient(int connectionTime,
			int soTime) {
		HttpParams params = new BasicHttpParams();
		/* 连接超时 */
		HttpConnectionParams.setConnectionTimeout(params, connectionTime);
		/* 请求超时 */
		HttpConnectionParams.setSoTimeout(params, soTime);
		return new DefaultHttpClient(params);
	}

}
