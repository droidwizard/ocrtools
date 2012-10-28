package com.uit.ocr;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class DemoOCR extends BaseOCR implements OnClickListener {
	private static final String TAG = "DemoOCR.java";

	public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/OCRTools/";

	public static final String lang = "eng";

	protected Button _button;
	protected EditText _field;
	protected String _path;
	protected boolean _taken;
	private String text;
	Bitmap bitmap;
	protected static final String PHOTO_TAKEN = "photo_taken";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.demoocr);

		_field = (EditText) findViewById(R.id.editText1);
		_button = (Button) findViewById(R.id.btn_demoocr_takephoto);
		_button.setOnClickListener(DemoOCR.this);

		_path = DATA_PATH + "/ocr.jpg";
	}

	public void onClick(View v) {
		if (v == _button) {
			Log.v(TAG, "Starting Camera app");
			startCameraActivity();
		}

	}

	protected void startCameraActivity() {
		File file = new File(_path);
		Uri outputFileUri = Uri.fromFile(file);

		final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.i(TAG, "resultCode: " + resultCode);

		if (resultCode == -1) {
			onPhotoTaken();
		} else {
			Log.v(TAG, "User cancelled");
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(DemoOCR.PHOTO_TAKEN, _taken);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.i(TAG, "onRestoreInstanceState()");
		if (savedInstanceState.getBoolean(DemoOCR.PHOTO_TAKEN)) {
			onPhotoTaken();
		}
	}

	protected void onPhotoTaken() {
		_taken = true;
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;

		bitmap = BitmapFactory.decodeFile(_path, options);

		text = onHanldeOCR(bitmap);

		if (text.length() != 0) {
			_field.setText(_field.getText().toString().length() == 0 ? text
					: _field.getText() + " " + text);
			_field.setSelection(_field.getText().toString().length());
		}
	}

}
