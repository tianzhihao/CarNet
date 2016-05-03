package cn.edu.qtc.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import cn.edu.qtc.main.R;
import cn.edu.qtc.map.route.FuzzySearch;

/**
 * 路线地点模糊查询界面
 * 
 * @author linlin
 * 
 */
public class SearchActivity extends Activity implements OnClickListener {

	private EditText search_edit;
	private TextView location_tv;
	private ListView itemList;
	private ArrayAdapter<String> adapter;
	private List<String> itemGroup;
	// 找寻的类型
	private int START = 1;
	private int END = 2;
	private String type;
	// 模糊查询
	private FuzzySearch fuzzySearch;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				// 获取数据完成，添加适配器
				itemGroup = fuzzySearch.getData();
				adapter = new ArrayAdapter<String>(SearchActivity.this,
						android.R.layout.simple_list_item_1, itemGroup);
				itemList.setAdapter(adapter);
				break;

			default:
				break;
			}
		};
	};

	/**
	 * 重写返回键
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent i = new Intent();
			i.putExtra("value", "");
			if (type.equals(START + "")) {
				setResult(START, i);
			} else {
				setResult(END, i);
			}
			SearchActivity.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		init();
		listener();

	}

	/**
	 * 输入框，列表监听
	 */
	private void listener() {

		itemList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int location, long arg3) {
				if (itemGroup != null) {
					Intent i = new Intent();
					i.putExtra("value", itemGroup.get(location));

					if (type.equals(START + "")) {
						setResult(START, i);
					} else {
						setResult(END, i);
					}

					SearchActivity.this.finish();
				}

			}
		});

	}

	private void init() {
		search_edit = (EditText) findViewById(R.id.search_topbar_edittext);
		location_tv = (TextView) findViewById(R.id.search_topbar_location_tv);
		itemList = (ListView) findViewById(R.id.search_listview);
		search_edit.addTextChangedListener(new MyEditTextListener());
		getData();

	}

	/**
	 * 获取主界面传来的数据
	 */
	private void getData() {
		Intent intent = getIntent();
		String city = intent.getStringExtra("city");
		type = intent.getStringExtra("type");
		location_tv.setText(city);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.search_topbar_back:
			Intent i = new Intent();
			i.putExtra("value", "");
			if (type.equals(START + "")) {
				setResult(START, i);
			} else {
				setResult(END, i);
			}
			SearchActivity.this.finish();
			break;
		case R.id.search_topbar_location_ll:

			break;

		default:
			break;
		}

	}

	/**
	 * 监听输入框的变化
	 * 
	 * @author linlin
	 * 
	 */
	private class MyEditTextListener implements TextWatcher {

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence text, int arg1, int arg2,
				int arg3) {
			String value = search_edit.getText().toString();
			if (value.equals("")) {

			} else {
				fuzzySearch = new FuzzySearch(SearchActivity.this, handler);
				fuzzySearch.SearchKey(value, location_tv.getText().toString());

			}

			// 添加适配器

		}

	}

}
