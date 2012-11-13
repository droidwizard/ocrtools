package com.uit.ocr;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Settings extends Activity implements OnCheckedChangeListener {
	CheckBox cbSettings2ColorImage, cbSettingsCharfilter;
	private Context context = Settings.this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.settings);
		super.onCreate(savedInstanceState);
		cbSettings2ColorImage = (CheckBox) findViewById(R.id.cb_settings_2colorimage);
		cbSettingsCharfilter = (CheckBox) findViewById(R.id.cb_settings_charfilter);
		setCheckBox();
		cbSettings2ColorImage.setOnCheckedChangeListener(this);
		cbSettingsCharfilter.setOnCheckedChangeListener(this);

	}

	public void setCheckBox() {

		cbSettings2ColorImage.setChecked(MainActivity.is2ColorImage);
		cbSettingsCharfilter.setChecked(MainActivity.isCharFilter);
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView == cbSettings2ColorImage) {
			if (isChecked == true) {
				MainActivity.is2ColorImage = true;
				cbSettings2ColorImage.setChecked(MainActivity.is2ColorImage);

				Toast.makeText(context, "đã thiết lập quét hình trắng đen",
						2000).show();
			} else {
				MainActivity.is2ColorImage = false;
				cbSettings2ColorImage.setChecked(MainActivity.is2ColorImage);

				Toast.makeText(context, "đã tắt thiết lập quét hình trắng đen",
						2000).show();
			}

		}
		if (buttonView==cbSettingsCharfilter){
			if (isChecked == true) {
				MainActivity.isCharFilter = true;
				cbSettingsCharfilter.setChecked(MainActivity.isCharFilter);

				Toast.makeText(context, "đã thiết lập lọc ký tự việt",
						2000).show();
			} else {
				MainActivity.isCharFilter = false;
				cbSettingsCharfilter.setChecked(MainActivity.isCharFilter);

				Toast.makeText(context, "đã tắt thiết lập lọc ký tự việt",
						2000).show();
			}
		}
	}

}
