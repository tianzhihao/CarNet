package cn.edu.qtc.vo;

import java.util.List;
import java.util.Map;

public class GasStationInfo {
	// ����վ����
	public static String id;
	// ����վ����
	public static String name;
	// �����ʱ�
	public static String area;
	// ��������
	public static String areaname;
	// ����վ��ַ
	public static String address;
	// ��Ӫ������
	public static String brandname;
	// ����վ����
	public static String type;
	// �Ƿ���ۼ���վ
	public static String discount;
	// β���ŷű�׼
	public static String exhaust;
	// �ȸ��ͼ����-------û�н�����Ȣ����ֵ
	public static String position;
	// ʡ�ػ�׼�ͼ� ------û�н�����ȡ����ֵ
	public static List<Map<String, String>> price;
	// ����վ�ͼۣ������ο���ʵ���Լ���վ������Ϊ׼��
	public static List<Map<String, String>> gastprice;
	// ���Ϳ���Ϣ
	public static String fwlsmc;
	// ������ľ��룬��λM��
	public static String distance;
	// �ٶȵ�ͼγ��
	public static Double lat;
	// �ٶȵ�ͼ����
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
