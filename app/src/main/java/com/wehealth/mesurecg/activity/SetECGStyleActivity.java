package com.wehealth.mesurecg.activity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.wehealth.mesurecg.BaseActivity;
import com.wehealth.mesurecg.R;
import com.wehealth.mesurecg.utils.PreferUtils;

public class SetECGStyleActivity extends BaseActivity {

	private CheckBox oneMinRB, hourRB;
	private int saveFile;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setecg_style);
		
		initView();
	}

	private void initView() {
		oneMinRB = (CheckBox) findViewById(R.id.setecg_senior_1min);
		hourRB = (CheckBox) findViewById(R.id.setecg_senior_24h);
		
		saveFile = PreferUtils.getIntance().getSaveFileStyle();
		if (saveFile == 1) {
			oneMinRB.setChecked(true);
		}
		if (saveFile == 2) {
			hourRB.setChecked(true);
		}
		
		oneMinRB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				oneMinRB.setChecked(isChecked);
				if (isChecked) {
					hourRB.setChecked(false);
					PreferUtils.getIntance().setSaveFileStyle(1);
					PreferUtils.getIntance().setDisplayStyle(0);
				}else {
					PreferUtils.getIntance().setSaveFileStyle(0);
					PreferUtils.getIntance().setDisplayStyle(0);
				}
			}
		});
		
		hourRB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				hourRB.setChecked(isChecked);
				if (isChecked) {
					oneMinRB.setChecked(false);
					PreferUtils.getIntance().setSaveFileStyle(2);
					PreferUtils.getIntance().setDisplayStyle(2);
				}else {
					PreferUtils.getIntance().setSaveFileStyle(0);
					PreferUtils.getIntance().setDisplayStyle(0);
				}
			}
		});
	}

}
