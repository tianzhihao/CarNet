package cn.edu.qtc.main;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.apache.http.Header;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;
import cn.edu.qtc.activity.SearchActivity;
import cn.edu.qtc.car.dbhelper.CarDBDao;
import cn.edu.qtc.car.jsonparser.CarJsonParser;
import cn.edu.qtc.car.jsonparser.CarJsonParser.JsonParserCompleteCallBack;
import cn.edu.qtc.config.Configs;
import cn.edu.qtc.map.location.Location;
import cn.edu.qtc.map.route.RoutePlan;
import cn.edu.qtc.net.HttpUntils;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.TextureMapView;
import com.dtr.zxing.activity.CaptureActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.server.car.domain.Car;

public class MainActivity extends SlidingFragmentActivity implements
		OnClickListener,JsonParserCompleteCallBack {

	// ********地图相关********
	// 地图基本组件
	private BaiduMap baiduMap;
	private TextureMapView mapView;

	// 地图上的按钮
	private ImageView locatonImg;
	// 定位类
	private Location location;
	// 路线规划
	private LinearLayout routePlan_start_ll;
	private LinearLayout routePlan_end_ll;
	private TextView routePlan_start_tv;
	private TextView routePlan_end_tv;
	private int START = 1;
	private int END = 2;

	// 驾车规划
	private RoutePlan plan;
	private String DEFAULT_START = "当前位置";
	private String DEFAULT_END = "你要去哪儿";

	private String nowCity;
	// *******侧滑相关**********
	private SlidingMenu slidingMenu;

	private long exitTime = 0;
	// ******二维码扫描*******
	public static int QR_REQUEST_CODE = 1011;//二维码扫描请求码
	private CarJsonParser mCarJsonParser;//汽车json解析类
	private CarDBDao mCarDBDao;//汽车数据库获取类

	/**
	 * 重写返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 初始化地图
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_main);
		// 加载地图
		initMapView();
		// 加载侧滑
		initSlidingMeun();
		//初始化二维码
		initQRScan();
		Configs mConfigs = new Configs(MainActivity.this);
	}
	/*
	 * 二维码扫描初始化
	 */
	private void initQRScan() {
		// TODO Auto-generated method stub
        mCarJsonParser = new CarJsonParser();
        mCarJsonParser.setCallBack(MainActivity.this);
        mCarDBDao  = new CarDBDao(MainActivity.this);
	}

	/**
	 * 初始化侧滑
	 */
	private void initSlidingMeun() {
		// 左滑
		setBehindContentView(R.layout.menu_frame_left);
		FragmentTransaction leftFragementTransaction = getSupportFragmentManager()
				.beginTransaction();
		Fragment fragmenLeft = new LeftFragment();
		leftFragementTransaction.replace(R.id.menu_frame, fragmenLeft);
		leftFragementTransaction.commit();
		// customize the SlidingMenu
		slidingMenu = getSlidingMenu();
		// 设置是左滑还是右滑，
		slidingMenu.setMode(SlidingMenu.LEFT);
		// 设置菜单宽度
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置淡入淡出的比例
		slidingMenu.setFadeDegree(0.3f);
		// 设置手势模式
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		// 设置左菜单阴影图片
		slidingMenu.setShadowDrawable(R.drawable.shadow);//
		// 设置滑动时菜单的是否淡入淡出
		slidingMenu.setFadeEnabled(true);
		// 设置滑动时拖拽效果
		slidingMenu.setBehindScrollScale(0.65f);

	}

	/**
	 * 初始化地图
	 */
	private void initMapView() {
		// ----------------------------------基础地图-------------------------------------
		// 初始化基础地图
		locatonImg=(ImageView)findViewById(R.id.main_location_img);
		mapView = (TextureMapView) findViewById(R.id.mTexturemap);
		baiduMap = mapView.getMap();
		// 隐藏logo
		View child = mapView.getChildAt(1);
		if (child != null
				&& (child instanceof ImageView || child instanceof ZoomControls)) {
			child.setVisibility(View.INVISIBLE);
		}
		// 地图上比例尺
		mapView.showScaleControl(false);
		// 设置缩放控件的位置
		baiduMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {

			@Override
			public void onMapLoaded() {
				int[] loc=new int[2];
				locatonImg.getLocationOnScreen(loc);
				Toast.makeText(getApplicationContext(), loc[0]+"--"+loc[1], 0).show();
				mapView.setZoomControlsPosition(new Point(loc[0]+15, loc[1]-530));
			}
		});
		// 设置普通图
		baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		// 设置交通图
		baiduMap.setTrafficEnabled(true);

		// ----------------------------------基础地图-----------------------------------

		// ------------------------------------定位-------------------------------
		// 初始化定位
		location = new Location(mapView, MainActivity.this);
		// 开启定位
		location.startClient(true);

		// ------------------------------------定位-------------------------------
		//
		//
		// ----------------------------------路线规划-------------------------------
		// 获取路线规划的空控件
		routePlan_end_tv = (TextView) findViewById(R.id.mian_route_end_tv);
		routePlan_start_tv = (TextView) findViewById(R.id.mian_route_start_tv);

		// ----------------------------------路线规划--------------------------------

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 左侧滑按钮
		case R.id.title_bar_menu_btn_left:
			// 利用sharedpreferences 存储账号 判断是否登录
			// 是：侧滑
			// 否：Dialog 提示登录 提示登录
			boolean islogin = Configs.getBooleanConfig(Configs.ISlOGIN);
			if(!islogin){
				onLogin();
			}else{
				slidingMenu.showMenu();//显示侧滑
			}
			break;

		// 定位按钮
		case R.id.main_location_img:
			location.startClient(false);
			break;
		// 路线规划开始LinearLayout按钮
		case R.id.main_routeplan_start_ll:
			// 跳转到查询界面
			enterSearchActivity(START);
			break;
		// 路线规划结束LinearLayout按钮
		case R.id.main_routeplan_end_ll:
			// 跳转到查询界面
			enterSearchActivity(END);
			break;
		// 交换起点终点信息
		case R.id.main_route_change:
			changeStrEnd();
			break;
		// 规划路线
		case R.id.main_route_plan:
			routePlan();
			break;
		default:
			break;
		}
	}

	/**
	 * 进入搜索界面
	 * 
	 * @param type
	 *            判断起点还是重点
	 */
	public void enterSearchActivity(int type) {
		Intent intent = new Intent(MainActivity.this, SearchActivity.class);
		if (location.nowCity != null) {
			intent.putExtra("city", location.nowCity);
			intent.putExtra("type", type + "");
		} else {
			intent.putExtra("city", "定位失败");
			intent.putExtra("type", type + "");
		}
		startActivityForResult(intent, type);

	}

	/**
	 * 是否具备开始路线规划的条件
	 * 
	 * @return
	 */
	public boolean isCanPlan(String nowCity, String stValue, String enValue) {
		// 当前城市不能为空
		// 起始或者终点不能为提示信息

		return location.nowCity != null && stValue != null && enValue != null
				&& !stValue.equals(DEFAULT_END) && !enValue.equals(DEFAULT_END);

	}

	/**
	 * 交换起始的地址信息
	 */
	public void changeStrEnd() {
		if (routePlan_start_tv.getText().toString().equals(DEFAULT_START)
				&& routePlan_end_tv.getText().toString().equals(DEFAULT_END)) {
			// 不交换信息

		} else {
			// 交换信息
			String endInfo = routePlan_end_tv.getText().toString();
			String startInfo = routePlan_start_tv.getText().toString();

			routePlan_start_tv.setText(endInfo);
			routePlan_end_tv.setText(startInfo);
		}

	}

	/**
	 * 路线规划
	 */
	public void routePlan() {
		String city = location.getNowCity();
		String locationStr = location.getLocation();
		String stValue = routePlan_start_tv.getText().toString();
		String enValue = routePlan_end_tv.getText().toString();
		if (isCanPlan(city, stValue, enValue)) {
			if (stValue.equals(DEFAULT_START)) {
				plan = new RoutePlan(MainActivity.this, baiduMap);
				plan.drivePlan(city, locationStr, city, enValue);
			} else if (enValue.equals(DEFAULT_START)) {
				plan = new RoutePlan(MainActivity.this, baiduMap);
				plan.drivePlan(city, stValue, city, locationStr);
			} else {
				plan = new RoutePlan(MainActivity.this, baiduMap);
				plan.drivePlan(city, stValue, city, enValue);
			}

		} else {
			// Toast.makeText(MainActivity.this, "路线规划失败，请检查条件！", 0).show();

		}
	}
	/*
	 * 二维码扫描OnClick()
	 */
    public void onQRScan(View view){
   	 Intent mIntent = new Intent(MainActivity.this,CaptureActivity.class);
   	 startActivityForResult(mIntent,QR_REQUEST_CODE);
   }
	/*
	 * 登陆按钮OnClick()
	 */
	public void onLogin(){
	   	final View mView  = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_login,null);
    	AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(MainActivity.this);//必须用Activity为上下文，不能以全局，Activity能添加窗口
    	mDialogBuilder
    	.setView(mView)
    	.setPositiveButton("我要注册",new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
//				Intent mIntent = new Intent(MainActivity.this,RegisteActivity.class);
//				startActivity(mIntent);
			}
		}).setNegativeButton("登录", new android.content.DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String mpnumber = ((EditText)mView.findViewById(R.id.mpnumber)).getText().toString();
				String passwprd = ((EditText)mView.findViewById(R.id.password)).getText().toString();
				Toast.makeText(MainActivity.this,mpnumber+passwprd, Toast.LENGTH_SHORT).show();
				RequestParams mParams = new RequestParams();
				mParams.put("mpnumber",mpnumber);
				mParams.put("password",passwprd);
				HttpUntils.postWithParams(Configs.URL_LOGIN, mParams, new AsyncHttpResponseHandler() {				
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						String uid = new String(arg2);
						Toast.makeText(MainActivity.this,uid,Toast.LENGTH_SHORT).show();
					}				
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub
					}
				});
			}
		});
    	mDialogBuilder.create().show();
	    }
	/**
	 * Activity 返回监听
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == START) {
			if (!data.getStringExtra("value").equals("")) {
				routePlan_start_tv.setText(data.getStringExtra("value"));

			}

		}
		else if (requestCode == END) {
			if (!data.getStringExtra("value").equals("")) {
				routePlan_end_tv.setText(data.getStringExtra("value"));
			}
		}
		//二维码扫描结果返回
		else if(requestCode==QR_REQUEST_CODE&&resultCode==RESULT_OK){
	    		Bundle extras = data.getExtras();
	    		String result = extras.getString("result");
	    		//Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
	    		mCarJsonParser.parseCarJson(result);
		}
	}

	@Override
	protected void onPause() {
		Log.d("MainActivity", "onPause");

		mapView.onPause();
		location.stopClient();
		super.onPause();
	}

	@Override
	protected void onResume() {
		Log.d("MainActivity", "onResume");
		mapView.onResume();
		location.startClient(false);
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mapView.onDestroy();
		super.onDestroy();
	}
	//json解析接口回调
	@Override
	public void onCompleted(Car mCar) {
		// TODO Auto-generated method stub
		final Car mCar2 = mCar;
		Toast.makeText(MainActivity.this, mCar2.toString(), Toast.LENGTH_SHORT).show();
		mCarDBDao.insertRow(mCar);
		new Thread(){
			public void run() {
				try {
					URL mUrl = new URL(Configs.URL_CARPOST);
					HttpURLConnection mConnection = (HttpURLConnection) mUrl.openConnection();
					mConnection.setConnectTimeout(3000);//设置连接超时
					mConnection.setRequestMethod("POST");
					mConnection.setDoInput(true);//是否允许输出，默认为true
					mConnection.setDoOutput(true);//是否允许输入，默认为false
					mConnection.setUseCaches(false);//url是否缓存
					mConnection.setRequestProperty("Content-Type","application/x-java-serialized-object");
					ObjectOutputStream mStream = new ObjectOutputStream(mConnection.getOutputStream());
					mStream.writeObject(mCar2);
					mStream.flush();// 刷新对象输出流，将任何字节都写入潜在的流中（些处为ObjectOutputStream）   
					mStream.close();// 关闭流对象。此时，不能再向对象输出流写入任何数据，先前写入的数据存在于内存缓冲区中,   
					// 在调用下边的getInputStream()函数时才把准备好的http请求正式发送到服务器  !!!!!
					mConnection.getInputStream();//必须调用
					mConnection.disconnect();
//					int ResultCode = mConnection.getResponseCode();
//					if(HttpURLConnection.HTTP_OK==ResultCode){
//						System.out.println("HTTP_OK");
//					}else{
//						System.out.println("ResponceCode="+ResultCode);
//					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
	}

}
