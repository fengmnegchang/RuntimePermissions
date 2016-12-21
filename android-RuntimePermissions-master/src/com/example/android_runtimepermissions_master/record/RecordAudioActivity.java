package com.example.android_runtimepermissions_master.record;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android_runtimepermissions_master.R;

public class RecordAudioActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback, android.view.View.OnClickListener {
	 
	boolean flag = false;
	int RECORD_AUDIO = 1;
	Button bt_audio;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_audio);
		prepareView();
		 

	}

	private void prepareView() {
		bt_audio = (Button) findViewById(R.id.bt_audio);
		bt_audio.setOnClickListener(this);
	}


	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		 switch (v.getId()) {
		case R.id.bt_audio:
			checkPermission(Manifest.permission.RECORD_AUDIO);
			break;
		default:
			break;
		}
		
	}

	@SuppressLint("NewApi")
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == RECORD_AUDIO) {
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				Toast.makeText(this, "RECORD_AUDIO permission has now been granted. Showing preview.", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "RECORD_AUDIO permission was NOT granted.", Toast.LENGTH_LONG).show();
			}
		} else {
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	private void checkPermission(String permission) {
		int code = ActivityCompat.checkSelfPermission(this, permission);
		Log.d("checkPermission", permission + " state " + code);
		Toast.makeText(this, permission + "  was  " + code, Toast.LENGTH_LONG).show();
		requestPermission(permission);
//		if (code == PackageManager.PERMISSION_GRANTED) {
//		} else if (code == PackageManager.PERMISSION_DENIED) {
//			requestPermission(permission);
//		}
	}

	private void requestPermission(final String permission) {
		if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(RecordAudioActivity.this);
			dialog.setTitle(permission);
			dialog.setMessage(permission + "  permission is needed to show  preview");
			dialog.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ActivityCompat.requestPermissions(RecordAudioActivity.this, new String[] { permission }, RECORD_AUDIO);
					dialog.dismiss();
				}
			});
			dialog.show();
		} else {
			ActivityCompat.requestPermissions(this, new String[] { permission }, RECORD_AUDIO);
		}
	}

	 
}
