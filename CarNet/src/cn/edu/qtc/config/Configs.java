package cn.edu.qtc.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Configs {
	public static String ISlOGIN = "ISLOGIN";//�Ƿ��Ѿ���½
	public static String PREFERENCESNAME = "MYCONFIGS";
	public static String USERNAME = "USERNAME";
	public static String URL_LOGIN = "http://10.0.2.2:8081/CarServer/login.jsp";
	private static Context mContext;
	private static SharedPreferences mPreferences;
	private static Editor mEditor;
	public Configs(Context mCon){
		mContext = mCon;
		mPreferences = mContext.getSharedPreferences(PREFERENCESNAME,mContext.MODE_PRIVATE);
		mEditor = mPreferences.edit();
		mEditor.commit();
}
	/*
	 * @param key �ֶ��� value ֵ
	 */
	public static void saveConfig(String key,boolean value){
		mEditor.putBoolean(key, value);
		mEditor.commit();
	}
	public static void saveConfig(String key,String value){
		mEditor.putString(key, value);
		mEditor.commit();
		
	}
	public static void saveConfig(String key,int value){
		mEditor.putInt(key, value);
		mEditor.commit();
	}
	public static String getStringConfig(String key){//����ֵ��ͬ�������أ�
		return mPreferences.getString(key, "-1");
	}
	public static Boolean getBooleanConfig(String key){
		return mPreferences.getBoolean(key,false);
	}
	public static int getIntConfig(String key){
		return mPreferences.getInt(key,-1);
	}

}
