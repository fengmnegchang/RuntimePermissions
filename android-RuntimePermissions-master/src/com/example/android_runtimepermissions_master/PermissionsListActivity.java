package com.example.android_runtimepermissions_master;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.android_runtimepermissions_master.appOps.AppOpsActivity;
import com.example.android_runtimepermissions_master.camera.CameraActivity;
import com.example.android_runtimepermissions_master.contacts.ContactActivity;
import com.example.android_runtimepermissions_master.internet.InternetActivity;
import com.example.android_runtimepermissions_master.location.LocationActivity;
import com.example.android_runtimepermissions_master.packages.PackagesActivity;
import com.example.android_runtimepermissions_master.record.RecordAudioActivity;
import com.example.android_runtimepermissions_master.storage.StorageActivity;

public class PermissionsListActivity extends AppCompatActivity implements OnClickListener {
	Button btn_storage, btn_internet, btn_camera, btn_contacts, btn_location, btn_appOps,btn_package,btn_audio;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_permissions_list);
		btn_internet = (Button) findViewById(R.id.btn_internet);
		btn_storage = (Button) findViewById(R.id.btn_storage);
		btn_camera = (Button) findViewById(R.id.btn_camera);
		btn_contacts = (Button) findViewById(R.id.btn_contacts);
		btn_location = (Button) findViewById(R.id.btn_location);
		btn_appOps = (Button) findViewById(R.id.btn_appOps);
		btn_package = (Button) findViewById(R.id.btn_package);
		btn_audio = (Button) findViewById(R.id.btn_audio);

		btn_internet.setOnClickListener(this);
		btn_storage.setOnClickListener(this);
		btn_camera.setOnClickListener(this);
		btn_contacts.setOnClickListener(this);
		btn_location.setOnClickListener(this);
		btn_appOps.setOnClickListener(this);
		btn_package.setOnClickListener(this);
		btn_audio.setOnClickListener(this);
	}

	

	public void storage() {
		Intent intent = new Intent();
		intent.setClass(PermissionsListActivity.this, StorageActivity.class);
		startActivity(intent);
	}

	public void internet() {
		Intent intent = new Intent();
		intent.setClass(PermissionsListActivity.this, InternetActivity.class);
		startActivity(intent);
	}

	public void camera() {
		Intent intent = new Intent();
		intent.setClass(PermissionsListActivity.this, CameraActivity.class);
		startActivity(intent);
	}

	public void contact() {
		Intent intent = new Intent();
		intent.setClass(PermissionsListActivity.this, ContactActivity.class);
		startActivity(intent);
	}

	public void location() {
		Intent intent = new Intent();
		intent.setClass(PermissionsListActivity.this, LocationActivity.class);
		startActivity(intent);
	}
	
	public void appOps() {
		Intent intent = new Intent();
		intent.setClass(PermissionsListActivity.this, AppOpsActivity.class);
		startActivity(intent);
	}
	
	public void packages() {
		Intent intent = new Intent();
		intent.setClass(PermissionsListActivity.this, PackagesActivity.class);
		startActivity(intent);
	}
	
	public void audio() {
		Intent intent = new Intent();
		intent.setClass(PermissionsListActivity.this, RecordAudioActivity.class);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_storage:
			storage();
			break;
		case R.id.btn_internet:
			// showInstalledAppDetails(PermissionsListActivity.this,getPackageName());
			internet();
			break;
		case R.id.btn_camera:
			camera();
			break;
		case R.id.btn_contacts:
			contact();
			break;
		case R.id.btn_location:
			location();
			break;
		case R.id.btn_appOps:
			appOps();
			break;
		case R.id.btn_package:
			packages();
			break;
		case R.id.btn_audio:
			audio();
			break;
		default:
			break;
		}
	}

	private static final String SCHEME = "package:";
	private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";
	private static final String APP_PKG_NAME_22 = "pkg";
	private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
	private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";

	public static void showInstalledAppDetails(Context context, String packageName) {
		Intent intent = new Intent();
		final int apiLevel = Build.VERSION.SDK_INT;
		if (apiLevel >= 9) { // 2.3（ApiLevel 9）以上，使用SDK提供的接口
			intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
			Uri uri = Uri.parse(SCHEME + packageName);
			intent.setData(uri);
		} else { // 2.3以下，使用非公开的接口（查看InstalledAppDetails源码）
			// 2.2和2.1中，InstalledAppDetails使用的APP_PKG_NAME不同。
			final String appPkgName = (apiLevel == 8 ? APP_PKG_NAME_22 : APP_PKG_NAME_21);
			intent.setAction(Intent.ACTION_VIEW);
			intent.setClassName(APP_DETAILS_PACKAGE_NAME, APP_DETAILS_CLASS_NAME);
			intent.putExtra(appPkgName, packageName);
		}
		context.startActivity(intent);
	}

}
