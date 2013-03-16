package com.uit.ocr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.uit.ocr.utils.ConnectionDetector;
import com.uit.ocr.utils.Consts;
import com.uit.ocr.utils.JSONfunctions;
import com.uit.ocr.utils.ResultClass;

public class NormalResult extends ResultClass {
	private Context context = NormalResult.this;	
	
	private TextView textResult;
	private EditText textTranslate;
	private EditText textEdit;
	private Button changeMode;
	private Button translate;
	private Spinner spTranslate;
	private int langTranslate;
	private String textBase;
	private String resultTranslate;
	private String sourceLanguage;
	private String encoding;
	private String tranLanguage = Consts.TRANS_ENGLISH;
	private int mode = Consts.MODE_TEXT;
	private RecognizeThread thread;
	private ProgressDialog progressDialog;
	private boolean isComplete = false;
	
	ConnectionDetector mConnect;
	
	
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
		
		switch (MainActivity.mKeyLang) {
		case Consts.LANG_KEY_ENGLISH:
			sourceLanguage = Consts.TRANS_ENGLISH;
			break;
		case Consts.LANG_KEY_GERMAN:
			sourceLanguage = Consts.TRANS_GERMAN;
			break;
		case Consts.LANG_KEY_DUTCH:
			sourceLanguage = Consts.TRANS_DUTCH;
			break;
		case Consts.LANG_KEY_KOREAN:
			sourceLanguage = Consts.TRANS_KOREAN;
			break;
		case Consts.LANG_KEY_INDONESIA:
			sourceLanguage = Consts.TRANS_INDONESIA;
			break;
		case Consts.LANG_KEY_ITALIA:
			sourceLanguage = Consts.TRANS_ITALIA;
			break;
		case Consts.LANG_KEY_MALAYSIA:
			sourceLanguage = Consts.TRANS_MALAYSIA;
			break;
		case Consts.LANG_KEY_RUSSIA:
			sourceLanguage = Consts.TRANS_RUSSIA;
			break;
		case Consts.LANG_KEY_JAPAN:
			sourceLanguage = Consts.TRANS_JAPAN;
			break;
		case Consts.LANG_KEY_FRANCE:
			sourceLanguage = Consts.TRANS_FRANCE;
			break;
		case Consts.LANG_KEY_SPAIN:
			sourceLanguage = Consts.TRANS_SPAIN;
			break;
		case Consts.LANG_KEY_THAILAND:
			sourceLanguage = Consts.TRANS_THAILAND;
			break;
		case Consts.LANG_KEY_CHINA:
			sourceLanguage = Consts.TRANS_CHINA;
			break;
		case Consts.LANG_KEY_VIETNAM:
			sourceLanguage = Consts.TRANS_VIETNAM;
			break;

		default:
			break;
		}
		//sourceLanguage = "vi";
		tranLanguage = Consts.TRANS_ENGLISH;
		
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
		
	}

	@Override
	public void onReceiveResult() {
		textBase = getIntent().getStringExtra("textBaseResult");
	}

	private OnItemSelectedListener spinnerListener = new OnItemSelectedListener() {
		public void onItemSelected(AdapterView<?> parent, View arg1, int pos,
				long arg3) {
			//langTranslate = parent.getItemAtPosition(pos).toString();
			langTranslate = pos;
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
				mConnect = new ConnectionDetector(context);
				if(mConnect.isConnectingToInternet())
				{
					progressDialog = ProgressDialog.show(v.getContext(),
							"Vui lòng đợi", "Đang xử lý...");
					thread = new RecognizeThread();
					thread.start();
				}
				else
				{
					Toast.makeText(context, "Vui lòng kết nối mạng", Toast.LENGTH_SHORT).show();
				}
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
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		switch (item.getItemId()) {
		case Consts.EXPORT_ID:
			boolean rExport = exportText();
			if(rExport == true)
			{
				Toast.makeText(context, "Xuất file thành công", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(context, "Xuất file thất bại", Toast.LENGTH_SHORT).show();
			}
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
		encoding = "ISO-8859-1";
		switch (langTranslate) {
		case Consts.LANG_KEY_ENGLISH:
			tranLanguage = Consts.TRANS_ENGLISH;
			break;
		case Consts.LANG_KEY_GERMAN:
			tranLanguage = Consts.TRANS_GERMAN;
			break;
		case Consts.LANG_KEY_DUTCH:
			tranLanguage = Consts.TRANS_DUTCH;
			break;
		case Consts.LANG_KEY_KOREAN:
			tranLanguage = Consts.TRANS_KOREAN;
			encoding = "x-windows-949";
			break;
		case Consts.LANG_KEY_INDONESIA:
			tranLanguage = Consts.TRANS_INDONESIA;
			break;
		case Consts.LANG_KEY_ITALIA:
			tranLanguage = Consts.TRANS_ITALIA;
			break;
		case Consts.LANG_KEY_MALAYSIA:
			tranLanguage = Consts.TRANS_MALAYSIA;
			break;
		case Consts.LANG_KEY_RUSSIA:
			tranLanguage = Consts.TRANS_RUSSIA;
			encoding = "KOI8-R";
			break;
		case Consts.LANG_KEY_JAPAN:
			tranLanguage = Consts.TRANS_JAPAN;
			encoding = "SHIFT-JIS";
			break;
		case Consts.LANG_KEY_FRANCE:
			tranLanguage = Consts.TRANS_FRANCE;
			break;
		case Consts.LANG_KEY_SPAIN:
			tranLanguage = Consts.TRANS_SPAIN;
			break;
		case Consts.LANG_KEY_THAILAND:
			tranLanguage = Consts.TRANS_THAILAND;
			encoding = "TIS-620";
			break;
		case Consts.LANG_KEY_CHINA:
			tranLanguage = Consts.TRANS_CHINA;
			encoding = "BIG-5";
			break;
		case Consts.LANG_KEY_VIETNAM:
			tranLanguage = Consts.TRANS_VIETNAM;
			break;

		default:
			break;
		}		
		
		resultTranslate = translate(sourceLanguage, tranLanguage, textBase);

		isComplete = true;
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
	
	private static ArrayList<String> listString;
	private static String input = "";
		
	private String initParams(String scrText) {		
		String tmp = "";
		
		input = scrText.toString() + '\n';
		listString = new ArrayList<String>();
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) != '\n') {
				tmp += input.charAt(i);
			} else {				
				listString.add(tmp);
				tmp = "";						
			}
		}		
		
		String strForMSTrans = input.replace("\n", " | ");
		return strForMSTrans;
		
	}
		

	public String translate(String scrLang, String destLang, String scrText){
		String result = "";
		
		String temp = translateFirst(scrLang, scrText);
		input = input.replace("%", "%25");
		input = temp.replace(" |", "%0A");
		input = input.replace(" ", "%20");
		input = input.replace("/", "%2F");
		input = input.replace("?", "%3F");
		input = input.replace("\\", "%5C");
		input = input.replace("|", "%7C");
		input = input.replace("`", "%60");
		input = input.replace("@", "%40");
		input = input.replace("#", "%23");
		input = input.replace("$", "%24");		
		input = input.replace("^", "%5E");
		input = input.replace("&", "%26");
		input = input.replace("=", "%3D");
		input = input.replace("+", "%2B");
		input = input.replace("[", "%5B");
		input = input.replace("]", "%5D");
		input = input.replace("{", "%7B");
		input = input.replace("}", "%7D");
		input = input.replace(";", "%3B");
		input = input.replace(":", "%3A");
		input = input.replace("\"", "%22");
		input = input.replace("<", "%3C");
		input = input.replace(">", "%3E");
		input = input.replace(",", "%2C");
		input = input.replace("«", "%C2%AB");
		input = input.replace("»", "%C2%BB");
		Log.d("Test input", input);
		JSONObject json = JSONfunctions.getJSONfromURL("http://translate.google.vn/translate_a/t?client=j&text=" + input + "&sl=en&tl="+destLang, encoding);
		try {
			if(json != null)
			{
				JSONArray sentences = json.getJSONArray("sentences");
				for (int i = 0; i < sentences.length(); i++) {
					JSONObject res = sentences.getJSONObject(i);
					Log.d("Test result", res.getString("trans"));
					result += res.getString("trans").toString();
				}		
			}			
			
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
				
		return result;
	}

	
	public String translateFirst(String scrLang, String scrText) {
		String result = "";
		String urlTrans = "http://translate.google.com/m?hl=vi";
		String strForMSTrans = initParams(scrText);
		
		try {
			urlTrans += "&sl=" + scrLang + "&tl=en&ie=UTF-8&q="
					+ URLEncoder.encode(strForMSTrans, "UTF-8");
			result = JSONfunctions.msTranslate(urlTrans);
			Log.d("Test result ****", result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("Test microsoft trans", result);
		return result.toLowerCase();
	}
	
	public Boolean exportText()
	{		
		File myFile = new File(Consts.ROOT_PATH + Consts.FILE_NAME + "/text.txt");
		try {
			myFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myFile);
			OutputStreamWriter myOutWriter = 
									new OutputStreamWriter(fOut);
			myOutWriter.append(textBase);
			myOutWriter.close();
			fOut.close();
			return true;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;					
		}
		
	}
	
}
