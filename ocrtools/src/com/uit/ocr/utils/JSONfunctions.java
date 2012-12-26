package com.uit.ocr.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class JSONfunctions {
	private static final String TAG = JSONfunctions.class.getSimpleName();
	private static JSONfunctions mJson=new JSONfunctions();

	public static JSONfunctions getInstance(){
		return mJson;
	}

	public JSONObject getJSONfromURL(String url) {
		InputStream is = null;
		String result = "";
		JSONObject jArray = null;
		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();

			is = entity.getContent();

		} catch (Exception e) {
			Log.e(TAG, "Error in http connection " + e.toString());
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		} catch (Exception e) {
			Log.e(TAG, "Error converting result " + e.toString());
		}

		try {

			jArray = new JSONObject(result);
		} catch (JSONException e) {
			Log.e(TAG, "Error parsing data " + e.toString());
		}
		return jArray;
	}
}
