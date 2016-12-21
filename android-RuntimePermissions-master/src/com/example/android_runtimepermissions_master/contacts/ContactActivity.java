package com.example.android_runtimepermissions_master.contacts;



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

import com.example.android_runtimepermissions_master.PermissionUtil;
import com.example.android_runtimepermissions_master.R;


public class ContactActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

	public static final String TAG = "ContactActivity";

	/**
	 * Id to identify a contacts permission request.
	 */
	private static final int REQUEST_CONTACTS = 1;

	/**
	 * Permissions required to read and write contacts. Used by the
	 * {@link ContactsFragment}.
	 */
	private static String[] PERMISSIONS_CONTACT = { Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS };
	 

	/**
	 * Called when the 'show camera' button is clicked. Callback is defined in
	 * resource layout definition.
	 */
	public void showContacts(View v) {
		Log.i(TAG, "Show contacts button pressed. Checking permissions.");

		// Verify that all required contact permissions have been granted.
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
				|| ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
			// Contacts permissions have not been granted.
			Log.i(TAG, "Contact permissions has NOT been granted. Requesting permissions.");
			requestContactsPermissions();
		} else {
			// Contact permissions have been granted. Show the contacts
			// fragment.
			Log.i(TAG, "Contact permissions have already been granted. Displaying contact details.");
			showContactDetails();
		}
	}

	/**
	 * Requests the Contacts permissions. If the permission has been denied
	 * previously, a SnackBar will prompt the user to grant the permission,
	 * otherwise it is requested directly.
	 */
	private void requestContactsPermissions() {
		// BEGIN_INCLUDE(contacts_permission_request)
		if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)
				|| ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CONTACTS)) {

			// Provide an additional rationale to the user if the permission was
			// not granted
			// and the user would benefit from additional context for the use of
			// the permission.
			// For example, if the request has been denied previously.
			Log.i(TAG, "Displaying contacts permission rationale to provide additional context.");

			// Display a SnackBar with an explanation and a button to trigger
			// the request.

			AlertDialog.Builder dialog = new AlertDialog.Builder(ContactActivity.this);
			dialog.setTitle("contacts");
			dialog.setMessage(" contacts permission is needed to show  preview");
			dialog.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ActivityCompat.requestPermissions(ContactActivity.this, PERMISSIONS_CONTACT, REQUEST_CONTACTS);
					dialog.dismiss();
				}
			});
			dialog.show();
		} else {
			// Contact permissions have not been granted yet. Request them
			// directly.
			ActivityCompat.requestPermissions(this, PERMISSIONS_CONTACT, REQUEST_CONTACTS);
		}
		// END_INCLUDE(contacts_permission_request)
	}

 

	/**
	 * Display the {@link ContactsFragment} in the content area if the required
	 * contacts permissions have been granted.
	 */
	private void showContactDetails() {
		getSupportFragmentManager().beginTransaction().replace(R.id.sample_content_fragment, ContactsFragment.newInstance()).addToBackStack("contacts").commit();
	}

	/**
	 * Callback received when a permissions request has been completed.
	 */
	@SuppressLint("NewApi") @Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

		  if (requestCode == REQUEST_CONTACTS) {
			Log.i(TAG, "Received response for contact permissions request.");

			// We have requested multiple permissions for contacts, so all of
			// them need to be
			// checked.
			if (PermissionUtil.verifyPermissions(grantResults)) {
				// All required permissions have been granted, display contacts
				// fragment.
				Toast.makeText(this, "permision_available_contacts", Toast.LENGTH_LONG).show();
			} else {
				Log.i(TAG, "Contacts permissions were NOT granted.");
				Toast.makeText(this, "permissions_not_granted", Toast.LENGTH_LONG).show();
			}

		} else {
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
		setContentView(R.layout.activity_contact);

		if (savedInstanceState == null) {
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			ContactPermissionsFragment fragment = new ContactPermissionsFragment();
			transaction.replace(R.id.sample_content_fragment, fragment);
			transaction.commit();
		}

		// This method sets up our custom logger, which will print all log
		// messages to the device
		// screen, as well as to adb logcat.
	}
}