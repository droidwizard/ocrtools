package com.uit.ocr.utils;

import android.app.Activity;

public abstract class ResultClass extends Activity{

	/**
	 * nhận kết quả trả về từ BaseOCR.java
	 */
	public abstract void onReceiveResult();
}
