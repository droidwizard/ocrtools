package com.uit.ocr;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
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
	private static final String MOBILE = "0[19][\\d]+[. \\-,\\d]+";
	// tìm tên
	private static final String POPNAME_LOWER = "Nguyễn|Phạm|Lê|Trần|Hoàng|Huỳnh|Phan|Vũ|Võ|Đặng|Bùi|Đỗ|Hồ|Ngô|Dương|Lý";
	private static final String POPNAME_UPPER = "NGUYỄN|PHẠM|LÊ|TRẦN|HOÀNG|HUỲNH|PHAN|VŨ|VÕ|ĐẶNG|BÙI|ĐỖ|HỒ|NGÔ|DƯƠNG|LÝ";

	private static final String RARENAME_LOWER1 = "An|Ánh|Ân|Âu|Ấu|Bá|Bạc|Bạch|Bàng|Bành|Bảo|Bửu|Bế|Bì|Biện|Bồ|Ca|Cái|Cam|Cao|Cát|Cầm|Cấn|Chế|Chiêm|Chu|Châu|Chung|Chương|Chử|Cổ|Cù|Cung|Cự|Dã|Danh|Doãn|Dư";
	private static final String RARENAME_LOWER2 = "Đàm|Đan|Đào|Đậu|Đinh|Điền|Đoàn|Đôn|Đồng|Đổng|Đới|Đái|Đường|Giả|Giao|Giang|Giáp|Hà|Hạ|Hàn|Hán|Hy|Hình|Hoa|Hồng|Hùng|Hứa|Kha|Khương|Khâu|Khưu|Khổng|Khu|Khuất|Khúc|Kiều|Kim|La";
	private static final String RARENAME_LOWER3 = "Lạc|Lại|Lâm|Lều|Liễu|Lò|Lục|Lư|Lô|Lữ|Lã|Lương|Lưu|Ma|Mã|Mạc|Mạch|Mai|Mang|Mẫn|Mộc|Ninh|Nhâm|Nhậm|Nhiệm|Nghiêm|Ngụy|Nhữ|Nông|Ông|Phi|Phí|Phó|Phùng|Phương|Quản|Quách|Sơn|Tạ|Tào";
	private static final String RARENAME_LOWER4 = "Tăng|Thạch|Thái|Thào|Thân|Thập|Thi|Thiều|Thịnh|Thôi|Tiêu|Tòng|Tô|Tôn|Tông|Tống|Trà|Trác|Triệu|Trịnh|Trình|Trưng|Trương|Từ|Uông|Ung|Văn|Vi|Viên|Vương|Xa|Yên";

	private static final String RARENAME_UPPER1 = "AN|ÁNH|ÂN|ÂU|ẤU|BÁ|BẠC|BẠCH|BÀNG|BÀNH|BẢO|BỬU|BẾ|BÌ|BIỆN|BỒ|CA|CÁI|CAM|CAO|CÁT|CẦM|CẤN|CHẾ|CHIÊM|CHU|CHÂU|CHUNG|CHƯƠNG|CHỬ|CỔ|CÙ|CUNG|CỰ|DÃ|DANH|DOÃN|DƯ";
	private static final String RARENAME_UPPER2 = "ĐÀM|ĐAN|ĐÀO|ĐẬU|ĐIỀN|ĐINH|ĐOÀN|ĐÔN|ĐỒNG|ĐỔNG|ĐỚI|ĐÁI|ĐƯỜNG|GIẢ|GIAO|GIANG|GIÁP|HÀ|HẠ|HÀN|HÁN|HY|HÌNH|HOA|HỒNG|HÙNG|HỨA|KHA|KHƯƠNG|KHÂU|KHƯU|KHỔNG|KHU|KHUẤT|KHÚC|KIỀU|KIM|LA";
	private static final String RARENAME_UPPER3 = "LẠC|LẠI|LÂM|LỀU|LIỄU|LÒ|LỤC|LƯ|LÔ|LỮ|LÃ|LƯƠNG|LƯU|MA|MÃ|MẠC|MẠCH|MAI|MANG|MẪN|MỘC|NINH|NHÂM|NHẬM|NHIỆM|NGHIÊM|NGỤY|NHỮ|NÔNG|ÔNG|PHI|PHÍ|PHÓ|PHÙNG|PHƯƠNG|QUẢN|QUÁCH|SƠN|TẠ|TÀO";
	private static final String RARENAME_UPPER4 = "TĂNG|THẠCH|THÁI|THÀO|THÂN|THẬP|THI|THIỀU|THỊNH|THÔI|TIÊU|TÒNG|TÔ|TÔN|TÔNG|TỐNG|TRÀ|TRÁC|TRIỆU|TRỊNH|TRÌNH|TRƯNG|TRƯƠNG|TỪ|UÔNG|UNG|VI|VIÊN|VƯƠNG|XA|YÊN";

	private static final String POS_NAME = "GSTS|PSGTS|TS|Ths|CH|TSKH|CN";

	private static final String NAME = "("+ POS_NAME + "|" + POPNAME_LOWER + "|"
			+ POPNAME_UPPER + "|" + RARENAME_LOWER1 + "|" + RARENAME_LOWER2
			+ "|" + RARENAME_LOWER3 + "|" + RARENAME_LOWER4 + "|"
			+ RARENAME_UPPER1 + "|" + RARENAME_UPPER2 + "|" + RARENAME_UPPER3
			+ "|" + RARENAME_UPPER4 + ").+";


	public BaseOCR() {

	}

	String recognizedText;

	public String onHanldeOCR(Bitmap bitmap) {
		/*try {

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
		}*/

		Log.v(TAG, "Before baseApi");
		try {
			bitmap=toBlackWhite(bitmap);
			bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
			TessBaseAPI baseApi = new TessBaseAPI();
			baseApi.setDebug(true);
			baseApi.init(DATA_PATH, lang);
			baseApi.setImage(bitmap);

			recognizedText = baseApi.getUTF8Text();

			baseApi.end();

			Log.v(TAG, "OCRED TEXT: " + recognizedText);

			/*
			 * if (lang.equalsIgnoreCase("eng")) { recognizedText =
			 * recognizedText .replaceAll("[^a-zA-Z0-9]+", " "); }
			 */
			/*
			 * if (lang.equalsIgnoreCase("vie")) { recognizedText =
			 * recognizedText .replaceAll(
			 * "[^AĂÂBCDĐEÊGHIKLMNOÔƠPQRSTUƯVXYÁÀẢÃẠẮẰẲẴẶẤẦẨẪẬÉÈẺẼẸẾỀỂỄỆÓÒỎÕỌỐỒỔỖỘỚỜỞỠỢÚÙỦŨỤỨỪỬỮỰaăâbcdđeêghiklmnoôơpqrstuưvxyáàảãạắằẳẵặấầẩẫậéèẻẽẹếềểễệóòỏõọốồổỗộớờởỡợúùủũụứừửữự0123456789]+"
			 * , " "); }
			 */

			recognizedText = recognizedText.trim();

		} catch (Exception e) {
			// TODO: handle exception
		}
		return recognizedText;
	}
	// chuyển color image sang blackwhite
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
				} else
					result[i] = "nothing";
				break;
			case 1:
				if (matcher.find()) {
					String tmp = matcher.group();
					tmp = tmp.replaceAll("[- .,]+", "");
					result[i] = tmp;
				} else
					result[i] = "nothing";
				break;
			case 2:
				if (matcher.find()) {
					result[i] = matcher.group();
				} else
					result[i] = "nothing";
				break;
			}
		}
		return result;
	}
}
