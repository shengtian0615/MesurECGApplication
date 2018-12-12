package com.wehealth.mesurecg.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.wehealth.mesurecg.R;
import com.wehealth.mesurecg.dao.ECGDataLong2DeviceDao;
import com.wehealth.mesurecg.utils.PreferUtils;
import com.wehealth.model.domain.model.ECGDataLong2Device;
import com.wehealth.model.util.StringUtil;

public class EcgDataLongAdapter extends BaseAdapter {

	private Context context;
	private List<ECGDataLong2Device> dataList;
	private LayoutInflater inflater;
	private String idCardNo;
	
	public EcgDataLongAdapter(Context ctx){
		context = ctx;
		inflater = LayoutInflater.from(context);
		dataList = new ArrayList<ECGDataLong2Device>();
		idCardNo = PreferUtils.getIntance().getIdCardNo();
	}
	
	public List<ECGDataLong2Device> getDataList() {
		return dataList;
	}

	public void setDataList(List<ECGDataLong2Device> dataList) {
		this.dataList = dataList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = inflater.inflate(R.layout.row_ecg_data_l, null);
			vh.timeTV = (TextView) convertView.findViewById(R.id.ecgdata_l_time);
			vh.heartNum = (TextView) convertView.findViewById(R.id.ecgdata_l_heartbeat);
			vh.name = (TextView) convertView.findViewById(R.id.ecgdata_l_name);
			vh.delBtn = (Button) convertView.findViewById(R.id.ecgdata_l_del);
			convertView.setTag(vh);
		}else {
			vh = (ViewHolder) convertView.getTag();
		}
		
		vh.heartNum.setText(String.valueOf(dataList.get(position).getHeartRate()));
		if (dataList.get(position).getHeartRate() > 100 || dataList.get(position).getHeartRate() < 60) {
			vh.heartNum.setTextColor(context.getResources().getColor(R.color.text_red));
		} else {
			vh.heartNum.setTextColor(context.getResources().getColor(R.color.text_green));
		}
		
		
		vh.timeTV.setText(new Date(dataList.get(position).getTime()).toLocaleString());
		vh.timeTV.setTextColor(context.getResources().getColor(R.color.set_edit_text_1));
		vh.delBtn.setTextColor(context.getResources().getColor(R.color.text_white));

		

		vh.delBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				delECGDateNotify(dataList.get(position));
			}
		});
		return convertView;
	}
	
	protected void delECGDateNotify(final ECGDataLong2Device ecgData) {
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
				ECGDataLong2DeviceDao.getInstance(idCardNo).deleteECGDataL(ecgData.getTime());
				dataList.remove(ecgData);
				StringUtil.deleteECGLongFile(ecgData.getTime());
				notifyDataSetChanged();
			}
		});
		builder.show();
	}

	class ViewHolder{
		TextView heartNum;
		TextView timeTV;
		TextView name;
		Button delBtn;
	}

}
