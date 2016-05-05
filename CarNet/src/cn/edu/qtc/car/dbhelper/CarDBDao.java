package cn.edu.qtc.car.dbhelper;

import java.util.ArrayList;
import java.util.List;

import com.server.car.domain.Car;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CarDBDao {
	private CarDbHelper mDbHelper;
	public CarDBDao(Context mContext) {
		mDbHelper = new CarDbHelper(mContext);
	}
	/*将一个车对象保存到数据库
	 * @Params mCar保存到数据库的车对象
	 */
	public boolean insertRow(Car mCar){
		SQLiteDatabase mLiteDatabase = mDbHelper.getWritableDatabase();
		ContentValues mValues = new ContentValues();
		mValues.put("uid",1);
		mValues.put("brand", mCar.getBrand());
		mValues.put("version",mCar.getVersion());
		mValues.put("carnb",mCar.getCarnb());
		mValues.put("enginenb",mCar.getEnginenb());
		mValues.put("level",mCar.getLevel());
		mValues.put("distance",mCar.getDistance());
		mValues.put("oil",mCar.getOil());
		mValues.put("engine",mCar.getEngine());
		mValues.put("speed",mCar.getSpeed());
		mValues.put("light",mCar.getLight());
		long mLong = mLiteDatabase.insert("car_table",null,mValues);
		Log.d("db","return"+mLong);
		mLiteDatabase.close();
		if(mLong!=-1)
			return true;
		else
			return false;
	}
	public List<Car> getCarsFromDb(){
		List<Car> mCars = new ArrayList<Car>();
		SQLiteDatabase mLiteDatabase = mDbHelper.getReadableDatabase();
		Cursor mCursor = mLiteDatabase.query("car_table",null,null,null,null,null,null);
		mCursor.moveToFirst();
//		{"brand":"bmb","version":"x5","carnb":"椴丅6Y110D","enginenb":"45234y7",
//		"level":"5","distance":"5512","oil":"61","engine":"0","speed":"1",
//		"light":"0"}
		do {
			Car mCar = new Car();
			mCar.setBrand(mCursor.getString(mCursor.getColumnIndex("brand")));
			mCar.setVersion(mCursor.getString(mCursor.getColumnIndex("version")));
			mCar.setCarnb(mCursor.getString(mCursor.getColumnIndex("carnb")));
			mCar.setEnginenb(mCursor.getString(mCursor.getColumnIndex("enginenb")));
			mCar.setLevel(mCursor.getString(mCursor.getColumnIndex("level")));
			mCar.setDistance(mCursor.getInt(mCursor.getColumnIndex("distance")));
			mCar.setOil(mCursor.getInt(mCursor.getColumnIndex("oil")));
			mCar.setEngine(mCursor.getInt(mCursor.getColumnIndex("engine")));
			mCar.setSpeed(mCursor.getInt(mCursor.getColumnIndex("speed")));
			mCar.setLight(mCursor.getInt(mCursor.getColumnIndex("light")));
			mCars.add(mCar);
		} while (mCursor.moveToNext());
		return mCars;
	}
}
