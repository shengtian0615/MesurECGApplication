package com.wehealth.mesurecg.activity;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wehealth.mesurecg.BaseActivity;
import com.wehealth.mesurecg.R;
import com.wehealth.mesurecg.adapter.MsgAdapter;
import com.wehealth.mesurecg.dao.AppNotificationMessageDao;
import com.wehealth.mesurecg.utils.PreferUtils;
import com.wehealth.model.domain.enumutil.NotificationMesageStatus;
import com.wehealth.model.domain.model.AppNotificationMessage;

public class MessageActivity extends BaseActivity implements OnItemClickListener, Observer{

	private MsgAdapter msgAdapter;
	private ListView msgListView;
	private TextView noMsg;
	private LinearLayout backLayout;
	private RelativeLayout titleLayout;
	private String idCardNo;
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==10) {
				List<AppNotificationMessage> listMsgs = AppNotificationMessageDao.getAppInstance(idCardNo).getAllMessageList();
				if (listMsgs==null || listMsgs.isEmpty()) {
					noMsg.setVisibility(View.VISIBLE);
					msgListView.setVisibility(View.GONE);
					return;
				}
				noMsg.setVisibility(View.GONE);
				msgListView.setVisibility(View.VISIBLE);
				msgAdapter.setMsgs(listMsgs);
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg);
		
		msgListView = (ListView) findViewById(R.id.msg_listview);
		noMsg = (TextView) findViewById(R.id.msg_null);
		backLayout = (LinearLayout) findViewById(R.id.background);
		titleLayout =  (RelativeLayout) findViewById(R.id.title_layout);
		msgAdapter = new MsgAdapter(this);
		msgListView.setAdapter(msgAdapter);
		idCardNo = PreferUtils.getIntance().getIdCardNo();
		
		msgListView.setOnItemClickListener(this);
		reflushStyle();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		handler.sendEmptyMessageDelayed(10, 50);
//		BaseNotifyObserver.getInstance().addObserver(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
//		BaseNotifyObserver.getInstance().deleteObserver(this);
	}
	
	private void reflushStyle() {
			backLayout.setBackgroundColor(getResources().getColor(R.color.page_background_color));
			titleLayout.setBackgroundColor(getResources().getColor(R.color.page_title_bar_color));

	}
	
	public void onBackBottonClick(View view) {
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		AppNotificationMessage appMsg = (AppNotificationMessage) parent.getItemAtPosition(position);
		if (appMsg==null) {
			return;
		}
		appMsg.setStatus(NotificationMesageStatus.READ);
		AppNotificationMessageDao.getAppInstance(idCardNo).updateMessage(appMsg);

//		Intent intent = new Intent(this, DetailMsgActivity.class);
//		intent.putExtra("msg", appMsg);
//		startActivity(intent);
	}

	@Override
	public void update(Observable observable, Object data) {
		if (data instanceof AppNotificationMessage) {
			handler.sendEmptyMessageDelayed(10, 100);
		}
	}
}
