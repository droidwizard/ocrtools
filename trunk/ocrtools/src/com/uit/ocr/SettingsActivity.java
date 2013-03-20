package com.uit.ocr;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	private CheckBoxPreference mColor;
	private CheckBoxPreference mfilter;
	private ListPreference mLangs;
	private EditTextPreference mInvitation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings_activity);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
		
		mColor = (CheckBoxPreference) findPreference("cbColor");
		mfilter = (CheckBoxPreference) findPreference("cbfilter");
		mLangs = (ListPreference) findPreference("listLangs");
		mInvitation = (EditTextPreference) findPreference("titleEvents");
	}

	public void onSharedPreferenceChanged(SharedPreferences preference, String key) {
		// TODO Auto-generated method stub
		if(key.equals("listLangs"))
		{
			//String mText = preference.getString("listLangs", "14");
			String value = preference.getString(key, "13");
            int index = mLangs.findIndexOfValue(value);            
            if (index >= 0) {
                final String summary = (String)mLangs.getEntries()[index];         
                mLangs.setSummary(summary);
            }
		}
		else if(key.equals("titleEvents"))
		{
			String text = preference.getString(key, "");
			if(text.equals(""))
			{
				text = "Cài đặt tiêu đề cuộc hẹn";
			}
			mInvitation.setSummary(text);
		}
		
	}		

}
