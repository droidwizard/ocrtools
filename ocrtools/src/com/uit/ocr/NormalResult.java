package com.uit.ocr;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.uit.ocr.utils.Consts;
import com.uit.ocr.utils.JSONfunctions;
import com.uit.ocr.utils.ResultClass;

public class NormalResult extends ResultClass {
	private static final String TAG = NormalResult.class.getSimpleName();
	private Context context = NormalResult.this;
	
	private TextView textResult;
	private EditText textTranslate;
	private EditText textEdit;
	private Button changeMode;
	private Button translate;
	private Spinner spTranslate;
	private String langTranslate;
	private String textBase;
	private String resultTranslate;
	private String sourceLanguage;
	private String tranLanguage = Consts.TRANS_ENGLISH;
	private int mode = Consts.MODE_TEXT;
	private RecognizeThread thread;
	private ProgressDialog progressDialog;
	private boolean isComplete = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_normalresult_activity);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		textResult = (TextView) findViewById(R.id.tv_text_result);
		textEdit = (EditText) findViewById(R.id.edt_text_result);
		textTranslate = (EditText) findViewById(R.id.edt_result_translate);
		spTranslate = (Spinner) findViewById(R.id.sp_translate);
		changeMode = (Button) findViewById(R.id.btn_edit_text_result);
		translate = (Button) findViewById(R.id.btn_translate);
		
		onReceiveResult();
		
		textResult.setText(textBase);
		Linkify.addLinks(textResult, Linkify.ALL);

		if (mode == Consts.MODE_TEXT) {
			textEdit.setVisibility(View.GONE);
		}

		changeMode.setText(Consts.TEXT_VIEW);

		changeMode.setOnClickListener(clickListener);
		translate.setOnClickListener(clickListener);
		spTranslate.setOnItemSelectedListener(spinnerListener);

		if (BaseOCR.lang.equals(Consts.DATA_VIETNAM)) {
			sourceLanguage = Consts.TRANS_VIETNAM;
		} else if (BaseOCR.lang.equals(Consts.DATA_GERMAN)) {
			sourceLanguage = Consts.TRANS_GERMAN;
		} else if (BaseOCR.lang.equals(Consts.DATA_DUTCH)) {
			sourceLanguage = Consts.TRANS_DUTCH;
		} else if (BaseOCR.lang.equals(Consts.DATA_KOREAN)) {
			sourceLanguage = Consts.TRANS_KOREAN;
		} else if (BaseOCR.lang.equals(Consts.DATA_INDONESIA)) {
			sourceLanguage = Consts.TRANS_INDONESIA;
		} else if (BaseOCR.lang.equals(Consts.DATA_ITALIA)) {
			sourceLanguage = Consts.TRANS_ITALIA;
		} else if (BaseOCR.lang.equals(Consts.DATA_MALAYSIA)) {
			sourceLanguage = Consts.TRANS_MALAYSIA;
		} else if (BaseOCR.lang.equals(Consts.DATA_RUSSIA)) {
			sourceLanguage = Consts.TRANS_RUSSIA;
		} else if (BaseOCR.lang.equals(Consts.DATA_JAPAN)) {
			sourceLanguage = Consts.TRANS_JAPAN;
		} else if (BaseOCR.lang.equals(Consts.DATA_FRANCE)) {
			sourceLanguage = Consts.TRANS_FRANCE;
		} else if (BaseOCR.lang.equals(Consts.DATA_SPAIN)) {
			sourceLanguage = Consts.TRANS_SPAIN;
		} else if (BaseOCR.lang.equals(Consts.DATA_THAILAND)) {
			sourceLanguage = Consts.TRANS_THAILAND;
		} else if (BaseOCR.lang.equals(Consts.DATA_CHINA)) {
			sourceLanguage = Consts.TRANS_CHINA;
		} else if (BaseOCR.lang.equals(Consts.DATA_ENGLISH)) {
			sourceLanguage = Consts.TRANS_ENGLISH;
		}
	}

	@Override
	public void onReceiveResult() {
		textBase = getIntent().getStringExtra("textBaseResult");
	}

	private OnItemSelectedListener spinnerListener = new OnItemSelectedListener() {
		public void onItemSelected(AdapterView<?> parent, View arg1, int pos,
				long arg3) {
			langTranslate = parent.getItemAtPosition(pos).toString();
		}
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};

	private OnClickListener clickListener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_edit_text_result:
				if (mode == Consts.MODE_TEXT) {
					textEdit.setText(textBase);
					textEdit.setVisibility(View.VISIBLE);
					textResult.setVisibility(View.GONE);
					changeMode.setText(Consts.EDIT_TEXT);
					mode = Consts.MODE_EDIT;
				} else {
					textBase = textEdit.getText().toString();
					textResult.setText(textBase);
					Linkify.addLinks(textResult, Linkify.ALL);
					textResult.setVisibility(View.VISIBLE);
					textEdit.setVisibility(View.GONE);
					changeMode.setText(Consts.TEXT_VIEW);
					mode = Consts.MODE_TEXT;

				}
				break;
			case R.id.btn_translate:
				progressDialog = ProgressDialog.show(v.getContext(),
						"Vui lòng đợi", "Đang xử lý...");
				thread = new RecognizeThread();
				thread.start();
				break;

			default:
				break;
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, Consts.EXPORT_ID, Menu.NONE, R.string.menu_export)
				.setIcon(android.R.drawable.ic_menu_view);
		menu.add(Menu.NONE, Consts.CAMERA_ID, Menu.NONE, R.string.menu_camera)
				.setIcon(android.R.drawable.ic_menu_camera);
		menu.add(Menu.NONE, Consts.IMAGE_ID, Menu.NONE, R.string.menu_image)
				.setIcon(android.R.drawable.ic_menu_gallery);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		switch (item.getItemId()) {
		case Consts.EXPORT_ID:

			break;
		case Consts.CAMERA_ID:
			Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(camera, 103);
			break;
		case Consts.IMAGE_ID:
			Intent i = new Intent();
			i.setType("image/*");
			i.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(
					Intent.createChooser(i, "Application Chooser"), 102);
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 102) {
			if (resultCode == RESULT_OK) {
				Intent i = new Intent(context, ImageHandler.class);
				Bundle b = new Bundle();
				b.putString("uriData", data.getDataString());
				i.putExtras(b);
				startActivity(i);
			}
		}
		if (requestCode == 103) {
			if (resultCode == RESULT_OK) {
				Intent i = new Intent(context, ImageHandler.class);
				Bundle b = new Bundle();
				b.putString("uriData", data.getDataString());
				i.putExtras(b);
				startActivity(i);
			}
		}
	}

	private void progress() {
		if (langTranslate.equals(Consts.LANG_VIETNAM)) {
			tranLanguage = Consts.TRANS_VIETNAM;
		} else if (langTranslate.equals(Consts.LANG_GERMAN)) {
			tranLanguage = Consts.TRANS_GERMAN;
		} else if (langTranslate.equals(Consts.LANG_DUTCH)) {
			tranLanguage = Consts.TRANS_DUTCH;
		} else if (langTranslate.equals(Consts.LANG_KOREAN)) {
			tranLanguage = Consts.TRANS_KOREAN;
		} else if (langTranslate.equals(Consts.LANG_INDONESIA)) {
			tranLanguage = Consts.TRANS_INDONESIA;
		} else if (langTranslate.equals(Consts.LANG_ITALIA)) {
			tranLanguage = Consts.TRANS_ITALIA;
		} else if (langTranslate.equals(Consts.LANG_MALAYSIA)) {
			tranLanguage = Consts.TRANS_MALAYSIA;
		} else if (langTranslate.equals(Consts.LANG_RUSSIA)) {
			tranLanguage = Consts.TRANS_RUSSIA;
		} else if (langTranslate.equals(Consts.LANG_JAPAN)) {
			tranLanguage = Consts.TRANS_JAPAN;
		} else if (langTranslate.equals(Consts.LANG_FRANCE)) {
			tranLanguage = Consts.TRANS_FRANCE;
		} else if (langTranslate.equals(Consts.LANG_SPAIN)) {
			tranLanguage = Consts.TRANS_SPAIN;
		} else if (langTranslate.equals(Consts.LANG_THAILAND)) {
			tranLanguage = Consts.TRANS_THAILAND;
		} else if (langTranslate.equals(Consts.LANG_CHINA)) {
			tranLanguage = Consts.TRANS_CHINA;
		} else if (langTranslate.equals(Consts.LANG_ENGLISH)) {
			tranLanguage = Consts.TRANS_ENGLISH;
		}
		resultTranslate = transText(textBase, sourceLanguage, tranLanguage);

		isComplete = true;
	}

	private String transText(String str, String sl, String tl) {
		String result = "";
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) != ' ') {
				result += str.charAt(i);
			} else {
				result += "%20";
			}
		}

		JSONObject json = JSONfunctions.getInstance().getJSONfromURL(
				"http://translate.google.vn/translate_a/t?client=p&text="
						+ result + "&sl=" + sl + "&tl=" + tl);
		try {
			JSONArray sentences = json.getJSONArray("sentences");
			JSONObject res = sentences.getJSONObject(0);
			result = res.getString("trans");

		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		return result;
	}

	class RecognizeThread extends Thread {
		public RecognizeThread() {
		}

		@Override
		public void run() {
			progress();
			while (!isComplete) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			handler.sendEmptyMessage(0);
		}

		private Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				progressDialog.dismiss();
				textTranslate.setText(resultTranslate);
				isComplete = false;
			}
		};
	}

}
