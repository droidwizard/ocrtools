package com.uit.ocr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

public class BaseOCR extends Activity {
	private static final String TAG = BaseOCR.class.getSimpleName();

	public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/OCRTools/";

	public static final String lang = "vie";
	private String recognizedText;

	private static final String VIETCHAR = "[a-z A-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ]*";
	// tìm thời gian mời
	// private static final String TIME =
	// "(Thời gian|Vào lúc)"+VIETCHAR+"[: ]*([0-9]|[0-9][0-9])[a-zA-ZờỜ ]+( |[0-9][0-9]|,|-)+([a-zA-ZàÀ ]+([0-9]| )+[ /]([0-9]| )+[ /]([0-9]| )+)|([a-zA-ZàÀ ]+([0-9]| )+[a-zA-ZáÁ ]+([0-9]| )+[a-zA-ZăĂ ]+([0-9]| )+)";

	private static final String DATE = "(0?[1-9]|[12][0-9]|3[01])[- /.]+(0?[1-9]|1[012])[- /.]+((19|20)?[0-9]{2})";
	private static final String DATE2 = "[a-zA-Zngayàáảãắẳẵằằéèẽõóò ]+(0?[1-9]|[12][0-9]|3[01])[a-zA-Zãthngàăáắằẵặíìỉĩi ]+(0?[1-9]|1[012])[a-zA-Znmăáắằẵặã ]+((19|20)?[0-9]{2}).*";

	private static final String TIME = "[Thời gian.]+(0?[1-9]|[12][0-9])([giờHỜ“ h'Ò:]+)([0-9]{2})?";
	// địa điểm
	private static final String PLACE = "(Địa[ điểmễ:]+)(.+)";
	// tìm email
	private static final String EMAIL = "[a-zA-Z][\\w]+@[\\w]+.[\\w]+(.[\\w]*)";
	// tìm số dt dạng
	private static final String MOBILE = "0[19][\\d]+[. \\-,\\d]+";
	// tìm tên
	private static final String POPNAME_LOWER = "Nguyễn| Nguyễn|( Phạm|Phạm)|( Lê |Lê )|( Trần |Trần )|( Hoàng|Hoàng)|( Huỳnh|Huỳnh)|( Phan |Phan )|( Vũ |Vũ )|( Võ |Võ )|( Đặng|Đặng)|( Bùi|Bùi)|( Đỗ |Đỗ )|( Hồ |Hồ )|( Ngô |Ngô )|( Dương|Dương)|( Lý|Lý)";
	private static final String POPNAME_UPPER = "NGUYỄN| NGUYỄN|( PHẠM|PHẠM)|( LÊ |LÊ )|( TRẦN |TRẦN )|( HOÀNG|HOÀNG)|( HUỲNH|HUỲNH)|( PHAN |PHAN )|( VŨ |VŨ )|( VÕ |VÕ )|( ĐẶNG|ĐẶNG)|( BÙI|BÙI)|( ĐỖ |ĐỖ )|( HỒ |HỒ )|( NGÔ |NGÔ )|( DƯƠNG|DƯƠNG)|( LÝ|LÝ)";

