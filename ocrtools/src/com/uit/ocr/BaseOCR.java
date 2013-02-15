package com.uit.ocr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.uit.ocr.utils.Consts;

public class BaseOCR extends Activity {
	private static final String TAG = BaseOCR.class.getSimpleName();

	private String recognizedText;
	
	public static String lang = "vie";
	
	private static final String VIETCHAR = "[a-z A-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ]*";

	private static final String VIETCHAR2= "[a-z A-ZÁÀẢÃẠẤẦẨẪẬÂĂẮẰẲẴẶáàảảãạâăấầẩẫậắằẳẵặéèẻẽẹêếềểễệÉÈẺẼẸẾỀỂỄỆíìỉĩịÍÌỈĨỊÚÙỦŨỤỨỪỬỮỰúùủũụứừửữựóòỏõọôốồổỗộơớờởỡợÓÒỎÕỌỐỒỔỖỘƠỚỜỞỠỢÝỲỶỸỴýỳỷỹỵĐđ]*";
	
	public BaseOCR() {
	}

	public String onHanldeOCR(Bitmap bitmap) {

		Log.v(TAG, "Before baseApi");
		try {
			if (MainActivity.is2ColorImage == true) {
				bitmap = toBlackWhite(bitmap);
			}
			bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
			TessBaseAPI baseApi = new TessBaseAPI();
			baseApi.setDebug(true);
			baseApi.init(Consts.DATA_PATH, Consts.DATA_VIETNAM);
			baseApi.setImage(bitmap);

			recognizedText = baseApi.getUTF8Text();

			baseApi.end();

			Log.v(TAG, "OCRED TEXT: " + recognizedText);

			if (MainActivity.isCharFilter == true) {
				//if (Consts.DATA_VIETNAM.equalsIgnoreCase("vie")) {
					recognizedText = recognizedText.replaceAll("[:,?></\\;`~'\".!#$%^&*)(}{][+=|]*", "");
				//}
			}

			recognizedText = recognizedText.trim();
			Log.v(TAG, "AFTER TEXT: " + recognizedText);

		} catch (Exception e) {
		}
		return recognizedText;
	}

	// chuyển color image sang blackwhite
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

	public String[] onInvitationAnalisys(String input) {
		String[] result = new String[10];
		String lists[] = { Consts.TIME, Consts.DATE, Consts.DATE2, Consts.PLACE };
		for (int i = 0; i < lists.length; i++) {
			Pattern pattern = Pattern.compile(lists[i]);
			Matcher matcher = pattern.matcher(input);

			switch (i) {
			case 0:
				if (matcher.find()) {
					if (matcher.group(3) == null || matcher.group(3).equals("")) {
						result[0] = matcher.group(1) + ":" + "00";
					} else
						result[0] = matcher.group(1) + ":" + matcher.group(3);
				} else
					result[0] = "8:00";
				break;
			case 1:
				if (matcher.find()) {
					result[1] = matcher.group(1);
					result[2] = matcher.group(2);
					result[3] = matcher.group(3);
					Log.i(TAG, result[1] + "/" + result[2] + "/" + result[3]);
				} else {
					result[1] = "1";
					result[2] = "1";
					result[3] = "2012";
				}
				break;
			case 2:
				if (matcher.find()) {
					result[4] = matcher.group(1);
					result[5] = matcher.group(2);
					result[6] = matcher.group(3);
					Log.i(TAG, result[4] + "/" + result[5] + "/" + result[6]);
				} else {
					result[4] = "1";
					result[5] = "1";
					result[6] = "2012";
				}
				break;
			case 3:
				if (matcher.find()) {
					result[7] = matcher.group(2);
				} else
					result[7] = " ";
				break;
			}
		}
		return result;
	}

	public String[] onNameCardAnalisys(String input) {
		String[] result = new String[10];
		String lists[] = { Consts.EMAIL, Consts.MOBILE, Consts.NAME };
		for (int i = 0; i < lists.length; i++) {
			Pattern pattern = Pattern.compile(lists[i]);
			Matcher matcher = pattern.matcher(input);

			switch (i) {
			case 0:
				if (matcher.find()) {
					result[i] = matcher.group();
				} else
					result[i] = " ";
				break;
			case 1:
				if (matcher.find()) {
					String tmp = matcher.group();
					tmp = tmp.replaceAll("[- .,]+", "");
					result[i] = tmp;
				} else
					result[i] = " ";
				break;
			case 2:
				if (matcher.find()) {
					result[i] = matcher.group();
				} else
					result[i] = " ";
				break;
			}
		}
		return result;
	}
}
