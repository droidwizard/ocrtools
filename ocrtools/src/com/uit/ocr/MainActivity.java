package com.uit.ocr;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.uit.ocr.camera.MyCamera;

public class MainActivity extends Activity implements OnClickListener {
	// private static final String TAG = "MainActivity.java";

	private Context context = MainActivity.this;
	private Button btn_main_DemoOCR;
	private Button btn_main_InputImage;
	private ProgressDialog progressDialog;
	private InitData mInitData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn_main_DemoOCR = (Button) findViewById(R.id.btn_main_1);
		btn_main_DemoOCR.setOnClickListener(MainActivity.this);
		btn_main_InputImage = (Button) findViewById(R.id.btn_main_2);
		btn_main_InputImage.setOnClickListener((OnClickListener) context);


	}

	@Override
	protected void onStart() {
		super.onStart();
		progressDialog = ProgressDialog.show(context,
				"Please wait for a second", "Processing");
		mInitData = new InitData(context, progressDialog);

		mInitData.start();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	String _path;
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
				Intent i=new Intent(context, ImageCrop.class);
				Bundle b=new Bundle();
				b.putString("uriData", data.getDataString());
				i.putExtras(b);
				startActivity(i);
			}
		}
		if (requestCode==103){
			if (resultCode==RESULT_OK){
				Intent i=new Intent(context, ImageCrop.class);
				Bundle b=new Bundle();
				b.putString("uriData", data.getDataString());
				i.putExtras(b);
				startActivity(i);
			}
		}
	}

}
