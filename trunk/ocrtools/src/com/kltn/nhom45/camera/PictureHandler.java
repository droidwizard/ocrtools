package com.kltn.nhom45.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.kltn.nhom45.ocrtools.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class PictureHandler implements PictureCallback {
	private static final String TAG = "PictureHandler.java";
	@SuppressWarnings("unused")
	private Context mContext;
	private File mediaFile = null;
	public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/OCRTools/images/";

	public PictureHandler(Context context) {
		mContext = context;
	}

	public void onPictureTaken(byte[] data, Camera camera) {

		File emptyPictureFile = getOutputMediaFile();
		// ktra tạo file rỗng thành công hay ko.
		if (emptyPictureFile == null) {
			Log.e(TAG, String.valueOf(R.string.picturehandler_checksdcard));
			return;
		}
		try {
			// ghi dữ liệu vào file rỗng
			FileOutputStream fos = new FileOutputStream(emptyPictureFile);
			fos.write(data);
			fos.close();
			// cập nhật folder image vào gallery app
			mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
					.parse("file://"
							+ Environment.getExternalStorageDirectory())));
			/*
			 * ExifInterface exif = new
			 * ExifInterface(emptyPictureFile.getName()); int tmp =
			 * exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
			 * ExifInterface.ORIENTATION_NORMAL);
			 * 
			 * int rotationInDegrees = exifToDegrees(tmp); Matrix matrix = new
			 * Matrix(); if (tmp != 0f) { matrix.preRotate(rotationInDegrees); }
			 * Bitmap adjustedBitmap = Bitmap.createBitmap(
			 * BitmapFactory.decodeFile(emptyPictureFile.getPath()), 0, 0, 640,
			 * 480, matrix, true);
			 */

		} catch (FileNotFoundException e) {
			Log.e(TAG,
					String.valueOf(R.string.picturehandler_checkfile)
							+ e.getMessage());
		} catch (IOException e) {
			Log.e(TAG,
					String.valueOf(R.string.picturehandler_accessfile)
							+ e.getMessage());
		}
		camera.startPreview();
	}

	private static int exifToDegrees(int exifOrientation) {
		if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
			return 90;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
			return 180;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
			return 270;
		}
		return 0;
	}

	// tạo file rỗng dùng để lưu hình
	private File getOutputMediaFile() {
		// ktra có thẻ nhớ hay ko
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// tạo thời gian chụp hình
			String timeTmp = new SimpleDateFormat("yyyyMMdd_HHmmss")
					.format(new Date());
			// tạo file hình ảnh rỗng
			mediaFile = new File(DATA_PATH + "IMG_" + timeTmp + ".jpg");
			return mediaFile;
		} else {
			Log.e(TAG, String.valueOf(R.string.picturehandler_nosdcard));
			return mediaFile;
		}
	}

}
