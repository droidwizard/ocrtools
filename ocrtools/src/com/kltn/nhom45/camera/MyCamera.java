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
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Surface;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

public class MyCamera extends Activity implements OnClickListener {
	private static final String TAG = "MyCamera.java";
	private Context mContext = MyCamera.this;
	private Resources res;
	private Camera mCamera;
	private CameraPreview mPreview;
	private PictureHandler mPicturehandler;
	private Button btn_MyCamera_TakePhoto;
	private Display sizeWindows;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mycamera);

		sizeWindows = getSize();

		res = mContext.getResources();

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

		// khởi tạo 1 button chụp trên framelayout
		btn_MyCamera_TakePhoto = new Button(mContext);
		btn_MyCamera_TakePhoto.setBackgroundResource(R.drawable.camera_icon);
		FrameLayout.LayoutParams param = new LayoutParams(100, 100);
		param.setMargins(sizeWindows.getWidth() / 2 - 50,
				sizeWindows.getHeight() - 120, sizeWindows.getWidth() / 2 - 50,
				30);
		frameLayout.addView(btn_MyCamera_TakePhoto, param);
		btn_MyCamera_TakePhoto.setOnClickListener(MyCamera.this);
		
		//list resolution camera hỗ trợ
		Parameters p=mCamera.getParameters();
		List<Size> s=p.getSupportedPreviewSizes();
		List<Size> ss=p.getSupportedPictureSizes();
		
		TextView tv=new TextView(mContext);
		for (int i=0;i<s.size();i++){
			tv.append(s.size()+s.get(i).toString());
		}
		//frameLayout.addView(tv);
		
		TextView tv2=new TextView(mContext);
		int a=getSmallestPhoto(p).height;
		int b=getSmallestPhoto(p).width;
		tv.append(a+" "+b+" "+a*b+" "+ss.get(0).toString());
		
		frameLayout.addView(tv2);
		

	}
	
	public Size getSmallestPhoto(Parameters param){
		Size now = null;
		for (Camera.Size s:param.getSupportedPictureSizes()){
			if (now==null){
				now=s;
			}else{
				int nowS=now.height*now.width;
				int nextS=s.height*s.width;
				if (nowS>nextS)
					nowS=nextS;
			}
		}
		return now;
	}

	public Display getSize() {
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		display.getWidth();
		display.getHeight();
		return display;
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
		if (v == btn_MyCamera_TakePhoto) {
			// chụp 1 bức hình nào
			//mCamera.setParameters(getFlash());
			// mCamera.setParameters(getdddd());
			//mCamera.setParameters(getFocus());
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

	public Parameters getFocus() {
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
		Parameters params = camera.getParameters();
		// params.setRotation(degrees);
		// params.setPreviewSize(480, 640);
		// camera.setParameters(params);
		// camera.setDisplayOrientation(result);
	}

}
