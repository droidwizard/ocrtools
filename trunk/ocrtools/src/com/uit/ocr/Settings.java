package com.uit.ocr;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Settings extends Activity implements OnCheckedChangeListener {
	CheckBox cbSettings2ColorImage;
	private Context context = Settings.this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.settings);
		super.onCreate(savedInstanceState);
		cbSettings2ColorImage = (CheckBox) findViewById(R.id.cb_settings_2colorimage);
		setCheckBox();
		cbSettings2ColorImage.setOnCheckedChangeListener(this);
		
	}

	public void setCheckBox() {

		cbSettings2ColorImage.setChecked(MainActivity.is2ColorImage);

	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked == true) {
			MainActivity.is2ColorImage = true;
			cbSettings2ColorImage.setChecked(MainActivity.is2ColorImage);

			Toast.makeText(context, "đã thiết lập quét hình trắng đen", 2000)
					.show();
		} else {
			MainActivity.is2ColorImage = false;
			cbSettings2ColorImage.setChecked(MainActivity.is2ColorImage);

			Toast.makeText(context, "đã tắt thiết lập quét hình trắng đen",
					2000).show();
		}

	}

}
