package com.uit.ocr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NormalResult extends Activity implements OnClickListener{
	
	private TextView textResult;
	private EditText textTranslate;
	private EditText textEdit;
	private Button changeMode;
	private String textBase;
	
	private Context context = this;
	
	private final String TEXT_VIEW = "Chỉnh sửa";
	private final String EDIT_TEXT = "Xong";
	
	private final int MODE_TEXT = 0;
	private final int MODE_EDIT = 1;
	
	private int mode = MODE_TEXT;
	
	private static final int EXPORT_ID = Menu.FIRST;
	private static final int CAMERA_ID = Menu.FIRST + 1;
	private static final int IMAGE_ID = Menu.FIRST + 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.normalmode_layout);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		textBase = getIntent().getStringExtra("textBaseResult");
		
		textResult = (TextView) findViewById(R.id.tv_text_result);
		//textResult.setText("Số điện thoại 0975983991. Email nam.hd23@gmail.com. địa chỉ web google.com.vn");
		textResult.setText(textBase);
		Linkify.addLinks(textResult, Linkify.ALL);	
		
		textEdit = (EditText) findViewById(R.id.edt_text_result);
		
		textTranslate = (EditText) findViewById(R.id.edt_result_translate);
		textTranslate.setText("Chưa dịch được @@");
		//textTranslate.setText("Số điện thoại 0975983991. Email nam.hd23@gmail.com. địa chỉ web google.com.vn");
		
		if(mode == MODE_TEXT)
		{
			textEdit.setVisibility(View.GONE);
		}
		
		changeMode = (Button) findViewById(R.id.btn_edit_text_result);
		changeMode.setText(TEXT_VIEW);
		changeMode.setOnClickListener(this);
		
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(mode == MODE_TEXT)
		{
			textEdit.setText(textBase);
			textEdit.setVisibility(View.VISIBLE);
			textResult.setVisibility(View.GONE);
			changeMode.setText(EDIT_TEXT);
			mode = MODE_EDIT;
		}
		else
		{
			textBase = textEdit.getText().toString();
			textResult.setText(textBase);
			Linkify.addLinks(textResult, Linkify.ALL);
			textResult.setVisibility(View.VISIBLE);
			textEdit.setVisibility(View.GONE);
			changeMode.setText(TEXT_VIEW);
			mode = MODE_TEXT;
			
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);		
		menu.add(Menu.NONE, EXPORT_ID, Menu.NONE, R.string.menu_export).setIcon(
				android.R.drawable.ic_menu_view);
		menu.add(Menu.NONE, CAMERA_ID, Menu.NONE, R.string.menu_camera).setIcon(
				android.R.drawable.ic_menu_camera);
		menu.add(Menu.NONE, IMAGE_ID, Menu.NONE, R.string.menu_image).setIcon(
				android.R.drawable.ic_menu_gallery);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		switch (item.getItemId()) {
		case EXPORT_ID:		
			
            break;
        case CAMERA_ID:
        	Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);   
	        startActivityForResult(camera, 103);
            break;
        case IMAGE_ID:
        	Intent i = new Intent();
			i.setType("image/*");
			i.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(i, "Application Chooser"),102);
        	break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode==102){
			if (resultCode==RESULT_OK){
				Intent i=new Intent(context, ImageHandler.class);
				Bundle b=new Bundle();
				b.putString("uriData", data.getDataString());
				i.putExtras(b);
				startActivity(i);
			}
		}
		if (requestCode==103){
			if (resultCode==RESULT_OK){
				Intent i=new Intent(context, ImageHandler.class);
				Bundle b=new Bundle();
				b.putString("uriData", data.getDataString());
				i.putExtras(b);
				startActivity(i);
			}
		}
	}
}
