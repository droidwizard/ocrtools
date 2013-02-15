package com.uit.ocr;

import com.uit.ocr.utils.Consts;
import com.uit.ocr.utils.ResultClass;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

@SuppressWarnings("deprecation")
public class NameCardResult extends ResultClass {
	private static final String TAG = NameCardResult.class.getSimpleName();

	private EditText edtNameCardResultMobile, edtNameCardResultEmail,
			edtNameCardResultName;

	private ImageView ivNameCardResultImage;

	private Button btnNameCardResultAddContact;
	private String[] textAnalysis;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_namecardresult_activity);
		edtNameCardResultMobile = (EditText) findViewById(R.id.edt_namecardresult_phone);
		edtNameCardResultEmail = (EditText) findViewById(R.id.edt_namecardresult_email);
		edtNameCardResultName = (EditText) findViewById(R.id.edt_namecardresult_name);
		btnNameCardResultAddContact = (Button) findViewById(R.id.btn_namecardresult_add);
		ivNameCardResultImage = (ImageView) findViewById(R.id.iv_namecardresult_image);

		// hàm lấy kết quả
		onReceiveResult();
		ivNameCardResultImage.setImageBitmap(ImageHandler.inputImage);

		edtNameCardResultEmail.setText(textAnalysis[0].toString());
		edtNameCardResultMobile.setText(textAnalysis[1].toString());
		edtNameCardResultName.setText(textAnalysis[2].toString());

		btnNameCardResultAddContact.setOnClickListener(addContactsListener);

	}

	@Override
	public void onReceiveResult() {
		textAnalysis = getIntent().getStringArrayExtra(Consts.TEXTANALISYS);
	}

	private OnClickListener addContactsListener = new OnClickListener() {
		public void onClick(View v) {
			if (v == btnNameCardResultAddContact) {
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
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}
	

}