	private static final String RARENAME_LOWER1 = " An |( Ánh|Ánh)| Ân | Âu | Ấu |( Bá |Bá )|( Bạc|Bạc)|( Bạch|Bạch)|( Bàng|Bàng)|( Bành|Bành)|( Bảo|Bảo)|( Bửu|Bửu)|( Bế |Bế )|( Bì |Bì )|( Biện |Biện )|( Bồ |Bồ )|( Ca |Ca )|( Cái|Cái)|( Cam|Cam)|( Cao|Cao)|( Cát|Cát)|( Cầm|Cầm)|( Cấn|Cấn)|( Chế |Chế )|( Chiêm|Chiêm)|( Chu |Chu )|( Châu|Châu)|( Chung|Chung)|( Chương|Chương)|( Chử |Chử )|( Cổ|Cổ)|( Cù |Cù )|( Cung|Cung)|( Cự |Cự )|( Dã |Dã )|( Danh|Danh)|( Doãn|Doãn)|( Dư |Dư )";
	private static final String RARENAME_LOWER2 = "( Đàm|Đàm)|( Đan |Đan )|( Đào|Đào)|( Đậu|Đậu)|( Đinh|Đinh)|( Điền |Điền )|( Đoàn |Đoàn )|( Đôn |Đôn )|( Đồng|Đồng)|( Đổng|Đổng)|( Đới|Đới)|( Đái|Đái)|( Đường|Đường)|( Giả |Giả )|( Giao|Giao)|( Giang|Giang)|( Giáp|Giáp)|( Hà |Hà )|( Hạ |Hạ )|( Hàn |Hàn )|( Hán |Hán )|( Hy |Hy )|( Hình|Hình)|( Hoa|Hoa)|( Hồng|Hồng)|( Hùng|Hùng)|( Hứa|Hứa)|( Kha |Kha )|( Khương|Khương)|( Khâu|Khâu)|( Khưu|Khưu)|( Khổng|Khổng)|( Khu |Khu )|( Khuất|Khuất)|( Khúc|Khúc)|( Kiều|Kiều)|( Kim|Kim)|( La |La )";
	private static final String RARENAME_LOWER3 = "Lạc| Lạc|Lại| Lại|Lâm| Lâm|Lều| Lều|Liễu| Liễu|Lò | Lò |Lục| Lục|Lư | Lư |Lô | Lô |Lữ | Lữ |Lã | Lã |Lương| Lương|Lưu| Lưu|Ma | Ma |Mã | Mã |Mạc | Mạc |Mạch| Mạch|Mai| Mai|Mang| Mang|Mẫn | Mẫn |Mộc| Mộc|Ninh| Ninh|Nhâm| Nhâm|Nhậm| Nhậm|Nhiệm| Nhiệm|Nghiêm| Nghiêm|Ngụy| Ngụy|Nhữ | Nhữ |Nông| Nông| Ông| Ông |Phi | Phi |Phí | Phí |Phó | Phó |Phùng| Phùng|Phương| Phương|Quản | Quản |Quách| Quách|Sơn | Sơn |Tạ | Tạ |Tào| Tào";
	private static final String RARENAME_LOWER4 = "Tăng| Tăng|Thạch| Thạch|Thái| Thái|Thào| Thào|Thân | Thân |Thập| Thập|Thi | Thi |Thiều| Thiều|Thịnh| Thịnh|Thôi| Thôi|Tiêu| Tiêu|Tòng| Tòng|Tô | Tô |Tôn | Tôn |Tông| Tông|Tống| Tống|Trà | Trà |Trác | Trác |Triệu| Triệu|Trịnh| Trịnh|Trình| Trình|Trưng| Trưng|Trương| Trương|Từ | Từ | Uông| Uông | Ung| Ung |Văn | Văn |Vi | Vi |Viên | Viên |Vương| Vương|Xa | Xa | Yên ";

