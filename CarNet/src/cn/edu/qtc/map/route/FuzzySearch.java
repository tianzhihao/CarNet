package cn.edu.qtc.map.route;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;

import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

/**
 * ����Ϊģ���������������
 * 
 * @author linlin
 * 
 */
public class FuzzySearch implements OnGetSuggestionResultListener {
	private SuggestionSearch mSuggestionSearch = null;
	private Context context;
	private Handler handler;
	private List<String> itemList;

	public FuzzySearch(Context context, Handler handler) {

		this.context = context;
		this.handler = handler;

		init();

	}

	/**
	 * ģ����ѯ
	 */
	public void SearchKey(String key, String city) {
		mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
				.keyword(key).city(city));

	}

	public List<String> getData() {
		return itemList;
	}

	/**
	 * �������������ʼ��
	 */
	public void init() {

		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(this);
		itemList = new ArrayList<String>();
		//�����ҵ�λ��
		itemList.add("��ǰλ��");
	}

	@Override
	public void onGetSuggestionResult(SuggestionResult result) {
		if (result == null || result.getAllSuggestions() == null) {
			return;
		}

		for (SuggestionResult.SuggestionInfo info : result.getAllSuggestions()) {
			if (info.key != null) {
				itemList.add(info.key);
			}

		}

		handler.sendEmptyMessage(0);

	}
}
