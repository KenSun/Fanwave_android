package com.wildmind.fanwave.guide;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class ListViewAdapter extends BaseAdapter {



    private ArrayList<ListView> ListViewlist = new ArrayList<ListView>();
    
    
	public ListViewAdapter ( ArrayList<ListView> List) {
		this.ListViewlist = List;
	}
	
	/**
	 * Refresh data.
	 * @param ArrayList
	 */
	public void refreshData (ArrayList<ListView> list) {
		this.ListViewlist = list;
		this.notifyDataSetChanged();
	}

	/**
	 * Clear all resources.
	 */
	public void clear () {
		if(ListViewlist!=null)
			ListViewlist.clear();
		ListViewlist = null;
	}
	
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return ListViewlist.size();
    }

    @Override
    public int getCount() {
        return ListViewlist != null ? ListViewlist.size() : 0;
    }

    @Override
    public ListView getItem(int position) {
		return ListViewlist!= null? ListViewlist.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       convertView = getItem(position);
        return convertView;
    }


}
