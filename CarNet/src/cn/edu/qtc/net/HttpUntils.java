package cn.edu.qtc.net;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpUntils {
	/**
	 * 默认访问超时时间为10秒test
	 */
	private static AsyncHttpClient client = new AsyncHttpClient();

	public static void get(String urlString, AsyncHttpResponseHandler res) // 用一个完整url获取一个string对象,不带参数
	{
		client.get(urlString, res);
	}

	public static void getWithParams(String urlString, RequestParams params,
			AsyncHttpResponseHandler res) // 用一个完整url获取一个string对象,
											// url里面带参数
	{
		client.get(urlString, params, res);
	}

	public static void post(String urlString, AsyncHttpResponseHandler res) // 用一个完整url获取一个string对象,不带参数
	{
		client.post(urlString, res);
	}

	public static void postWithParams(String urlString, RequestParams params,
			AsyncHttpResponseHandler res) // 用一个完整url获取一个string对象,
											// url里面带参数
	{
		client.post(urlString, params, res);
	}

	public static void getJson(String urlString, JsonHttpResponseHandler res)
	// 不带参数，获取json对象或者数组
	{
		client.get(urlString, res);
	}

	public static void getJsonWithParams(String urlString,
			RequestParams params, JsonHttpResponseHandler res) // 带参数，获取json对象或者数组
	{
		client.get(urlString, params, res);
	}

	public static void getByte(String uString,
			BinaryHttpResponseHandler bHandler) // 下载数据使用，会返回byte数据
	{
		client.get(uString, bHandler);
	}

	public static AsyncHttpClient getClient() {
		return client;//返回static client
	}

//	public static void download(String uString,
//			BinaryHttpResponseHandler bHandler) // 下载数据使用，会返回byte数据
//	{
//		client.get(uString, bHandler);
//	}
	public static void download(String uString,
			FileAsyncHttpResponseHandler bHandler) // 下载数据使用，会返回byte数据
	{
		client.get(uString, bHandler);
	}
	public static void upload(String urlString, RequestParams params,
			AsyncHttpResponseHandler res) {//上传
		client.post(urlString, params, res);
	}
}
