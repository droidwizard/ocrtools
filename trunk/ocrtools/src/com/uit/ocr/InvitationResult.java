package com.uit.ocr;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.uit.ocr.utils.Consts;
import com.uit.ocr.utils.ResultClass;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class InvitationResult extends ResultClass {
	private static final String TAG = InvitationResult.class.getSimpleName();
	private String[] textAnalysis;
	private EditText edtInvitationResult_location, edtInvitationResult_time;
	private Spinner spinInvitationResult_content;
	private ImageView ivInvitationResult_main;
	private Button btnInvitationResult_save;
	private String eventLocation, eventTitle;
	private Calendar cal;
	private ArrayList<String> DefaultEvents = new ArrayList<String>();
	private String[] tmpResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invitationresult);
		edtInvitationResult_location = (EditText) findViewById(R.id.edt_invitation_location);
		edtInvitationResult_time = (EditText) findViewById(R.id.edt_invitaionresult_time);
		spinInvitationResult_content = (Spinner) findViewById(R.id.spin_invitaionresult_content);
		ivInvitationResult_main = (ImageView) findViewById(R.id.iv_invitaionresult_main);
		btnInvitationResult_save = (Button) findViewById(R.id.btn_invitationresult_save);

		onReceiveResult();

		onLoadData();

		btnInvitationResult_save.setOnClickListener(saveListener);

		onLoadTitleEvent();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				InvitationResult.this,
				android.R.layout.simple_spinner_dropdown_item, DefaultEvents);
		spinInvitationResult_content.setAdapter(adapter);

		spinInvitationResult_content.setOnItemSelectedListener(spinListener);

	}

	/**
	 * xóa dữ liệu cũ, thêm dữ liệu mặc định, thêm dữ liệu từ Settings activity
	 * (được lưu trong SharedPreference)
	 */
	private void onLoadTitleEvent() {
		DefaultEvents.clear();

		DefaultEvents.add(Consts.DEFAULT_EVENT_1);
		DefaultEvents.add(Consts.DEFAULT_EVENT_2);
		DefaultEvents.add(Consts.DEFAULT_EVENT_3);
		
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		String settingsEventTitles;
		settingsEventTitles = preferences.getString(
				Consts.SETTINGS_TITLE_EVENTS, "");
		if (settingsEventTitles != null && settingsEventTitles != "") {
			if (settingsEventTitles.contains("-")) {
				String[] tmps = settingsEventTitles.split("-");
				for (String tmp : tmps) {
					if (!tmp.equals("") && tmp != null && !tmp.equals(" ")) {
						DefaultEvents.add(tmp);
					}
				}
			}
		}
	}

	/**
	 * load dữ liệu vào edittext, imageview
	 */
	private void onLoadData() {
		ivInvitationResult_main.setImageBitmap(ImageHandler.imageForResult);

		int[] time = getTime(textAnalysis[0].toString());

		if (textAnalysis[1].equals("1") && textAnalysis[2].equals("1")
				&& textAnalysis[3].equals("2012")) {
			tmpResult[0] = textAnalysis[0];
			tmpResult[1] = textAnalysis[4];
			tmpResult[2] = textAnalysis[5];
			tmpResult[3] = textAnalysis[6];

			edtInvitationResult_time.setText(tmpResult[0] + "-" + tmpResult[1]
					+ "/" + tmpResult[2] + "/" + tmpResult[3]);

			String a = edtInvitationResult_time.getText().toString();
			Log.i(TAG, a);

			cal = setTimeEvent(Integer.valueOf(textAnalysis[6].toString()),
					Integer.valueOf(textAnalysis[5].toString()),
					Integer.valueOf(textAnalysis[4].toString()), time[0],
					time[1]);

		} else {
			edtInvitationResult_time.setText(textAnalysis[0].toString() + "-"
					+ textAnalysis[1].toString() + "/"
					+ textAnalysis[2].toString() + "/"
					+ textAnalysis[3].toString());
			String a = edtInvitationResult_time.getText().toString();

			Log.i(TAG, a + " ");

			cal = setTimeEvent(Integer.valueOf(textAnalysis[3].toString()),
					Integer.valueOf(textAnalysis[2].toString()),
					Integer.valueOf(textAnalysis[1].toString()), time[0],
					time[1]);
		}

		eventLocation = textAnalysis[7].toString();
		edtInvitationResult_location.setText(eventLocation);
	}

	/**
	 * click event
	 */
	private OnClickListener saveListener = new OnClickListener() {
		public void onClick(View v) {
			onInvite(cal);
		}
	};

	/**
	 * lấy về pos của item đã chọn trong spin
	 */
	private OnItemSelectedListener spinListener = new OnItemSelectedListener() {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {
			eventTitle = (String) arg0.getItemAtPosition(pos);
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};

	@Override
	public void onReceiveResult() {
		textAnalysis = getIntent().getStringArrayExtra("textAnalisys");
	}

	/**
	 * set thời gian cho việc xếp lịch
	 * 
	 * @param year
	 * @param month
	 * @param date
	 * @param hour
	 * @param minutes
	 * @return calendar Object
	 */
	private Calendar setTimeEvent(int year, int month, int date, int hour,
			int minutes) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1);
		c.set(Calendar.DATE, date);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minutes);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c;
	}

	/**
	 * phân tích thời gian (ko cần thiết lắm có thể ko xài)
	 * 
	 * @param time
	 * @return
	 */
	private int[] getTime(String time) {
		int result[] = new int[2];
		Pattern pattern = Pattern.compile("(0?[1-9]|[12][0-9]):([0-9]{2})");
		Matcher matcher = pattern.matcher(time);
		if (matcher.find()) {
			result[0] = Integer.valueOf(matcher.group(1));// 0 là giờ
			result[1] = Integer.valueOf(matcher.group(2));// 1 là phút
			Log.i(TAG, "Giờ phút : " + result[0] + " " + result[1]);
		}
		return result;
	}

	/**
	 * hàm start Calendar App để xếp lịch
	 * 
	 * @param startTime
	 */
	private void onInvite(Calendar startTime) {
		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra("beginTime",
				startTime.getTimeInMillis() - 10 * 60 * 1000);
		intent.putExtra("allDay", 1);
		intent.putExtra("endTime", startTime.getTimeInMillis() + 60 * 60 * 1000);
		intent.putExtra("title", eventTitle);
		intent.putExtra("eventLocation", eventLocation);
		// intent.putExtra("description", "Nội dung họp");
		startActivity(intent);
	}

}
