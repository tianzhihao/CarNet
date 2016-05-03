package cn.edu.qtc.map.search;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.qtc.main.R;
import cn.edu.qtc.map.location.Location;
import cn.edu.qtc.map.route.RoutePlan;
import cn.edu.qtc.utils.ContextValue;
import cn.edu.qtc.utils.JsonUtils;
import cn.edu.qtc.vo.GasStationInfo;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * �����ṩ����������
 * 
 * @author linlin
 * 
 */
public class PoiOverlaySearch implements OnGetPoiSearchResultListener {

	private PoiSearch mPoiSearch = null;
	private Context context;
	private BaiduMap mBaiduMap = null;
	private Dialog dialog;
	private MyPoiOverlay myPoiOverlay;
	private int chocie;
	private TextureMapView mapView;

	public PoiOverlaySearch(Context context, BaiduMap mBaiduMap,
			TextureMapView mapView) {
		this.mBaiduMap = mBaiduMap;
		this.mapView = mapView;
		this.context = context;
		init();
	}

	/**
	 * ִ����������
	 */
	public void SearchNearBy(LatLng point, String value, int area) {

		PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption();
		nearbySearchOption.keyword(value);
		nearbySearchOption.location(point);
		nearbySearchOption.radius(area);
		mPoiSearch.searchNearby(nearbySearchOption);
	}

