package cn.edu.qtc.car.dbhelper;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CarDbHelper extends SQLiteOpenHelper {
	private static String DB_NAME = "mycar.db";
	private static int DB_VERSION = 1;

	public CarDbHelper(Context context) {
		super(context,DB_NAME,null,DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE car_table(cid integer not null primary key,uid integer not null,brand varchar(45) not null,version varchar(45) not null,carnb varchar(45) not null,enginenb varchar(45) not null,level varchar(45) not null,distance integer not null,oil integer not null,engine integer not null,speed integer not null,light integer not null)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}
