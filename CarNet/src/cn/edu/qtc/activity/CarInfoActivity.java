package cn.edu.qtc.activity;

import cn.edu.qtc.car.dbhelper.CarDBDao;
import cn.edu.qtc.main.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;
/**
 * ≥µ¡æ–≈œ¢
 * @author linlin
 *
 */
public class CarInfoActivity extends Activity{
	private CarDBDao mCarDBDao;
	private ListView mListView;
	private SimpleAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_carinfo);
		mCarDBDao = new CarDBDao(CarInfoActivity.this);
		mListView = new ListView(CarInfoActivity.this);
		//mAdapter = new SimpleAdapter(context, data, resource, from, to)
	}
}
