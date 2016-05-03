package cn.edu.qtc.main;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;
import cn.edu.qtc.activity.SearchActivity;
import cn.edu.qtc.map.location.Location;
import cn.edu.qtc.map.route.RoutePlan;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.TextureMapView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity implements
		OnClickListener {

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
				mapView.setZoomControlsPosition(new Point(925, 800));
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
			slidingMenu.showMenu();
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

}
