package com.uit.ocr;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;

public class InputImageResult extends Activity{
	private static final String TAG = "InputImageResult.java";
	Context mContext=InputImageResult.this;
	
	private EditText edtInputImageResultResult, edt_inputimageresult_mobile,
			edt_inputimageresult_email, edt_inputimageresult_name;

	private String recognizedResult;
	private String[] textAnalysis;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inputimageresult);
		edtInputImageResultResult = (EditText) findViewById(R.id.edt_inputimageresult_result);
		edt_inputimageresult_mobile = (EditText) findViewById(R.id.edt_inputimageresult_mobile);
		edt_inputimageresult_email = (EditText) findViewById(R.id.edt_inputimageresult_email);
		edt_inputimageresult_name = (EditText) findViewById(R.id.edt_inputimageresult_name);
		// hàm lấy kết quả
		onReceiveResult();
		edtInputImageResultResult.setText(recognizedResult);
		edt_inputimageresult_email.setText(textAnalysis[0].toString());
		edt_inputimageresult_mobile.setText(textAnalysis[1].toString());
		edt_inputimageresult_name.setText(textAnalysis[2].toString());
		
	}

	public void onReceiveResult() {
		recognizedResult = getIntent().getStringExtra("textResult");
		textAnalysis=getIntent().getStringArrayExtra("textAnalisys");
	}
	
}
