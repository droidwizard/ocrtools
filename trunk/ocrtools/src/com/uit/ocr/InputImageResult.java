package com.uit.ocr;

import com.uit.ocr.utils.ResultClass;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class InputImageResult extends ResultClass implements OnClickListener {
	private static final String TAG = "InputImageResult.java";
	Context mContext = InputImageResult.this;

	private EditText edtInputImageResultResult, edt_inputimageresult_mobile,
			edt_inputimageresult_email, edt_inputimageresult_name;

	private Button btnInputImageResult_AddContact;
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

		btnInputImageResult_AddContact = (Button) findViewById(R.id.btn_inputimageresult_addcontacts);
		// hàm lấy kết quả
		onReceiveResult();
		edtInputImageResultResult.setText(recognizedResult);
		edt_inputimageresult_email.setText(textAnalysis[0].toString());
		edt_inputimageresult_mobile.setText(textAnalysis[1].toString());
		edt_inputimageresult_name.setText(textAnalysis[2].toString());

		btnInputImageResult_AddContact.setOnClickListener(this);

	}
	
	@Override
	public void onReceiveResult() {
		recognizedResult = getIntent().getStringExtra("textResult");
		textAnalysis = getIntent().getStringArrayExtra("textAnalisys");
	}

	public void onClick(View v) {
		if (v == btnInputImageResult_AddContact) {
			Intent addContact = new Intent(Contacts.Intents.Insert.ACTION,
					Contacts.People.CONTENT_URI);
			addContact.putExtra(Contacts.Intents.Insert.NAME,
					textAnalysis[2].toString());
			addContact.putExtra(Contacts.Intents.Insert.EMAIL,
					textAnalysis[0].toString());
			addContact.putExtra(Contacts.Intents.Insert.PHONE,
					textAnalysis[1].toString());
			startActivity(addContact);
		}

	}

}
