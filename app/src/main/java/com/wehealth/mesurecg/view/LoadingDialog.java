package com.wehealth.mesurecg.view;

import com.wehealth.mesurecg.R;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

/**
 * 加载中的对话框控件
 */
public class LoadingDialog extends Dialog {

	private Context mContext;
	private LayoutInflater inflater;
	private LayoutParams lp;
	private TextView loadtext;

	@SuppressLint("InflateParams")
	public LoadingDialog(Context context) {
		super(context, R.style.MyDialogStyle);
		
		this.mContext = context;
		
		inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.loadingdialog, null);
		loadtext = (TextView) layout.findViewById(R.id.loading_text);
		setContentView(layout);
		
		// 设置window属性
		lp = getWindow().getAttributes();
		lp.gravity = Gravity.CENTER;
		lp.dimAmount = 0; // 去背景遮盖
		lp.alpha = 1.0f;
		getWindow().setAttributes(lp);

	}

	public void setLoadText(String content){
		loadtext.setText(content);
	}
}