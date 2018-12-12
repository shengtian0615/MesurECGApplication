package com.wehealth.mesurecg.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.wehealth.mesurecg.BaseActivity;
import com.wehealth.mesurecg.R;

public class AboutActivity extends BaseActivity {

	WebView webView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		webView = (WebView) findViewById(R.id.about_webview);
		webView.loadUrl("file:///android_asset/patient_registe_agreement.html");
	}
	
	public void onBackBottonClick(View view){
		 finish();
	}
}
