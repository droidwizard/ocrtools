package com.kltn.nhom45.ocrtools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.kltn.nhom45.ocrtools.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

public class InitData extends Thread {
	private ProgressDialog mDialog;
	private static final String TAG = "MyThread.java";

	private boolean isComplete=false;
	Context mContext;
	
	public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/OCRTools/";

	public static final String lang = "eng";

	public InitData(Context context,ProgressDialog dialog) {
		mDialog = dialog;
		mContext=context;
	}

	@Override
	public void run() {
		Log.i(TAG, "gọi hàm initOctTools");
		initOcrTools();
		while(!isComplete){
			Log.i(TAG, "trong vòng while ngủ");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Log.i(TAG, "tat progressdialog");
		handler.sendEmptyMessage(0);

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mDialog.dismiss();
		};
	};
	
	
	public boolean initOcrTools(){
		if (initPath()==true){
			Log.i(TAG, String.valueOf(R.string.String_success)+" initPath");
			if (initTrainedData()==true){
				Log.i(TAG, String.valueOf(R.string.String_success)+" initTrainedData");
				Log.i(TAG, "gan iscomplete bang true");
				isComplete=true;
				return true;
			}
		}
		return false;
	}
	
	private boolean initPath(){
		String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };
		for (String path : paths) {
			File dir = new File(path);
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					Log.v(TAG, String.valueOf(R.string.String_error_dir) + path);
					return false;
				} else {
					Log.v(TAG, String.valueOf(R.string.String_error_dir) + path);
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean initTrainedData(){
		if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata"))
				.exists()) {
			try {

				AssetManager assetManager = mContext.getAssets();
				InputStream in = assetManager.open("tessdata/eng.traineddata");

				OutputStream out = new FileOutputStream(DATA_PATH
						+ "tessdata/eng.traineddata");

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;

				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();

				out.close();

				Log.v(TAG, String.valueOf(R.string.String_success));
			} catch (IOException e) {
				Log.e(TAG, String.valueOf(R.string.String_fail));
			}
			return true;
		}
		else{
			return true;
		}
	}
}
