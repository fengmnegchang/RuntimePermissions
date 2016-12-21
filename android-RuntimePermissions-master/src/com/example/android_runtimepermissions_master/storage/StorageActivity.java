package com.example.android_runtimepermissions_master.storage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_runtimepermissions_master.R;

public class StorageActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback, View.OnClickListener {

	int WRITE_EXTERNAL_STORAGE = 1;
	int READ_EXTERNAL_STORAGE = 2;
	EditText edit_content;
	Button bt_write;
	Button bt_read;
	TextView txt_result;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_storage);

		prepareView();
	}

	private void prepareView() {
		edit_content = (EditText) findViewById(R.id.edit_content);
		bt_write = (Button) findViewById(R.id.bt_write);
		bt_read = (Button) findViewById(R.id.bt_read);
		txt_result = (TextView) findViewById(R.id.txt_result);
		bt_write.setOnClickListener(this);
		bt_read.setOnClickListener(this);
	}

	@SuppressLint("NewApi")
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == WRITE_EXTERNAL_STORAGE) {
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				Toast.makeText(this, "WRITE_EXTERNAL_STORAGE permission has now been granted. Showing preview.", Toast.LENGTH_LONG).show();
				txt_result.setText(write());
			} else {
				Toast.makeText(this, "WRITE_EXTERNAL_STORAGE permission was NOT granted.", Toast.LENGTH_LONG).show();
			}
		} else if (requestCode == READ_EXTERNAL_STORAGE) {
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				Toast.makeText(this, "READ_EXTERNAL_STORAGE permission has now been granted. Showing preview.", Toast.LENGTH_LONG).show();
				txt_result.setText(read());
			} else {
				Toast.makeText(this, "READ_EXTERNAL_STORAGE permission was NOT granted.", Toast.LENGTH_LONG).show();
			}
		} else {
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_write:
			txt_result.setText("");
			checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
			break;
		case R.id.bt_read:
			txt_result.setText("");
			checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
			break;
		default:
			break;
		}
	}

	private void checkPermission(String permission) {
		int code = ActivityCompat.checkSelfPermission(this, permission);
		Log.d("checkPermission", permission + " state " + code);
		Toast.makeText(this, permission + "  was  " + code, Toast.LENGTH_LONG).show();
		if (code == PackageManager.PERMISSION_GRANTED) {
			if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
				txt_result.setText(write());
			} else if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
				txt_result.setText(read());
			}
		} else if (code == PackageManager.PERMISSION_DENIED) {
			requestPermission(permission);
		}
	}

	int requestCode = 0;

	private void requestPermission(final String permission) {
		if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
			requestCode = WRITE_EXTERNAL_STORAGE;
		} else if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
			requestCode = READ_EXTERNAL_STORAGE;
		}

		if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(StorageActivity.this);
			dialog.setTitle(permission);
			dialog.setMessage(permission + "  permission is needed to show  preview");
			dialog.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ActivityCompat.requestPermissions(StorageActivity.this, new String[] { permission }, requestCode);
					dialog.dismiss();
				}
			});
			dialog.show();

		} else {
			ActivityCompat.requestPermissions(this, new String[] { permission }, requestCode);
		}
	}

	public String  write() {
		String code = "";
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) { // 通过getExternalStorageDirectory方法获取SDCard的文件路径
			File file = new File(Environment.getExternalStorageDirectory(), "storage.txt");
			// 获取输出流
			String content = edit_content.getText().toString();
			FileOutputStream outStream = null;
			try {
				outStream = new FileOutputStream(file);
				outStream.write(content.getBytes());
				outStream.close();
				code = "写入成功；文件路径："+file.getAbsolutePath();
			} catch (Throwable e) {
				e.printStackTrace();
				code = crashToString(e);
			}
		}else{
			code = "没有sdcard";
		}
		return code;
	}

	private String crashToString(Throwable ex) {
		StringBuffer buffer = new StringBuffer();
		
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		 String result = writer.toString();  
		 buffer.append(result);  
		return buffer.toString();
	}

	/**
	 * 读取文件的内容
	 * 
	 * @param filename
	 *            文件名称
	 * @return
	 * @throws Exception
	 */
	public String read() {
		// 获得输入流
		FileInputStream inStream = null;
		byte[] data = null;
		try {
			File file = new File(Environment.getExternalStorageDirectory(), "storage.txt");
			inStream = new FileInputStream(file);
			// new一个缓冲区
			byte[] buffer = new byte[1024];
			int len = 0;
			// 使用ByteArrayOutputStream类来处理输出流
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			while ((len = inStream.read(buffer)) != -1) {
				// 写入数据
				outStream.write(buffer, 0, len);
			}
			// 得到文件的二进制数据
			data = outStream.toByteArray();
			// 关闭流
			outStream.close();
			inStream.close();
		} catch (Throwable e) {
			e.printStackTrace();
			return crashToString(e);
		}
		return new String(data);
	}

}
