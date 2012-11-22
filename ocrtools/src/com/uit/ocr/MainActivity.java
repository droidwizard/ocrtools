package com.uit.ocr;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends Activity implements OnClickListener {
	// private static final String TAG = "MainActivity.java";

	private Context context = MainActivity.this;
	private Button btn_main_DemoOCR;
	private Button btn_main_InputImage;
	private ProgressDialog progressDialog;
	private InitData mInitData;
	public static boolean is2ColorImage;
	public static boolean isCharFilter;
	public static boolean isNameCard;
	private RadioGroup rg_main;
	private RadioButton rabtn_main_name_card;
	private RadioButton rabtn_main_invite;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		rg_main=(RadioGroup) findViewById(R.id.rg_main);
		rabtn_main_name_card=(RadioButton) findViewById(R.id.rabtn_main_1);
		rabtn_main_invite=(RadioButton) findViewById(R.id.rabtn_main_2);
		btn_main_DemoOCR = (Button) findViewById(R.id.btn_main_1);
		btn_main_DemoOCR.setOnClickListener(MainActivity.this);
		btn_main_InputImage = (Button) findViewById(R.id.btn_main_2);
		btn_main_InputImage.setOnClickListener((OnClickListener) context);
		onLoadStates();

	}
	public void onLoadStates(){
		if (isNameCard){
			rabtn_main_name_card.setChecked(true);
		}else{
			rabtn_main_invite.setChecked(true);
		}
	}
	
	public void onRadioButtonClicked(View view){
	    boolean checked = ((RadioButton) view).isChecked();
	    switch(view.getId()) {
	        case R.id.rabtn_main_1:
	            if (checked)
	            	isNameCard=true;
	            break;
	        case R.id.rabtn_main_2:
	            if (checked)
	            	isNameCard=false;
	            break;
	    }
	}
	
	
	@Override
	protected void onResume() {
		progressDialog = ProgressDialog.show(context,
				"Please wait for a second", "Processing");
		mInitData = new InitData(context, progressDialog);
		mInitData.start();
		super.onResume();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals("Settings")){
			startActivityForResult(new Intent(context, Settings.class), 104);
		}
		return super.onOptionsItemSelected(item);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_main_1: {
			
			Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);   
	        startActivityForResult(camera, 103);

			break;
		}
		case R.id.btn_main_2: {
			Intent i = new Intent();
			i.setType("image/*");
			i.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(i, "Application Chooser"),
					102);
			break;
		}
		}
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

}
