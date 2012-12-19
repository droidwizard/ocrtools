package com.uit.ocr;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
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
import android.widget.Toast;

import com.uit.ocr.ImageHandler.RecognizeThread;
import com.uit.ocr.utils.JSONfunctions;

public class NormalResult extends Activity implements OnClickListener, OnItemSelectedListener{
	
	private final String DATA_ENGLISH = "eng";
	private final String DATA_GERMAN = "deu";
	private final String DATA_DUTCH = "nld";
	private final String DATA_KOREAN = "kor";
	private final String DATA_INDONESIA = "ind";
	private final String DATA_ITALIA = "ita";
	private final String DATA_MALAYSIA = "msa";
	private final String DATA_RUSSIA = "rus";
	private final String DATA_JAPAN = "jpn";
	private final String DATA_FRANCE = "fra";
	private final String DATA_SPAIN = "spa";
	private final String DATA_THAILAND = "tha";
	private final String DATA_CHINA = "chi_tra";
	private final String DATA_VIETNAM = "vie";
	
	private final String TRANS_ENGLISH = "en";
	private final String TRANS_GERMAN = "de";
	private final String TRANS_DUTCH = "nl";
	private final String TRANS_KOREAN = "ko";
	private final String TRANS_INDONESIA = "id";
	private final String TRANS_ITALIA = "it";
	private final String TRANS_MALAYSIA = "ms";
	private final String TRANS_RUSSIA = "ru";
	private final String TRANS_JAPAN = "ja";
	private final String TRANS_FRANCE = "fr";
	private final String TRANS_SPAIN = "es";
	private final String TRANS_THAILAND = "th";
	private final String TRANS_CHINA = "zh-TW";
	private final String TRANS_VIETNAM = "vi";
	
	private final String LANG_ENGLISH = "Anh";
	private final String LANG_GERMAN = "Đức";
	private final String LANG_DUTCH = "Hà Lan";
	private final String LANG_KOREAN = "Hàn Quốc";
	private final String LANG_INDONESIA = "Indonesia";
	private final String LANG_ITALIA = "Italia";
	private final String LANG_MALAYSIA = "Malaysia";
	private final String LANG_RUSSIA = "Nga";
	private final String LANG_JAPAN = "Nhật Bản";
	private final String LANG_FRANCE = "Pháp";
	private final String LANG_SPAIN = "Tây Ban Nha";
	private final String LANG_THAILAND = "Thái Lan";
	private final String LANG_CHINA = "Trung Quốc";
	private final String LANG_VIETNAM = "Việt Nam";
		
	private TextView textResult;
	private EditText textTranslate;
	private EditText textEdit;
	private Button changeMode;
	private Button translate;
	private Spinner spTranslate;
	
	private String langTranslate;
	private String textBase;
	private String resultTranslate;
		
	private Context context = this;
	
	private String sourceLanguage;
	private String tranLanguage;
	
	private final String TEXT_VIEW = "Chỉnh sửa";
	private final String EDIT_TEXT = "Xong";
	
	private final int MODE_TEXT = 0;
	private final int MODE_EDIT = 1;
	
	private int mode = MODE_TEXT;
	
	private static final int EXPORT_ID = Menu.FIRST;
	private static final int CAMERA_ID = Menu.FIRST + 1;
	private static final int IMAGE_ID = Menu.FIRST + 2;
	
