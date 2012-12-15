package com.uit.ocr.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenceClass {
	private Context ctx;
	private static SharedPreferences pref;
	private SharedPreferences.Editor editor;
	
	public SharedPreferenceClass(Context ctx){
		this.ctx=ctx;
		pref = PreferenceManager.getDefaultSharedPreferences(ctx);
	}
	public static SharedPreferences getInstance(){
		return pref;
	}
	public static void readString(String key,String defValue){
		pref.getString(key, defValue);
	}
	public static void writeString(String key,String defValue){
		
	}

}
