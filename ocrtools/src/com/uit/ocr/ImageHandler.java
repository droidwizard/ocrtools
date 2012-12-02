package com.uit.ocr;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.uit.ocr.utils.CustomImageView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;


public class ImageHandler extends BaseOCR implements OnTouchListener {
	private static final String TAG = "ImageCrop.java";
	private ImageView ivImageCropProcess, ivImageCropNext, ivImageCropBack;
	private ImageView ivImageCropMain;

	private String textResult;
	public static Bitmap imageForResult;
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
			imageForResult = onResizeImage(onReceiveImage(), 500, 500);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ivImageCropMain.setBitmap(imageForResult);

		if (MainActivity.is2ColorImage==true){
			imageForResult=toBlackWhite(imageForResult);
		}
		
		// hình ảnh sau khi lấy dc
		ivImageCropMain.setImageBitmap(imageForResult);
		ivImageCropProcess.setOnTouchListener(this);
		ivImageCropNext.setOnTouchListener(this);
		ivImageCropBack.setOnTouchListener(this);

	}
	
	public static Bitmap toBlackWhite(Bitmap bitmap)
	{        
	    int height = bitmap.getHeight();
	    int width = bitmap.getWidth();    
	    Bitmap bitmapBlackWhite = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
	    Canvas c = new Canvas(bitmapBlackWhite);
	    Paint paint = new Paint();
	    ColorMatrix cm = new ColorMatrix();
	    cm.setSaturation(0);
	    ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
	    paint.setColorFilter(f);
	    c.drawBitmap(bitmap, 0, 0, paint);
	    return bitmapBlackWhite;
	}
	// nhận Uri từ activity trc đó.
	public Uri onReceiveImage() {
		Bundle bundle = getIntent().getExtras();
		return Uri.parse(bundle.getString("uriData"));
	}

	// thay đổi size phù hợp với imageview tránh bị tràng bộ nhớ
	public Bitmap onResizeImage(Uri uri, int requestHeight, int requestWidth)
			throws FileNotFoundException {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeStream(getContentResolver().openInputStream(uri),
				null, options);
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
		if (MainActivity.isNameCard){
			textAnalisys = onTextAnalisys(textResult);
		}else{
			textAnalisys=onInvitationAnalisys(textResult);
		}
		isComplete = true;
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
				if (MainActivity.isNameCard){
					Intent i = new Intent(mContext, InputImageResult.class);
					i.putExtra("textResult", textResult);
					i.putExtra("textAnalisys", textAnalisys);
					startActivity(i);
				}else{
					Intent i = new Intent(mContext, InvitationResult.class);
					i.putExtra("textResult", textResult);
					i.putExtra("textAnalisys", textAnalisys);
					startActivity(i);
				}
				isComplete = false;
			}
		};
	}
	// hàm xoay hình bằng matran
	private Bitmap onImageRotation(Bitmap bitmap, float degrees) {
		Matrix matrix = new Matrix();
		matrix.postRotate(degrees);
		try {
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, true);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		return bitmap;
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
			case R.id.iv_imagecrop_btnNext:

				imageForResult=onImageRotation(imageForResult, 90);
				ivImageCropMain.setImageBitmap(imageForResult);
				break;
			case R.id.iv_imagecrop_btnBack:

				imageForResult=onImageRotation(imageForResult, -90);
				ivImageCropMain.setImageBitmap(imageForResult);
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
