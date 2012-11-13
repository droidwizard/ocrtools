/*
 * Copyright (C) 2011 Google Inc.
 * Copyright (C) 2011 Robert Theis
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.googlecode.tesseract.android;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

import com.googlecode.leptonica.android.Pixa;
import com.googlecode.leptonica.android.Pix;
import com.googlecode.leptonica.android.ReadFile;

import java.io.File;

/**
 * Java interface for the Tesseract OCR engine. Does not implement all available
 * JNI methods, but does implement enough to be useful. Comments are adapted
 * from original Tesseract source.
 * 
 * Modified from the original version. Added getRegions(), getTextlines(),
 * getWords(), and getCharacters(), and modified finalize().
 *
 * @author alanv@google.com (Alan Viverette)
 */
/**
 * @author ThanhTrieu
 * 
 */
public class TessBaseAPI {
	/**
	 * Used by the native implementation of the class.
	 */
	private int mNativeData;

	static {
		System.loadLibrary("lept");
		System.loadLibrary("tess");

		nativeClassInit();
	}

	/** Orientation and script detection only. */
	public static final int PSM_OSD_ONLY = 0;

	/** Automatic page segmentation with orientation and script detection. (OSD) */
	public static final int PSM_AUTO_OSD = 1;

	/** Fully automatic page segmentation, but no OSD, or OCR. */
	public static final int PSM_AUTO_ONLY = 2;

	/** Fully automatic page segmentation, but no OSD. */
	public static final int PSM_AUTO = 3;

	/** Assume a single column of text of variable sizes. */
	public static final int PSM_SINGLE_COLUMN = 4;

	/** Assume a single uniform block of vertically aligned text. */
	public static final int PSM_SINGLE_BLOCK_VERT_TEXT = 5;

	/** Assume a single uniform block of text. (Default.) */
	public static final int PSM_SINGLE_BLOCK = 6;

	/** Treat the image as a single text line. */
	public static final int PSM_SINGLE_LINE = 7;

	/** Treat the image as a single word. */
	public static final int PSM_SINGLE_WORD = 8;

	/** Treat the image as a single character. */
	public static final int PSM_SINGLE_CHAR = 9;

	/** Whitelist of characters to recognize. */
	public static final String VAR_CHAR_WHITELIST = "tessedit_char_whitelist";

	/** Blacklist of characters to not recognize. */
	public static final String VAR_CHAR_BLACKLIST = "tessedit_char_blacklist";

	/** Run Tesseract only - fastest */
	public static final int OEM_TESSERACT_ONLY = 0;

	/** Run Cube only - better accuracy, but slower */
	public static final int OEM_CUBE_ONLY = 1;

	/** Run both and combine results - best accuracy */
	public static final int OEM_TESSERACT_CUBE_COMBINED = 2;

	/** Default OCR engine mode. */
	public static final int OEM_DEFAULT = 3;

	/**
	 * Constructs an instance of TessBaseAPI.
	 */
	public TessBaseAPI() {
		nativeConstruct();
	}

	/**
	 * Called by the GC to clean up the native data that we set up when we
	 * construct the object.
	 * 
	 * Altered from original version to avoid a crash-causing bug in OCR Test
	 * application.
	 */
	@Override
	protected void finalize() throws Throwable {
		// TODO Find out why finalize() is getting called when we change
		// languages, even though
		// we're still using the object. Is bypassing nativeFinalize() OK if we
		// still call
		// baseApi.end() in the Activity's onDestroy()?

		try {
			Log.d("TessBaseAPI.java",
					"finalize(): NOT calling nativeFinalize() due to premature garbage collection");
			// nativeFinalize();
		} finally {
			Log.d("TessBaseAPI.java", "finalize(): calling super.finalize()");
			super.finalize();
		}
	}

