package com.uit.ocr;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

public class BaseOCR extends Activity {
	private static final String TAG = "BaseOCR.java";

	public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/OCRTools/";

	public static final String lang = "vie";

	protected String _path = DATA_PATH + "/ocr.jpg";
	private String recognizedResult;
	// tìm email
	private static final String EMAIL = "[a-zA-Z][\\w]+@[\\w]+.[\\w]+(.[\\w]*)";
	// tìm số dt dạng
	private static final String MOBILE = "0[\\d]+[. \\-,\\d]+";
	// tìm tên
	private static final String NAME = "(Lê|LÊ|Trần|TRẦN|Nguyễn|NGUYỄN|Bùi|BÙI|Phạm|PHẠM|Võ|VÕ).+";

	public BaseOCR() {

	}

	String recognizedText;

	public String onHanldeOCR(Bitmap bitmap) {
		try {

			ExifInterface exif = new ExifInterface(_path);
			int exifOrientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			Log.v(TAG, "Orient: " + exifOrientation);

			int rotate = 0;

			switch (exifOrientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			}

			Log.v(TAG, "Rotation: " + rotate);

			if (rotate != 0) {

				// Getting width & height of the given image.
				int w = bitmap.getWidth();
				int h = bitmap.getHeight();

				// Setting pre rotate
				Matrix mtx = new Matrix();
				mtx.preRotate(rotate);

				// Rotating Bitmap
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
			}

			bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

		} catch (IOException e) {
			Log.e(TAG, "Couldn't correct orientation: " + e.toString());
		}

		Log.v(TAG, "Before baseApi");
		try {
			TessBaseAPI baseApi = new TessBaseAPI();
			baseApi.setDebug(true);
			baseApi.init(DATA_PATH, lang);
			baseApi.setImage(bitmap);

			recognizedText = baseApi.getUTF8Text();

			baseApi.end();

			Log.v(TAG, "OCRED TEXT: " + recognizedText);

			/*if (lang.equalsIgnoreCase("eng")) {
				recognizedText = recognizedText
						.replaceAll("[^a-zA-Z0-9]+", " ");
			}*/
			/*if (lang.equalsIgnoreCase("vie")) {
				recognizedText = recognizedText
						.replaceAll(
								"[^AĂÂBCDĐEÊGHIKLMNOÔƠPQRSTUƯVXYÁÀẢÃẠẮẰẲẴẶẤẦẨẪẬÉÈẺẼẸẾỀỂỄỆÓÒỎÕỌỐỒỔỖỘỚỜỞỠỢÚÙỦŨỤỨỪỬỮỰaăâbcdđeêghiklmnoôơpqrstuưvxyáàảãạắằẳẵặấầẩẫậéèẻẽẹếềểễệóòỏõọốồổỗộớờởỡợúùủũụứừửữự0123456789]+",
								" ");
			}*/

			recognizedText = recognizedText.trim();

		} catch (Exception e) {
			// TODO: handle exception
		}
		return recognizedText;
	}

	public String[] onTextAnalisys(String input) {
		String[] result = new String[10];
		String lists[] = { EMAIL, MOBILE, NAME };
		for (int i = 0; i < lists.length; i++) {
			Pattern pattern = Pattern.compile(lists[i]);
			Matcher matcher = pattern.matcher(input);

			switch (i) {
			case 0:
				if (matcher.find()) {
					result[i] = matcher.group();
					Log.i(TAG, "got 0");
				} else
					result[i] = "nothing";
				break;
			case 1:
				if (matcher.find()) {
					result[i] = matcher.group();
					Log.i(TAG, "got 1");
				} else
					result[i] = "nothing";
				break;
			case 2:
				if (matcher.find()) {
					result[i] = matcher.group();
					Log.i(TAG, "got 2");
				} else
					result[i] = "nothing";
				break;
			}
		}
		return result;
	}
}