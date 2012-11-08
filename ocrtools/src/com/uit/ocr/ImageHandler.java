package com.uit.ocr;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.uit.ocr.utils.CustomImageView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable.Factory;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ImageHandler extends BaseOCR implements OnTouchListener {
	private static final String TAG = "ImageCrop.java";
	private ImageView ivImageCropProcess, ivImageCropNext, ivImageCropBack;
	private ImageView ivImageCropMain;

	private String textResult;
	private Bitmap imageForResult;
	private Context mContext = ImageHandler.this;
	RecognizeThread thread;
	ProgressDialog progressDialog;
	private boolean isComplete = false;
	private String[] textAnalisys;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imagehandler);
		ivImageCropMain = (ImageView) findViewById(R.id.custom_image);
		ivImageCropProcess = (ImageView) findViewById(R.id.iv_imagecrop_btnProcess);
		ivImageCropNext = (ImageView) findViewById(R.id.iv_imagecrop_btnNext);
		ivImageCropBack = (ImageView) findViewById(R.id.iv_imagecrop_btnBack);
		
		try {
			imageForResult = onResizeImage(onReceiveData(),500, 500);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ivImageCropMain.setBitmap(imageForResult);

		// hình ảnh sau khi lấy dc
		ivImageCropMain.setImageBitmap(imageForResult);
		ivImageCropProcess.setOnTouchListener(this);
		ivImageCropNext.setOnTouchListener(this);
		ivImageCropBack.setOnTouchListener(this);

	}
	// nhận Uri từ activity trc đó.
	public Uri onReceiveData(){
		Bundle bundle = getIntent().getExtras();
		return Uri.parse(bundle.getString("uriData"));
	}
	// thay đổi size phù hợp với imageview tránh bị tràng bộ nhớ
	public Bitmap onResizeImage(Uri uri,int requestHeight, int requestWidth)
			throws FileNotFoundException {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		
		BitmapFactory.decodeStream(getContentResolver()
				.openInputStream(uri), null, options);
		options.inSampleSize = setInSampleSize(options, requestHeight,
				requestWidth);
		
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeStream(
				getContentResolver().openInputStream(uri), null, options);
	}
	// tìm size thich hợp
	public static int setInSampleSize(BitmapFactory.Options options,
			int requestHeight, int requestWidth) {
		
		int imageHeight = options.outHeight;
		int imageWidth = options.outWidth;
		int inSampleSize = 1;
		
		if (imageHeight > requestHeight || imageWidth > requestWidth) {
			if (imageWidth > imageHeight) {
				inSampleSize = Math.round((float) imageHeight
						/ (float) requestHeight);
			} else {
				inSampleSize = Math.round((float) imageWidth
						/ (float) requestWidth);
			}
		}
		return inSampleSize;
	}
	
	public void onPhotoChosen() {
		textResult = onHanldeOCR(imageForResult);
		Log.i(TAG, "phan tich hinh anh xong");
		textAnalisys = onTextAnalisys(textResult);
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
				isComplete = false;
			}
		};
	}

	Bitmap rotated = null;

	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			switch (v.getId()) {
			case R.id.iv_imagecrop_btnProcess:
				progressDialog = ProgressDialog.show(mContext,
						"Please wait for a second", "Processing");
				thread = new RecognizeThread();
				thread.start();
				break;
			case R.id.iv_imagecrop_btnNext:

				Matrix matrix = new Matrix();
				matrix.postRotate(90);
				try {
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 4;
					rotated = Bitmap.createBitmap(imageForResult, 0, 0,
							imageForResult.getWidth(),
							imageForResult.getHeight(), matrix, true);
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
				}

				ivImageCropMain.setImageBitmap(rotated);
				rotated = null;
				break;
			case R.id.iv_imagecrop_btnBack:
				Matrix matrix2 = new Matrix();
				matrix2.postRotate(-90);
				try {
					rotated = Bitmap.createBitmap(imageForResult, 0, 0,
							imageForResult.getWidth(),
							imageForResult.getHeight(), matrix2, true);
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
				}

				ivImageCropMain.setImageBitmap(rotated);
				rotated = null;
				break;
			}
		}
		return true;
	}

	@Override
	protected void onPause() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.cancel();
		}
		super.onPause();
	}

	
}