	/**
	 * khai báo đường dẫn gói dữ liệu huấn luyện, ngôn ngữ sử dụng. truyền chuỗi
	 * tên ngôn ngữ theo định dạng ISO
	 */
	public boolean init(String datapath, String language) {
		if (datapath == null)
			throw new IllegalArgumentException("Data path must not be null!");
		if (!datapath.endsWith(File.separator))
			datapath += File.separator;

		File tessdata = new File(datapath + "tessdata");
		if (!tessdata.exists() || !tessdata.isDirectory())
			throw new IllegalArgumentException(
					"Data path must contain subfolder tessdata!");

		return nativeInit(datapath, language);
	}

	/**
	 * khai báo đường dẫn gói dữ liệu huấn luyện, ngôn ngữ sử dụng. truyền chuỗi
	 * tên ngôn ngữ theo định dạng ISO. mode là chế độ sử dụng gồm
	 * OEM_TESSERACT_ONLY, // Run Tesseract only - fastest OEM_CUBE_ONLY, // Run
	 * Cube only - better accuracy, but slower OEM_TESSERACT_CUBE_COMBINED //Run
	 * both and combine results - best accuracy OEM_DEFAULT
	 */
	public boolean init(String datapath, String language, int ocrEngineMode) {
		if (datapath == null)
			throw new IllegalArgumentException("Data path must not be null!");
		if (!datapath.endsWith(File.separator))
			datapath += File.separator;

		File tessdata = new File(datapath + "tessdata");
		if (!tessdata.exists() || !tessdata.isDirectory())
			throw new IllegalArgumentException(
					"Data path must contain subfolder tessdata!");

		return nativeInitOem(datapath, language, ocrEngineMode);
	}

	/**
	 * trả về tên ngôn ngữ sử dụng gần nhất
	 */
	public String getInitLanguagesAsString() {
		return nativeGetInitLanguagesAsString();
	}

	/**
	 * Frees up recognition results and any stored image data, without actually
	 * freeing any recognition data that would be time-consuming to reload.
	 * Afterwards, you must call SetImage or SetRectangle before doing any
	 * Recognize or Get* operation.
	 */
	public void clear() {
		nativeClear();
	}

	/**
	 * Closes down tesseract and free up all memory. End() is equivalent to
	 * destructing and reconstructing your TessBaseAPI.
	 * <p>
	 * Once End() has been used, none of the other API functions may be used
	 * other than Init and anything declared above it in the class definition.
	 */
	public void end() {
		nativeEnd();
	}

	/**
	 * thiết lập giá trị cho các tham số Returns false if the name lookup
	 * failed. Eg SetVariable("tessedit_char_blacklist", "xyz"); to ignore x, y
	 * and z. Or SetVariable("classify_bln_numeric_mode", "1"); to set
	 * numeric-only mode. SetVariable may be used before Init, but settings will
	 * revert to defaults on End(). Note: Must be called after Init(). Only
	 * works for non-init variables (init variables should be passed to Init()).
	 */
	public boolean setVariable(String var, String value) {
		return nativeSetVariable(var, value);
	}

	/**
	 * Thiết lập chế độ xử lý hình ảnh. vd text ở dạng cột, dòng, 1 chữ cái ...
	 */
	public void setPageSegMode(int mode) {
		nativeSetPageSegMode(mode);
	}

	/**
	 * Thiết lập xem thông tin trong logcat
	 */
	public void setDebug(boolean enabled) {
		nativeSetDebug(enabled);
	}

	/**
	 * tạo 1 vùng hcn nhỏ bên trong hình nhận dạng. đó là vùng sẽ nhận dạng Call
	 * after SetImage. Each SetRectangle clears the recogntion results so
	 * multiple rectangles can be recognized with the same image.
	 */
	public void setRectangle(Rect rect) {
		setRectangle(rect.left, rect.top, rect.width(), rect.height());
	}

	public void setRectangle(int left, int top, int width, int height) {
		nativeSetRectangle(left, top, width, height);
	}

	/**
	 * Provides an image for Tesseract to recognize.
	 */
	public void setImage(File file) {
		Pix image = ReadFile.readFile(file);

		if (image == null) {
			throw new RuntimeException("Failed to read image file");
		}

		nativeSetImagePix(image.getNativePix());
	}

