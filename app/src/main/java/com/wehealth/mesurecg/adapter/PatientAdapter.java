package com.wehealth.mesurecg.adapter;

import java.util.ArrayList;
import java.util.List;

import com.wehealth.mesurecg.R;
import com.wehealth.model.domain.model.Patient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PatientAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<Patient> patients;
	
	public PatientAdapter(Context mContext){
		this.context = mContext;
		inflater = LayoutInflater.from(context);
		patients = new ArrayList<Patient>();
	}
	@Override
	public int getCount() {
		return patients.size();
	}

	@Override
	public Object getItem(int arg0) {
		return patients.get(arg0);
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
			convertView = inflater.inflate(R.layout.patient_item, parent, false);
			holder.name = (TextView) convertView.findViewById(R.id.patient_item_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(patients.get(position).getName());
		return convertView;
	}
	
	static class ViewHolder {
		private TextView name;
	}

	public void setNameList(List<Patient> pats) {
		this.patients.clear();
		this.patients.addAll(pats);
		this.notifyDataSetChanged();
	}

}
