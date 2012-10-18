package com.kltn.nhom45.ocrtools;

import java.io.FileNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class InputImage extends BaseOCR implements OnClickListener {
	private ImageView iv_inputimage_photo;
	private Button btn_inputimage_choose;
	private EditText edt_inputimage_outtext;
	private Bitmap bitmap;
	private String text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inputimage);
		edt_inputimage_outtext = (EditText) findViewById(R.id.edt_inputimage_outtext);
		iv_inputimage_photo = (ImageView) findViewById(R.id.iv_inputimage_photo);
		btn_inputimage_choose = (Button) findViewById(R.id.btn_inputimage_choose);
		btn_inputimage_choose.setOnClickListener(InputImage.this);

		
	}

	public void onTakeImage() {
		Intent i = new Intent();
		i.setType("image/*");
		i.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(i, "Application Chooser"),
				101);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 101) {
			if (resultCode == RESULT_OK) {
				Uri imageUri = data.getData();
				try {
					bitmap = BitmapFactory.decodeStream(getContentResolver()
							.openInputStream(imageUri));
					iv_inputimage_photo.setImageBitmap(bitmap);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (bitmap!=null)
					onPhotoChosen();
			}
			
		}
	}

	public void onClick(View arg0) {
		if (arg0 == btn_inputimage_choose) {
			onTakeImage();
		}
	}

	public void onPhotoChosen() {
		text = onHanldeOCR(bitmap);
		if (text.length() != 0) {
			edt_inputimage_outtext.setText(edt_inputimage_outtext.getText()
					.toString().length() == 0 ? text : edt_inputimage_outtext
					.getText() + " " + text);
			edt_inputimage_outtext.setSelection(edt_inputimage_outtext
					.getText().toString().length());
		}

	}

}
