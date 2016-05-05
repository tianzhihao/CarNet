package cn.edu.qtc.car.jsonparser;

import org.json.JSONException;
import org.json.JSONObject;

import com.server.car.domain.Car;

public class CarJsonParser {
	private JsonParserCompleteCallBack mCallBack;
	public CarJsonParser() {
		// TODO Auto-generated constructor stub
	}
	public void setCallBack(JsonParserCompleteCallBack mBack){
		this.mCallBack = mBack;
	}
	public void parseCarJson(String mString){
		try {
//			{"brand":"bmb","version":"x5","carnb":"鲁B6Y110D","enginenb":"45234y7",
//				"level":"5","distance":5512,"oil":61,"engine":0,"speed":1,
//				"light":0}
			Car mCar = new Car();
			JSONObject mJsonObject = new JSONObject(mString);
			mCar.setBrand(mJsonObject.getString("brand"));
			mCar.setVersion(mJsonObject.getString("version"));
			mCar.setCarnb(mJsonObject.getString("carnb"));
			mCar.setEnginenb(mJsonObject.getString("enginenb"));
			mCar.setLevel(mJsonObject.getString("level"));
			mCar.setDistance(mJsonObject.getInt("distance"));
			mCar.setOil(mJsonObject.getInt("oil"));
			mCar.setEngine(mJsonObject.getInt("engine"));
			mCar.setSpeed(mJsonObject.getInt("speed"));
			mCar.setLight(mJsonObject.getInt("light"));
			mCallBack.onCompleted(mCar);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	   public interface JsonParserCompleteCallBack{
	    	void onCompleted(Car mCar);
	    }
}
