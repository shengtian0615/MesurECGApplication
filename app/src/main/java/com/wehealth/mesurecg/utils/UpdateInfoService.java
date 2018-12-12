package com.wehealth.mesurecg.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;


import com.wehealth.model.domain.model.AppVersion;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class UpdateInfoService {
	Handler handler;
	Context context;
	AppVersion updateInfo;
	private static final String PATH = Environment.getExternalStorageDirectory().getPath();
	private static final String APK_SAVE_PATH = "/wehealthvip/doctorclinic";
	private static final String SUFFIX = ".apk";
	private static final String APK_PATH = "APK_PATH";
	private HashMap<String, String> cache = new HashMap<String, String>();
	
	public UpdateInfoService(Context context){
		this.context=context;
	}
	
	public void downLoadFile(final AppVersion version, final ProgressDialog pDialog){
		AsyncDownLoad asyncDownLoad = new AsyncDownLoad(pDialog);
		asyncDownLoad.execute(version);
	}
	
	/**
	 * 异步下载app任务
	 */
	public class AsyncDownLoad extends AsyncTask<AppVersion, Integer, Boolean> {
		
		ProgressDialog progressDialog;

		public AsyncDownLoad(ProgressDialog pDialog) {
			progressDialog = pDialog;
		}

		@Override
		protected Boolean doInBackground(AppVersion... params) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(params[0].getLink());
			try {
				HttpResponse response = httpClient.execute(httpGet);
				if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
					return false;
				} else {
					HttpEntity entity = response.getEntity();
					InputStream inputStream = entity.getContent();
					int total = (int)entity.getContentLength();
					progressDialog.setMax(100);
					String apkName = params[0].getVersionName() + SUFFIX;
					cache.put(APK_PATH,
							PATH + APK_SAVE_PATH + "/" + apkName);
					File savePath = new File(PATH + APK_SAVE_PATH);
					if (!savePath.exists())
						savePath.mkdirs();
					File apkFile = new File(savePath, apkName);
					if (apkFile.exists()) {
						apkFile.delete();
					}
					FileOutputStream fos = new FileOutputStream(apkFile);
					byte[] buf = new byte[1024];
					int count = 0;
					int length = -1;
					while ((length = inputStream.read(buf)) != -1) {
						fos.write(buf, 0, length);
						count += length;
						int progress = (int) ((count / (float) total) * 100);
						progressDialog.setProgress(progress);
					}
					inputStream.close();
					fos.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean flag) {
			if (progressDialog!=null) {
				progressDialog.dismiss();
			}
			if (flag) {
				installApk(Uri.parse("file://" + cache.get(APK_PATH)));
			} else {
				Toast.makeText(context, "由于网络环境不好致使，下载失败！", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	private void installApk(Uri data) {
		if (context != null) {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setDataAndType(data, "application/vnd.android.package-archive");
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		} else {
			Log.e("NullPointerException", "The context must not be null.");
		}

	}
	
//	void down() {
//		handler.post(new Runnable() {
//			public void run() {
//				progressDialog.cancel();
//				update();
//			}
//		});
//	}
//
//	void update() {
//		Intent intent = new Intent(Intent.ACTION_VIEW);
//		intent.setDataAndType(Uri.fromFile(new File(Environment
//				.getExternalStorageDirectory(), "Test.apk")),
//				"application/vnd.android.package-archive");
//		context.startActivity(intent);
//	}
}
