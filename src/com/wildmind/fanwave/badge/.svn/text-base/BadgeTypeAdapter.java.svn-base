package com.wildmind.fanwave.badge;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.animation.AlphaSetter;
import com.wildmind.fanwave.media.ImageListener;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.activity.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BadgeTypeAdapter extends BaseAdapter {

	private ListView badge_listview;
	
	private Context context;
	private LayoutInflater inflater;
	private ArrayList<TVBadge> badge_list = null;
	
	/**
	 * Whether are badges wearable for user. If true, still need to check whether user has achieved
	 * each wearable condition of badges.
	 */
	private boolean wearable = false;
	
	private TypeImageListener image_listener = new TypeImageListener();
	private ConcurrentHashMap<String, String> badge_requests = new ConcurrentHashMap<String, String>();
	
	/**
	 * Constructor
	 * @param listview
	 * @param list
	 * @param context
	 */
	public BadgeTypeAdapter (ListView listview, ArrayList<TVBadge> list, boolean wearable, Context context) {
		this.badge_listview = listview;
		this.badge_list = list;
		this.wearable = wearable;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		
		ImageManager.addImageListener(image_listener);
	}
	
	/**
	 * Clear all resources.
	 */
	public void clear () {
		badge_listview = null;
		context = null;
		inflater = null;
		badge_list = null;
		
		badge_requests.clear();
		badge_requests = null;
		
		ImageManager.removeImageListener(image_listener);
		image_listener = null;
	}
	
	/**
	 * List View Resources Methods
	 */
	@Override
	public int getCount() {
		return badge_list != null ? badge_list.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return badge_list != null ? badge_list.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public boolean isEnabled (int position) {
		TVBadge badge = badge_list.get(position);
		return badge.getProgress() == 1.0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		TVBadge badge = badge_list.get(position);
		ViewHolder viewHolder;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = inflater.inflate(R.layout.badge_row, null);
			viewHolder.badge = (ImageView) view.findViewById(R.id.badge_imageview);
			viewHolder.title = (TextView) view.findViewById(R.id.title_textview);
			viewHolder.description = (TextView) view.findViewById(R.id.description_textview);
			viewHolder.progress = (TextView) view.findViewById(R.id.progress_textview);
			viewHolder.wear = (Button) view.findViewById(R.id.wear_button);
			view.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) view.getTag();
		
		// Badge
		viewHolder.badge.clearAnimation();
		viewHolder.badge.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.badge_loading));
		if (!wearable)
			loadBadge(viewHolder.badge, badge.getId());
		else {
			if (badge.getProgress() == 1.0)
				loadBadge(viewHolder.badge, badge.getId());
			else
				loadBadge(viewHolder.badge, badge.getId(), badge.getProgress());
		}
		
		// Title
		viewHolder.title.setText(badge.getTitle());
		
		// Description
		viewHolder.description.setText(badge.getDescription());
		
		// Wear
		viewHolder.wear.setOnClickListener(getWearButtonClickedListener(badge.getId()));
		if (wearable && badge.getProgress() == 1.0 &&
			!AccountManager.getCurrentUser().getBadgeId().equals(badge.getId())) 
			viewHolder.wear.setVisibility(View.VISIBLE);
		else
			viewHolder.wear.setVisibility(View.GONE);
		
		// Progress
		viewHolder.progress.setText((int)(badge.getProgress() * 100) + "%");
		
		return view;
	}
	
	/**
	 * Set wearing badge.
	 */
	private void setWearingBadge (final String badge_id) {
		new Thread ( new Runnable () {
			public void run () {
				String username = AccountManager.getCurrentUser().getUsername();
				final boolean success = BadgeManager.setSelectedBadge(username, badge_id);
				
				if (context != null) {
					((Activity) context).runOnUiThread (new Runnable () {
						public void run () {
							notifyDataSetChanged();
							if (!success)
								Toast.makeText(context, R.string.badge_wear_failed, Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Get callback for wear button clicked.
	 * @param badgeId
	 * @return
	 */
	private View.OnClickListener getWearButtonClickedListener (final String badge_id) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setVisibility(View.INVISIBLE);
				setWearingBadge(badge_id);
			}
		};
	}

	/**
	 * Refersh badge list data.
	 * @param list
	 */
	public void refershData (ArrayList<TVBadge> list) {
		this.badge_list = list;
		notifyDataSetChanged();
	}
	
	private class ViewHolder {
		ImageView 	badge;
		TextView	title;
		TextView	description;
		TextView	progress;
		Button		wear;
	}
	
	/**
	 * Image Process Methods
	 */
	
	/**
	 * Load badge.
	 * @param iv
	 * @param badge_id
	 */
	private void loadBadge (ImageView iv, String badge_id) {
		if (badge_id == null || badge_id.length() == 0)
			return;
		
		if (!ImageManager.drawBadgeImage(iv, badge_id, true))
			badge_requests.put(badge_id, "");
	}
	
	private void loadBadge (ImageView iv, String badge_id, double progress) {
		if (badge_id == null || badge_id.length() == 0)
			return;
		
		if (ImageManager.drawBadgeImage(iv, badge_id, true))
			iv.startAnimation(new AlphaSetter( progress > 0.1 ? (float) progress : 0.1f));
		else
			badge_requests.put(badge_id, "");
	}
	
	/**
	 * Listener for listening image loading requests.
	 * @author Kencool
	 *
	 */
	private class TypeImageListener implements ImageListener {

		@Override
		public void retrieveAvatar(String username, Bitmap bmp) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void retrieveBadge(final String badge_id, boolean scaled, final Bitmap bmp) {
			if (badge_requests == null || !badge_requests.containsKey(badge_id) || !scaled || bmp == null)
				return;
			
			if (badge_listview != null) {
				badge_listview.post( new Runnable () {
					public void run () {
						if (badge_listview == null)
							return;
						
						// go through current visible rows and check if refresh is need
						//
						for (int i = badge_listview.getFirstVisiblePosition(); 
							 i <= badge_listview.getLastVisiblePosition(); 
							 i++) {
							// over feed list position, may be other purpose row
							if (i == badge_list.size())
								break;
							
							TVBadge badge = badge_list.get(i);
							if (badge != null && badge_id.equals(badge.getId())) {
								int childPosition = i - badge_listview.getFirstVisiblePosition();
								ViewHolder viewHolder = 
										(ViewHolder) badge_listview.getChildAt(childPosition).getTag();
								viewHolder.badge.setImageBitmap(bmp);
								if (wearable && badge.getProgress() != 1.0)
									viewHolder.badge.startAnimation(
										new AlphaSetter(badge.getProgress() > 0.1 ? (float) badge.getProgress() : 0.1f));
							}
						}
						
						badge_requests.remove(badge_id);
					}
				});
			}
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
		public void retrieveChannelIcon(String title, Bitmap bmp) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
