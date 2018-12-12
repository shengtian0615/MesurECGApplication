package com.wehealth.mesurecg.adapter;

import java.util.ArrayList;
import java.util.List;

import com.wehealth.mesurecg.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BloodModelAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<String> bloodTypes;
	
	public BloodModelAdapter(Context mContext){
		this.context = mContext;
		inflater = LayoutInflater.from(context);
		bloodTypes = new ArrayList<String>();
	}
	@Override
	public int getCount() {
		return bloodTypes.size();
	}

	@Override
	public Object getItem(int arg0) {
		return bloodTypes.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.blood_model_item, parent, false);
			holder.name = (TextView) convertView.findViewById(R.id.blood_model_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(bloodTypes.get(position));
		return convertView;
	}
	
	static class ViewHolder {
		private TextView name;
	}

	public void setNameList(String[] strs) {
		this.bloodTypes.clear();
		for (String str : strs) {
			this.bloodTypes.add(str);
		}
		this.notifyDataSetChanged();
	}

}
