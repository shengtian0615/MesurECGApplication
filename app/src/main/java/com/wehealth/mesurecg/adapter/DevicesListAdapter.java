//package com.wehealth.mesurecg.adapter;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//public class DevicesListAdapter extends BaseAdapter {
//
//	private ArrayList<SiriListItem> list;
//	private LayoutInflater mInflater;
//	private Context ctx;
//
//	public DevicesListAdapter(Context context, ArrayList<SiriListItem> list2) {
//		list = list2;
//		ctx = context;
//		mInflater = LayoutInflater.from(context);
//	}
//
//	public int getCount() {
//		return list.size();
//	}
//
//	public Object getItem(int position) {
//		return list.get(position);
//	}
//
//	public long getItemId(int position) {
//		return position;
//	}
//
//	public int getItemViewType(int position) {
//		return position;
//	}
//
//	@SuppressLint("InflateParams")
//	public View getView(int position, View convertView, ViewGroup parent) {
//		ViewHolder viewHolder = null;
//		SiriListItem item = list.get(position);
//		if (convertView == null) {
//			convertView = mInflater.inflate(R.layout.list_item, null);
//			viewHolder = new ViewHolder(
//								(View) convertView.findViewById(R.id.list_child),
//								(TextView) convertView.findViewById(R.id.chat_msg));
//			convertView.setTag(viewHolder);
//		} else {
//			viewHolder = (ViewHolder) convertView.getTag();
//		}
//
//		viewHolder.msg.setText(item.message);
//		convertView.setBackgroundColor(ctx.getResources().getColor(R.color.page_background_color));
//		viewHolder.msg.setTextColor(ctx.getResources().getColor(R.color.text_white));
//
//
//		return convertView;
//	}
//
//	class ViewHolder {
//		protected View child;
//		protected TextView msg;
//
//		public ViewHolder(View child, TextView msg) {
//			this.child = child;
//			this.msg = msg;
//
//		}
//	}
//}
