package com.wehealth.mesurecg.fragment;

import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.wehealth.mesurecg.R;
import com.wehealth.mesurecg.activity.DetailEcgHistoryReport;
import com.wehealth.mesurecg.adapter.EcgDatAdapter;
import com.wehealth.mesurecg.dao.ECGDao;
import com.wehealth.mesurecg.utils.PreferUtils;
import com.wehealth.model.domain.model.ECGData;

public class ECGHistoryFragment extends Fragment {

	private ListView listView;
	private TextView noData;
	private EcgDatAdapter adapter;
	private String idCardNo;
	private List<ECGData> dataList;

	private String ECG_DATA_SEND_KEY = "ecgdata";

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		listView = (ListView) getView().findViewById(R.id.history_listview);
		noData = (TextView) getView().findViewById(R.id.history_nodata);
		idCardNo = PreferUtils.getIntance().getIdCardNo();
		adapter = new EcgDatAdapter(getActivity());

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ECGData ecgData = (ECGData) parent.getItemAtPosition(position);
				Intent ecgInent = new Intent(getActivity(),
						DetailEcgHistoryReport.class);
				ecgInent.putExtra(ECG_DATA_SEND_KEY, ecgData);
				startActivity(ecgInent);
			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();
		dataList = getECGData();

		if (dataList == null || dataList.isEmpty()) {
			listView.setVisibility(View.GONE);
			noData.setVisibility(View.VISIBLE);
		} else {
			listView.setVisibility(View.VISIBLE);
			noData.setVisibility(View.GONE);
			Collections.sort(dataList);
			adapter.setDataList(dataList);
			listView.setAdapter(adapter);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.history_list, container, false);
	}

	private List<ECGData> getECGData() {
		return ECGDao.getECGIntance(idCardNo).getAllECGData();
	}

}
