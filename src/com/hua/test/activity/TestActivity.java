package com.hua.test.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TextView;

import com.hua.test.widget.swipeback.SwipeBackActivity;

public class TestActivity extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		TextView textView = new TextView(getApplicationContext());
		textView.setText("1235466");
		textView.setTextSize(TypedValue.complexToDimension(20, getResources().getDisplayMetrics()));
		setContentView(textView);
		
	}
	
}
