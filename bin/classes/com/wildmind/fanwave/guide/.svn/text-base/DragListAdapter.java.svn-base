package com.wildmind.fanwave.guide;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.wildmind.fanwave.media.ImageListener;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.activity.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DragListAdapter extends ArrayAdapter<TVChannel>{
	
	private listImageListener image_listener = new listImageListener();
	private ConcurrentHashMap<String, String> chicon_requests = new ConcurrentHashMap<String, String>();
	private ListView guide_list_listview;
	private ArrayList<TVChannel> Channellist = null;
	private LayoutInflater inflater;

    public DragListAdapter(Context context, ArrayList<TVChannel> Channellist, ListView Epg_listview) {
        super(context, 0, Channellist);
		this.Channellist = Channellist;
		this.guide_list_listview = Epg_listview;
		this.inflater = LayoutInflater.from(context);
		ImageManager.addImageListener(image_listener);
    }
    
	/**
	 * Clear all resources.
	 */
	public void clear () {
		inflater = null;
		guide_list_listview = null;
		ImageManager.removeImageListener(image_listener);
		image_listener = null;
		
		Channellist.clear();
		Channellist = null;
		chicon_requests.clear();
		chicon_requests = null;
	}
    

    public  ArrayList<TVChannel> getList(){
        return Channellist;
    }
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Channellist != null ? Channellist.size() : 0;
	}

	@Override
	public TVChannel getItem(int position) {
		// TODO Auto-generated method stub
		return Channellist!= null? Channellist.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final TVChannel tvc = Channellist.get(position);
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.guide_channel_reorder_listitem, null);
			viewHolder.chnum = (TextView)convertView.findViewById(R.id.channel_id_textview);
			viewHolder.chname = (TextView)convertView.findViewById(R.id.channel_title_textview);
			viewHolder.logo = (ImageView)convertView.findViewById(R.id.channel_icon_imageview);
			convertView.setTag(viewHolder);
		}else 
			viewHolder = (ViewHolder) convertView.getTag();
	
		String chnum = String.format("%03d", Integer.valueOf(tvc.getChnum()));
		viewHolder.chnum.setText(chnum);
		viewHolder.chname.setText(tvc.getChname());
		viewHolder.logo.setImageBitmap(null);			
		loadChicon(viewHolder.logo, tvc.getChcode());
		return convertView;
			
	}
	private class   ViewHolder {
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
		public void retrieveChannelIcon(final String chcode,final Bitmap bmp) {
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
							if (chcode.equals(Channellist.get(i).getChcode())) {
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
	 
		
		
}
