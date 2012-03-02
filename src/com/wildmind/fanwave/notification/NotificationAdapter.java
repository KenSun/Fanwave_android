package com.wildmind.fanwave.notification;

import java.util.ArrayList;
import java.util.Map;

import com.wildmind.fanwave.activity.R;

import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class NotificationAdapter extends BaseAdapter {
	
	private ListView notif_listview;
	private LayoutInflater inflater;
	
	private ArrayList<SystemNotification> systems = null;
	private ArrayList<Map<String, String>> generals = null;
	
	/**
	 * Constructor
	 * @param listview
	 * @param systems
	 * @param context
	 */
	public NotificationAdapter (ListView listview, ArrayList<SystemNotification> systems, Context context) {
		this.notif_listview = listview;
		this.systems = systems;
		this.inflater = LayoutInflater.from(context);
	}
	
	/**
	 * Clear all resources.
	 */
	public void clear () {
		notif_listview = null;
		systems = null;
		inflater = null;
	}
	
	@Override
	public int getCount() {
		return (systems != null) ? (systems.size()+((generals !=null)?generals.size():0)) : 0;
	}

	@Override
	public Object getItem(int position) {
		return (systems != null) ? ((position>=systems.size())?generals.get(position-systems.size()):systems.get(position)) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public boolean isEnabled (int position) {
		SystemNotification system = (position<systems.size())?(SystemNotification) getItem(position):null;
		return (system!=null)?!system.getType().equals(SystemNotification.TYPE_MESSAGE):false;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		Object notif = getItem(position);
		ViewHolder viewHolder;
		if (view == null) {
			view = inflater.inflate(R.layout.notif_row_system, null);
			
			viewHolder = new ViewHolder();
			viewHolder.icon = (ImageView) view.findViewById(R.id.icon_imageview);
			viewHolder.nickname = (TextView) view.findViewById(R.id.nickname_textview);
			viewHolder.title = (TextView) view.findViewById(R.id.title_textview);
			view.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) view.getTag();
		
		// Icon
		
		// Title
		viewHolder.title.setText((position<systems.size())?((SystemNotification)notif).getContent():Html.fromHtml(((Map<String,String>)notif).get("content")));
		viewHolder.title.setTextSize((position<systems.size())?14:15);
		viewHolder.nickname.setVisibility((position<systems.size())?View.VISIBLE:View.GONE);
		
		return view;
	}
	
	/**
	 * Refresh data.
	 * @param systems
	 */
	public void refreshData (ArrayList<SystemNotification> systems, ArrayList<Map<String, String>> generals) {
		this.systems = systems;
		this.generals=generals;
		notifyDataSetChanged();
	}
	
	/**
	 * View Holder class
	 * @author Kencool
	 *
	 */
	private class ViewHolder {
		ImageView 	icon;
		TextView 	nickname;
		TextView 	title;
	}
}
