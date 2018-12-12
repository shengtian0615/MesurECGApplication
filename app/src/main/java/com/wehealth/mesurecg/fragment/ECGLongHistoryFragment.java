package com.wehealth.mesurecg.fragment;

import java.util.Collections;
import java.util.List;

import com.wehealth.mesurecg.R;
import com.wehealth.mesurecg.activity.ReviewECGWaveActivity;
import com.wehealth.mesurecg.adapter.EcgDataLongAdapter;
import com.wehealth.mesurecg.dao.ECGDataLong2DeviceDao;
import com.wehealth.mesurecg.utils.PreferUtils;
import com.wehealth.model.domain.model.ECGDataLong2Device;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ECGLongHistoryFragment extends Fragment implements OnItemClickListener{

	private ListView listView;
	private TextView noDataTV;
	private EcgDataLongAdapter adapter;
	private String idCardNo;
	private List<ECGDataLong2Device> dataList;

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		listView = (ListView) getView().findViewById(R.id.history_listview);
		noDataTV = (TextView) getView().findViewById(R.id.history_nodata);
		idCardNo = PreferUtils.getIntance().getIdCardNo();
		adapter = new EcgDataLongAdapter(getActivity());
		dataList = getECGDataL();

		listView.setOnItemClickListener(this);
		
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.history_list, container, false);
	}
		
	private List<ECGDataLong2Device> getECGDataL() {
		return ECGDataLong2DeviceDao.getInstance(idCardNo).getECGDataLsByPatient_ID(idCardNo);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		dataList = getECGDataL();

		if (dataList==null || dataList.isEmpty()) {
			listView.setVisibility(View.GONE);
			noDataTV.setVisibility(View.VISIBLE);
		} else {
			listView.setVisibility(View.VISIBLE);
			noDataTV.setVisibility(View.GONE);
			Collections.sort(dataList);
			adapter.setDataList(dataList);
			listView.setAdapter(adapter);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ECGDataLong2Device d = (ECGDataLong2Device) arg0.getItemAtPosition(arg2);
		Intent intent = new Intent(getActivity(), ReviewECGWaveActivity.class);
		intent.putExtra("ecg_long", d);
		startActivity(intent);
	}

}
