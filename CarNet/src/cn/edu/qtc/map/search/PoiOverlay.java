package cn.edu.qtc.map.search;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import cn.edu.qtc.main.R;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.search.poi.PoiResult;

/**
 * ������ʾpoi��overly
 */
public class PoiOverlay extends OverlayManager {

	private static final int MAX_POI_SIZE = 10;
	private Context context;
	private PoiResult mPoiResult = null;

	/**
	 * 构�?�函�?
	 * 
	 * @param baiduMap
	 *            �? PoiOverlay 引用�? BaiduMap 对象
	 */
	public PoiOverlay(BaiduMap baiduMap, Context context) {
		super(baiduMap);
		this.context = context;
	}

	/**
	 * 设置POI数据
	 * 
	 * @param poiResult
	 *            设置POI数据
	 */
	public void setData(PoiResult poiResult) {
		this.mPoiResult = poiResult;
	}

	@Override
	public final List<OverlayOptions> getOverlayOptions() {
		if (mPoiResult == null || mPoiResult.getAllPoi() == null) {
			return null;
		}
		List<OverlayOptions> markerList = new ArrayList<OverlayOptions>();
		int markerSize = 0;
		for (int i = 0; i < mPoiResult.getAllPoi().size()
				&& markerSize < MAX_POI_SIZE; i++) {
			if (mPoiResult.getAllPoi().get(i).location == null) {
				continue;
			}
			markerSize++;
			Bundle bundle = new Bundle();
			bundle.putInt("index", i);

			Resources res;
			markerList.add(new MarkerOptions()
					.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
							.decodeResource(context.getResources(),
									R.drawable.icon_route_nearby_gas_station)))
					.extraInfo(bundle)
					.position(mPoiResult.getAllPoi().get(i).location));

		}
		return markerList;
	}

	/**
	 * 获取�? PoiOverlay �? poi数据
	 * 
	 * @return
	 */
	public PoiResult getPoiResult() {
		return mPoiResult;
	}

	/**
	 * 覆写此方法以改变默认点击行为
	 * 
	 * @param i
	 *            被点击的poi�?
	 *            {@link com.baidu.mapapi.search.poi.PoiResult#getAllPoi()}
	 *            中的索引
	 * @return
	 */
	public boolean onPoiClick(int i) {
		// if (mPoiResult.getAllPoi() != null
		// && mPoiResult.getAllPoi().get(i) != null) {
		// Toast.makeText(BMapManager.getInstance().getContext(),
		// mPoiResult.getAllPoi().get(i).name, Toast.LENGTH_LONG)
		// .show();
		// }
		return false;
	}

	@Override
	public final boolean onMarkerClick(Marker marker) {
		if (!mOverlayList.contains(marker)) {
			return false;
		}
		if (marker.getExtraInfo() != null) {
			return onPoiClick(marker.getExtraInfo().getInt("index"));
		}
		return false;
	}

	@Override
	public boolean onPolylineClick(Polyline polyline) {
		// TODO Auto-generated method stub
		return false;
	}
}
