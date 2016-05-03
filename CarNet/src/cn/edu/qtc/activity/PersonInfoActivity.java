package cn.edu.qtc.activity;

import cn.edu.qtc.main.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
/**
 * 个人资料
 * @author linlin
 *
 */
public class PersonInfoActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personinfo);
	}

	
}
