package com.wildmind.fanwave.guide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.wildmind.fanwave.media.ImageListener;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.animation.AlphaSetter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class HidderAdapter extends BaseAdapter {
	
	private listImageListener image_listener = new listImageListener();
	private ConcurrentHashMap<String, String> chicon_requests = new ConcurrentHashMap<String, String>();
	private ListView guide_list_listview;
	private ArrayList<TVChannel> Channels = null;
	private HashMap<String, TVChannel> selected_channels = new HashMap<String, TVChannel>();
	private LayoutInflater inflater;
	private HidderGroupListener listener = null;

	
	// constructor
	//
	public HidderAdapter ( Context _context, ListView Epg_listview) {
		this.guide_list_listview = Epg_listview;
		this.inflater = LayoutInflater.from(_context);
		ImageManager.addImageListener(image_listener);
	}
	
	/**
	 * Set splash group listener.
	 * @param listener
	 */
	public void setHidderGroupListener (HidderGroupListener listener) {
		this.listener = listener;
	}
	
	/**
	 * Clear all resources.
	 */
	public void clear () {
		inflater = null;
		guide_list_listview = null;
		ImageManager.removeImageListener(image_listener);
		image_listener = null;
		listener = null;
		Channels.clear();
		Channels = null;
		chicon_requests.clear();
		chicon_requests = null;
	}
	
	/**
	 * Refresh data.
	 * @param list
	 */
	public void refreshData (ArrayList<TVChannel> list) {
		this.Channels = list;
		for(TVChannel tvc : Channels){
			if(tvc.getIsVisible())
					selected_channels.put(tvc.getChcode(), tvc);
				else
					if(selected_channels.containsKey(tvc.getChcode()))
						selected_channels.remove(tvc.getChcode());
		}
		if (selected_channels.size() == Channels.size())
			listener.onAllSelected();
		
		this.notifyDataSetChanged();
	}
	
	
	public boolean isSelectAll(){
		if (selected_channels.size() == Channels.size())
			return true;
		else
			return false;
		
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return Channels != null ? Channels.size() : 0;
	}
	
	
	public ArrayList<TVChannel> getChannels() {
		// TODO Auto-generated method stub
		return Channels;
	}

	@Override
	public TVChannel getItem(int position) {
		// TODO Auto-generated method stub
		return Channels!= null? Channels.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final TVChannel tvc = Channels.get(position);

		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.guide_channel_hidder_listitem, null);
			viewHolder.chnum = (TextView)convertView.findViewById(R.id.channel_id_textview);
			viewHolder.chname = (TextView)convertView.findViewById(R.id.channel_title_textview);
			viewHolder.logo = (ImageView)convertView.findViewById(R.id.channel_icon_imageview);
			viewHolder.cb = (CheckBox)convertView.findViewById(R.id.channel_select_checkbox);
			convertView.setTag(viewHolder);
		}else 
			viewHolder = (ViewHolder) convertView.getTag();
		final View view = convertView;
		setView(view, tvc.getIsVisible());
				
		viewHolder.cb.setChecked(tvc.getIsVisible());
		String chnum = String.format("%03d", Integer.valueOf(tvc.getChnum()));
		viewHolder.chnum.setText(chnum);
		viewHolder.chname.setText(tvc.getChname());
		viewHolder.logo.setImageBitmap(null);		
		loadChicon(viewHolder.logo, tvc.getChcode());

		viewHolder.cb.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				boolean select = tvc.getIsVisible();

				if (select)
					unselectUser(tvc, view);
				else
					selectUser(tvc, view);
			}
			
		});
		

		return convertView;
	}
		
	/**
	 * Select all.
	 */
	public void selectAll () {
		for (TVChannel tvc:Channels){
			tvc.setIsVisible(true);
			selected_channels.put(tvc.getChcode(), tvc);	
		}		

		this.notifyDataSetChanged();
				
		if (listener != null) {
			listener.onAllSelected();
		}
	}
	
	/**
	 * Unselect all.
	 */
	public void unselectAll () {
		for (TVChannel tvc:Channels){
			tvc.setIsVisible(false);
		}	
		selected_channels.clear();
		this.notifyDataSetChanged();
		
		if (listener != null) {
			listener.onAllSelectedCancel();
		}
		
	}
	
	/**
	 * Select user.
	 * @param position
	 * @param view
	 */
	public void selectUser (TVChannel tvc, View view) {
		selected_channels.put(tvc.getChcode(), tvc);	

		ViewHolder viewHolder = (ViewHolder) view.getTag();
		viewHolder.cb.setSelected(true);
    	setHidder(tvc.getChcode(), true);
    	setView(view, true);
		if (listener != null) {			
			if (selected_channels.size() == Channels.size())
				listener.onAllSelected();
		}
	}
	
	/**
	 * Unselect user.
	 * @param position
	 * @param view
	 */
	public void unselectUser (TVChannel tvc, View view) {
		if(selected_channels.containsKey(tvc.getChcode()))
			selected_channels.remove(tvc.getChcode());
		
		ViewHolder viewHolder = (ViewHolder) view.getTag();
		viewHolder.cb.setSelected(false);
    	setHidder(tvc.getChcode(), false);
    	setView(view, false);
		if (listener != null) {
			if (selected_channels.size() < Channels.size())
				listener.onAllSelectedCancel();
		}
	}
	
	
	private class ViewHolder {
		CheckBox 	cb;
		ImageView 	logo;
		TextView 	chnum;
		TextView 	chname;
	}
	
	/**
	 * Load ChannelIcon.
	 * @param chname
	 * @param chicon_url
	 */		 	
	 private void loadChicon (ImageView iv, String chcode) {
		if (chcode == null || chcode.length() == 0)
			return;			
		if (!ImageManager.drawChannelIcon(iv, chcode))
			chicon_requests.put(chcode, "");
	 }
	
	/**
	 * Listener for listening image loading request.
	 * @author Kencool
	 *
	 */
	 private class listImageListener implements ImageListener {
	
		@Override
		public void retrieveAvatar(final String username, final Bitmap bmp) {
			// TODO Auto-generated method stub
		}
	
		@Override
		public void retrieveBadge(String badge_id, boolean scaled, Bitmap bmp) {
			// TODO Auto-generated method stub
		}
	
		@Override
		public void retrieveAttach(String token, boolean is_thumb, Bitmap bmp) {
			// TODO Auto-generated method stub
		}
	
		@Override
		public void retrieveProgramIcon(String title, int sampleBase, Bitmap bmp) {
		}

		@Override
		public void retrieveChannelIcon(final String chcode, final Bitmap bmp) {
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			if (chicon_requests == null || !chicon_requests.containsKey(chcode)||bmp == null)
				return;
				
			if (guide_list_listview != null) {
				guide_list_listview.post( new Runnable () {
					public void run () {
						if (guide_list_listview == null) 
							return;
						
						int firstPosition = guide_list_listview.getFirstVisiblePosition();
						int lastPosition = guide_list_listview.getLastVisiblePosition();
						
						for (int i = firstPosition; i <= lastPosition; i++) {
							if (chcode.equals(Channels.get(i).getChcode())) {
								int childPosition = i - firstPosition;
								ViewHolder viewHolder = 
										(ViewHolder) guide_list_listview.getChildAt(childPosition).getTag();
								viewHolder.logo.setImageBitmap(bmp);
							}
						}
						
						chicon_requests.remove(chcode);
					}
				});
			}
		}
			
			
	}
	 /**
	  * 
	  * @param chcode
	  * @param isVisible
	  */
	 public void setHidder(String chcode, boolean isVisible){
		  for(TVChannel tvc : Channels){
			  if(tvc.getChcode().equals(chcode))
				  tvc.setIsVisible(isVisible);
	       }
	 }
		
	 public void setView(View v , boolean isVisible){
		 if(isVisible){
			v.startAnimation(new AlphaSetter(1f));
			v.setEnabled(true);
		 }else{
			v.startAnimation(new AlphaSetter(0.3f));
			v.setEnabled(false);
		 }
		
	 }
	/**
	 * Hidder Group Listener interface
	 * @author 
	 *
	 */
	public interface HidderGroupListener {
						
		public void onAllSelected();
			
		public void onAllSelectedCancel();
	}
			
}
