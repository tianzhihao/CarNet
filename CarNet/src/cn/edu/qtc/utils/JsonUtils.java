package cn.edu.qtc.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Json 数据 格式转换
 * 
 * @author linlin
 * 
 */
public class JsonUtils {
	// {
	// "resultcode": "200",
	// "reason": "Successed!",
	// "result": {
	// "data": [
	// {
	// "id": "34299",
	// "name": "中油燕宾北邮加油站‎（办卡优惠）",
	// "area": "chongwen",
	// "areaname": "北京市 崇文区",
	// "address": "北京市崇文区天坛路12号，与东市场东街路交叉西南角（天坛北门往西一公里路南）。",
	// "brandname": "中石油",
	// "type": "加盟店",
	// "discount": "打折加油站",
	// "exhaust": "京Ⅴ",
	// "position": "116.401654,39.886973",
	// "lon": "116.40804671453",
	// "lat": "39.893324983272",
	// "price": [
	// {
	// "type": "E90",
	// "price": "7.31"
	// },
	// {
	// "type": "E93",
	// "price": "6.92"
	// },
	// {
	// "type": "E97",
	// "price": "7.36"
	// },
	// {
	// "type": "E0",
	// "price": "6.84"
	// }
	// ],
	// "gastprice": [
	// {
	// "name": "92#",
	// "price": "6.77"
	// },
	// {
	// "name": "95#",
	// "price": "7.36"
	// }
	// ],
	// "fwlsmc": "银联卡,信用卡支付",
	// "distance": 2564
	// }
	/***
	 * 把Josn 数据封装成 GasStationInfo
	 * 
	 * @param jsonStr
	 * @throws JSONException
	 */
	public static cn.edu.qtc.vo.GasStationInfo GasStationInfo(String jsonStr,Context context) {
		cn.edu.qtc.vo.GasStationInfo stationInfo = new cn.edu.qtc.vo.GasStationInfo();
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			// 一级目录
			JSONObject result = jsonObject.getJSONObject("result");
			// 二级目录 result（可能会有多个Item）
			JSONArray data = result.getJSONArray("data");
			// 三级目录 data
			//
			if (data.length() >= 1) {
				JSONObject firstDataGroup = new JSONObject(data.get(0)
						.toString());
				// 其他信息
				stationInfo.setId(firstDataGroup.getString("id"));
				stationInfo.setName(firstDataGroup.getString("name"));
				stationInfo.setAddress(firstDataGroup.getString("address"));
				stationInfo.setBrandname(firstDataGroup.getString("brandname"));
				stationInfo.setType(firstDataGroup.getString("type"));
				stationInfo.setDiscount(firstDataGroup.getString("discount"));
				stationInfo.setExhaust(firstDataGroup.getString("exhaust"));
				stationInfo.setFwlsmc(firstDataGroup.getString("fwlsmc"));
				stationInfo.setDistance(firstDataGroup.getString("distance"));
				stationInfo.setDistance(firstDataGroup.getString("distance"));
				stationInfo.setLat(Double.valueOf(firstDataGroup
						.getString("lat")));
				stationInfo.setLon(Double.valueOf(firstDataGroup
						.getString("lon")));

				// 油价 gastprice {"93#":"5.7","0#车柴":"5.29"}
				// {"name":"93#","price":"5.7"},{"name":"0#车柴","price":"5.29"}
				Log.i("ssssssssssssssss", firstDataGroup.toString());
				JSONArray gastprice = firstDataGroup.getJSONArray("gastprice");
			//	 Log.i("ssssssssssssssss", gastprice.toString());
				List<Map<String, String>> list = new ArrayList<Map<String, String>>();
				for (int i = 0; i < gastprice.length(); ++i) {
					Map<String, String> map = new HashMap<String, String>();
					JSONObject object = gastprice.getJSONObject(i);
					map.put("name", object.getString("name"));
					map.put("price", object.getString("price"));
					list.add(map);
				}
				stationInfo.gastprice = list;
				//
				// Toast.makeText(context, stationInfo.address + "",
				// Toast.LENGTH_LONG).show();

				return stationInfo;

			} else if (data.length() > 3) {
				// 搜索到了多个加油站，进行筛选

			} else {
				// 尽量保证取到的点只有一个加油站 如果取到多个不显示

				Toast.makeText(context, "取到了多个加油站信息！", Toast.LENGTH_LONG)
						.show();
				return null;
			}

		} catch (JSONException e) {
			Toast.makeText(context, "该加油站无信息！", Toast.LENGTH_LONG).show();
			e.printStackTrace();
			return null;
		}
		return null;
	}
}
