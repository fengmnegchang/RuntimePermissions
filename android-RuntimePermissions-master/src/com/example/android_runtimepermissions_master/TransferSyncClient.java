package com.example.android_runtimepermissions_master;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.util.Log;

import com.sun.transfer.client.sync.TransferSyncResponseData;
import com.sun.transfer.client.sync.a;
import com.sun.transfer.client.sync.b;
import com.sun.transfer.client.sync.c;

public class TransferSyncClient {
	public static TransferSyncResponseData request(String paramString1, String paramString2, int paramInt, String paramString3, String paramString4, String paramString5) {
		TransferSyncResponseData localTransferSyncResponseData;
		(localTransferSyncResponseData = new TransferSyncResponseData()).setStatus(-1L);
		localTransferSyncResponseData.setResult("");
		String str = "http://";
		if (paramInt == 1)
			str = "https://";
		str = str + paramString1;
		str = str + ":";
		str = str + paramString2;
		str = str + "?category=";
		str = str + paramString3;
		str = str + "&action=";
		str = str + paramString4;
		
		System.out.println("参数＝＝＝＝ "+str);
		System.out.println("request ＝＝＝＝ "+paramString5);
		if (paramInt == 1) {
			c paramStringc = new c();
			b paramStringb = new b();
			try {
				SSLContext sSLContext = SSLContext.getInstance("TLS");
				TrustManager[] trustManager = { paramStringc };
				sSLContext.init(null, trustManager, new SecureRandom());
				if (sSLContext != null)

					HttpsURLConnection.setDefaultSSLSocketFactory(sSLContext.getSocketFactory());
				 
				HttpsURLConnection.setDefaultHostnameVerifier(paramStringb);

				HttpsURLConnection httpsURLConnection = (HttpsURLConnection) new URL(str).openConnection();
				httpsURLConnection.setDoInput(true);

				httpsURLConnection.setDoOutput(true);
				httpsURLConnection.setRequestMethod("POST");

				BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(httpsURLConnection.getOutputStream());
				bufferedOutputStream.write(paramString5.getBytes("UTF-8"));
				
				
				bufferedOutputStream.flush();
				bufferedOutputStream.close();
				int responseCode = httpsURLConnection.getResponseCode();
				if (200 == responseCode) {
					localTransferSyncResponseData.setStatus(518630185893888L);
					InputStream inputStream = httpsURLConnection.getInputStream();
					byte[] results = a.a(inputStream);
					paramString1 = new String(results, "UTF-8");
					localTransferSyncResponseData.setResult(paramString1);

				}
				localTransferSyncResponseData.setStatus(-1);
			} catch (Exception localException1) {
			}
		} else {
			try {
				HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
				httpURLConnection.setDoInput(true);

				httpURLConnection.setDoOutput(true);
				httpURLConnection.setRequestMethod("POST");

				BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(httpURLConnection.getOutputStream());

				bufferedOutputStream.write(paramString5.getBytes("UTF-8"));
				bufferedOutputStream.flush();
				bufferedOutputStream.close();
				paramInt = httpURLConnection.getResponseCode();
				if (200 == paramInt) {
					localTransferSyncResponseData.setStatus(518630185893888L);

					InputStream inputStream = httpURLConnection.getInputStream();
					byte[] arrayOfByte = a.a(inputStream);
					paramString2 = new String(arrayOfByte, "UTF-8");
					localTransferSyncResponseData.setResult(paramString2);
					// break label498:
				}
				localTransferSyncResponseData.setStatus(paramInt);
			} catch (Exception localException2) {
				Log.e("TransferSyncClient", localException2.toString());
			}
		}
		return localTransferSyncResponseData;
	}
}