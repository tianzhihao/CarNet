package cn.edu.qtc.net;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpUntils {
	/**
	 * Ĭ�Ϸ��ʳ�ʱʱ��Ϊ10��test
	 */
	private static AsyncHttpClient client = new AsyncHttpClient();

	public static void get(String urlString, AsyncHttpResponseHandler res) // ��һ������url��ȡһ��string����,��������
	{
		client.get(urlString, res);
	}

	public static void getWithParams(String urlString, RequestParams params,
			AsyncHttpResponseHandler res) // ��һ������url��ȡһ��string����,
											// url���������
	{
		client.get(urlString, params, res);
	}

	public static void post(String urlString, AsyncHttpResponseHandler res) // ��һ������url��ȡһ��string����,��������
	{
		client.post(urlString, res);
	}

	public static void postWithParams(String urlString, RequestParams params,
			AsyncHttpResponseHandler res) // ��һ������url��ȡһ��string����,
											// url���������
	{
		client.post(urlString, params, res);
	}

	public static void getJson(String urlString, JsonHttpResponseHandler res)
	// ������������ȡjson�����������
	{
		client.get(urlString, res);
	}

	public static void getJsonWithParams(String urlString,
			RequestParams params, JsonHttpResponseHandler res) // ����������ȡjson�����������
	{
		client.get(urlString, params, res);
	}

	public static void getByte(String uString,
			BinaryHttpResponseHandler bHandler) // ��������ʹ�ã��᷵��byte����
	{
		client.get(uString, bHandler);
	}

	public static AsyncHttpClient getClient() {
		return client;//����static client
	}

//	public static void download(String uString,
//			BinaryHttpResponseHandler bHandler) // ��������ʹ�ã��᷵��byte����
//	{
//		client.get(uString, bHandler);
//	}
	public static void download(String uString,
			FileAsyncHttpResponseHandler bHandler) // ��������ʹ�ã��᷵��byte����
	{
		client.get(uString, bHandler);
	}
	public static void upload(String urlString, RequestParams params,
			AsyncHttpResponseHandler res) {//�ϴ�
		client.post(urlString, params, res);
	}
}
