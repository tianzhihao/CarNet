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
 * 路线规划
 * 
 * @author linlin
 * 
 */
public class RoutePlan implements OnGetRoutePlanResultListener,
		OnGetGeoCoderResultListener {

	private Context context;
	private boolean useDefaultIcon = false;
	private BaiduMap baiduMap = null;
	// 搜索相关
	private RoutePlanSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private GeoCoder mSearchGeo = null;

	private Double Lat;
	private Double Lon;

	private String startCity;
	private String endCity;
	private String stValue;
	private String enValue;
	// 路线规划方式
	private DrivingPolicy ROUTE_WAY = DrivingPolicy.ECAR_AVOID_JAM;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				// 起点为坐标
				String[] pointData = stValue.split("-");
				PlanNode stNode = PlanNode.withLocation(new LatLng(Double
						.valueOf(pointData[0]), Double.valueOf(pointData[1])));
				PlanNode enNode = PlanNode.withLocation(new LatLng(Lat, Lon));
				DrivingPolicy policy = ROUTE_WAY;
				mSearch.drivingSearch((new DrivingRoutePlanOption())
						.from(stNode).to(enNode).policy(policy));

				// 初始化坐标
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
				// 初始化坐标
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
	 * 驾车路线规划
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
			// 起点为坐标
			if (stValue.indexOf(".") != -1&&enValue.indexOf(".") == -1) {
				mSearchGeo.geocode(new GeoCodeOption().city(startCity).address(
						enValue));
			//	Toast.makeText(context, "起点为坐标", 0).show();

			} else if (enValue.indexOf(".") != -1&&stValue.indexOf(".") == -1) {
				// 终点为坐标
				mSearchGeo.geocode(new GeoCodeOption().city(startCity).address(
						stValue));
			//	Toast.makeText(context, "终点为坐标", 0).show();
			} else if (enValue.indexOf(".") != -1 && stValue.indexOf(".") != -1) {
				// 起点 终点都为坐标
				String[] stGroup = stValue.split("-");
				String[] enGroup = enValue.split("-");
				PlanNode stNode = PlanNode.withLocation(new LatLng(Double
						.valueOf(stGroup[0]), Double.valueOf(stGroup[1])));
				PlanNode enNode = PlanNode.withLocation(new LatLng(Double
						.valueOf(enGroup[0]), Double.valueOf(enGroup[1])));
				// 规划策略 避免拥堵
				DrivingPolicy policy = ROUTE_WAY;
				mSearch.drivingSearch((new DrivingRoutePlanOption())
						.from(stNode).to(enNode).policy(policy));
			//.makeText(context, "起点 终点都为坐标", 0).show();
			} else {

				PlanNode stNode = PlanNode.withCityNameAndPlaceName(startCity,
						stValue);
				PlanNode enNode = PlanNode.withCityNameAndPlaceName(endCity,
						enValue);
				// 规划策略 避免拥堵
				DrivingPolicy policy = ROUTE_WAY;
				mSearch.drivingSearch((new DrivingRoutePlanOption())
						.from(stNode).to(enNode).policy(policy));
			//	Toast.makeText(context, "起点 终点都为中文", 0).show();
			}

		}

	}

	private void init() {
		// 初始化搜索模块，注册事件监听
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
			Toast.makeText(context, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息

			Toast.makeText(context, "起终点或途经点地址有岐义", Toast.LENGTH_SHORT).show();
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

	// 定制RouteOverly
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
	 * 地点转换为坐标
	 */
	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(context, "坐标转换失败", Toast.LENGTH_LONG).show();
			return;
		}
		Lat = result.getLocation().latitude;
		Lon = result.getLocation().longitude;
		if (Lat != null || Lon != null) {
			if (stValue.indexOf(".") != -1) {
				// 起点为坐标
				handler.sendEmptyMessage(0);
			} else {
				// 终点为坐标
				handler.sendEmptyMessage(1);
			}

		} else {
			Toast.makeText(context, "路线规划失败！", Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
		// TODO Auto-generated method stub

	}

}
