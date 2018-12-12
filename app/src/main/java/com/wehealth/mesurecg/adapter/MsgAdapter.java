package com.wehealth.mesurecg.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wehealth.mesurecg.R;
import com.wehealth.mesurecg.dao.AppNotificationMessageDao;
import com.wehealth.mesurecg.dao.ECGDao;
import com.wehealth.mesurecg.utils.PreferUtils;
import com.wehealth.model.domain.enumutil.NotificationMesageStatus;
import com.wehealth.model.domain.enumutil.NotifyDoctorAskStatus;
import com.wehealth.model.domain.model.AppNotificationMessage;
import com.wehealth.model.domain.model.ECGData;
import com.wehealth.model.util.Constant;
import com.wehealth.model.util.ECGDataUtil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MsgAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<AppNotificationMessage> listMsgs;
	private Context context;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String idCardNo;
	
	public MsgAdapter(Context ctx){
		context = ctx;
		idCardNo = PreferUtils.getIntance().getIdCardNo();
		inflater = LayoutInflater.from(ctx);
		listMsgs = new ArrayList<AppNotificationMessage>();
	}
	@Override
	public int getCount() {
		if (listMsgs==null) {
			return 0;
		}
		return listMsgs.size();
	}

	@Override
	public Object getItem(int position) {
		if (listMsgs==null) {
			return null;
		}
		return listMsgs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = inflater.inflate(R.layout.msg_item, null);
			vh.timeTV = (TextView) convertView.findViewById(R.id.msg_item_time);
			vh.checkResult = (TextView) convertView.findViewById(R.id.msg_item_result);
			vh.idcodeTV = (TextView) convertView.findViewById(R.id.msg_item_idcode);
			vh.readState = (ImageView) convertView.findViewById(R.id.msg_item_state);
			vh.delBtn = (Button) convertView.findViewById(R.id.msg_item_delete);
			convertView.setTag(vh);
		}else {
			vh = (ViewHolder) convertView.getTag();
		}
			vh.timeTV.setTextColor(context.getResources().getColor(R.color.set_edit_text_1));
			vh.checkResult.setTextColor(context.getResources().getColor(R.color.text_white));
			vh.idcodeTV.setTextColor(context.getResources().getColor(R.color.text_white));

		final AppNotificationMessage msg = listMsgs.get(position);
		if (Constant.MSG_ECG_Free_Check_Request.equals(msg.getSubject())) {
			vh.idcodeTV.setText("用户ID："+msg.getMsgPatientIdCardNo());
			vh.checkResult.setText("医生："+msg.getDoctorName()+"  读图结论："+ECGDataUtil.getResuByJson(msg.getMessage()));
			vh.timeTV.setText(new Date(msg.getTime()).toString());
		}else if (Constant.Manual_Diagnosis.equals(msg.getSubject())) {
			vh.idcodeTV.setText("读图费用");
			vh.checkResult.setText("心电读图扣费："+msg.getMessage());
		}else if (Constant.Order_Diagnosis.equals(msg.getSubject())) {
			vh.idcodeTV.setText("咨询费用");
			vh.checkResult.setText("咨询医生扣费："+msg.getMessage());
		}else if(Constant.MSG_ECG_NEW_TESTED_DATA.equals(msg.getSubject())){
			vh.idcodeTV.setText("自动分析报告");
			String classStr = ECGDataUtil.getClassByJson(msg.getMessage());
			String resutStr = ECGDataUtil.getResuByJson(msg.getMessage());
			if (TextUtils.isEmpty(classStr) || TextUtils.isEmpty(resutStr)) {
				vh.checkResult.setText("自动分析结论"+" : "+msg.getMessage());
			}else {
				vh.checkResult.setText("自动分析结论"+" : "+classStr+"  "+resutStr);
			}
		}else {
			vh.idcodeTV.setText(msg.getSubject());
			vh.checkResult.setText(msg.getMessage());
		}
		
		if (NotificationMesageStatus.UNREAD.ordinal()==msg.getStatus().ordinal()) {
			vh.readState.setVisibility(View.VISIBLE);
		}else {
			vh.readState.setVisibility(View.INVISIBLE);
		}
		vh.timeTV.setText(sdf.format(new Date(msg.getTime())));
		vh.delBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				delAppMsgNotify(msg);
			}
		});
		return convertView;
	}
	
	protected void delAppMsgNotify(final AppNotificationMessage msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.friend_notify);
		builder.setMessage(R.string.sure_delete);
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (Constant.MSG_ECG_Free_Check_Request.equals(msg.getSubject())) {
					ECGData ecgData = ECGDao.getECGIntance(idCardNo).getDataByTime(msg.getTestTime());
					if (ecgData==null) {
						AppNotificationMessageDao.getAppInstance(idCardNo).deleteMessage(msg.getMsgId());
					}else {
						msg.setAskStatus(NotifyDoctorAskStatus.UNASK);
						msg.setStatus(NotificationMesageStatus.READ);
						AppNotificationMessageDao.getAppInstance(idCardNo).updateMessage(msg);
					}
				}else {
					AppNotificationMessageDao.getAppInstance(idCardNo).deleteMessage(msg.getMsgId());
				}
				List<AppNotificationMessage> msgs = AppNotificationMessageDao.getAppInstance(idCardNo).getAllMessageList();
				setMsgs(msgs);
			}
		});
		builder.show();
	}

	class ViewHolder {
		ImageView readState;
		TextView timeTV;
		TextView idcodeTV;
		TextView checkResult;
		Button delBtn;
	}

	public void setMsgs(List<AppNotificationMessage> listMsg) {
		listMsgs = listMsg;
		notifyDataSetChanged();
	}
}
