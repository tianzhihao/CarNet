package cn.edu.qtc.map.location;

import android.content.Context;
import android.util.Log;
import cn.edu.qtc.map.search.PoiOverlaySearch;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

/**
 * �����ṩ��λ���� �����������������ڴ��� �� ��λͬʱҲ����������
 * 
 * @author linlin
 * 
 */
public class Location {

	public static String nowCity;
	public static String nowLocation;
	public MyLocationListenner myListener;
	private LocationMode mCurrentMode;
	private TextureMapView mapView;
	private boolean isFirstLoc;
	private BaiduMap baiduMap;
	private LocationClient mLocClient;
	private Context context;
	// �Զ���ͼ��
	private BitmapDescriptor mCurrentMarker;

	public Location(TextureMapView mapView, Context context) {
		this.context = context;
		this.mapView = mapView;
		baiduMap = mapView.getMap();

		init();

	}

	private void init() {
		myListener = new MyLocationListenner();
		// ������λͼ��
		baiduMap.setMyLocationEnabled(true);
		mLocClient = new LocationClient(context);
		mLocClient.registerLocationListener(myListener);
		mCurrentMode = LocationMode.NORMAL;
		baiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, null));
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // ��gps
		option.setIsNeedAddress(true);
		option.setLocationMode(com.baidu.location.LocationClientOption.LocationMode.Hight_Accuracy);
		option.setPriority(LocationClientOption.NetWorkFirst);
		option.disableCache(true);
		option.setOpenGps(true);
		option.setAddrType("all");
		option.setCoorType("bd09ll");// ���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02
		option.disableCache(true);
		option.setScanSpan(5000);// ���÷���λ����ļ��ʱ��Ϊ5000ms
		mLocClient.setLocOption(option);
	}

	public void startClient(boolean isFist) {
		if (isFirstLoc == false) {
			isFirstLoc = true;
		} else {
			mLocClient.start();
		}

	}

	public void stopClient() {
		mLocClient.stop();

	}

	/**
	 * ��ȡ��ǰ����
	 * 
	 * @return
	 */
	public String getNowCity() {
		return nowCity;
	}

	/**
	 * ��ȡ��ǰ��ַ
	 * 
	 * @return
	 */
	public String getLocation() {
		return nowLocation;
	}

	/**
	 * ��λSDK��������
	 */
	public class MyLocationListenner implements BDLocationListener {

		private PoiOverlaySearch overlaySearch;

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null || mapView == null) {
				return;
			}
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			baiduMap.setMyLocationData(locData);
			nowCity = location.getCity();
			nowLocation = location.getLatitude()+"-"+location.getLongitude();
			LatLng ll = new LatLng(location.getLatitude(),
					location.getLongitude());
			Log.i("sssssssssssssss", location.getLatitude()+"--"+location.getLongitude());
			if (isFirstLoc) {

				MapStatus.Builder builder = new MapStatus.Builder();
				builder.target(ll).zoom(18.0f);
				baiduMap.animateMapStatus(MapStatusUpdateFactory
						.newMapStatus(builder.build()));
				isFirstLoc = false;
			}

			overlaySearch = new PoiOverlaySearch(context, baiduMap,mapView);
			overlaySearch.SearchNearBy(ll, "����վ", 5000);

		}

	}

}
