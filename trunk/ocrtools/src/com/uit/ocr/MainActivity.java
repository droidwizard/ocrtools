package com.uit.ocr;

import com.uit.ocr.utils.Consts;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;

public class MainActivity extends Activity{
	private static final String TAG = MainActivity.class.getSimpleName();
	private Context context = MainActivity.this;
	
	public static int mode = Consts.MODE_NONE;
	public static boolean flagLock = true;

	private Button btn_main_DemoOCR;
	private Button btn_main_InputImage;
	private ProgressDialog progressDialog;
	private InitData mInitData;
	public static boolean is2ColorImage;
	public static boolean isCharFilter;
	//public static boolean isNameCard;
	//private RadioGroup rg_main;
	private RadioButton rabtn_main_name_card;
	private RadioButton rabtn_main_invite;
	private RadioButton rabtn_main_normal;
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//rg_main=(RadioGroup) findViewById(R.id.rg_main);
		
		rabtn_main_name_card=(RadioButton) findViewById(R.id.rabtn_danhthiep);
		rabtn_main_invite=(RadioButton) findViewById(R.id.rabtn_thiepmoi);
		rabtn_main_normal = (RadioButton) findViewById(R.id.rabtn_thuong);
		
		btn_main_DemoOCR = (Button) findViewById(R.id.btn_main_camera);		
		btn_main_InputImage = (Button) findViewById(R.id.btn_main_image);
		
		btn_main_DemoOCR.setOnClickListener(btnListener);
		btn_main_InputImage.setOnClickListener(btnListener);
		
		if(flagLock == false)
		{
			rabtn_main_invite.setEnabled(false);
			rabtn_main_name_card.setEnabled(false);
		}
		else
		{
			rabtn_main_invite.setEnabled(true);
			rabtn_main_name_card.setEnabled(true);
		}
		//rabtn_main_normal.setChecked(true);		
		//onLoadStates();

	}
	public void onLoadStates(){
		/*if (isNameCard){
			rabtn_main_name_card.setChecked(true);
		}else{
			rabtn_main_invite.setChecked(true);
		}*/
		//rabtn_main_normal.setChecked(true);
	}
	
	public void onRadioButtonClicked(View view){
	    boolean checked = ((RadioButton) view).isChecked();
	    switch(view.getId()) {
	    	case R.id.rabtn_thuong:
	    		if (checked) {
					mode = Consts.MODE_NONE;
				}
	    		break;
	        case R.id.rabtn_danhthiep:
	            if (checked){
	            	//isNameCard=true;
	            	mode = Consts.MODE_NAMECARD;
	            }
	            break;
	        case R.id.rabtn_thiepmoi:
	            if (checked){
	            	//isNameCard=false;
	            	mode = Consts.MODE_INVITATION;
	            }
	            break;
	    }	   
	}
	
	
	@Override
	protected void onResume() {
		progressDialog = ProgressDialog.show(context,
				"Vui lòng đợi", "Đang xử lý...");
		mInitData = new InitData(context, progressDialog);
		mInitData.start();
		super.onResume();
		if(flagLock == false)
		{
			rabtn_main_invite.setEnabled(false);
			rabtn_main_name_card.setEnabled(false);
		}
		else
		{
			rabtn_main_invite.setEnabled(true);
			rabtn_main_name_card.setEnabled(true);
		}
	}


	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals("Settings")){
			startActivityForResult(new Intent(context, Settings.class), 104);
		}
		return super.onOptionsItemSelected(item);
	}*/
	
	private OnClickListener btnListener=new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_main_camera:
				Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);   
		        startActivityForResult(camera, 103);
				break;
			case R.id.btn_main_image: 
				Intent i = new Intent();
				i.setType("image/*");
				i.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(i, "Application Chooser"),102);
				break;
			}
			
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode==102){
			if (resultCode==RESULT_OK){
				Intent i=new Intent(context, ImageHandler.class);
				Bundle b=new Bundle();
				b.putString(Consts.URIDATA, data.getDataString());
				i.putExtras(b);
				startActivity(i);
			}
		}
		if (requestCode==103){
			if (resultCode==RESULT_OK){
				Intent i=new Intent(context, ImageHandler.class);
				Bundle b=new Bundle();
				b.putString(Consts.URIDATA, data.getDataString());
				i.putExtras(b);
				startActivity(i);
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);		
		menu.add(Menu.NONE, Consts.SETTINGS_ID, Menu.NONE, R.string.menu_settings).setIcon(
				android.R.drawable.ic_menu_manage);
		menu.add(Menu.NONE, Consts.ABOUT_ID, Menu.NONE, R.string.menu_about).setIcon(
				android.R.drawable.ic_menu_info_details);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		switch (item.getItemId()) {
		case Consts.SETTINGS_ID:
			startActivityForResult(new Intent(context, Settings.class), 104);
            break;
        case Consts.ABOUT_ID:
            break;	
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

}
