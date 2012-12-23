package com.uit.ocr;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.uit.ocr.utils.Consts;

public class Settings extends Activity implements OnCheckedChangeListener,
		OnItemSelectedListener {

	private String language;

	CheckBox cbSettings2ColorImage, cbSettingsCharfilter;

	private Context context = Settings.this;
	private Spinner itemLanguages;
	private EditText edtSettingsTitleEvent;
	private Button btnSettingsSave;
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.settings);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		cbSettings2ColorImage = (CheckBox) findViewById(R.id.cb_settings_2colorimage);
		cbSettingsCharfilter = (CheckBox) findViewById(R.id.cb_settings_charfilter);
		itemLanguages = (Spinner) findViewById(R.id.sp_languages);
		edtSettingsTitleEvent = (EditText) findViewById(R.id.edt_settings_titleevent);
		btnSettingsSave = (Button) findViewById(R.id.btn_settings_saveevent);
		setCheckBox();

		cbSettings2ColorImage.setOnCheckedChangeListener(this);
		cbSettingsCharfilter.setOnCheckedChangeListener(this);
		cbSettings2ColorImage.setOnCheckedChangeListener(checkboxListener);
		cbSettingsCharfilter.setOnCheckedChangeListener(checkboxListener);
		btnSettingsSave.setOnClickListener(saveListener);
		itemLanguages.setOnItemSelectedListener(this);
		itemLanguages.setSelection(13);

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
			editor.putString(Consts.SETTINGS_TITLE_EVENTS,
					edtSettingsTitleEvent.getText().toString());
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
	private OnCheckedChangeListener checkboxListener = new OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (buttonView == cbSettings2ColorImage) {
				if (isChecked == true) {
					MainActivity.is2ColorImage = true;
					cbSettings2ColorImage
							.setChecked(MainActivity.is2ColorImage);
					Toast.makeText(context, "đã thiết lập quét hình trắng đen",
							Toast.LENGTH_SHORT).show();
				} else {
					MainActivity.is2ColorImage = false;
					cbSettings2ColorImage
							.setChecked(MainActivity.is2ColorImage);
					Toast.makeText(context,
							"đã tắt thiết lập quét hình trắng đen",
							Toast.LENGTH_SHORT).show();
				}
			}
			if (buttonView == cbSettingsCharfilter) {
				if (isChecked == true) {
					MainActivity.isCharFilter = true;
					cbSettingsCharfilter.setChecked(MainActivity.isCharFilter);
					Toast.makeText(context, "đã thiết lập lọc ký tự việt",
							Toast.LENGTH_SHORT).show();
				} else {
					MainActivity.isCharFilter = false;
					cbSettingsCharfilter.setChecked(MainActivity.isCharFilter);
					Toast.makeText(context, "đã tắt thiết lập lọc ký tự việt",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	};

	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		// TODO Auto-generated method stub
		language = parent.getItemAtPosition(pos).toString();
		if (language.equals(Consts.LANG_VIETNAM)) {
			MainActivity.flagLock = true;
			BaseOCR.lang = Consts.DATA_VIETNAM;
		} else {
			MainActivity.flagLock = false;
			if (language.equals(Consts.LANG_GERMAN)) {
				BaseOCR.lang = Consts.DATA_GERMAN;
			} else if (language.equals(Consts.LANG_DUTCH)) {
				BaseOCR.lang = Consts.DATA_DUTCH;
			} else if (language.equals(Consts.LANG_KOREAN)) {
				BaseOCR.lang = Consts.DATA_KOREAN;
			} else if (language.equals(Consts.LANG_INDONESIA)) {
				BaseOCR.lang = Consts.DATA_INDONESIA;
			} else if (language.equals(Consts.LANG_ITALIA)) {
				BaseOCR.lang = Consts.DATA_ITALIA;
			} else if (language.equals(Consts.LANG_MALAYSIA)) {
				BaseOCR.lang = Consts.DATA_MALAYSIA;
			} else if (language.equals(Consts.LANG_RUSSIA)) {
				BaseOCR.lang = Consts.DATA_RUSSIA;
			} else if (language.equals(Consts.LANG_JAPAN)) {
				BaseOCR.lang = Consts.DATA_JAPAN;
			} else if (language.equals(Consts.LANG_FRANCE)) {
				BaseOCR.lang = Consts.DATA_FRANCE;
			} else if (language.equals(Consts.LANG_SPAIN)) {
				BaseOCR.lang = Consts.DATA_SPAIN;
			} else if (language.equals(Consts.LANG_THAILAND)) {
				BaseOCR.lang = Consts.DATA_THAILAND;
			} else if (language.equals(Consts.LANG_CHINA)) {
				BaseOCR.lang = Consts.DATA_CHINA;
			} else if (language.equals(Consts.LANG_ENGLISH)) {
				BaseOCR.lang = Consts.DATA_ENGLISH;
			}
		}

	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub

	}

}