	RecognizeThread thread;
	ProgressDialog progressDialog;
	private boolean isComplete = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.normalmode_layout);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		//textBase = getIntent().getStringExtra("textBaseResult");
		textBase = "This has now been incorporated into the library and is run as the main class. You can view the latest source code here.";
		
		textResult = (TextView) findViewById(R.id.tv_text_result);
		//textResult.setText("Số điện thoại 0975983991. Email nam.hd23@gmail.com. địa chỉ web google.com.vn");
		textResult.setText(textBase);
		Linkify.addLinks(textResult, Linkify.ALL);	
		
		textEdit = (EditText) findViewById(R.id.edt_text_result);
		
		textTranslate = (EditText) findViewById(R.id.edt_result_translate);
		spTranslate = (Spinner) findViewById(R.id.sp_translate);
		
		//textTranslate.setText("Số điện thoại 0975983991. Email nam.hd23@gmail.com. địa chỉ web google.com.vn");
		
		if(mode == MODE_TEXT)
		{
			textEdit.setVisibility(View.GONE);
		}
		
		changeMode = (Button) findViewById(R.id.btn_edit_text_result);
		changeMode.setText(TEXT_VIEW);		
		
		translate = (Button) findViewById(R.id.btn_translate);
		
		changeMode.setOnClickListener(this);
		translate.setOnClickListener(this);
		spTranslate.setOnItemSelectedListener(this);
		
		if(BaseOCR.lang.equals(DATA_VIETNAM))
		{
			sourceLanguage = TRANS_VIETNAM;
		}
		else if(BaseOCR.lang.equals(DATA_GERMAN))
		{
			sourceLanguage = TRANS_GERMAN;
		}
		else if(BaseOCR.lang.equals(DATA_DUTCH))
		{
			sourceLanguage = TRANS_DUTCH;	
		}
		else if(BaseOCR.lang.equals(DATA_KOREAN))
		{
			sourceLanguage = TRANS_KOREAN;
		}
		else if(BaseOCR.lang.equals(DATA_INDONESIA))
		{
			sourceLanguage = TRANS_INDONESIA;
		}
		else if(BaseOCR.lang.equals(DATA_ITALIA))
		{
			sourceLanguage = TRANS_ITALIA;
		}
		else if(BaseOCR.lang.equals(DATA_MALAYSIA))
		{
			sourceLanguage = TRANS_MALAYSIA;
		}
		else if(BaseOCR.lang.equals(DATA_RUSSIA))
		{
			sourceLanguage = TRANS_RUSSIA;
		}
		else if(BaseOCR.lang.equals(DATA_JAPAN))
		{
			sourceLanguage = TRANS_JAPAN;
		}
		else if(BaseOCR.lang.equals(DATA_FRANCE))
		{
			sourceLanguage = TRANS_FRANCE;
		}
		else if(BaseOCR.lang.equals(DATA_SPAIN))
		{
			sourceLanguage = TRANS_SPAIN;
		}
		else if(BaseOCR.lang.equals(DATA_THAILAND))
		{
			sourceLanguage = TRANS_THAILAND;
		}
		else if(BaseOCR.lang.equals(DATA_CHINA))
		{
			sourceLanguage = TRANS_CHINA;
		}
		else if(BaseOCR.lang.equals(DATA_ENGLISH))
		{
			sourceLanguage = TRANS_ENGLISH;
		}
		
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_edit_text_result:
			if(mode == MODE_TEXT)
			{
				textEdit.setText(textBase);
				textEdit.setVisibility(View.VISIBLE);
				textResult.setVisibility(View.GONE);
				changeMode.setText(EDIT_TEXT);
				mode = MODE_EDIT;
			}
			else
			{
				textBase = textEdit.getText().toString();
				textResult.setText(textBase);
				Linkify.addLinks(textResult, Linkify.ALL);
				textResult.setVisibility(View.VISIBLE);
				textEdit.setVisibility(View.GONE);
				changeMode.setText(TEXT_VIEW);
				mode = MODE_TEXT;				
				
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);		
		menu.add(Menu.NONE, EXPORT_ID, Menu.NONE, R.string.menu_export).setIcon(
				android.R.drawable.ic_menu_view);
		menu.add(Menu.NONE, CAMERA_ID, Menu.NONE, R.string.menu_camera).setIcon(
				android.R.drawable.ic_menu_camera);
		menu.add(Menu.NONE, IMAGE_ID, Menu.NONE, R.string.menu_image).setIcon(
				android.R.drawable.ic_menu_gallery);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		switch (item.getItemId()) {
		case EXPORT_ID:		
			
            break;
        case CAMERA_ID:
        	Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);   
	        startActivityForResult(camera, 103);
            break;
        case IMAGE_ID:
        	Intent i = new Intent();
			i.setType("image/*");
			i.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(i, "Application Chooser"),102);
        	break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode==102){
			if (resultCode==RESULT_OK){
				Intent i=new Intent(context, ImageHandler.class);
				Bundle b=new Bundle();
				b.putString("uriData", data.getDataString());
				i.putExtras(b);
				startActivity(i);
			}
		}
		if (requestCode==103){
			if (resultCode==RESULT_OK){
				Intent i=new Intent(context, ImageHandler.class);
				Bundle b=new Bundle();
				b.putString("uriData", data.getDataString());
				i.putExtras(b);
				startActivity(i);
			}
		}
	}
	
	public void progress()
	{
		if(langTranslate.equals(LANG_VIETNAM))
		{
			tranLanguage = TRANS_VIETNAM;
		}
		else if(langTranslate.equals(LANG_GERMAN))
		{
			tranLanguage = TRANS_GERMAN;
		}
		else if(langTranslate.equals(LANG_DUTCH))
		{
			tranLanguage = TRANS_DUTCH;
		}
		else if(langTranslate.equals(LANG_KOREAN))
		{
			tranLanguage = TRANS_KOREAN;
		}
		else if(langTranslate.equals(LANG_INDONESIA))
		{
			tranLanguage = TRANS_INDONESIA;
		}
		else if(langTranslate.equals(LANG_ITALIA))
		{
			tranLanguage = TRANS_ITALIA;
		}
		else if(langTranslate.equals(LANG_MALAYSIA))
		{
			tranLanguage = TRANS_MALAYSIA;
		}
		else if(langTranslate.equals(LANG_RUSSIA))
		{
			tranLanguage = TRANS_RUSSIA;
		}
		else if(langTranslate.equals(LANG_JAPAN))
		{
			tranLanguage = TRANS_JAPAN;
		}
		else if(langTranslate.equals(LANG_FRANCE))
		{
			tranLanguage = TRANS_FRANCE;	
		}
		else if(langTranslate.equals(LANG_SPAIN))
		{
			tranLanguage = TRANS_SPAIN;
		}
		else if(langTranslate.equals(LANG_THAILAND))
		{
			tranLanguage = TRANS_THAILAND;
		}
		else if(langTranslate.equals(LANG_CHINA))
		{
			tranLanguage = TRANS_CHINA;
		}
		else if(langTranslate.equals(LANG_ENGLISH))
		{
			tranLanguage = TRANS_ENGLISH;
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
		
		JSONObject json = JSONfunctions
				.getJSONfromURL("http://translate.google.vn/translate_a/t?client=p&text=" + result + "&sl=" + sl + "&tl=" + tl);
		try {
			JSONArray sentences = json.getJSONArray("sentences");
			JSONObject res = sentences.getJSONObject(0);			
			result = res.getString("trans");
			
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		return result;	
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		// TODO Auto-generated method stub
		langTranslate = parent.getItemAtPosition(pos).toString();
		
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
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
