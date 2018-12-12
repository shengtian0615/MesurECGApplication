package com.wehealth.mesurecg.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.wehealth.mesurecg.R;

/**
 * 背景图片旋转的进度框
 */
public class TransparentPDialog extends Dialog {
	private TextView msgTextView;

	public TransparentPDialog(Context context) {
		this(context, R.style.halfTransparentDialog);
	}

	public TransparentPDialog(Context context, int theme) {
		super(context, theme);
		this.setContentView(R.layout.transparent_progress_dialog);
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