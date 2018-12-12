package com.wehealth.mesurecg.view;

import com.wehealth.mesurecg.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
/**
 * 背景图片旋转的进度框
 */
public class UIProgressDialog extends Dialog {
	private TextView msgTextView;

	public UIProgressDialog(Context context) {
		this(context, R.style.UIProgressDialog);
	}

	public UIProgressDialog(Context context, int theme) {
		super(context, theme);
		this.setContentView(R.layout.ui_progress_dialog);
		this.getWindow().getAttributes().gravity = Gravity.CENTER;
		msgTextView = (TextView) this.findViewById(R.id.ui_progress_msg);
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		if (!hasFocus) {
			dismiss();
		}
	}
	
	public void setMessage(String msg){
		msgTextView.setText(msg);
	}
	
	public void uiProgressDismiss(){
		dismiss();
	}
}