package com.uit.ocr;

import java.io.FileNotFoundException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.uit.ocr.utils.ResultClass;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class InvitationResult extends ResultClass implements OnClickListener {
	private static final String TAG = InvitationResult.class.getSimpleName();
	private String recognizedResult;
	private String[] textAnalysis;
	private EditText edtInvitationResult_location, edtInvitationResult_time;
	private Spinner spinInvitationResult_content;
	private ImageView ivInvitationResult_main;
	private Bitmap bitmapShow;
	private Button btnInvitationResult_save;
	private int[] time;
	private String eventLocation;
	Calendar cal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invitationresult);
		edtInvitationResult_location = (EditText) findViewById(R.id.edt_invitation_location);
		edtInvitationResult_time = (EditText) findViewById(R.id.edt_invitaionresult_time);
		// spinInvitationResult_content=(Spinner)
		// findViewById(R.id.spin_invitation_content);
		ivInvitationResult_main = (ImageView) findViewById(R.id.iv_invitaionresult_main);
		btnInvitationResult_save = (Button) findViewById(R.id.btn_invitationresult_save);

		onReceiveResult();
		time = getTime(textAnalysis[0].toString());
		Log.i(TAG, time[0] + "/" + time[1]);

		ivInvitationResult_main.setImageBitmap(ImageHandler.imageForResult);

		if (textAnalysis[1].equals("1") && textAnalysis[2].equals("1")
				&& textAnalysis[3].equals("2012")) {
			edtInvitationResult_time.setText(textAnalysis[0].toString() + " "
					+ textAnalysis[4].toString() + "/"
					+ textAnalysis[5].toString() + "/"
					+ textAnalysis[6].toString());

			cal = setTimeEvent(Integer.valueOf(textAnalysis[6].toString()),
					Integer.valueOf(textAnalysis[5].toString()),
					Integer.valueOf(textAnalysis[4].toString()), time[0],
					time[1]);

		} else {
			edtInvitationResult_time.setText(textAnalysis[0].toString() + " "
					+ textAnalysis[1].toString() + "/"
					+ textAnalysis[2].toString() + "/"
					+ textAnalysis[3].toString());

			cal = setTimeEvent(Integer.valueOf(textAnalysis[3].toString()),
					Integer.valueOf(textAnalysis[2].toString()),
					Integer.valueOf(textAnalysis[1].toString()), time[0],
					time[1]);
		}
		eventLocation = textAnalysis[7].toString();
		edtInvitationResult_location.setText(eventLocation);

		btnInvitationResult_save.setOnClickListener(this);

	}

	@Override
	public void onReceiveResult() {
		recognizedResult = getIntent().getStringExtra("textResult");
		textAnalysis = getIntent().getStringArrayExtra("textAnalisys");
	}

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

	/*
	 * private int[] getDate(String date) { int result[] = new int[3]; Pattern
	 * pattern = Pattern
	 * .compile("(0?[1-9]|[12][0-9])/([0-3][0-9])/([12][0-9]+)"); Matcher
	 * matcher = pattern.matcher(date); if (matcher.find()) { result[0] =
	 * Integer.valueOf(matcher.group(1));// 0 là ngày result[1] =
	 * Integer.valueOf(matcher.group(2));// 1 là tháng result[2] =
	 * Integer.valueOf(matcher.group(2));// 1 là năm Log.i(TAG,
	 * "Ngày tháng năm : " + result[0] + " " + result[1] + " " + result[2]); }
	 * return result; }
	 */

	private void onInvite(Calendar startTime) {

		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra("beginTime",
				startTime.getTimeInMillis() - 10 * 60 * 1000);
		intent.putExtra("allDay", 1);
		intent.putExtra("endTime", startTime.getTimeInMillis() + 60 * 60 * 1000);
		intent.putExtra("title", "Họp mặt");
		intent.putExtra("eventLocation", eventLocation);
		// intent.putExtra("description", "Nội dung họp");
		startActivity(intent);

	}

	public void onClick(View v) {
		if (v == btnInvitationResult_save) {

			onInvite(cal);

		}
	}

}
