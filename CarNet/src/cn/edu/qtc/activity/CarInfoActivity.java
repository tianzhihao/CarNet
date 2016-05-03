package cn.edu.qtc.activity;

import cn.edu.qtc.main.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
/**
 * ≥µ¡æ–≈œ¢
 * @author linlin
 *
 */
public class CarInfoActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_carinfo);
	}

	
}
