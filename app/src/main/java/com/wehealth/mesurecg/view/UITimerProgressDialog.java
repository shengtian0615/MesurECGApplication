package com.wehealth.mesurecg.view;

import android.app.Dialog;
import android.content.Context;
import android.os.SystemClock;
import android.view.Gravity;
import android.widget.Chronometer;

import com.wehealth.mesurecg.R;

/**
 * 背景图片旋转的进度框
 */
public class UITimerProgressDialog extends Dialog {
//	private TextView msgTextView;
	private Chronometer timer;

	public UITimerProgressDialog(Context context) {
		this(context, R.style.UIProgressDialog);
	}

	public UITimerProgressDialog(Context context, int theme) {
		super(context, theme);
		this.setContentView(R.layout.ui_timer_progress_dialog);
		this.getWindow().getAttributes().gravity = Gravity.CENTER;
//		msgTextView = (TextView) this.findViewById(R.id.ui_progress_msg);
		timer = (Chronometer) this.findViewById(R.id.chronometer);
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		if (!hasFocus) {
			dismiss();
		}
	}
	
//	public void setMessage(String msg){
//		msgTextView.setText(msg);
//	}
	
	public void startTimer(){
		timer.setBase(SystemClock.elapsedRealtime());   
        //开始计时  
        timer.start();  
	}
	
	public void uiProgressDismiss(){
		timer.stop();
		dismiss();
	}
}