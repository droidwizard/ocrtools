package com.uit.ocr;

import com.uit.ocr.utils.Consts;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Settings extends Activity{
	private CheckBox cbSettings2ColorImage, cbSettingsCharfilter;
	private Context context = Settings.this;
	private EditText edtSettingsTitleEvent;
	private Button btnSettingsSave;
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.settings);
		super.onCreate(savedInstanceState);
		cbSettings2ColorImage = (CheckBox) findViewById(R.id.cb_settings_2colorimage);
		cbSettingsCharfilter = (CheckBox) findViewById(R.id.cb_settings_charfilter);
		edtSettingsTitleEvent = (EditText) findViewById(R.id.edt_settings_titleevent);
		btnSettingsSave = (Button) findViewById(R.id.btn_settings_saveevent);
		setCheckBox();
		cbSettings2ColorImage.setOnCheckedChangeListener(checkboxListener);
		cbSettingsCharfilter.setOnCheckedChangeListener(checkboxListener);
		btnSettingsSave.setOnClickListener(saveListener);
		
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = preferences.edit();
		
		if (preferences.getString(Consts.SETTINGS_TITLE_EVENTS, "").equals("")
				|| preferences.getString(Consts.SETTINGS_TITLE_EVENTS, "") == null) {
			edtSettingsTitleEvent.setText("");
		} else {
			edtSettingsTitleEvent.setText(preferences.getString(
					Consts.SETTINGS_TITLE_EVENTS, ""));
		}
	}

	/**
	 * button click event
	 */
	private OnClickListener saveListener = new OnClickListener() {
		public void onClick(View v) {
			editor.putString(Consts.SETTINGS_TITLE_EVENTS, edtSettingsTitleEvent.getText().toString());
			editor.commit();
		}
	};

	/**
	 * set checkbox status
	 */
	private void setCheckBox() {

		cbSettings2ColorImage.setChecked(MainActivity.is2ColorImage);
		cbSettingsCharfilter.setChecked(MainActivity.isCharFilter);
	}
	
	/**
	 * checkbox click event
	 */
	private OnCheckedChangeListener checkboxListener=new OnCheckedChangeListener() {
		
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
			if (buttonView == cbSettingsCharfilter) {
				if (isChecked == true) {
					MainActivity.isCharFilter = true;
					cbSettingsCharfilter.setChecked(MainActivity.isCharFilter);

					Toast.makeText(context, "đã thiết lập lọc ký tự việt", 2000)
							.show();
				} else {
					MainActivity.isCharFilter = false;
					cbSettingsCharfilter.setChecked(MainActivity.isCharFilter);

					Toast.makeText(context, "đã tắt thiết lập lọc ký tự việt", 2000)
							.show();
				}
			}
			
		}
	}; 

}