	private static final String RARENAME_UPPER1 = " AN | ÁNH| ÂN | ẤU |BÁ | BÁ |BẠC| BẠC|BẠCH| BẠCH|BÀNG| BÀNG|BÀNH| BÀNH|BẢO| BẢO|BỬU| BỬU|BẾ | BẾ |BÌ | BÌ |BIỆN | BIỆN |BỒ | BỒ |CA | CA |CÁI| CÁI|CAM| CAM|CAO| CAO|CÁT| CÁT|CẦM| CẦM|CẤN| CẤN|CHẾ | CHẾ |CHIÊM| CHIÊM|CHU | CHU |CHÂU| CHÂU|CHUNG| CHUNG|CHƯƠNG| CHƯƠNG|CHỬ | CHỬ |CỔ | CỔ |CÙ | CÙ |CUNG| CUNG|CỰ | CỰ |DÃ | DÃ |DANH| DANH|DOÃN| DOÃN|DƯ | DƯ ";
	private static final String RARENAME_UPPER2 = "ĐÀM| ĐÀM|ĐAN | ĐAN |ĐÀO| ĐÀO|ĐẬU| ĐẬU|ĐIỀN| ĐIỀN|ĐINH| ĐINH|ĐOÀN | ĐOÀN |ĐÔN | ĐÔN |ĐỒNG| ĐỒNG|ĐỔNG| ĐỔNG|ĐỚI| ĐỚI|ĐÁI| ĐÁI|ĐƯỜNG| ĐƯỜNG|GIẢ | GIẢ |GIAO| GIAO|GIANG| GIANG|GIÁP| GIÁP|HÀ | HÀ |HẠ | HẠ |HÀN | HÀN |HÁN | HÁN |HY | HY |HÌNH| HÌNH|HOA| HOA|HỒNG| HỒNG|HÙNG| HÙNG|HỨA| HỨA|KHA | KHA |KHƯƠNG| KHƯƠNG|KHÂU| KHÂU|KHƯU| KHƯU|KHỔNG| KHỔNG|KHU | KHU |KHUẤT| KHUẤT|KHÚC| KHÚC|KIỀU| KIỀU|KIM| KIM|LA | LA ";
	private static final String RARENAME_UPPER3 = "LẠC| LẠC|LẠI| LẠI|LÂM| LÂM|LỀU| LỀU|LIỄU| LIỄU|LÒ | LÒ |LỤC| LỤC|LƯ | LƯ |LÔ | LÔ |LỮ | LỮ |LÃ | LÃ |LƯƠNG| LƯƠNG|LƯU| LƯU|MA | MA |MÃ | MÃ |MẠC | MẠC |MẠCH| MẠCH|MAI| MAI|MANG| MANG|MẪN | MẪN |MỘC| MỘC|NINH| NINH|NHÂM| NHÂM|NHẬM| NHẬM|NHIỆM| NHIỆM|NGHIÊM| NGHIÊM|NGỤY| NGỤY|NHỮ | NHỮ |NÔNG| NÔNG| ÔNG| ÔNG |PHI | PHI |PHÍ | PHÍ |PHÓ | PHÓ |PHÙNG| PHÙNG|PHƯƠNG| PHƯƠNG|QUẢN | QUẢN |QUÁCH| QUÁCH|SƠN| SƠN |TẠ | TẠ |TÀO| TÀO";
	private static final String RARENAME_UPPER4 = "TĂNG| TĂNG|THẠCH| THẠCH|THÁI| THÁI|THÀO| THÀO|THÂN | THÂN |THẬP| THẬP|THI | THI |THIỀU| THIỀU|THỊNH| THỊNH|THÔI| THÔI|TIÊU| TIÊU|TÒNG| TÒNG|TÔ | TÔ |TÔN | TÔN |TÔNG| TÔNG|TỐNG| TỐNG|TRÀ | TRÀ |TRÁC | TRÁC |TRIỆU| TRIỆU|TRỊNH| TRỊNH|TRÌNH| TRÌNH|TRƯNG| TRƯNG|TRƯƠNG| TRƯƠNG|TỪ | TỪ | UÔNG| UÔNG | UNG| UNG |VI | VI |VIÊN | VIÊN |VƯƠNG| VƯƠNG|XA | XA | YÊN ";

