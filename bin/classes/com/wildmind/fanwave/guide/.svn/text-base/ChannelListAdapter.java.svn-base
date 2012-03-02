package com.wildmind.fanwave.guide;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.wildmind.fanwave.activity.GuideEpgforPgbyChActivity;
import com.wildmind.fanwave.media.ImageListener;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.activity.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ChannelListAdapter extends BaseAdapter {
	
	private Context context;
	private LayoutInflater inflater;
	private ArrayList<TVChannel> Channellist = null;
	private ListView Epg_listview;

	private ChannellistImageListener image_listener = new ChannellistImageListener();

	private ConcurrentHashMap<String, String> chicon_requests = new ConcurrentHashMap<String, String>();

	// constructor
	//
	public ChannelListAdapter ( Context _context, ListView Epg_listview, ArrayList<TVChannel> Channellist) {
		this.context = _context;
		this.Channellist = Channellist;
		this.Epg_listview = Epg_listview;
		this.inflater = LayoutInflater.from(_context);
		ImageManager.addImageListener(image_listener);
	}
	
	// instance variable getters
	//
	public ArrayList<TVChannel> getChannellist () {
		return this.Channellist;
	}
	
	// instance variable setters
	//
	public void setChannellist (ArrayList<TVChannel> list) {
		this.Channellist = list;
	}
	
	/**
	 * Clear all resources.
	 */
	public void clear () {
		context = null;
		inflater = null;
		
		if(Channellist!=null)
		Channellist.clear();
		
		Channellist = null;
		
		ImageManager.removeImageListener(image_listener);
		image_listener = null;
	}
	

	/**
	 * Refresh data.
	 * @param list
	 */
	public void refreshData (ArrayList<TVChannel> list) {
		this.Channellist = list;
		this.notifyDataSetChanged();
	}
	
	/**
	 * List View Resources Methods
	 */
	@Override
	public int getCount() {
		return Channellist!=null? Channellist.size() : 0;
	}

	@Override
	public TVChannel getItem(int position) {
		return Channellist!= null? Channellist.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getItemViewType (int position) {	
			return position;	
		}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// get more view

		final TVChannel tvc = Channellist.get(position);
		EpgViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new EpgViewHolder();
			convertView = inflater.inflate(R.layout.guide_epg_list_item_ch, null);
			viewHolder.channelbutton = (Button) convertView.findViewById(R.id.guide_epg_channel_button);
			viewHolder.chicon = (ImageView) convertView.findViewById(R.id.guide_epg_channel_imageview);
			viewHolder.channel_num = (TextView) convertView.findViewById(R.id.guide_epg_channel_id_textview);
			viewHolder.channel_name = (TextView) convertView.findViewById(R.id.guide_epg_channel_title_textview);

			convertView.setTag(viewHolder);
		} else
			viewHolder = (EpgViewHolder) convertView.getTag();
		String chnum = String.format("%03d", Integer.valueOf(tvc.getChnum()));
		viewHolder.channel_num.setText(chnum);
		viewHolder.channel_name.setText(tvc.getChname());
		viewHolder.chicon.setImageBitmap(null);
		loadChicon(viewHolder.chicon, tvc.getChcode());

		
		viewHolder.channelbutton.setOnClickListener( new OnClickListener(){		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openActivity(tvc);
			}});		
		
		return convertView;
	}

	
	private class EpgViewHolder {
		Button channelbutton;
		ImageView 	chicon;
		TextView 	channel_name;
		TextView 	channel_num;
	}
	
	
	/**
	 * Open activity with TVChannel tvc.
	 * @param TVChannel tvc
	 */
	public void openActivity (TVChannel tvc) {
		if (tvc.getChname().length() == 0)
			return;
		
		Intent intent = new Intent();
		intent.setClass(context, GuideEpgforPgbyChActivity.class);
		intent.putExtra("channel", tvc);
		context.startActivity(intent);
	}
	
	
	
	

	/**
	 * Image Process Methods
	 */
	
	
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
	private class ChannellistImageListener implements ImageListener {

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
			if (chicon_requests == null || !chicon_requests.containsKey(chcode) || bmp == null)
				return;
			
			if (Epg_listview != null) {
				Epg_listview.post( new Runnable () {
					public void run () {
						int firstPosition = Epg_listview.getFirstVisiblePosition();
						int lastPosition = Epg_listview.getLastVisiblePosition();
						
						for (int i = firstPosition; i <= lastPosition; i++) {
							if (chcode.equals(Channellist.get(i).getChcode())) {
								int childPosition = i - firstPosition;
								EpgViewHolder viewHolder = 
										(EpgViewHolder) Epg_listview.getChildAt(childPosition).getTag();
								viewHolder.chicon.setImageBitmap(bmp);
							}
						}
						
						chicon_requests.remove(chcode);
					}
				});
			}
		}
		
		
	}
}
