package cn.edu.qtc.vo;

import java.util.List;
import java.util.Map;

public class GasStationInfo {
	// 加油站名称
	public static String id;
	// 加油站名称
	public static String name;
	// 城市邮编
	public static String area;
	// 城市区域
	public static String areaname;
	// 加油站地址
	public static String address;
	// 运营商类型
	public static String brandname;
	// 加油站类型
	public static String type;
	// 是否打折加油站
	public static String discount;
	// 尾气排放标准
	public static String exhaust;
	// 谷歌地图坐标-------没有解析，娶不到值
	public static String position;
	// 省控基准油价 ------没有解析，取不到值
	public static List<Map<String, String>> price;
	// 加油站油价（仅供参考，实际以加油站公布价为准）
	public static List<Map<String, String>> gastprice;
	// 加油卡信息
	public static String fwlsmc;
	// 与坐标的距离，单位M　
	public static String distance;
	// 百度地图纬度
	public static Double lat;
	// 百度地图经度
	public static Double lon;

	public static void setId(String id) {
		GasStationInfo.id = id;
	}

	public static void setName(String name) {
		GasStationInfo.name = name;
	}

	public static void setArea(String area) {
		GasStationInfo.area = area;
	}

	public static void setAreaname(String areaname) {
		GasStationInfo.areaname = areaname;
	}

	public static void setAddress(String address) {
		GasStationInfo.address = address;
	}

	public static void setBrandname(String brandname) {
		GasStationInfo.brandname = brandname;
	}

	public static void setType(String type) {
		GasStationInfo.type = type;
	}

	public static void setDiscount(String discount) {
		GasStationInfo.discount = discount;
	}

	public static void setExhaust(String exhaust) {
		GasStationInfo.exhaust = exhaust;
	}

	public static void setPosition(String position) {
		GasStationInfo.position = position;
	}

	public static void setPrice(List<Map<String, String>> price) {
		GasStationInfo.price = price;
	}

	public static void setGastprice(List<Map<String, String>> gastprice) {
		GasStationInfo.gastprice = gastprice;
	}

	public static void setFwlsmc(String fwlsmc) {
		GasStationInfo.fwlsmc = fwlsmc;
	}

	public static void setDistance(String distance) {
		GasStationInfo.distance = distance;
	}

	public static void setLat(Double lat) {
		GasStationInfo.lat = lat;
	}

	public static void setLon(Double lon) {
		GasStationInfo.lon = lon;
	}

}
