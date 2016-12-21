package com.example.android_runtimepermissions_master.packages;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android_runtimepermissions_master.R;

public class PackagesActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback, View.OnClickListener {
	EditText et_apkname, et_packagename;
	Button bt_install, bt_uninstall;
	TextView txt_taoguba,txt_volley;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_packages);
		prepareView();
	}

	private void prepareView() {
		et_apkname = (EditText) findViewById(R.id.et_apkname);
		et_packagename = (EditText) findViewById(R.id.et_packagename);
		bt_install = (Button) findViewById(R.id.bt_install);
		bt_uninstall = (Button) findViewById(R.id.bt_uninstall);
		
		txt_taoguba = (TextView) findViewById(R.id.txt_taoguba);
		txt_volley = (TextView) findViewById(R.id.txt_volley);

		bt_install.setOnClickListener(this);
		bt_uninstall.setOnClickListener(this);
		txt_taoguba.setOnClickListener(this);
		txt_volley.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_install:
			installapk(et_apkname.getText().toString());
			break;
		case R.id.bt_uninstall:
			uninstall(et_packagename.getText().toString());
			break;
		case R.id.txt_taoguba:
			et_apkname.setText("Taoguba_App2014.apk");
			et_packagename.setText("com.taoguba.app");
			break;
		case R.id.txt_volley:
			et_apkname.setText("android-volley-master-sample.apk");
			et_packagename.setText("com.example.android_volley_master_sample");
			break;
		default:
			break;
		}
	}

	public void uninstall(String packagename) {
		Uri packageURI = Uri.parse("package:" + packagename);
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
		startActivity(uninstallIntent);
	}

	public void installapk(String apkname) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		// /storage/11E1-1E11/android-volley-master-sample.apk
		File f = new File(Environment.getExternalStorageDirectory() + "/"+apkname);
		Log.e("installapk", f.getAbsolutePath());
		intent.setDataAndType(Uri.fromFile(f), "application/vnd.android.package-archive");
		startActivity(intent);
	}

}
