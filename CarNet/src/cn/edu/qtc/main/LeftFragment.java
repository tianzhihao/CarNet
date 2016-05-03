package cn.edu.qtc.main;

import cn.edu.qtc.activity.CarInfoActivity;
import cn.edu.qtc.activity.PersonInfoActivity;
import cn.edu.qtc.activity.SettingActivity;
import cn.edu.qtc.trafficserach.TrafficSearchActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * @description 左侧滑Frament
 */
public class LeftFragment extends Fragment implements OnClickListener {
	private ImageView personaInfo;
	private LinearLayout order;
	private LinearLayout carInfo;
	private LinearLayout trafficSearch;
	private LinearLayout setting;
	private LinearLayout exit;
	private View view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_left, null);
		init();
		getData();
		setData();

		return view;
	}

	/**
	 * 设置数据
	 */
	private void setData() {
		// TODO Auto-generated method stub

	}

	/**
	 * 获取 sharedpreferences 填充信息
	 */
	private void getData() {
		// TODO Auto-generated method stub

	}

	private void init() {
		personaInfo = (ImageView) view.findViewById(R.id.left_img_personinfo);
		order = (LinearLayout) view.findViewById(R.id.left_ll_order);
		carInfo = (LinearLayout) view.findViewById(R.id.left_ll_carinfo);
		trafficSearch = (LinearLayout) view
				.findViewById(R.id.left_ll_traffic_search);
		setting = (LinearLayout) view.findViewById(R.id.left_ll_setting);
		exit = (LinearLayout) view.findViewById(R.id.left_ll_exit);

		personaInfo.setOnClickListener(this);
		order.setOnClickListener(this);
		carInfo.setOnClickListener(this);
		trafficSearch.setOnClickListener(this);
		setting.setOnClickListener(this);
		exit.setOnClickListener(this);

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 个人信息
		case R.id.left_img_personinfo:
			Toast.makeText(getActivity(), " 个人信息", 0).show();
			Intent personIntent = new Intent(getActivity(),
					PersonInfoActivity.class);
			startActivity(personIntent);

			break;
		// 车辆信息
		case R.id.left_ll_carinfo:
			Toast.makeText(getActivity(), "车辆信息", 0).show();
			Intent carinfoIntent = new Intent(getActivity(),
					CarInfoActivity.class);
			startActivity(carinfoIntent);
			break;
		// 加油订单
		case R.id.left_ll_order:
			Toast.makeText(getActivity(), " 加油订单", 0).show();
			Intent orderIntent = new Intent(getActivity(),
					CarInfoActivity.class);
			startActivity(orderIntent);
			break;
		// 交通查询
		case R.id.left_ll_traffic_search:
			Toast.makeText(getActivity(), " 交通查询", 0).show();
			Intent traffic_searchIntent = new Intent(getActivity(),
					TrafficSearchActivity.class);
			startActivity(traffic_searchIntent);
			break;
		// 设置
		case R.id.left_ll_setting:
			Toast.makeText(getActivity(), " 设置", 0).show();
			Intent settingIntent = new Intent(getActivity(),
					SettingActivity.class);
			startActivity(settingIntent);
			break;
		// 退出程序
		case R.id.left_ll_exit:
			System.exit(0);
			break;
		default:
			break;
		}
	}

}
