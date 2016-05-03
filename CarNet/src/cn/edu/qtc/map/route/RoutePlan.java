package cn.edu.qtc.map.route;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption.DrivingPolicy;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

/**
 * ·�߹滮
 * 
 * @author linlin
 * 
 */
public class RoutePlan implements OnGetRoutePlanResultListener,
		OnGetGeoCoderResultListener {

	private Context context;
	private boolean useDefaultIcon = false;
	private BaiduMap baiduMap = null;
	// �������
	private RoutePlanSearch mSearch = null; // ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
	private GeoCoder mSearchGeo = null;

	private Double Lat;
	private Double Lon;

	private String startCity;
	private String endCity;
	private String stValue;
	private String enValue;
	// ·�߹滮��ʽ
	private DrivingPolicy ROUTE_WAY = DrivingPolicy.ECAR_AVOID_JAM;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				// ���Ϊ����
				String[] pointData = stValue.split("-");
				PlanNode stNode = PlanNode.withLocation(new LatLng(Double
						.valueOf(pointData[0]), Double.valueOf(pointData[1])));
				PlanNode enNode = PlanNode.withLocation(new LatLng(Lat, Lon));
				DrivingPolicy policy = ROUTE_WAY;
				mSearch.drivingSearch((new DrivingRoutePlanOption())
						.from(stNode).to(enNode).policy(policy));

				// ��ʼ������
				Lat = 0.0;
				Lon = 0.0;

				break;
			case 1:
				String[] pointData2 = enValue.split("-");
				PlanNode enNode2 = PlanNode
						.withLocation(new LatLng(Double.valueOf(pointData2[0]),
								Double.valueOf(pointData2[1])));
				PlanNode stNode2 = PlanNode.withLocation(new LatLng(Lat, Lon));

				DrivingPolicy policy2 = ROUTE_WAY;
				mSearch.drivingSearch((new DrivingRoutePlanOption())
						.from(stNode2).to(enNode2).policy(policy2));
				// ��ʼ������
				Lat = 0.0;
				Lon = 0.0;
				break;
			default:
				break;
			}
		};
	};

	public RoutePlan(Context context, BaiduMap baiduMap) {
		this.baiduMap = baiduMap;
		this.context = context;

		init();
	}

	/***
	 * �ݳ�·�߹滮
	 * 
	 * @param startCity
	 * @param startPoint
	 * @param endCity
	 * @param endPoint
	 */
	public void drivePlan(String startCity, String stValue, String endCity,
			String enValue) {
		if (!startCity.equals("") && !stValue.equals("") && !endCity.equals("")
				&& !enValue.equals("")) {
			this.startCity = startCity;
			this.endCity = endCity;
			this.stValue = stValue;
			this.enValue = enValue;
			
		//	Toast.makeText(context, stValue+"++++++++++++"+enValue, 0).show();
			// ���Ϊ����
			if (stValue.indexOf(".") != -1&&enValue.indexOf(".") == -1) {
				mSearchGeo.geocode(new GeoCodeOption().city(startCity).address(
						enValue));
			//	Toast.makeText(context, "���Ϊ����", 0).show();

			} else if (enValue.indexOf(".") != -1&&stValue.indexOf(".") == -1) {
				// �յ�Ϊ����
				mSearchGeo.geocode(new GeoCodeOption().city(startCity).address(
						stValue));
			//	Toast.makeText(context, "�յ�Ϊ����", 0).show();
			} else if (enValue.indexOf(".") != -1 && stValue.indexOf(".") != -1) {
				// ��� �յ㶼Ϊ����
				String[] stGroup = stValue.split("-");
				String[] enGroup = enValue.split("-");
				PlanNode stNode = PlanNode.withLocation(new LatLng(Double
						.valueOf(stGroup[0]), Double.valueOf(stGroup[1])));
				PlanNode enNode = PlanNode.withLocation(new LatLng(Double
						.valueOf(enGroup[0]), Double.valueOf(enGroup[1])));
				// �滮���� ����ӵ��
				DrivingPolicy policy = ROUTE_WAY;
				mSearch.drivingSearch((new DrivingRoutePlanOption())
						.from(stNode).to(enNode).policy(policy));
			//.makeText(context, "��� �յ㶼Ϊ����", 0).show();
			} else {

				PlanNode stNode = PlanNode.withCityNameAndPlaceName(startCity,
						stValue);
				PlanNode enNode = PlanNode.withCityNameAndPlaceName(endCity,
						enValue);
				// �滮���� ����ӵ��
				DrivingPolicy policy = ROUTE_WAY;
				mSearch.drivingSearch((new DrivingRoutePlanOption())
						.from(stNode).to(enNode).policy(policy));
			//	Toast.makeText(context, "��� �յ㶼Ϊ����", 0).show();
			}

		}

	}

	private void init() {
		// ��ʼ������ģ�飬ע���¼�����
		mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(this);
		mSearchGeo = GeoCoder.newInstance();
		mSearchGeo.setOnGetGeoCodeResultListener(this);

	}

	@Override
	public void onGetBikingRouteResult(BikingRouteResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(context, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// ���յ��;�����ַ����壬ͨ�����½ӿڻ�ȡ�����ѯ��Ϣ

			Toast.makeText(context, "���յ��;�����ַ�����", Toast.LENGTH_SHORT).show();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			// nodeIndex = -1;
			// mBtnPre.setVisibility(View.VISIBLE);
			// mBtnNext.setVisibility(View.VISIBLE);
			// route = result.getRouteLines().get(0);
			DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(baiduMap);
			baiduMap.clear();
			baiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult arg0) {
		// TODO Auto-generated method stub

	}

	// ����RouteOverly
	private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

		public MyDrivingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory
						.fromResource(cn.edu.qtc.main.R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory
						.fromResource(cn.edu.qtc.main.R.drawable.icon_en);
			}
			return null;
		}
	}

	/**
	 * �ص�ת��Ϊ����
	 */
	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(context, "����ת��ʧ��", Toast.LENGTH_LONG).show();
			return;
		}
		Lat = result.getLocation().latitude;
		Lon = result.getLocation().longitude;
		if (Lat != null || Lon != null) {
			if (stValue.indexOf(".") != -1) {
				// ���Ϊ����
				handler.sendEmptyMessage(0);
			} else {
				// �յ�Ϊ����
				handler.sendEmptyMessage(1);
			}

		} else {
			Toast.makeText(context, "·�߹滮ʧ�ܣ�", Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
		// TODO Auto-generated method stub

	}

}
