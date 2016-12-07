package com.nxt.wly.util;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;

import com.loopj.android.http.AsyncHttpClient;

public class FinalAsyncHttpClent {

	private AsyncHttpClient client;

	/* 构造方法 */
	public FinalAsyncHttpClent() {
		client = new AsyncHttpClient();//实例化client
		client.setTimeout(5);//设置5秒超时
		// 获取cookie列表
		if (CookieUtils.getCookies() != null) {
			BasicCookieStore bcs = new BasicCookieStore();
			bcs.addCookies(CookieUtils.getCookies().toArray(
					new Cookie[CookieUtils.getCookies().size()]));//得到cookie列表
			client.setCookieStore(bcs);//给client加载cookie
		}
	}

	/* 得到client对象方法 */
	public AsyncHttpClient getAsyncHttpClient() {
		return this.client;
	}

}