package com.wehealth.mesurecg.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wehealth.mesurecg.R;
import com.wehealth.model.domain.model.ECGDataLong2Device;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ECGLongDataListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<ECGDataLong2Device> list;
	
	public ECGLongDataListAdapter(Context ctx){
		context = ctx;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<ECGDataLong2Device>();
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams") @Override
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
		
		vh.heartNum.setText(String.valueOf(list.get(position).getHeartRate()));
		if (list.get(position).getHeartRate() > 100 || list.get(position).getHeartRate() < 60) {
			vh.heartNum.setTextColor(context.getResources().getColor(R.color.text_red));
		} else {
			vh.heartNum.setTextColor(context.getResources().getColor(R.color.text_green));
		}
		
		
		vh.timeTV.setText(new Date(list.get(position).getTime()).toLocaleString());
			vh.timeTV.setTextColor(context.getResources().getColor(R.color.set_edit_text_1));
			vh.delBtn.setTextColor(context.getResources().getColor(R.color.text_white));

		
//		vh.name.setText("测试者："+dataList.get(position).getPatientName());

//		vh.delBtn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				delECGDateNotify(dataList.get(position));
//			}
//		});
		return convertView;
	}
	
	
	
//	protected void delECGDateNotify(final ECGDataLong ecgData) {
//		AlertDialog.Builder builder = new AlertDialog.Builder(context);
//		builder.setTitle(R.string.friend_notify);
//		builder.setMessage(R.string.sure_delete);
//		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//			}
//		});
//		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				edlDao.deleteECGDataL(ecgData.getFilePath());
//				list.remove(ecgData);
//					StringUtils.deleteECGFile(ecgData.getTesTime());
//					notifyDataSetChanged();
//			}
//		});
//		builder.show();
//	}

	class ViewHolder{
		TextView heartNum;
		TextView timeTV;
		TextView name;
		Button delBtn;
	}

	public void setList(List<ECGDataLong2Device> lists) {
		list.addAll(lists);
		notifyDataSetChanged();
	}

}
