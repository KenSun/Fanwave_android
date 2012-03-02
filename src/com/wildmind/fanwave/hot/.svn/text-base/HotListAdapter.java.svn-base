package com.wildmind.fanwave.hot;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.guide.ChannelManager;
import com.wildmind.fanwave.guide.TVChannel;
import com.wildmind.fanwave.media.ImageListener;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.media.ImageManager.SampleBase;
import com.wildmind.fanwave.media.ProgramIconManager;
import com.wildmind.fanwave.util.StringGenerator;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class HotListAdapter extends BaseAdapter {
	
	public static final int TYPE_LIST_HOT 	= 0;
	public static final int TYPE_LIST_GUIDE = 1;

	public static int Hot = 0;
	public static int Event = 1;
	
	private LayoutInflater inflater;
	private ListView hot_listview;
	private int list_type = -1;
	private int type = 0;
	
	private boolean subtitle = false;

	private ArrayList<TVHot> hotlist = null;
	private ArrayList<TVEvent> eventlist = null;
	private HotListImageListener image_listener = new HotListImageListener();
	private ConcurrentHashMap<String, String> avatar_requests = new ConcurrentHashMap<String, String>();
	private ConcurrentHashMap<String, String> pgicon_requests = new ConcurrentHashMap<String, String>();

	// constructor
	//
	public HotListAdapter (Context _context, ListView hot_listview, int list_type) {
		this.inflater = LayoutInflater.from(_context);
		this.hot_listview = hot_listview;
		this.list_type = list_type;
		
		ImageManager.addImageListener(image_listener);
	}
	
	/**
	 * Clear all resources.
	 */
	public void clear () {
		inflater = null;
		hot_listview = null;
		hotlist = null;
		
		ImageManager.removeImageListener(image_listener);
		image_listener = null;
		
		if(avatar_requests!=null)
			avatar_requests.clear();
		avatar_requests = null;
		
		if(pgicon_requests!=null)
			pgicon_requests.clear();
		pgicon_requests = null;
	}
	

	/**
	 * Refresh data.
	 * @param list
	 * @param is_more
	 */
	public void refreshData (ArrayList<TVHot> hotlist, ArrayList<TVEvent> eventlist, int type, boolean subtitle) {
		this.type = type;
		this.subtitle = subtitle;
		if(hotlist!=null)
			this.hotlist = hotlist;
		if(eventlist!=null)
			this.eventlist = eventlist;
		this.notifyDataSetChanged();
	}
	
	/**
	 * List View Resources Methods
	 */
	@Override
	public int getCount() {
		if(type == Hot)
			return hotlist != null ? hotlist.size() : 0;
		else
			return eventlist != null ? eventlist.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		if(type == Hot)
			return hotlist != null ? hotlist.get(position) : null;
		else
			return eventlist != null ? eventlist.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder viewHolder;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = inflater.inflate(R.layout.hot_row, null);
			viewHolder.ranking = (TextView) view.findViewById(R.id.ranking_textview);
			viewHolder.title = (TextView) view.findViewById(R.id.title_textview);
			viewHolder.channel = (TextView) view.findViewById(R.id.channel_textview);
			viewHolder.avatar = (ImageView) view.findViewById(R.id.avatar_imageview);
			viewHolder.pgicon = (ImageView) view.findViewById(R.id.pgicon_imageview);
			viewHolder.topfan_layout = (LinearLayout) view.findViewById(R.id.topfan_layout);
			view.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) view.getTag();
		
		// get channel info here
		if(list_type == TYPE_LIST_HOT){
			viewHolder.ranking.setVisibility(View.VISIBLE);
			viewHolder.ranking.setText(position+1+"");
		}else if(list_type == TYPE_LIST_GUIDE)
			viewHolder.ranking.setVisibility(View.GONE);
		
		//set default
		viewHolder.channel.setText("");
		viewHolder.channel.setVisibility(View.GONE);
		viewHolder.avatar.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.avatar_default));
		viewHolder.pgicon.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.program_default));
		
		//set data
		if(type == Hot){
			TVHot hot = hotlist.get(position);
	
			viewHolder.title.setText(hot.getTitle());
			
			viewHolder.topfan_layout.setVisibility(View.VISIBLE);
			viewHolder.avatar.setVisibility(View.VISIBLE);
			if (hot.getTopfan().length() == 0)
				viewHolder.avatar.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.avatar_default));
			else {
				viewHolder.avatar.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.avatar_loading));
				loadAvatar(viewHolder.avatar, hot.getTopfan());
			}

			if (hot.getIconUrl().length() == 0){
				viewHolder.pgicon.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.program_default));
			}else {
				viewHolder.pgicon.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.program_loading));
				loadProgramIcon(viewHolder.pgicon, hot.getTitle(), hot.getIconUrl());
			}
			if (subtitle){
				// Subtitle
				TVChannel ch = ChannelManager.getChannel(hot.getChannelCode());
				String chname = ch != null ? ch.getChname() + " " : "";
				String subtitle = chname + StringGenerator.playTimeStringFromTimeString(hot.getStartTime());
				viewHolder.channel.setVisibility(View.VISIBLE);
				viewHolder.channel.setText(subtitle);
			}
			
		}else{
			
			TVEvent event = eventlist.get(position);
			viewHolder.avatar.setVisibility(View.GONE);
			viewHolder.topfan_layout.setVisibility(View.GONE);
			viewHolder.title.setText(event.getEvent());
			
			String time = event.getEventStartTime().substring(4,6) 
						  + "/" 
						  + event.getEventStartTime().substring(6,8)
						  + "~" 
						  + event.getEventEndTime().substring(4,6) 
						  + "/" 
						  + event.getEventEndTime().substring(6,8);
			
			viewHolder.channel.setVisibility(View.VISIBLE);
			viewHolder.channel.setText(time);
			if (event.getImageLink().length() != 0){
				loadProgramIcon(viewHolder.pgicon, event.getTitle()+"_event", event.getImageLink());
			}
		}
		
		return view;
	}

	private class ViewHolder {
		ImageView 	pgicon;
		ImageView 	avatar;
		LinearLayout topfan_layout;
		TextView 	ranking;
		TextView 	title;
		TextView 	channel;
	}
	
	/**
	 * Image Process Methods
	 */
	
	/**
	 * Load avatar.
	 * @param iv
	 * @param username
	 */
	private void loadAvatar (ImageView iv, String username) {
		if (username == null || username.length() == 0)
			return;
		
		if (!ImageManager.drawAvatarImage(iv, username))
			avatar_requests.put(username, "");
	}
	
	
	/**
	 * Load ProgramIcon.
	 * @param title
	 * @param pgicon_url
	 */
	private void loadProgramIcon (ImageView iv, String title, String pgicon_url) {
		if (pgicon_url == null || pgicon_url.length() == 0)
			return;
		
		if (!ImageManager.drawProgramIcon(iv, title, pgicon_url, SampleBase.RIGOROUS_SAMPLED));
			pgicon_requests.put(ProgramIconManager.buildProgramIconKey(title, SampleBase.RIGOROUS_SAMPLED), "");
	}
	
	/**
	 * Listener for listening image loading request.
	 * @author Kencool
	 *
	 */
	private class HotListImageListener implements ImageListener {

		@Override
		public void retrieveAvatar(final String username, final Bitmap bmp) {
			// TODO Auto-generated method stub
			if (avatar_requests == null || !avatar_requests.containsKey(username) || bmp == null)
				return;
			
			if (hot_listview != null) {
				hot_listview.post (new Runnable () {
					public void run () {
						if (hot_listview == null)
							return;
						
						// go through current visible rows and check if refresh is need
						//
						for (int i = hot_listview.getFirstVisiblePosition(); 
							 i <= hot_listview.getLastVisiblePosition(); 
							 i++) {
							if(type == Hot)
								if (i == hotlist.size())
									break;
							
							if(type == Hot)
								if (username.equals(hotlist.get(i).getTopfan())) {
									int childPosition = i -  hot_listview.getFirstVisiblePosition();
									ViewHolder viewHolder = 
											(ViewHolder) hot_listview.getChildAt(childPosition).getTag();
									viewHolder.avatar.setImageBitmap(bmp);
								}

						}
						
						avatar_requests.remove(username);
					}
				});
			}
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
		public void retrieveProgramIcon(final String title, int sampleBase, final Bitmap bmp) {
			final String key = ProgramIconManager.buildProgramIconKey(title, sampleBase);
			if (pgicon_requests == null || !pgicon_requests.containsKey(key) || bmp == null)
				return;
			
			if (hot_listview != null) {
				hot_listview.post (new Runnable () {
					public void run () {
						if (hot_listview == null)
							return;
						
						// go through current visible rows and check if refresh is need
						//
						for (int i = hot_listview.getFirstVisiblePosition(); 
							 i <= hot_listview.getLastVisiblePosition(); 
							 i++) {
							if(type == Hot){
								if (i == hotlist.size())
									break;
							}else
								if (i == eventlist.size())
									break;
							if(type == Hot){
								if (title.equals(hotlist.get(i).getTitle())) {
									int childPosition = i - hot_listview.getFirstVisiblePosition();
									ViewHolder viewHolder = 
											(ViewHolder) hot_listview.getChildAt(childPosition).getTag();
									viewHolder.pgicon.setImageBitmap(bmp);
								}
							}else
								if (title.equals(eventlist.get(i).getTitle()+"_event")) {
									int childPosition = i - hot_listview.getFirstVisiblePosition();
									ViewHolder viewHolder = 
											(ViewHolder) hot_listview.getChildAt(childPosition).getTag();
									viewHolder.pgicon.setImageBitmap(bmp);
								}
						}
						
						pgicon_requests.remove(key);
					}
				});
			}
		}

		@Override
		public void retrieveChannelIcon(String title, Bitmap bmp) {
			// TODO Auto-generated method stub
			
		}
	}
	
	
}
