package com.wildmind.fanwave.splash;

import java.util.ArrayList;

import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.media.ImageListener;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.util.StringGenerator;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SplashPrivateAdapter extends BaseAdapter {

	/**
	 * Row view type count.
	 * Send		: 0
	 * Receive	: 1
	 * More		: 2
	 */
	private final int ROW_VIEW_TYPE_COUNT 	= 3;
	private final int ROW_TYPE_SEND 		= 0;	
	private final int ROW_TYPE_RECEIVE 		= 1;
	private final int ROW_TYPE_MORE			= 2;
	
	private ListView splash_listview;
	private LayoutInflater inflater;
	
	private ArrayList<Splash> splashes = null;
	private String sender = null;
	private String receiver = null;
	private SplashImageListener image_listener = new SplashImageListener();
	private SplashPrivateListener listener = null;
	
	private boolean sender_avatar_loaded = false;
	private boolean receiver_avatar_loaded = false;
	private boolean more_history = false;
	private boolean more_loading = false;
	
	/**
	 * Constructor
	 * @param listview
	 * @param splashes
	 * @param context
	 */
	public SplashPrivateAdapter (ListView listview, ArrayList<Splash> splashes, String sender,
			String receiver, Context context) {
		this.splash_listview = listview;
		this.splashes = splashes;
		this.sender = sender;
		this.receiver = receiver;
		this.inflater = LayoutInflater.from(context);
		
		ImageManager.addImageListener(image_listener);
	}
	
	public void setListener (SplashPrivateListener listener) {
		this.listener = listener;
	}
	
	/**
	 * Clear all resources.
	 */
	public void clear () {
		ImageManager.removeImageListener(image_listener);
		image_listener = null;
		
		this.listener = null;
		splash_listview = null;
		inflater = null;
		splashes = null;
		sender = null;
		receiver = null;
	}
	
	@Override
	public int getCount() {
		return splashes != null ? splashes.size() + (more_history ? 1 : 0) : 0;
	}

	@Override
	public Object getItem(int position) {
		Object obj = null;
		if (splashes != null) {
			if (!more_history)
				obj = splashes.get(position);
			else
				obj = position > 0 ? splashes.get(position - 1) : null;
		}
		return obj;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getViewTypeCount () {
		return ROW_VIEW_TYPE_COUNT;
	}
	
	@Override
	public int getItemViewType (int position) {
		if (more_history && position == 0)
			return ROW_TYPE_MORE;
		
		Splash splash = splashes.get(more_history ? position - 1 : position);
		if (splash.getType().equals(Splash.TYPE_SEND))
			return ROW_TYPE_SEND;
		else if (splash.getType().equals(Splash.TYPE_RECEIVE))
			return ROW_TYPE_RECEIVE;
		else
			return IGNORE_ITEM_VIEW_TYPE;
	}
	
	@Override
	public boolean areAllItemsEnabled () {
		return false;
	}
	
	@Override
	public boolean isEnabled (int position) {
		return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (more_history && position == 0)
			return getMoreView(convertView);
		
		Splash splash = splashes.get(more_history ? position - 1 : position);
		switch (getItemViewType(position)) {
		case ROW_TYPE_SEND:
			return getSendView(splash, convertView);
		case ROW_TYPE_RECEIVE:
			return getReceiveView(splash, convertView);
		default:
			return null;
		}
	}
	
	/**
	 * Get send splash view.
	 * @param splash
	 * @param view
	 * @return
	 */
	private View getSendView (Splash splash, View view) {
		ViewHolder viewHolder;
		if (view == null) {
			view = inflater.inflate(R.layout.splash_private_send_row, null);
			viewHolder = new ViewHolder();
			viewHolder.avatar = (ImageView) view.findViewById(R.id.avatar_imageview);
			viewHolder.avatar.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.avatar_loading));
			viewHolder.content = (TextView) view.findViewById(R.id.content_textview);
			viewHolder.created_time = (TextView) view.findViewById(R.id.created_time_textview);
			view.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) view.getTag();
		
		// Avatar
		loadReceiverAvatar(viewHolder.avatar);
		
		// Content
		viewHolder.content.setText(splash.getMessage());
		
		// Created Time
		viewHolder.created_time.setText(StringGenerator.distinctTimeStringFromTimeString(splash.getCreatedTime()));

		return view;
	}
	
	/**
	 * Get receive splash view.
	 * @param splash
	 * @param view
	 * @return
	 */
	private View getReceiveView (Splash splash, View view) {
		ViewHolder viewHolder;
		if (view == null) {
			view = inflater.inflate(R.layout.splash_private_receive_row, null);
			viewHolder = new ViewHolder();
			viewHolder.avatar = (ImageView) view.findViewById(R.id.avatar_imageview);
			viewHolder.avatar.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.avatar_loading));
			viewHolder.content = (TextView) view.findViewById(R.id.content_textview);
			viewHolder.created_time = (TextView) view.findViewById(R.id.created_time_textview);
			view.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) view.getTag();
		
		// Avatar
		loadSenderAvatar(viewHolder.avatar);
		
		// Content
		viewHolder.content.setText(splash.getMessage());
		
		// Created Time
		viewHolder.created_time.setText(StringGenerator.distinctTimeStringFromTimeString(splash.getCreatedTime()));
		
		return view;
	}
	
	/**
	 * Get more view.
	 * @return
	 */
	private View getMoreView (View view) {
		MoreViewHolder viewHolder;
		if (view == null) {
			view = inflater.inflate(R.layout.splash_history_row_more, null);
			viewHolder = new MoreViewHolder();
			viewHolder.more = (TextView) view.findViewById(R.id.no_more_textview);
			viewHolder.loading = (ProgressBar) view.findViewById(R.id.loading_progressbar);viewHolder.more_relativelayout = (RelativeLayout) view.findViewById(R.id.relativeLayout1);
			viewHolder.more.setText("View More");
			viewHolder.more_relativelayout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					more_loading = true;
					notifyDataSetChanged();
					if (listener != null)
						listener.onMoreRequest();
				}
			});
			
			view.setTag(viewHolder);
		} else
			viewHolder = (MoreViewHolder) view.getTag();
		
		// More
		viewHolder.more.setVisibility(more_loading ? View.GONE : View.VISIBLE);
		
		// Loading
		viewHolder.loading.setVisibility(more_loading ? View.VISIBLE : View.GONE);
		
		return view;
	}
	
	/**
	 * Refresh data.
	 * @param splashes
	 */
	public void refreshData (ArrayList<Splash> splashes, boolean more) {
		this.splashes = splashes;
		this.more_history = more;
		more_loading = false;
		notifyDataSetChanged();
	}
	
	/**
	 * ViewHolder class
	 * @author Kencool
	 *
	 */
	private class ViewHolder {
		ImageView	avatar;
		TextView	content;
		TextView	created_time;
	}
	
	/**
	 * MoreViewHolder class
	 * @author Kencool
	 *
	 */
	private class MoreViewHolder {
		TextView	more;
		ProgressBar	loading;
		RelativeLayout more_relativelayout;
	}
	
	/**
	 * Image Process Methods
	 */
	
	/**
	 * Load sender avatar.
	 * @param iv
	 */
	private void loadSenderAvatar (ImageView iv) {
		if (sender == null || sender.length() == 0)
			return;
		
		if (ImageManager.drawAvatarImage(iv, sender) && !sender_avatar_loaded) {
			sender_avatar_loaded = true;
			if (receiver_avatar_loaded)
				ImageManager.removeImageListener(image_listener);
		}
	}
	
	/**
	 * Load receiver avatar.
	 * @param iv
	 */
	private void loadReceiverAvatar (ImageView iv) {
		if (receiver == null || receiver.length() == 0)
			return;
		
		if (ImageManager.drawAvatarImage(iv, receiver) && !receiver_avatar_loaded) {
			receiver_avatar_loaded = true;
			if (sender_avatar_loaded)
				ImageManager.removeImageListener(image_listener);
		}
	}
	
	/**
	 * Listener for listening image requests.
	 * @author Kencool
	 *
	 */
	private class SplashImageListener implements ImageListener {

		@Override
		public void retrieveAvatar(String username, Bitmap bmp) {
			if ((!username.equals(sender) && !username.equals(receiver)) || bmp == null)
				return;
			
			if (splash_listview != null) {
				splash_listview.post( new Runnable () {
					public void run () {
						if (splash_listview == null)
							return;
						
						notifyDataSetChanged();
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
		public void retrieveProgramIcon(String title, int sampleBase, Bitmap bmp) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void retrieveChannelIcon(String chcode, Bitmap bmp) {
			// TODO Auto-generated method stub
			
		}
	}
	
	/**
	 * Splash Private Listener inferface
	 * @author Kencool
	 *
	 */
	public interface SplashPrivateListener {
		
		public void onMoreRequest();
	}
}
