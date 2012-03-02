package com.wildmind.fanwave.badge;

import java.util.ArrayList;

import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.activity.R;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BadgeProgramAdapter extends BaseAdapter {
	
	private ArrayList<String> title_list = null;
	
	/**
	 * Constructor
	 * @param list
	 */
	public BadgeProgramAdapter (ArrayList<String> list) {
		this.title_list = list;
	}
	
	/**
	 * Clear all resources.
	 */
	public void clear () {
		title_list = null;
	}
	
	@Override
	public int getCount() {
		return title_list != null ? title_list.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return title_list != null ? title_list.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public boolean areAllItemsEnabled () {
		return false;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			Context context = ApplicationManager.getAppContext();
			view = new TextView(context);
			((TextView) view).setTextColor(context.getResources().getColor(R.color.orange));
			((TextView) view).setTextSize(14);
			((TextView) view).setGravity(Gravity.CENTER_VERTICAL);
			((TextView) view).setPadding(16, 0, 0, 0);
		}
		((TextView) view).setText(title_list.get(position));
		
		return view;
	}

	/**
	 * Refresh title list data.
	 * @param list
	 */
	public void refreshData (ArrayList<String> list) {
		this.title_list = list;
		notifyDataSetChanged();
	}
}
