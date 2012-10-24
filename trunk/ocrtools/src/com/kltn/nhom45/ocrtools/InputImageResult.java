package com.kltn.nhom45.ocrtools;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class InputImageResult extends Activity{
	EditText edtInputImageResultResult;
	String result;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inputimageresult);
		edtInputImageResultResult=(EditText) findViewById(R.id.edt_inputimageresult_result);
		onReceiveResult();
		edtInputImageResultResult.setText(result);
		
	}
	
	public void onReceiveResult(){
		result=getIntent().getStringExtra("textResult");
	}

}
