package com.example.android_runtimepermissions_master.internet;

import java.util.HashMap;
import java.util.Map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_runtimepermissions_master.R;
import com.google.gson.Gson;
import com.sun.transfer.client.sync.TransferSyncResponseData;

public class InternetActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback, View.OnClickListener {

	private String TAG = "InternetActivity";
	private static final int REQUEST_INTERNET = 1;
	private static String IP = "123.127.198.49";
	private static String PORT = "7080";
	private static String TYP_SEC = "tpy_secu";
	private static int HTTP = 0;

	private String ACTION = "get_secu_detail";
	private String PARAM_KEY = "code";
	private String PARAM_VALUE = "SH600600";

	TextView txt_param_name;
	EditText edit_param_value;
	Button bt_request;
	TextView txt_result;
	LinearLayout mLayout;
	private TransferSyncResponseData result;

	private Handler jsonHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 200) {
				txt_result.setText(result.toString());
			}
		}
	};

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_internet);
		setTitle("INTERNET权限");

		prepareView();
	}

	private void prepareView() {
		mLayout = (LinearLayout) findViewById(R.id.mLayout);
		txt_param_name = (TextView) findViewById(R.id.txt_param_name);
		edit_param_value = (EditText) findViewById(R.id.edit_param_value);
		bt_request = (Button) findViewById(R.id.bt_request);
		txt_result = (TextView) findViewById(R.id.txt_result);

		bt_request.setOnClickListener(this);
	}

	@SuppressLint("NewApi")
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == REQUEST_INTERNET) {
			Log.i(TAG, "Received response for INTERNET permission request.");
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				Log.i(TAG, "INTERNET permission has now been granted. Showing preview.");
				Toast.makeText(this, "INTERNET permission has now been granted. Showing preview.", Toast.LENGTH_LONG).show();

				new Thread(jsonRunnable).start();
			} else {
				Log.i(TAG, "INTERNET permission was NOT granted.");
				Toast.makeText(this, "INTERNET permission was NOT granted.", Toast.LENGTH_LONG).show();
			}
		} else {
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	Runnable jsonRunnable = new Runnable() {
		@Override
		public void run() {
			Map mMap = new HashMap<String, String>();
			mMap.put(PARAM_KEY, PARAM_VALUE);
			String data = new Gson().toJson(mMap);
			result = com.example.android_runtimepermissions_master.TransferSyncClient.request(IP, PORT, HTTP, TYP_SEC, ACTION, data);
			System.out.println(result);
			jsonHandler.sendEmptyMessage(200);
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_request:
			txt_result.setText("加载中.....");
			result = null;
			PARAM_KEY = txt_param_name.getText().toString();
			PARAM_VALUE = edit_param_value.getText().toString();

			checkPermission();
			break;
		default:
			break;
		}
	}

	private void checkPermission() {
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
			requestInternetPermission();
		} else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED) {
			Toast.makeText(this, " INTERNET was  PERMISSION_DENIED.", Toast.LENGTH_LONG).show();
		} else {
			Log.i(TAG, "INTERNET permission has already been granted.");
			new Thread(jsonRunnable).start();
		}
	}

	private void requestInternetPermission() {
		if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
			Log.i(TAG, "Displaying INTERNET permission rationale to provide additional context.");
			AlertDialog.Builder dialog = new AlertDialog.Builder(InternetActivity.this);
			dialog.setTitle("INTERNET permission");
			dialog.setMessage("INTERNET permission is needed to show the INTERNET preview");
			dialog.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ActivityCompat.requestPermissions(InternetActivity.this, new String[] { Manifest.permission.INTERNET }, REQUEST_INTERNET);
					dialog.dismiss();
				}
			});
			dialog.show();

		} else {
			ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.INTERNET }, REQUEST_INTERNET);
		}
	}
}
