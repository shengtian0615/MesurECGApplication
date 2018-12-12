package com.wehealth.mesurecg.activity;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wehealth.mesurecg.BaseActivity;
import com.wehealth.mesurecg.R;
import com.wehealth.mesurecg.dao.ECGDataLong2DeviceDao;
import com.wehealth.mesurecg.utils.PreferUtils;
import com.wehealth.mesurecg.view.ECGDataH24Review;
import com.wehealth.mesurecg.view.ECGDataReview;
import com.wehealth.model.domain.model.ECGDataLong2Device;
import com.wehealth.model.util.StringUtil;

public class ReviewECGWaveActivity extends BaseActivity implements OnClickListener{

	private FrameLayout frameLayout;
	private ECGDataReview ecgDataReview;
	private ECGDataH24Review ecgDataH24Review;
	private RelativeLayout topLayout;
	private TextView timeSelect, titleView;
	private ListView listView;
	private Button selectBtn;
	private EditText timeCount;
	private ArrayAdapter<String> adapter;
	private boolean topLayoutVisible;
	private PopupWindow popWin;
	private String[] datas;
	private String filePath = StringUtil.getSDPath()+"/ECGDATA/Data2Device/";
	private String serialNo, idCardNo;
	private File[] file;
	/**等于2时表示24小时单导；等于1时为手动模式，12导数据**/
	private int SAVEFILE_TYPE;
	
	private int maxTotalCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ecglong_detail_review);
		frameLayout = (FrameLayout) findViewById(R.id.review_ecglong);
		topLayout = (RelativeLayout) findViewById(R.id.title_bar);
		
		titleView = (TextView) findViewById(R.id.page_title);
		timeSelect = (TextView) findViewById(R.id.review_ecgl_time);
		titleView.setText("心电数据回顾");
		timeSelect.setText("选择时间段");
		timeSelect.setOnClickListener(this);
		topLayoutVisible = true;
		serialNo = PreferUtils.getIntance().getSerialNo();
		idCardNo = PreferUtils.getIntance().getIdCardNo();
		Intent intent = getIntent();
		ECGDataLong2Device edl2d = (ECGDataLong2Device) intent.getSerializableExtra("ecg_long");
		if (edl2d==null) {
			Toast.makeText(this, "数据为空，无法查看", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		SAVEFILE_TYPE = edl2d.getSaveFileType();
		if (SAVEFILE_TYPE==2) {//24小时数据
			filePath = filePath+"/Hours24/";
			File file24H = new File(filePath+edl2d.getH24_path()+"_0.txt");
			if (!file24H.exists()) {
				ECGDataLong2DeviceDao.getInstance(idCardNo).deleteECGDataL(edl2d.getTime());
				Toast.makeText(this, "本地数据为空，无法查看", Toast.LENGTH_SHORT).show();
				finish();
				return;
			}
			maxTotalCount = edl2d.getTotalTime();
			ecgDataH24Review = new ECGDataH24Review(this, edl2d);
			frameLayout.addView(ecgDataH24Review);
			int count = edl2d.getTotalTime();
			if (count==0) {
				timeSelect.setVisibility(View.GONE);
			}else if (count>0) {
				datas = new String[count];
				for (int i = 0; i < count; i++) {
					datas[i] = "第"+(i+1)+"分钟";
				}
				adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datas);
			}
		}
		
		if(SAVEFILE_TYPE==1){//手动长时间数据
			boolean fileExists = checkIsFilExists(edl2d);
			if (!fileExists) {
				ECGDataLong2DeviceDao.getInstance(idCardNo).deleteECGDataL(edl2d.getTime());
				Toast.makeText(this, "本地数据为空，无法查看", Toast.LENGTH_SHORT).show();
				finish();
				return;
			}
			maxTotalCount = edl2d.getTotalTime();
			ecgDataReview = new ECGDataReview(this, edl2d);
			frameLayout.addView(ecgDataReview);
			int count = edl2d.getTotalTime();
			if (count==0) {
				timeSelect.setVisibility(View.GONE);
			}else if (count>0) {
				datas = new String[count];
				for (int i = 0; i < count; i++) {
					datas[i] = "第"+(i+1)+"分钟";
				}
				adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datas);
			}
		}
	}
	
	/**判断文件是否存在，有不存在的文件返回false**/
	private boolean checkIsFilExists(ECGDataLong2Device edl2d) {
		file = new File[12];
		file[0] = new File(filePath+edl2d.getAVF_path());
		file[1] = new File(filePath+edl2d.getAVL_path());
		file[2] = new File(filePath+edl2d.getAVR_path());
		file[3] = new File(filePath+edl2d.getI_path());
		file[4] = new File(filePath+edl2d.getII_path());
		file[5] = new File(filePath+edl2d.getIII_path());
		file[6] = new File(filePath+edl2d.getV1_path());
		file[7] = new File(filePath+edl2d.getV2_path());
		file[8] = new File(filePath+edl2d.getV3_path());
		file[9] = new File(filePath+edl2d.getV4_path());
		file[10] = new File(filePath+edl2d.getV5_path());
		file[11] = new File(filePath+edl2d.getV6_path());
		for (int i = 0; i < file.length; i++) {
			if (!file[i].exists()) {
				return false;
			}
		}
		return true;
	}

	public void onBackBottonClick(View view) {
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.review_ecgl_time:
			initPopWin();
			break;
		case R.id.ecglong_timeselect:
			String position = timeCount.getText().toString();
			int posi = Integer.valueOf(position);
			if (posi>maxTotalCount || posi<0) {
				timeCount.setText("");
				Toast.makeText(this, "请输入正确数值", Toast.LENGTH_SHORT).show();
				return;
			}
			if (SAVEFILE_TYPE == 2) {
				ecgDataH24Review.setOffsetIndex(Integer.valueOf(position));
			}else if(SAVEFILE_TYPE == 1){
				ecgDataReview.setOffsetIndex(Integer.valueOf(position));
			}
			if (popWin!=null) {
				popWin.dismiss();
			}
			break;

		default:
			break;
		}
	}

	@SuppressLint("InflateParams")
	private void initPopWin() {
		View popupView = getLayoutInflater().inflate(R.layout.ecglong_timepopw, null);

		popWin = new PopupWindow(popupView, 400, 450, true);
		popWin.setTouchable(true);
		popWin.setOutsideTouchable(true);
		popWin.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
		
		selectBtn = (Button) popupView.findViewById(R.id.ecglong_timeselect);
		timeCount = (EditText) popupView.findViewById(R.id.ecglong_timecount);
		listView = (ListView) popupView.findViewById(R.id.ecglong_timelv);
		timeCount.setHint("请输入1~~"+maxTotalCount+"之间的数");
		listView.setAdapter(adapter);
		selectBtn.setOnClickListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				popWin.dismiss();
				String timeStr = (String) arg0.getItemAtPosition(arg2);
				String time = timeStr.replace("第", "").replace("分钟", "");
				if (SAVEFILE_TYPE == 2) {
					ecgDataH24Review.setOffsetIndex(Integer.valueOf(time));
				}else if(SAVEFILE_TYPE == 1){
					ecgDataReview.setOffsetIndex(Integer.valueOf(time));
				}
			}
		});
		popWin.showAsDropDown(timeSelect);
	}

	public void setTopLayoutVisible(boolean b) {
		if (b && !topLayoutVisible) {
			topLayout.setVisibility(View.VISIBLE);
			topLayoutVisible = true;
		}
		if (!b && topLayoutVisible) {
			topLayout.setVisibility(View.GONE);
			topLayoutVisible = false;
		}
	}
}
