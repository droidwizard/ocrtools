package com.uit.ocr.utils;

import android.os.Environment;

public class Consts {

	public static final int MODE_NONE = 0;
	public static final int MODE_NAMECARD = 1;
	public static final int MODE_INVITATION = 2;
	public static final String ROOT_NAME = "OCRTools";
	public static final String IMAGE_NAME = "images";
	public static final String TESSDATA_NAME = "tessdata";
	public static final String SETTINGS_TITLE_EVENTS = "titleEvents";
	public static final String DEFAULT_EVENT_1 = "Hội họp";
	public static final String DEFAULT_EVENT_2 = "Tiệc cưới";
	public static final String DEFAULT_EVENT_3 = "Sinh nhật";
	
	public static final String TEXTBASERESULT="textBaseResult";
	public static final String TEXTRESULT="textResult";
	public static final String TEXTANALISYS="textAnalisys";
	public static final String URIDATA="uriData";

	public static final String DATA_ENGLISH = "eng";
	public static final String DATA_GERMAN = "deu";
	public static final String DATA_DUTCH = "nld";
	public static final String DATA_KOREAN = "kor";
	public static final String DATA_INDONESIA = "ind";
	public static final String DATA_ITALIA = "ita";
	public static final String DATA_MALAYSIA = "msa";
	public static final String DATA_RUSSIA = "rus";
	public static final String DATA_JAPAN = "jpn";
	public static final String DATA_FRANCE = "fra";
	public static final String DATA_SPAIN = "spa";
	public static final String DATA_THAILAND = "tha";
	public static final String DATA_CHINA = "chi_tra";
	public static final String DATA_VIETNAM = "vie";

	public static final String LANG_ENGLISH = "Anh";
	public static final String LANG_GERMAN = "Đức";
	public static final String LANG_DUTCH = "Hà Lan";
	public static final String LANG_KOREAN = "Hàn Quốc";
	public static final String LANG_INDONESIA = "Indonesia";
	public static final String LANG_ITALIA = "Italia";
	public static final String LANG_MALAYSIA = "Malaysia";
	public static final String LANG_RUSSIA = "Nga";
	public static final String LANG_JAPAN = "Nhật Bản";
	public static final String LANG_FRANCE = "Pháp";
	public static final String LANG_SPAIN = "Tây Ban Nha";
	public static final String LANG_THAILAND = "Thái Lan";
	public static final String LANG_CHINA = "Trung Quốc";
	public static final String LANG_VIETNAM = "Việt Nam";

	public static final String TRANS_ENGLISH = "en";
	public static final String TRANS_GERMAN = "de";
	public static final String TRANS_DUTCH = "nl";
	public static final String TRANS_KOREAN = "ko";
	public static final String TRANS_INDONESIA = "id";
	public static final String TRANS_ITALIA = "it";
	public static final String TRANS_MALAYSIA = "ms";
	public static final String TRANS_RUSSIA = "ru";
	public static final String TRANS_JAPAN = "ja";
	public static final String TRANS_FRANCE = "fr";
	public static final String TRANS_SPAIN = "es";
	public static final String TRANS_THAILAND = "th";
	public static final String TRANS_CHINA = "zh-TW";
	public static final String TRANS_VIETNAM = "vi";

	public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/OCRTools/";

	public static final String ROOT_PATH = Environment
			.getExternalStorageDirectory().toString()
			+ "/"
			+ Consts.ROOT_NAME
			+ "/";

	public static final String DATE = "(0?[1-9]|[12][0-9]|3[01])[- /.]+(0?[1-9]|1[012])[- /.]+((19|20)?[0-9]{2})";
	public static final String DATE2 = "[a-zA-Zngayàáảãắẳẵằằéèẽõóò ]+(0?[1-9]|[12][0-9]|3[01])[a-zA-Zãthngàăáắằẵặíìỉĩi ]+(0?[1-9]|1[012])[a-zA-Znmăáắằẵặã ]+((19|20)?[0-9]{2}).*";

	public static final String TIME = "[Thời gian.]+(0?[1-9]|[12][0-9])([giờHỜ“ h'Ò:]+)([0-9]{2})?";
	// địa điểm
	public static final String PLACE = "(Địa[ điểmễ:]+)(.+)";
	// tìm email
	public static final String EMAIL = "[a-zA-Z][\\w]+@[\\w]+.[\\w]+(.[\\w]*)";
	// tìm số dt dạng
	public static final String MOBILE = "0[19][\\d]+[. \\-,\\d]+";
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

	public static final String POS_NAME = "(GSTS|PSGTS|TS|Ths|CH|TSKH|CN|Dược Sĩ|Bác Sĩ).+";

	public static final String NAME = "(" + POS_NAME + "|" + POPNAME_LOWER
			+ "|" + POPNAME_UPPER + "|" + RARENAME_LOWER1 + "|"
			+ RARENAME_LOWER2 + "|" + RARENAME_LOWER3 + "|" + RARENAME_LOWER4
			+ "|" + RARENAME_UPPER1 + "|" + RARENAME_UPPER2 + "|"
			+ RARENAME_UPPER3 + "|" + RARENAME_UPPER4 + ").+";
}