	/**
	 * Provides an image for Tesseract to recognize. Does not copy the image
	 * buffer. The source image must persist until after Recognize or
	 * GetUTF8Chars is called.
	 */
	public void setImage(Bitmap bmp) {
		Pix image = ReadFile.readBitmap(bmp);

		if (image == null) {
			throw new RuntimeException("Failed to read bitmap");
		}

		nativeSetImagePix(image.getNativePix());
	}

	/**
	 * Provides a Leptonica pix format image for Tesseract to recognize. Clones
	 * the pix object. The source image may be destroyed immediately after
	 * SetImage is called, but its contents may not be modified.
	 */
	public void setImage(Pix image) {
		nativeSetImagePix(image.getNativePix());
	}

	/**
	 * thiết lập hình ảnh từ mảng byte
	 */
	public void setImage(byte[] imagedata, int width, int height, int bpp,
			int bpl) {
		nativeSetImageBytes(imagedata, width, height, bpp, bpl);
	}

	/**
	 * trả về text nhận dạng kiểu UTF8.
	 */
	public String getUTF8Text() {
		// dùng trim vì phải bỏ ký tự xuống dùng
		String text = nativeGetUTF8Text();
		return text.trim();
	}

	/**
	 * Returns the mean confidence of text recognition.
	 */
	public int meanConfidence() {
		return nativeMeanConfidence();
	}

	/**
	 * Returns all word confidences (between 0 and 100) in an array. Số lượng từ
	 * này tương ứng với khoảng trắng trong GetUTF8Text
	 */
	public int[] wordConfidences() {
		int[] conf = nativeWordConfidences();

		// We shouldn't return null confidences
		if (conf == null)
			conf = new int[0];

		return conf;
	}

	/**
	 * Returns the result of page layout analysis as a Pixa, in reading order.
	 * 
	 * @return Pixa contaning page layout bounding boxes
	 */
	public Pixa getRegions() {
		return new Pixa(nativeGetRegions(), 0, 0);
	}

	/**
	 * Returns the textlines as a Pixa.
	 * 
	 * Block IDs are not returned.
	 * 
	 * @return Pixa containing textlines
	 */
	public Pixa getTextlines() {
		return new Pixa(nativeGetTextlines(), 0, 0);
	}

	/**
	 * Returns the strips as a Pixa.
	 * 
	 * Block IDs are not returned.
	 * 
	 * @return Pixa containing strips
	 */
	public Pixa getStrips() {
		return new Pixa(nativeGetStrips(), 0, 0);
	}

	/**
	 * Returns the word bounding boxes as a Pixa, in reading order.
	 * 
	 * @return Pixa containing word bounding boxes
	 */
	public Pixa getWords() {
		return new Pixa(nativeGetWords(), 0, 0);
	}

	/**
	 * Returns the character bounding boxes as a Pixa, in reading order.
	 * 
	 * @return Pixa containing character bounding boxes
	 */
	public Pixa getCharacters() {
		return new Pixa(nativeGetCharacters(), 0, 0);
	}

	// ******************
	// * Native methods *
	// ******************

	private static native void nativeClassInit();

	private native void nativeConstruct();

	private native void nativeFinalize();

	private native boolean nativeInit(String datapath, String language);

	private native boolean nativeInitOem(String datapath, String language,
			int mode);

	private native String nativeGetInitLanguagesAsString();

	private native void nativeClear();

	private native void nativeEnd();

	private native void nativeSetImageBytes(byte[] imagedata, int width,
			int height, int bpp, int bpl);

	private native void nativeSetImagePix(int nativePix);

	private native void nativeSetRectangle(int left, int top, int width,
			int height);

	private native String nativeGetUTF8Text();

	private native int nativeMeanConfidence();

	private native int[] nativeWordConfidences();

	private native boolean nativeSetVariable(String var, String value);

	private native void nativeSetDebug(boolean debug);

	private native void nativeSetPageSegMode(int mode);

	private native int nativeGetRegions();

	private native int nativeGetTextlines();

	private native int nativeGetStrips();

	private native int nativeGetWords();

	private native int nativeGetCharacters();

}
