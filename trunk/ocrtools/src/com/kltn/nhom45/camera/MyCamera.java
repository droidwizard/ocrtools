package com.kltn.nhom45.camera;

import java.util.List;
import com.kltn.nhom45.ocrtools.R;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

public class MyCamera extends Activity implements OnClickListener {
	private static final String TAG = "MyCamera.java";
	private Context mContext = MyCamera.this;
	private Resources res;
	private Camera mCamera;
	private CameraPreview mPreview;
	private PictureHandler mPicturehandler;
	private Button btn_MyCamera_TakePhoto;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mycamera);
		
		res=mContext.getResources();

		// khởi tạo myPicturehandler dùng để xử lý hình ảnh đã chụp ( ghi hình
		// ra sdcard, thêm thông tin...)
		mPicturehandler = new PictureHandler(mContext);

		// gọi hàm ktra coi có camera ko? nếu có thì lấy sử dụng
		if (checkCameraHardware(mContext)) {
			mCamera = getCameraInstance();
		} else {
			Log.e(TAG, res.getString(R.string.mycamera_checkcamera));
		}
		// khởi tạo live preview và thêm nó vào layout của activity
		mPreview = new CameraPreview(mContext, mCamera);
		FrameLayout frameLayout = (FrameLayout) findViewById(R.id.cameraPreview);
		frameLayout.addView(mPreview);
		
		btn_MyCamera_TakePhoto=(Button) findViewById(R.id.btn_mycamera_takephoto);
		btn_MyCamera_TakePhoto.setOnClickListener(MyCamera.this);

	}

	@Override
	protected void onPause() {
		releaseCamera();
		super.onPause();
	}

	private void releaseCamera() {
		if (mCamera != null) {
			// giải phóng camera để cho app khác dùng
			mCamera.release();
			mCamera = null;
		}
	}
	
	public void onClick(View v) {
		if (v==btn_MyCamera_TakePhoto){
			// chụp 1 bức hình nào
			// myCamera.setParameters(getFlash());
			// myCamera.setParameters(getdddd());
			// myCamera.setParameters(getForcus());
			mCamera.takePicture(null, null, mPicturehandler);
		}
		
	}

	// ktra xem device có camera hay ko
	public boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA))
			// co camera
			return true;
		else
			return false;
	}

	// truy cập vào camera để sử dụng
	public Camera getCameraInstance() {
		Camera c = null;
		try {
			// thử lấy camera phía sau
			c = Camera.open(CameraInfo.CAMERA_FACING_BACK);

		} catch (Exception e) {
			// camera ko có giá trị or đang sử dụng or ko tồn tại
			Log.e(TAG, res.getString(R.string.mycamera_checkcameraforusing));
		}
		// trả về null nếu camera ko có
		return c;
	}

	public Parameters getFlash() {
		Parameters param = mCamera.getParameters();
		List<String> list = param.getSupportedFlashModes();
		if (list.contains(Parameters.FLASH_MODE_ON))
			param.setFlashMode(Parameters.FLASH_MODE_ON);
		return param;
	}

	public Parameters getForcus() {
		// get Camera parameters
		Parameters params = mCamera.getParameters();
		// set the focus mode
		params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
		return params;
	}

	public static void setCameraDisplayOrientation(Activity activity,
			int cameraId, android.hardware.Camera camera) {
		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}
		Parameters params = camera.getParameters();
		// params.setRotation(degrees);
		// params.setPreviewSize(480, 640);
		// camera.setParameters(params);
		// camera.setDisplayOrientation(result);
	}


}
