package com.wehealth.mesurecg.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wehealth.mesurecg.R;

/**
 * 对话框辅助类
 */
public class DialogHelp {

    /***
     * 获取一个dialog
     * @param context
     * @return
     */
    public static AlertDialog.Builder getDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialog);
        return builder;
    }
    
    @SuppressLint("InflateParams")
	public static AlertDialog.Builder getMyMessageDialog(Context context, String title, String[] message) {
        AlertDialog.Builder builder = getDialog(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_msg, null);
        TextView t1 = (TextView) view.findViewById(R.id.dialog_title);
        TextView t2 = (TextView) view.findViewById(R.id.dialog_content);
        ImageView imgview = (ImageView) view.findViewById(R.id.dialog_img);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.dialog_layout);
        t1.setText(title);
        t2.setText("  "+message[1]);
        if (message[0].equals("一")) {
        	imgview.setBackgroundResource(R.drawable.level_green1);
        	t1.setTextColor(context.getResources().getColor(R.color.color_029627));
        	layout.setBackgroundColor(context.getResources().getColor(R.color.color_C8FFD6));
		}
        if (message[0].equals("二")) {
        	imgview.setBackgroundResource(R.drawable.level_yellow1);
        	t1.setTextColor(context.getResources().getColor(R.color.color_FFB72C));
        	layout.setBackgroundColor(context.getResources().getColor(R.color.color_FFF0D2));
		}
        if (message[0].equals("三")) {
        	imgview.setBackgroundResource(R.drawable.level_red1);
        	t1.setTextColor(context.getResources().getColor(R.color.color_D20000));
        	layout.setBackgroundColor(context.getResources().getColor(R.color.color_FFC4C9));
        }
        layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
        return builder;
    }

    /***
     * 获取一个耗时等待对话框
     * @param context
     * @param message
     * @return
     */
    public static ProgressDialog getWaitDialog(Context context, String message) {
        ProgressDialog waitDialog = new ProgressDialog(context);
        if (!TextUtils.isEmpty(message)) {
            waitDialog.setMessage(message);
        }
        return waitDialog;
    }

    /***
     * 获取一个信息对话框，注意需要自己手动调用show方法显示
     * @param context
     * @param message
     * @param onClickListener
     * @return
     */
    public static AlertDialog.Builder getMessageDialog(Context context, String message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setMessage(message);
        builder.setPositiveButton("确定", onClickListener);
        return builder;
    }

    public static AlertDialog.Builder getMessageDialog(Context context, String message) {
        return getMessageDialog(context, message, null);
    }

    public static AlertDialog.Builder getTitleMessageDialog(Context context, String title, String message) {
    	AlertDialog.Builder builder = getDialog(context);
    	builder.setTitle(title);
        builder.setMessage(message);
        return builder;
    }
    
    public static AlertDialog.Builder getConfirmDialog(Context context, String message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setMessage(Html.fromHtml(message));
        builder.setPositiveButton("确定", onClickListener);
        builder.setNegativeButton("取消", null);
        return builder;
    }

    public static AlertDialog.Builder getConfirmDialog(Context context, String message, DialogInterface.OnClickListener onOkClickListener, DialogInterface.OnClickListener onCancleClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setMessage(message);
        builder.setPositiveButton("确定", onOkClickListener);
        builder.setNegativeButton("取消", onCancleClickListener);
        return builder;
    }

    public static AlertDialog.Builder getSelectDialog(Context context, String title, String[] arrays, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setItems(arrays, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setPositiveButton("取消", null);
        return builder;
    }

    public static AlertDialog.Builder getSelectDialog(Context context, String[] arrays, DialogInterface.OnClickListener onClickListener) {
        return getSelectDialog(context, "", arrays, onClickListener);
    }

    public static AlertDialog.Builder getSingleChoiceDialog(Context context, String title, String[] arrays, int selectIndex, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setSingleChoiceItems(arrays, selectIndex, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setNegativeButton("取消", null);
        return builder;
    }

    public static AlertDialog.Builder getSingleChoiceDialog(Context context, String[] arrays, int selectIndex, DialogInterface.OnClickListener onClickListener) {
        return getSingleChoiceDialog(context, "", arrays, selectIndex, onClickListener);
    }
}
