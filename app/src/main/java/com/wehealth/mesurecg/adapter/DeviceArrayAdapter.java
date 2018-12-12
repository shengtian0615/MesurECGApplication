package com.wehealth.mesurecg.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wehealth.mesurecg.R;

public class DeviceArrayAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<Map<String, String>> nameList;
	
	public DeviceArrayAdapter(Context context) {
		mContext = context;
		nameList = new ArrayList<Map<String,String>>();
	}

	@Override
	public int getCount() {
		return nameList.size();
	}

	@Override
	public Object getItem(int position) {
		return nameList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.device_name, parent, false);
		}

		TextView tv = (TextView) convertView.findViewById(R.id.device_name);
		Map<String, String> map = nameList.get(position);
		tv.setText(map.get("title")+"\n"+map.get("description"));
		convertView.setBackgroundColor(mContext.getResources().getColor(R.color.page_background_color));
		tv.setTextColor(mContext.getResources().getColor(R.color.text_white));

		return convertView;
	}
	
	public void setNameList(List<Map<String, String>> nameList) {
		this.nameList = nameList;
		this.notifyDataSetChanged();
	}
	
	public void adDeviceName(Map<String, String> name){
		this.nameList.add(name);
		this.notifyDataSetChanged();
	}

}
