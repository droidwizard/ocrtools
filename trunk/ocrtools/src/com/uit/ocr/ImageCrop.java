package com.uit.ocr;

import java.io.FileNotFoundException;

import com.uit.ocr.utils.CustomImageView;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class ImageCrop extends BaseOCR implements OnTouchListener {
	private static final String TAG = "ImageCrop.java";
	private ImageView ivImageCropProcess, ivImageCropNext,
			ivImageCropBack;
	private ImageView ivImageCropMain;

	private Uri uriData;
	private String textResult;
	private Bitmap imageForResult;
	private Context mContext = ImageCrop.this;
	RecognizeThread thread;
	ProgressDialog progressDialog;
	private boolean isComplete = false;
	private String[] textAnalisys;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imagecrop);
		ivImageCropMain = (ImageView) findViewById(R.id.custom_image);
		ivImageCropProcess = (ImageView) findViewById(R.id.iv_imagecrop_btnProcess);
		ivImageCropNext = (ImageView) findViewById(R.id.iv_imagecrop_btnNext);
		ivImageCropBack = (ImageView) findViewById(R.id.iv_imagecrop_btnBack);

		imageForResult = onReceiveImage();
		
		//ivImageCropMain.setBitmap(imageForResult);
		
		// hình ảnh sau khi lấy dc
		ivImageCropMain.setImageBitmap(imageForResult);
		ivImageCropProcess.setOnTouchListener(this);

		

	}

	public Bitmap onReceiveImage() {
		Bitmap bitmap = null;
		Bundle tmp = getIntent().getExtras();
		uriData = Uri.parse(tmp.getString("uriData"));
		try {
			bitmap = BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(uriData));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	public void onPhotoChosen() {
		textResult = onHanldeOCR(imageForResult);
		Log.i(TAG, "phan tich hinh anh xong");
		textAnalisys=onTextAnalisys(textResult);
		Log.i(TAG, "phan tich text xong");
		isComplete = true;
		Log.i(TAG, "gan true");
	}

	class RecognizeThread extends Thread {
		public RecognizeThread() {
		}

		@Override
		public void run() {
			onPhotoChosen();
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
				Intent i = new Intent(mContext, InputImageResult.class);
				i.putExtra("textResult", textResult);
				i.putExtra("textAnalisys", textAnalisys);
				startActivity(i);
				isComplete=false;
			}
		};
	}

	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			switch (v.getId()) {
			case R.id.iv_imagecrop_btnProcess:
				progressDialog = ProgressDialog.show(mContext,
						"Please wait for a second", "Processing");
				thread = new RecognizeThread();
				thread.start();
				break;
			}
		}
		return true;
	}
	@Override
	protected void onPause() {
		if ( progressDialog!=null && progressDialog.isShowing() ){
	        progressDialog.cancel();
	    }
		super.onPause();
	}
}
