package com.wehealth.mesurecg.adapter;

import android.content.Context;

import com.wehealth.mesurecg.R;
import com.wehealth.mesurecg.view.wheel.AbstractWheelTextAdapter;
import com.wehealth.model.domain.BloodModel;

import java.util.List;

public class WheelBloodAdapter extends AbstractWheelTextAdapter {

	private int type;
	private List<BloodModel> bloods;
	
	public WheelBloodAdapter(Context context, List<BloodModel> bloodList, int i) {
		super(context, R.layout.layout_station_wheel_text);
		bloods = bloodList;
		type = i;
		setItemTextResource(R.id.station_wheel_textView);
	}

	@Override
	public int getItemsCount() {
		return bloods.size();
	}

	@Override
	protected CharSequence getItemText(int index) {
		if (type==1) {
			return bloods.get(index).getDoubleValue()+"";
		}else if (type==2) {
			return bloods.get(index).getIntValue()+"";
		}
		
		return null;
	}
	
	public BloodModel getBM(int position){
			return bloods.get(position);
	}

}
