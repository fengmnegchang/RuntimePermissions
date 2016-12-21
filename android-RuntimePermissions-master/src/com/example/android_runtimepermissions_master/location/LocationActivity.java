package com.example.android_runtimepermissions_master.location;

import java.util.Iterator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_runtimepermissions_master.R;

public class LocationActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback, android.view.View.OnClickListener {
	private LocationManager locationManager;
	private GpsStatus gpsstatus;
	Location currentLocation;
	String currentProvider;
	boolean flag = false;

	TextView txt_location;
	Button bt_start;
	Button bt_stop;

	int ACCESS_COARSE_LOCATION = 1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		prepareView();
		// 获取到LocationManager对象
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		// 根据设置的Criteria对象，获取最符合此标准的provider对象
		currentProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER).getName();

		// 根据当前provider对象获取最后一次位置信息
		currentLocation = locationManager.getLastKnownLocation(currentProvider);
		// 如果位置信息为null，则请求更新位置信息
		if (currentLocation == null) {
			locationManager.requestLocationUpdates(currentProvider, 0, 0, locationListener);
		}

	}

	private void prepareView() {
		txt_location = (TextView) findViewById(R.id.txt_location);
		bt_start = (Button) findViewById(R.id.bt_start);
		bt_stop = (Button) findViewById(R.id.bt_stop);
		bt_start.setOnClickListener(this);
		bt_stop.setOnClickListener(this);
	}

	private void location() {
		// 直到获得最后一次位置信息为止，如果未获得最后一次位置信息，则显示默认经纬度
		// 每隔10秒获取一次位置信息
		// while(flag){
		currentLocation = locationManager.getLastKnownLocation(currentProvider);
		if (currentLocation != null) {
			Log.d("Location", "Latitude: " + currentLocation.getLatitude());
			Log.d("Location", "location: " + currentLocation.getLongitude());
			if (txt_location != null) {
				txt_location.setText("Latitude: " + currentLocation.getLatitude() + "\nlocation:" + currentLocation.getLongitude());
			}
		} else {
			Log.d("Location", "Latitude: " + 0);
			Log.d("Location", "location: " + 0);
			if (txt_location != null) {
				txt_location.setText("Latitude: " + 0 + "\nlocation:" + 0);
			}
		}
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			Log.e("Location", e.getMessage());
			txt_location.setText("Location: " + e.getMessage());
		}
		// }
	}

	@SuppressLint("NewApi")
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == ACCESS_COARSE_LOCATION) {
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				Toast.makeText(this, "ACCESS_COARSE_LOCATION permission has now been granted. Showing preview.", Toast.LENGTH_LONG).show();
				location();
			} else {
				Toast.makeText(this, "ACCESS_COARSE_LOCATION permission was NOT granted.", Toast.LENGTH_LONG).show();
			}
		} else {
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	private void checkPermission(String permission) {
		int code = ActivityCompat.checkSelfPermission(this, permission);
		Log.d("checkPermission", permission + " state " + code);
		Toast.makeText(this, permission + "  was  " + code, Toast.LENGTH_LONG).show();
		if (code == PackageManager.PERMISSION_GRANTED) {
			location();
		} else if (code == PackageManager.PERMISSION_DENIED) {
			requestPermission(permission);
		}
	}

	private void requestPermission(final String permission) {
		if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(LocationActivity.this);
			dialog.setTitle(permission);
			dialog.setMessage(permission + "  permission is needed to show  preview");
			dialog.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ActivityCompat.requestPermissions(LocationActivity.this, new String[] { permission }, ACCESS_COARSE_LOCATION);
					dialog.dismiss();
				}
			});
			dialog.show();
		} else {
			ActivityCompat.requestPermissions(this, new String[] { permission }, ACCESS_COARSE_LOCATION);
		}
	}

	private GpsStatus.Listener gpsListener = new GpsStatus.Listener() {
		// GPS状态发生变化时触发
		@Override
		public void onGpsStatusChanged(int event) {
			// 获取当前状态
			gpsstatus = locationManager.getGpsStatus(null);
			switch (event) {
			// 第一次定位时的事件
			case GpsStatus.GPS_EVENT_FIRST_FIX:
				break;
			// 开始定位的事件
			case GpsStatus.GPS_EVENT_STARTED:
				break;
			// 发送GPS卫星状态事件
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				Toast.makeText(LocationActivity.this, "GPS_EVENT_SATELLITE_STATUS", Toast.LENGTH_SHORT).show();
				Iterable<GpsSatellite> allSatellites = gpsstatus.getSatellites();
				Iterator<GpsSatellite> it = allSatellites.iterator();
				int count = 0;
				while (it.hasNext()) {
					count++;
				}
				Toast.makeText(LocationActivity.this, "Satellite Count:" + count, Toast.LENGTH_SHORT).show();
				break;
			// 停止定位事件
			case GpsStatus.GPS_EVENT_STOPPED:
				Log.d("Location", "GPS_EVENT_STOPPED");
				break;
			}
		}
	};

	// 创建位置监听器
	private LocationListener locationListener = new LocationListener() {
		// 位置发生改变时调用
		@Override
		public void onLocationChanged(Location location) {
			Log.d("Location", "onLocationChanged");
		}

		// provider失效时调用
		@Override
		public void onProviderDisabled(String provider) {
			Log.d("Location", "onProviderDisabled");
		}

		// provider启用时调用
		@Override
		public void onProviderEnabled(String provider) {
			Log.d("Location", "onProviderEnabled");
		}

		// 状态改变时调用
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.d("Location", "onStatusChanged");
		}
	};
	 
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_start:
			// 增加GPS状态监听器
			flag = true;
			locationManager.addGpsStatusListener(gpsListener);
			checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
			break;
		case R.id.bt_stop:
			flag = false;
			break;
		default:
			break;
		}

	}
}
