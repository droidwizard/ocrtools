package com.kltn.nhom45.ocrtools;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;



public class MainActivity extends Activity implements OnClickListener{
	//private static final String TAG = "MainActivity.java";
	
	private Context context=MainActivity.this;
	private Button btn_main_DemoOCR;
	private Button btn_main_InputImage;
	private ProgressDialog progressDialog;
	private InitData mInitData;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_main_DemoOCR=(Button) findViewById(R.id.btn_main_1);
        btn_main_DemoOCR.setOnClickListener(MainActivity.this);
        btn_main_InputImage=(Button) findViewById(R.id.btn_main_2);
        btn_main_InputImage.setOnClickListener((OnClickListener) context);
        
        
    }

    @Override
    protected void onStart() {
    	super.onStart();
    	progressDialog = ProgressDialog.show(context, "Please wait for a second",
				"Processing");
    	mInitData=new InitData(context,progressDialog);

    	mInitData.start();

    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	public void onClick(View v) {
		if (v==btn_main_DemoOCR){
			Intent i=new Intent(context, DemoOCR.class);
			startActivity(i);
		}
		if (v==btn_main_InputImage){
			Intent i=new Intent(context, InputImage.class);
			startActivity(i);
		}
	}
	
	

    
}