	/*
	 * private static final String POPNAME_LOWER =
	 * "( Nguyễn|Nguyễn)|( Phạm|Phạm)|( Lê|Lê)|( Trần|Trần)|( Hoàng|Hoàng)|( Huỳnh|Huỳnh)|( Phan|Phan)|( Vũ|Vũ)|( Võ|Võ)|( Đặng|Đặng)|( Bùi|Bùi)|( Đỗ|Đỗ)|( Hồ|Hồ)|( Ngô|Ngô)|( Dương|Dương)|( Lý|Lý)"
	 * ; private static final String POPNAME_UPPER =
	 * "( NGUYỄN|NGUYỄN)|( PHẠM|PHẠM)|( LÊ|LÊ)|( TRẦN|TRẦN)|( HOÀNG|HOÀNG)|( HUỲNH|HUỲNH)|( PHAN|PHAN)|( VŨ|VŨ)|( VÕ|VÕ)|( ĐẶNG|ĐẶNG)|( BÙI|BÙI)|( ĐỖ|ĐỖ)|( HỒ|HỒ)|( NGÔ|NGÔ)|( DƯƠNG|DƯƠNG)|( LÝ|LÝ)"
	 * ;
	 * 
	 * private static final String RARENAME_LOWER1 =
	 * "( An|An)|( Ánh|Ánh)|( Ân|Ân)|( Âu|Âu)|( Ấu|Ấu)|( Bá|Bá)|( Bạc|Bạc)|( Bạch|Bạch)|( Bàng|Bàng)|( Bành|Bành)|( Bảo|Bảo)|( Bửu|Bửu)|( Bế|Bế)|( Bì|Bì)|( Biện|Biện)|( Bồ|Bồ)|( Ca|Ca)|( Cái|Cái)|( Cam|Cam)|( Cao|Cao)|( Cát|Cát)|( Cầm|Cầm)|Cấn|Chế|Chiêm|Chu|Châu|Chung|Chương|Chử|Cổ|Cù|Cung|Cự|Dã|Danh|Doãn|Dư"
	 * ;
	 * 
	 * private static final String RARENAME_LOWER2 =
	 * "Đàm|Đan|Đào|Đậu|Đinh|Điền|Đoàn|Đôn|Đồng|Đổng|Đới|Đái|Đường|Giả|Giao|Giang|Giáp|Hà|Hạ|Hàn|Hán|Hy|Hình|Hoa|Hồng|Hùng|Hứa|Kha|Khương|Khâu|Khưu|Khổng|Khu|Khuất|Khúc|Kiều|Kim|La"
	 * ; private static final String RARENAME_LOWER3 =
	 * "Lạc|Lại|Lâm|Lều|Liễu|Lò|Lục|Lư|Lô|Lữ|Lã|Lương|Lưu|Ma|Mã|Mạc|Mạch|Mai|Mang|Mẫn|Mộc|Ninh|Nhâm|Nhậm|Nhiệm|Nghiêm|Ngụy|Nhữ|Nông|( Ông|Ông)|Phi|Phí|Phó|Phùng|Phương|Quản|Quách|Sơn|Tạ|Tào"
	 * ; private static final String RARENAME_LOWER4 =
	 * "Tăng|Thạch|Thái|Thào|Thân|Thập|Thi|Thiều|Thịnh|Thôi|Tiêu|Tòng|Tô|Tôn|Tông|Tống|Trà|Trác|Triệu|Trịnh|Trình|Trưng|Trương|Từ|Uông|Ung|Văn|Vi|Viên|Vương|Xa|\\nYên"
	 * ;
	 * 
	 * private static final String RARENAME_UPPER1 =
	 * "AN|ÁNH|ÂN|\\nÂU|ẤU|BÁ|BẠC|BẠCH|BÀNG|BÀNH|BẢO|BỬU|BẾ|BÌ|BIỆN|BỒ|CA|CÁI|CAM|CAO|CÁT|CẦM|CẤN|CHẾ|CHIÊM|CHU|CHÂU|CHUNG|CHƯƠNG|CHỬ|CỔ|CÙ|CUNG|CỰ|DÃ|DANH|DOÃN|DƯ"
	 * ; private static final String RARENAME_UPPER2 =
	 * "ĐÀM|ĐAN|ĐÀO|ĐẬU|ĐIỀN|ĐINH|ĐOÀN|ĐÔN|ĐỒNG|ĐỔNG|ĐỚI|ĐÁI|ĐƯỜNG|GIẢ|GIAO|GIANG|GIÁP|HÀ|HẠ|HÀN|HÁN|HY|HÌNH|HOA|HỒNG|HÙNG|HỨA|KHA|KHƯƠNG|KHÂU|KHƯU|KHỔNG|KHU|KHUẤT|KHÚC|KIỀU|KIM|LA"
	 * ; private static final String RARENAME_UPPER3 =
	 * "LẠC|LẠI|LÂM|LỀU|LIỄU|LÒ|LỤC|LƯ|LÔ|LỮ|LÃ|LƯƠNG|LƯU|MA|MÃ|MẠC|MẠCH|MAI|MANG|MẪN|MỘC|NINH|NHÂM|NHẬM|NHIỆM|NGHIÊM|NGỤY|NHỮ|NÔNG| ÔNG|PHI|PHÍ|PHÓ|PHÙNG|PHƯƠNG|QUẢN|QUÁCH|SƠN|TẠ|TÀO"
	 * ; private static final String RARENAME_UPPER4 =
	 * "TĂNG|THẠCH|THÁI|THÀO|THÂN|THẬP|THI|THIỀU|THỊNH|THÔI|TIÊU|TÒNG|TÔ|TÔN|TÔNG|TỐNG|TRÀ|TRÁC|TRIỆU|TRỊNH|TRÌNH|TRƯNG|TRƯƠNG|TỪ|UÔNG|UNG|VI|VIÊN|VƯƠNG|XA|\\nYÊN"
	 * ;
	 */

