package com.uit.ocr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.uit.ocr.utils.Consts;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.util.Log;

public class InitData extends Thread {
	private static final String TAG = InitData.class.getSimpleName();
	private Context mContext;

	private boolean isComplete = false;
	private ProgressDialog progressDialog;

	public InitData(Context context, ProgressDialog dialog) {
		progressDialog = dialog;
		mContext = context;
	}

	@Override
	public void run() {
		Log.i(TAG, "gọi hàm initOctTools");
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
			progressDialog.dismiss();
		};
	};

	private void initOcrTools() {
		if (initPath() == true) {
			if (initTrainedData() == true) {
				Log.i(TAG, "Gọi hàm initTrainedData()==true");
				isComplete = true;
			}
		}
	}

	private boolean initPath() {
		// tessdata thu mục chứa trainneddata, images thu mục chứa hình đã chụp
		String[] paths = new String[] { Consts.ROOT_PATH,
				Consts.ROOT_PATH + Consts.TESSDATA_NAME,
				Consts.ROOT_PATH + Consts.IMAGE_NAME };

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
		Log.i(TAG, "Gọi hàm initPath()");
		return true;
	}

	private boolean initTrainedData() {
		if (!(new File(Consts.ROOT_PATH + Consts.TESSDATA_NAME + "/"
				+ Consts.DATA_VIETNAM + ".traineddata")).exists()) {
			try {

				AssetManager assetManager = mContext.getAssets();
				InputStream in = assetManager.open(Consts.TESSDATA_NAME + "/"
						+ Consts.DATA_VIETNAM + ".traineddata");

				OutputStream out = new FileOutputStream(Consts.ROOT_PATH
						+ Consts.TESSDATA_NAME + "/" + Consts.DATA_VIETNAM
						+ ".traineddata");

				byte[] buf = new byte[1024];
				int len;

				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();

			} catch (IOException e) {
				Log.e(TAG, "fail");
			}
			return true;
		} else {
			return true;
		}
	}
}
