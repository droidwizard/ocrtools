package com.kltn.nhom45.camera;

import java.io.IOException;

import com.kltn.nhom45.ocrtools.R;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;

import android.util.Log;

import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;


public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{
	private static final String TAG = "CameraPreview.java";
	private Camera mCamera;
	private SurfaceHolder mHolder;
	private Callback callback = CameraPreview.this;
	private Context mContext;
	boolean isPreviewRunning;

	public CameraPreview(Context context, Camera camera) {
		super(context);
		mCamera = camera;
		// cài đặt surfaceview.callback để lấy những thay đổi khi underlying
		// surface is created and destroyed
		mHolder = getHolder();
		mHolder.addCallback(callback);
		// deprecated setting, but required on Android versions prior to 3.0
		// myHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		// chup anh ngược chiu
		// myCamera.setDisplayOrientation(180);
		mContext = context;

	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// chứa xự kiện dùng để xử lý khi preview thay đổi or xoay ...
		// phải STOP preview trước khi thay đổi kích thước or cấu hình lại.
		if (mHolder.getSurface() == null) {
			// preview surface ko tồn tại
			Log.e(TAG, String.valueOf(R.string.camerapreview_checksurfaceview));
			return;
		}
		// stop preview trước khi thay đổi j đó
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			Log.e(TAG, String.valueOf(R.string.camerapreview_checkstopsurfaceview) + e.getMessage());
		}

		// thiết lập lại kích thước của preview or xoay or cấu hình ở đây
		int rotation = ((Activity) mContext).getWindowManager()
				.getDefaultDisplay().getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 90;
			break;
		case Surface.ROTATION_90:
			degrees = 0;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}
		//mCamera.setDisplayOrientation(degrees);
		//Camera.Parameters parameters = mCamera.getParameters();
		//parameters.setPreviewSize(width, height);
		//mCamera.setParameters(parameters);
		//end thiết lập 

		// start preview sau khi thay đổi
		try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();
		} catch (Exception e) {
			Log.e(TAG, String.valueOf(R.string.camerapreview_checkstartsurfaceview) + e.getMessage());
		}
	}

	public void previewCamera() {
		try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();
			isPreviewRunning = true;
		} catch (Exception e) {
			Log.e(TAG, String.valueOf(R.string.camerapreview_checkstartpreview) + e.getMessage());
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// Surface được tạo, và hiện giờ nói cho camera biết chổ để vẽ preview.
		try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.setDisplayOrientation(0);
			mCamera.startPreview();
		} catch (IOException e) {
			Log.e(TAG, String.valueOf(R.string.camerapreview_checkcreatesurfaceview) + e.getMessage());
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// chịu trách nhiệm giải phóng camera preview trong activity

	}

}
