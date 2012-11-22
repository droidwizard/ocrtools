package com.uit.ocr;

import com.uit.ocr.utils.ResultClass;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class InvitationResult extends ResultClass{
	private String recognizedResult;
	private String[] textAnalysis;
	private EditText edtInvitationResult_main,edtInvitationResult_location,edtInvitationResult_time;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invitationresult);
		edtInvitationResult_main=(EditText) findViewById(R.id.edt_invitationresult_main);
		edtInvitationResult_location=(EditText) findViewById(R.id.edt_invitation_location);
		edtInvitationResult_time=(EditText) findViewById(R.id.edt_invitaionresult_time);
		
		onReceiveResult();
		
		edtInvitationResult_main.setText(recognizedResult);
		edtInvitationResult_time.setText(textAnalysis[0].toString()+" "+textAnalysis[1].toString());
		edtInvitationResult_location.setText(textAnalysis[2].toString());
	}
	

	@Override
	public void onReceiveResult() {
		recognizedResult = getIntent().getStringExtra("textResult");
		textAnalysis = getIntent().getStringArrayExtra("textAnalisys");
	}
}
