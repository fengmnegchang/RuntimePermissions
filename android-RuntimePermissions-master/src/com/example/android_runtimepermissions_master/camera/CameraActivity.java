package com.example.android_runtimepermissions_master.camera;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.android_runtimepermissions_master.R;
import com.example.android_runtimepermissions_master.appOps.AppOpsUtil;


public class CameraActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

	public static final String TAG = "CameraActivity";
	/**
	 * Id to identify a camera permission request.
	 */
	private static final int REQUEST_CAMERA = 0;
 
	/**
	 * Called when the 'show camera' button is clicked. Callback is defined in
	 * resource layout definition.
	 */
	public void showCamera(View view) {
		Log.i(TAG, "Show camera button pressed. Checking permission.");
		// BEGIN_INCLUDE(camera_permission)
		// Check if the Camera permission is already available.
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
			// Camera permission has not been granted.
			requestCameraPermission();
		} else {
			// Camera permissions is already available, show the camera preview.
			Log.i(TAG, "CAMERA permission has already been granted. Displaying camera preview.");
			showCameraPreview();
		}
		// END_INCLUDE(camera_permission)

//		//使用反射opsmap.put(26, "OP_CAMERA");
//		if(AppOpsUtil.reflect(this, 26)==0){
//			showCameraPreview();
//		}else{
//			requestCameraPermission();
//		}
	}

	/**
	 * Requests the Camera permission. If the permission has been denied
	 * previously, a SnackBar will prompt the user to grant the permission,
	 * otherwise it is requested directly.
	 */
	private void requestCameraPermission() {
		Log.i(TAG, "CAMERA permission has NOT been granted. Requesting permission.");

		// BEGIN_INCLUDE(camera_permission_request)
		if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
			// Provide an additional rationale to the user if the permission was
			// not granted
			// and the user would benefit from additional context for the use of
			// the permission.
			// For example if the user has previously denied the permission.
			Log.i(TAG, "Displaying camera permission rationale to provide additional context.");
			
			AlertDialog.Builder dialog = new AlertDialog.Builder(CameraActivity.this);
			dialog.setTitle("CAMERA");
			dialog.setMessage(" CAMERA permission is needed to show  preview");
			dialog.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ActivityCompat.requestPermissions(CameraActivity.this, new String[] { Manifest.permission.CAMERA }, REQUEST_CAMERA);
					dialog.dismiss();
				}
			});
			dialog.show();
		} else {

			// Camera permission has not been granted yet. Request it directly.
			ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA }, REQUEST_CAMERA);
		}
		// END_INCLUDE(camera_permission_request)
	}

	 

	/**
	 * Display the {@link CameraPreviewFragment} in the content area if the
	 * required Camera permission has been granted.
	 */
	private void showCameraPreview() {
		getSupportFragmentManager().beginTransaction().replace(R.id.sample_content_fragment, CameraPreviewFragment.newInstance()).addToBackStack("contacts").commit();
	}

	/**
	 * Callback received when a permissions request has been completed.
	 */
	@SuppressLint("NewApi") @Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == REQUEST_CAMERA) {
			// BEGIN_INCLUDE(permission_result)
			// Received permission result for camera permission.
			Log.i(TAG, "Received response for Camera permission request.");
			// Check if the only required permission has been granted
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// Camera permission has been granted, preview can be displayed
				Log.i(TAG, "CAMERA permission has now been granted. Showing preview.");
				Toast.makeText(this, "permision_available_camera", Toast.LENGTH_LONG).show();
			} else {
				Log.i(TAG, "CAMERA permission was NOT granted.");
				Toast.makeText(this, "permissions_not_granted", Toast.LENGTH_LONG).show();
			}
			// END_INCLUDE(permission_result)
		}  else {
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	/*
	 * Note: Methods and definitions below are only used to provide the UI for
	 * this sample and are not relevant for the execution of the runtime
	 * permissions API.
	 */

	public void onBackClick(View view) {
		getSupportFragmentManager().popBackStack();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);

		if (savedInstanceState == null) {
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			CameraPermissionsFragment fragment = new CameraPermissionsFragment();
			transaction.replace(R.id.sample_content_fragment, fragment);
			transaction.commit();
		}
		

		// This method sets up our custom logger, which will print all log
		// messages to the device
		// screen, as well as to adb logcat.
	}
}
