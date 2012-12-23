package com.uit.ocr;

import java.io.FileNotFoundException;
import com.uit.ocr.utils.Consts;
import com.uit.ocr.utils.CustomImageView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class ImageHandler extends BaseOCR implements OnTouchListener {
	private static final String TAG = ImageHandler.class.getSimpleName();
	private Context mContext = ImageHandler.this;
	
	private ImageView ivImageHandlerProcess, ivImageHandlerNext, ivImageHandlerBack;
	private CustomImageView ivImageCropMain;
	private ProgressDialog progressDialog;
	private String textResult;
	public static Bitmap inputImage;
	private boolean isComplete = false;
	private String[] textAnalisys;

	SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imagehandler);
		ivImageCropMain = (CustomImageView) findViewById(R.id.iv_custom_image);
		ivImageHandlerProcess = (ImageView) findViewById(R.id.iv_imagehandler_btnProcess);
		ivImageHandlerNext = (ImageView) findViewById(R.id.iv_imagehandler_btnNext);
		ivImageHandlerBack = (ImageView) findViewById(R.id.iv_imagehandler_btnBack);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		

		try {
			inputImage = onResizeImage(onReceiveImage(), 500, 500);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ivImageCropMain.setBitmap(imageForResult);

		if (MainActivity.is2ColorImage == true) {
			inputImage = toBlackWhite(inputImage);
		}

		// hình ảnh sau khi lấy dc
		ivImageCropMain.setImageBitmap(inputImage);
		ivImageCropMain.setBitmap(inputImage);
		ivImageHandlerProcess.setOnTouchListener(this);
		ivImageHandlerNext.setOnTouchListener(this);
		ivImageHandlerBack.setOnTouchListener(this);

	}

	public static Bitmap toBlackWhite(Bitmap bitmap) {
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();
		Bitmap bitmapBlackWhite = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
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
		Bitmap mBitmap = ivImageCropMain.cropBitmap();
		textResult = onHanldeOCR(mBitmap);
		switch (MainActivity.mode) {
		case Consts.MODE_NONE:
			
			break;
		case Consts.MODE_NAMECARD:
			textAnalisys = onNameCardAnalisys(textResult);
			break;
		case Consts.MODE_INVITATION:
			textAnalisys = onInvitationAnalisys(textResult);
			break;

		default:
			break;
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
				Intent mIntent;
				switch (MainActivity.mode) {
				case Consts.MODE_NONE:
					mIntent = new Intent(mContext, NormalResult.class);
					mIntent.putExtra("textBaseResult", textResult);
					startActivity(mIntent);
					break;
				case Consts.MODE_NAMECARD:
					mIntent = new Intent(mContext, InputImageResult.class);
					mIntent.putExtra("textResult", textResult);
					mIntent.putExtra("textAnalisys", textAnalisys);
					startActivity(mIntent);
					break;
				case Consts.MODE_INVITATION:
					mIntent = new Intent(mContext, InvitationResult.class);
					mIntent.putExtra("textResult", textResult);
					mIntent.putExtra("textAnalisys", textAnalisys);
					startActivity(mIntent);
					break;

				default:
					break;
				}
				/*if (MainActivity.isNameCard){
=======
				if (MainActivity.isNameCard) {
>>>>>>> .r26
					Intent i = new Intent(mContext, InputImageResult.class);
					i.putExtra("textResult", textResult);
					i.putExtra("textAnalisys", textAnalisys);
					startActivity(i);
				} else {
					Intent i = new Intent(mContext, InvitationResult.class);
					i.putExtra("textResult", textResult);
					i.putExtra("textAnalisys", textAnalisys);
					startActivity(i);
				}*/
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
			case R.id.iv_imagehandler_btnProcess:
				progressDialog = ProgressDialog.show(mContext,
						"Vui lòng đợi", "Đang xử lý...");
				RecognizeThread thread = new RecognizeThread();
				thread.start();
				break;
			case R.id.iv_imagehandler_btnNext:

				inputImage = onImageRotation(inputImage, 90);
				ivImageCropMain.setImageBitmap(inputImage);
				ivImageCropMain.setBitmap(inputImage);
				break;
			case R.id.iv_imagehandler_btnBack:

				inputImage = onImageRotation(inputImage, -90);
				ivImageCropMain.setImageBitmap(inputImage);
				ivImageCropMain.setBitmap(inputImage);
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
