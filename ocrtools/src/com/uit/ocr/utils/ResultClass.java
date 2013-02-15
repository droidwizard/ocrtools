package com.uit.ocr.utils;

import com.uit.ocr.R;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;

public abstract class ResultClass extends Activity{

	/**
	 * nhận kết quả trả về từ BaseOCR.java
	 */
	public abstract void onReceiveResult();
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, Consts.CAMERA_ID, Menu.NONE, R.string.menu_camera)
				.setIcon(android.R.drawable.ic_menu_camera);
		menu.add(Menu.NONE, Consts.IMAGE_ID, Menu.NONE, R.string.menu_image)
				.setIcon(android.R.drawable.ic_menu_gallery);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		switch (item.getItemId()) {
		case Consts.CAMERA_ID:
			Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(camera, 103);
			if (!isFinishing()){
				finish();
			}
			break;
		case Consts.IMAGE_ID:
			Intent i = new Intent();
			i.setType("image/*");
			i.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(
					Intent.createChooser(i, "Application Chooser"), 102);
			if (!isFinishing()){
				finish();
			}
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}
}
