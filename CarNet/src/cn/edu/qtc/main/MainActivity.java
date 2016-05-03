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

	// ********��ͼ���********
	// ��ͼ�������
	private BaiduMap baiduMap;
	private TextureMapView mapView;

	// ��ͼ�ϵİ�ť
	private ImageView locatonImg;
	// ��λ��
	private Location location;
	// ·�߹滮
	private LinearLayout routePlan_start_ll;
	private LinearLayout routePlan_end_ll;
	private TextView routePlan_start_tv;
	private TextView routePlan_end_tv;
	private int START = 1;
	private int END = 2;

	// �ݳ��滮
	private RoutePlan plan;
	private String DEFAULT_START = "��ǰλ��";
	private String DEFAULT_END = "��Ҫȥ�Ķ�";

	private String nowCity;
	// *******�໬���**********
	private SlidingMenu slidingMenu;

	private long exitTime = 0;

	/**
	 * ��д���ؼ�
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����",
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
		// ��ʼ����ͼ
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_main);
		// ���ص�ͼ
		initMapView();
		// ���ز໬
		initSlidingMeun();
	}

	/**
	 * ��ʼ���໬
	 */
	private void initSlidingMeun() {
		// ��
		setBehindContentView(R.layout.menu_frame_left);
		FragmentTransaction leftFragementTransaction = getSupportFragmentManager()
				.beginTransaction();
		Fragment fragmenLeft = new LeftFragment();
		leftFragementTransaction.replace(R.id.menu_frame, fragmenLeft);
		leftFragementTransaction.commit();
		// customize the SlidingMenu
		slidingMenu = getSlidingMenu();
		// �������󻬻����һ���
		slidingMenu.setMode(SlidingMenu.LEFT);
		// ���ò˵����
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// ���õ��뵭���ı���
		slidingMenu.setFadeDegree(0.3f);
		// ��������ģʽ
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		// ������˵���ӰͼƬ
		slidingMenu.setShadowDrawable(R.drawable.shadow);//
		// ���û���ʱ�˵����Ƿ��뵭��
		slidingMenu.setFadeEnabled(true);
		// ���û���ʱ��קЧ��
		slidingMenu.setBehindScrollScale(0.65f);

	}

	/**
	 * ��ʼ����ͼ
	 */
	private void initMapView() {
		// ----------------------------------������ͼ-------------------------------------
		// ��ʼ��������ͼ
		mapView = (TextureMapView) findViewById(R.id.mTexturemap);
		baiduMap = mapView.getMap();
		// ����logo
		View child = mapView.getChildAt(1);
		if (child != null
				&& (child instanceof ImageView || child instanceof ZoomControls)) {
			child.setVisibility(View.INVISIBLE);
		}
		// ��ͼ�ϱ�����
		mapView.showScaleControl(false);
		// �������ſؼ���λ��
		baiduMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {

			@Override
			public void onMapLoaded() {
				mapView.setZoomControlsPosition(new Point(925, 800));
			}
		});
		// ������ͨͼ
		baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		// ���ý�ͨͼ
		baiduMap.setTrafficEnabled(true);

		// ----------------------------------������ͼ-----------------------------------

		// ------------------------------------��λ-------------------------------
		// ��ʼ����λ
		location = new Location(mapView, MainActivity.this);
		// ������λ
		location.startClient(true);

		// ------------------------------------��λ-------------------------------
		//
		//
		// ----------------------------------·�߹滮-------------------------------
		// ��ȡ·�߹滮�Ŀտؼ�
		routePlan_end_tv = (TextView) findViewById(R.id.mian_route_end_tv);
		routePlan_start_tv = (TextView) findViewById(R.id.mian_route_start_tv);

		// ----------------------------------·�߹滮--------------------------------

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ��໬��ť
		case R.id.title_bar_menu_btn_left:
			// ����sharedpreferences �洢�˺� �ж��Ƿ��¼
			// �ǣ��໬
			// ��Dialog ��ʾ��¼ ��ʾ��¼
			slidingMenu.showMenu();
			break;

		// ��λ��ť
		case R.id.main_location_img:
			location.startClient(false);
			break;
		// ·�߹滮��ʼLinearLayout��ť
		case R.id.main_routeplan_start_ll:
			// ��ת����ѯ����
			enterSearchActivity(START);
			break;
		// ·�߹滮����LinearLayout��ť
		case R.id.main_routeplan_end_ll:
			// ��ת����ѯ����
			enterSearchActivity(END);
			break;
		// ��������յ���Ϣ
		case R.id.main_route_change:
			changeStrEnd();
			break;
		// �滮·��
		case R.id.main_route_plan:
			routePlan();
			break;
		default:
			break;
		}
	}

	/**
	 * ������������
	 * 
	 * @param type
	 *            �ж���㻹���ص�
	 */
	public void enterSearchActivity(int type) {
		Intent intent = new Intent(MainActivity.this, SearchActivity.class);
		if (location.nowCity != null) {
			intent.putExtra("city", location.nowCity);
			intent.putExtra("type", type + "");
		} else {
			intent.putExtra("city", "��λʧ��");
			intent.putExtra("type", type + "");
		}
		startActivityForResult(intent, type);

	}

	/**
	 * �Ƿ�߱���ʼ·�߹滮������
	 * 
	 * @return
	 */
	public boolean isCanPlan(String nowCity, String stValue, String enValue) {
		// ��ǰ���в���Ϊ��
		// ��ʼ�����յ㲻��Ϊ��ʾ��Ϣ

		return location.nowCity != null && stValue != null && enValue != null
				&& !stValue.equals(DEFAULT_END) && !enValue.equals(DEFAULT_END);

	}

	/**
	 * ������ʼ�ĵ�ַ��Ϣ
	 */
	public void changeStrEnd() {
		if (routePlan_start_tv.getText().toString().equals(DEFAULT_START)
				&& routePlan_end_tv.getText().toString().equals(DEFAULT_END)) {
			// ��������Ϣ

		} else {
			// ������Ϣ
			String endInfo = routePlan_end_tv.getText().toString();
			String startInfo = routePlan_start_tv.getText().toString();

			routePlan_start_tv.setText(endInfo);
			routePlan_end_tv.setText(startInfo);
		}

	}

	/**
	 * ·�߹滮
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
			// Toast.makeText(MainActivity.this, "·�߹滮ʧ�ܣ�����������", 0).show();

		}
	}

	/**
	 * Activity ���ؼ���
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
