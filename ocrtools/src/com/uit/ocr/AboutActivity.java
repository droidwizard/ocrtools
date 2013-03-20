package com.uit.ocr;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class AboutActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_about_activity);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
}
