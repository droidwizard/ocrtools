package com.uit.ocr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

public class InitData extends Thread {
	private ProgressDialog mDialog;
	private static final String TAG = "InitData.java";

	private boolean isComplete = false;
	Context mContext;
	Resources res;
	private static final String ROOTNAME = "OCRTools";
	private static final String IMAGENAME = "images";
	private static final String TESSDATANAME = "tessdata";

	public static final String ROOT_PATH = Environment
			.getExternalStorageDirectory().toString() + "/" + ROOTNAME + "/";

	public static final String lang = "vie";

	public InitData(Context context, ProgressDialog dialog) {
		mDialog = dialog;
		mContext = context;
		res = mContext.getResources();
	}

	@Override
	public void run() {
		Log.i(TAG, "g�?i hàm initOctTools");
		initOcrTools();
		while (!isComplete) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		handler.sendEmptyMessage(0);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mDialog.dismiss();
		};
	};

	public void initOcrTools() {
		if (initPath() == true) {	
			if (initTrainedData() == true) {
				Log.i(TAG, res.getString(R.string.String_success)
						+ " initTrainedData()");
				isComplete = true;
			}
		}
	}

	private boolean initPath() {
		// tessdata thu mục chứa trainneddata, images thu mục chứa hình đã chụp
		String[] paths = new String[] { ROOT_PATH, ROOT_PATH + TESSDATANAME,
				ROOT_PATH + IMAGENAME };

		for (String path : paths) {
			File dir = new File(path);

			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					Log.v(TAG, "ERROR: Creation of directory " + path
							+ " on sdcard failed");
					return false;
				} else {
					Log.v(TAG, "Created directory " + path + " on sdcard");
				}
			}
		}
		// Environment.getExternalStoragePublicDirectory(IMAGENAME);
		Log.i(TAG, res.getString(R.string.String_success) + " initPath()");
		return true;
	}

	private boolean initTrainedData() {
		if (!(new File(ROOT_PATH + TESSDATANAME + "/" + lang + ".traineddata"))
				.exists()) {
			try {

				AssetManager assetManager = mContext.getAssets();
				InputStream in = assetManager.open(TESSDATANAME + "/" + lang
						+ ".traineddata");

				OutputStream out = new FileOutputStream(ROOT_PATH
						+ TESSDATANAME + "/" + lang + ".traineddata");

				byte[] buf = new byte[1024];
				int len;

				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();

			} catch (IOException e) {
				Log.e(TAG, res.getString(R.string.String_fail));
			}
			return true;
		} else {
			return true;
		}
	}
}