	/**
	 * ��ʼ��
	 */
	public void init() {
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);

	}

	/**
	 * ��������ص�
	 */
	@Override
	public void onGetPoiDetailResult(PoiDetailResult arg0) {

	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			Toast.makeText(context, "δ�ҵ����", Toast.LENGTH_LONG).show();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			// mBaiduMap.clear();
			myPoiOverlay = new MyPoiOverlay(mBaiduMap, context);

			PoiOverlay overlay = myPoiOverlay;
			mBaiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result);
			overlay.addToMap();
			// overlay.zoomToSpan();
			return;
		}

		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

			// ������ؼ����ڱ���û���ҵ����������������ҵ�ʱ�����ذ����ùؼ�����Ϣ�ĳ����б�
			String strInfo = "��";
			for (CityInfo cityInfo : result.getSuggestCityList()) {
				strInfo += cityInfo.city;
				strInfo += ",";
			}
			strInfo += "�ҵ����";
			Toast.makeText(context, strInfo, Toast.LENGTH_LONG).show();
		}

	}

	public class MyPoiOverlay extends cn.edu.qtc.map.search.PoiOverlay {
		// �ۺ�����URL
		private String urlString = "http://apis.juhe.cn/oil/local";
		public List<PoiInfo> infos = null;

		public MyPoiOverlay(BaiduMap baiduMap, Context context) {
			super(baiduMap, context);

		}

		public List<PoiInfo> getPoiInfo() {
			return infos;
		}

		@Override
		public boolean onPoiClick(int index) {
			super.onPoiClick(index);
			PoiInfo poi = getPoiResult().getAllPoi().get(index);
			infos = getPoiResult().getAllPoi();
			chocie = index;
			// Toast.makeText(context,
			// poi.location.longitude+"---"+poi.location.latitude, 0).show();
			// Toast.makeText(context, poi.address+poi.name+poi.uid, 0).show();
			if (poi != null) {
				accessNet(poi.location.longitude, poi.location.latitude,
						urlString);
				// Log.i("����", poi.location.longitude+""+poi.location.latitude);
			} else {
				Toast.makeText(context, "��ȡ����ʧ��~", 0).show();
			}

			return true;
		}

	}

	/**
	 * չʾ����վ��ϢDialog
	 */
	public void showDialog(GasStationInfo stationInfo) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.main_custom_dialog, null);
		// ��ʼ���ؼ�
		ListView listView = (ListView) view
				.findViewById(R.id.main_custom_dialog_listview);
		TextView title = (TextView) view
				.findViewById(R.id.main_custom_dialog_title);

		((Button) view.findViewById(R.id.main_custom_dialog_ordergas))
				.setOnClickListener(new MyDialogOnClickListener());
		((Button) view.findViewById(R.id.main_custom_dialog_routeplan))
				.setOnClickListener(new MyDialogOnClickListener());
		((Button) view.findViewById(R.id.main_custom_dialog_next))
				.setOnClickListener(new MyDialogOnClickListener());
		((Button) view.findViewById(R.id.main_custom_dialog_dismiss))
				.setOnClickListener(new MyDialogOnClickListener());

		// ׼������
		List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();

		Map<String, Object> adress = new HashMap<String, Object>();
		adress.put("itme", "��ַ");
		adress.put("value", stationInfo.address);
		listems.add(adress);
		Map<String, Object> brandname = new HashMap<String, Object>();
		brandname.put("itme", "��Ӫ������");
		brandname.put("value", stationInfo.brandname);
		listems.add(brandname);
		Map<String, Object> type = new HashMap<String, Object>();
		type.put("itme", "����վ����");
		type.put("value", stationInfo.type);
		listems.add(type);
		Map<String, Object> fwlsmc = new HashMap<String, Object>();
		fwlsmc.put("itme", "���Ϳ���Ϣ");
		fwlsmc.put("value", stationInfo.fwlsmc);
		listems.add(fwlsmc);
		Map<String, Object> price = new HashMap<String, Object>();
		price.put("itme", "��Ǯ");
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < stationInfo.gastprice.size(); ++i) {
			buffer.append(stationInfo.gastprice.get(i).get("name") + " : ");
			buffer.append(stationInfo.gastprice.get(i).get("price") + " Ԫ    ");
		}
		price.put("value", buffer.toString());
		listems.add(price);
		Map<String, Object> point = new HashMap<String, Object>();
		point.put("itme", "����");
		point.put("value", stationInfo.lat + "--" + stationInfo.lon);
		listems.add(point);

		// ��������
		title.setText(stationInfo.name);
		SimpleAdapter adapter = new SimpleAdapter(context, listems,
				android.R.layout.simple_list_item_2, new String[] { "itme",
						"value" }, new int[] { android.R.id.text1,
						android.R.id.text2 });
		listView.setAdapter(adapter);
		dialog = new Dialog(context, R.style.DialogTheme);
		dialog.setContentView(view);
		dialog.setCancelable(true);
		dialog.show();
	}

	/**
	 * Dialog ��ť����
	 * 
	 * @author linlin
	 * 
	 */
	private class MyDialogOnClickListener implements OnClickListener {
		private String urlString = "http://apis.juhe.cn/oil/local";

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			// �ص��Ի���
			case R.id.main_custom_dialog_dismiss:
				dialog.dismiss();

				break;
			// ԤԼ����
			case R.id.main_custom_dialog_ordergas:
				dialog.dismiss();

				break;
			// ·�߹滮
			case R.id.main_custom_dialog_routeplan:
				dialog.dismiss();

				DecimalFormat df = new DecimalFormat("###.000000");
				String lon = df
						.format(myPoiOverlay.getPoiInfo().get(chocie).location.longitude);
				String lat = df
						.format(myPoiOverlay.getPoiInfo().get(chocie).location.latitude);

				RoutePlan plan = new RoutePlan(context, mBaiduMap);
				Location location = new Location(mapView, context);
				if (location != null) {
					plan.drivePlan(location.getNowCity(),
							location.getLocation(), location.getNowCity(), lat
									+ "-" + lon);
				}

				break;
			// ��һ������վ
			case R.id.main_custom_dialog_next:
				dialog.dismiss();
				int sizenext = myPoiOverlay.getPoiInfo().size();
				int randomnext = (int) (Math.random() * sizenext);
				Log.i("next", randomnext + "");
				if (randomnext != chocie) {
					chocie = randomnext;
					PoiInfo poi = myPoiOverlay.getPoiInfo().get(randomnext);
					accessNet(poi.location.longitude, poi.location.latitude,
							urlString);
				}
				break;
			default:
				break;
			}

		}

	}

	/**
	 * ���������ѯ������Ϣ
	 * 
	 */
	public void accessNet(Double lonD, Double latD, String url) {
		DecimalFormat df = new DecimalFormat("###.000000");
		Double lon = Double.parseDouble(df.format(lonD));
		Double lat = Double.parseDouble(df.format(latD));
		MyAsyncHttpResponseHandler handler = new MyAsyncHttpResponseHandler();
		RequestParams params = new RequestParams();
		// key string
		params.put("key", ContextValue.key);
		// lon double
		params.put("lon", lon);
		// lat double
		params.put("lat", lat);
		// r int
		params.put("r", 200);
		// format int ��ʽѡ��1��2��Ĭ��1
		params.put("format", 2);

		cn.edu.qtc.net.HttpUntils.getWithParams(url, params, handler);

	}

	/**
	 * �������վ���ݼ���
	 * 
	 * @author linlin
	 * 
	 */
	private class MyAsyncHttpResponseHandler extends AsyncHttpResponseHandler {

		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2,
				Throwable arg3) {
			Toast.makeText(context, "��������ʧ�ܣ�", 0).show();

		}

		@Override
		public void onSuccess(int arg0, Header[] arg1, byte[] data) {
			// Toast.makeText(context, new String(data), 0).show();
			// Log.i("Json", new String(data));
			try {
				JSONObject jsonObject = new JSONObject(new String(data));
				String result = jsonObject.getString("result");

				if (!result.equals("null")) {
					GasStationInfo stationInfo = JsonUtils.GasStationInfo(
							new String(data), context);
					if (stationInfo != null) {
						showDialog(stationInfo);
					} else {
						Toast.makeText(context, "�ü���վ����Ϣ��", 0).show();
					}
				} else {
					Toast.makeText(context, "�ü���վ����Ϣ��", 0).show();
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Toast.makeText(context, "Json���������", 0).show();
				e.printStackTrace();
			}

		}

	}

	/**
	 * ����search
	 */
	public void searchDestory() {
		mPoiSearch.destroy();
	}

}