	private static final String POS_NAME = "(GSTS|PSGTS|TS|Ths|CH|TSKH|CN|Dược Sĩ|Bác Sĩ).+";

	private static final String NAME = "(" + POS_NAME + "|" + POPNAME_LOWER
			+ "|" + POPNAME_UPPER + "|" + RARENAME_LOWER1 + "|"
			+ RARENAME_LOWER2 + "|" + RARENAME_LOWER3 + "|" + RARENAME_LOWER4
			+ "|" + RARENAME_UPPER1 + "|" + RARENAME_UPPER2 + "|"
			+ RARENAME_UPPER3 + "|" + RARENAME_UPPER4 + ").+";

	public BaseOCR() {
	}

	public String onHanldeOCR(Bitmap bitmap) {
		/*
		 * try {
		 * 
		 * ExifInterface exif = new ExifInterface(_path); int exifOrientation =
		 * exif.getAttributeInt( ExifInterface.TAG_ORIENTATION,
		 * ExifInterface.ORIENTATION_NORMAL);
		 * 
		 * Log.v(TAG, "Orient: " + exifOrientation);
		 * 
		 * int rotate = 0;
		 * 
		 * switch (exifOrientation) { case ExifInterface.ORIENTATION_ROTATE_90:
		 * rotate = 90; break; case ExifInterface.ORIENTATION_ROTATE_180: rotate
		 * = 180; break; case ExifInterface.ORIENTATION_ROTATE_270: rotate =
		 * 270; break; }
		 * 
		 * Log.v(TAG, "Rotation: " + rotate);
		 * 
		 * if (rotate != 0) {
		 * 
		 * // Getting width & height of the given image. int w =
		 * bitmap.getWidth(); int h = bitmap.getHeight();
		 * 
		 * // Setting pre rotate Matrix mtx = new Matrix();
		 * mtx.preRotate(rotate);
		 * 
		 * // Rotating Bitmap bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h,
		 * mtx, false); }
		 * 
		 * bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
		 * 
		 * } catch (IOException e) { Log.e(TAG, "Couldn't correct orientation: "
		 * + e.toString()); }
		 */

		Log.v(TAG, "Before baseApi");
		try {
			if (MainActivity.is2ColorImage == true) {
				bitmap = toBlackWhite(bitmap);
			}
			bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
			TessBaseAPI baseApi = new TessBaseAPI();
			baseApi.setDebug(true);
			baseApi.init(DATA_PATH, lang);
			baseApi.setImage(bitmap);

			recognizedText = baseApi.getUTF8Text();

			baseApi.end();

			Log.v(TAG, "OCRED TEXT: " + recognizedText);

			if (MainActivity.isCharFilter == true) {
				if (lang.equalsIgnoreCase("vie")) {
					recognizedText = recognizedText
					/*
					 * .replaceAll(
					 * "[^AĂÂBCDĐEÊGHIÍÌỈĨỊKLMNOÔƠPQRSTUƯVXYÝỲỶỸÁÀẢÃẠẮẰẲẴẶẤẦẨẪẬÉÈẺẼẸẾỀỂỄỆÓÒỎÕỌỐỒỔỖỘỚỜỞỠỢÚÙỦŨỤỨỪỬỮỰaăâbcdđeêghiíìỉĩịklmnoôơpqrstuưvxyýỳỷỹáàảãạắằẳẵặấầẩẫậéèẻẽẹếềểễệóòỏõọốồổỗộớờởỡợúùủũụứừửữự0123456789@:.,/\\\\n\\-_()\\^]+"
					 * , " ");
					 */
					.replace("[\\d]11[\\d]", "[\\d]h[\\d]");
				}
			}

			recognizedText = recognizedText.trim();

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
		String lists[] = { TIME, DATE, DATE2, PLACE };
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
					result[7] = "location";
				break;
			}
		}
		return result;
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
