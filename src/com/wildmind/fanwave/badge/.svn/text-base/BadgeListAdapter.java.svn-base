package com.wildmind.fanwave.badge;

import java.util.concurrent.ConcurrentHashMap;

import com.wildmind.fanwave.media.ImageListener;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.activity.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class BadgeListAdapter extends BaseAdapter {

	/**
	 * Row view type count.
	 * Bar		: 0
	 * Badge	: 1
	 */
	private final int ROW_VIEW_TYPE_COUNT = 2;
	private final int ROW_TYPE_BAR 		= 0;	
	private final int ROW_TYPE_BADGE 	= 1;
	
	private ListView badge_listview;
	private Context context;
	private LayoutInflater inflater;
	
	private BadgeManager badge_manager = null;
	private BadgeListImageListener image_listener = new BadgeListImageListener();
	private ConcurrentHashMap<String, String> badge_requests = new ConcurrentHashMap<String, String>();
	
	/**
	 * Constructor
	 * @param listview
	 * @param bm
	 * @param context
	 */
	public BadgeListAdapter (ListView listview, BadgeManager bm, Context context) {
		this.badge_listview = listview;
		this.badge_manager = bm;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		
		ImageManager.addImageListener(image_listener);
	}
	
	/**
	 * Clear all resources.
	 */
	public void clear () {
		ImageManager.removeImageListener(image_listener);
		image_listener = null;
		
		badge_listview = null;
		context = null;
		inflater = null;
		badge_manager = null;
		
		badge_requests.clear();
		badge_requests = null;
	}
	
	@Override
	public int getCount() {
		if (badge_manager == null)
			return 0;
		
		int count = 0;
		count += badge_manager.getEventBadges().size() > 0
				 ? badge_manager.getEventBadges().size() + 1 : 0;
		count += badge_manager.getSystemBadges().size() > 0 
				 ? badge_manager.getSystemBadges().size() + 1 : 0;
		count += badge_manager.getProgramBadges().size() > 0
				 ? badge_manager.getProgramBadges().size() + 1 : 0;
		return count;
	}

	@Override
	public Object getItem(int position) {
		if (getItemViewType(position) == ROW_TYPE_BAR)
			return null;
		else {
			Object obj = getEventItem(position);
			if (obj == null)
				obj = getSystemItem(position);
			if (obj == null)
				obj = getProgramItem(position);
			return obj;
		}
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
		if (position == getEventBarPosition() ||
			position == getSystemBarPosition() ||
			position == getProgramBarPosition())
			return ROW_TYPE_BAR;
		else
			return ROW_TYPE_BADGE;
	}
	
	@Override
	public boolean isEnabled (int position) {
		return getItemViewType(position) != ROW_TYPE_BAR;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (getItemViewType(position) == ROW_TYPE_BAR)
			return getBarView(position, convertView, parent);
		else 
			return getBadgeView((TVBadge)getItem(position), convertView, position);
	}
	
	/**
	 * Get event bar position.
	 * @return
	 */
	public int getEventBarPosition () {
		return badge_manager.getEventBadges().size() > 0 ? 0 : -1;
	}
	/**
	 * Get system bar position.
	 * @return
	 */
	public int getSystemBarPosition () {
		if (badge_manager.getSystemBadges().size() == 0)
			return -1;
		else {
			if (badge_manager.getEventBadges().size() == 0)
				return 0;
			else
				return badge_manager.getEventBadges().size() + 1;
		}
	}
	/**
	 * Get program bar position.
	 * @return
	 */
	public int getProgramBarPosition () {
		if (badge_manager.getProgramBadges().size() == 0)
			return -1;
		else {
			int count = 0;
			count += badge_manager.getEventBadges().size() > 0 ? badge_manager.getEventBadges().size() + 1 : 0;
			count += badge_manager.getSystemBadges().size() > 0 ? badge_manager.getSystemBadges().size() + 1 : 0;
			return count;
		}
	}
	
	/**
	 * Get event item for position.
	 * If no event items or position is not in event section, return null.
	 * @param position
	 * @return Object or null
	 */
	public Object getEventItem (int position) {
		if (getEventBarPosition() == -1)
			return null;
		
		int itemPosition = position - 1;
		return itemPosition < badge_manager.getEventBadges().size()
			   ? badge_manager.getEventBadges().get(itemPosition) : null;
	}
	/**
	 * Get system item for position.
	 * If no system items or position is not in system section, return null.
	 * @param position
	 * @return Object or null
	 */
	public Object getSystemItem (int position) {
		if (getSystemBarPosition() == -1)
			return null;
		
		int itemPosition = position - (getSystemBarPosition() + 1);
		if (itemPosition < 0)
			return null;
		else
			return itemPosition < badge_manager.getSystemBadges().size() 
					? badge_manager.getSystemBadges().get(itemPosition) : null;
	}
	/**
	 * Get program item for position.
	 * If no program items or position is not in program section, return null.
	 * @param position
	 * @return Object or null
	 */
	public Object getProgramItem (int position) {
		if (getProgramBarPosition() == -1)
			return null;
		
		int itemPosition = position - (getProgramBarPosition() + 1);
		if (itemPosition < 0)
			return null;
		else
			return itemPosition < badge_manager.getProgramBadges().size()
					? badge_manager.getProgramBadges().get(itemPosition) : null;
	}
	
	/**
	 * Get bar row view.
	 * @param position
	 * @param view
	 * @param parent
	 * @return
	 */
	private View getBarView (int position, View view, ViewGroup parent) {
		if (view == null) {
			view = new TextView(context);
			((TextView) view).setTextColor(Color.WHITE);
			((TextView) view).setTextSize(14);
			((TextView) view).setTypeface(null,Typeface.BOLD);
			((TextView) view).setGravity(Gravity.CENTER_VERTICAL);
			((TextView) view).setPadding(16, 0, 0, 0);
			view.setBackgroundResource(R.drawable.bar_gray);
		}
		
		if (position == getEventBarPosition())
			((TextView) view).setText(R.string.badge_activity);
		else if (position == getSystemBarPosition()) 
			((TextView) view).setText(R.string.badge_system);
		else if (position == getProgramBarPosition())
			((TextView) view).setText(R.string.badge_program);
		
		return view;
	}
	
	/**
	 * Get badge row view.
	 * @param position
	 * @param view
	 * @param parent
	 * @return
	 */
	private View getBadgeView (TVBadge badge, View view, int position) {
		BadgeViewHolder viewHolder;
		if (view == null) {
			view = inflater.inflate(R.layout.personal_badge_row, null);
			viewHolder = new BadgeViewHolder();
			viewHolder.badge = (ImageView) view.findViewById(R.id.badge_imageview);
			viewHolder.divider = (ImageView) view.findViewById(R.id.divider_imageview);
			viewHolder.title = (TextView) view.findViewById(R.id.title_textview);
			viewHolder.description = (TextView) view.findViewById(R.id.description_textview);
			view.setTag(viewHolder);
		} else
			viewHolder = (BadgeViewHolder) view.getTag();
		
		// Badge
		viewHolder.badge.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.badge_loading));
		loadBadge(viewHolder.badge, badge.getId());
		
		// Title
		viewHolder.title.setText(badge.getTitle());
		
		// Description
		viewHolder.description.setText(badge.getDescription());
		
		// Divider
		boolean invisible = position == getSystemBarPosition() - 1 ||
							position == getProgramBarPosition() - 1;
		viewHolder.divider.setVisibility(invisible ? View.INVISIBLE : View.VISIBLE);
		
		return view;
	}
	
	private class BadgeViewHolder {
		ImageView 	badge;
		ImageView	divider;
		TextView	title;
		TextView	description;
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
	
	/**
	 * Listener for listening image loading requests.
	 * @author Kencool
	 *
	 */
	private class BadgeListImageListener implements ImageListener {

		@Override
		public void retrieveAvatar(String username, Bitmap bmp) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void retrieveBadge(final String badge_id, final boolean scaled, final Bitmap bmp) {
			if (badge_requests == null || !badge_requests.containsKey(badge_id) || !scaled || bmp == null)
				return;
			
			if (badge_listview != null) {
				badge_listview.post( new Runnable () {
					public void run () {
						if (badge_listview == null)
							return;
						
						// go through current visible rows and check if refresh is need
						//
						int firstPosition = badge_listview.getFirstVisiblePosition();
						int lastPosition = badge_listview.getLastVisiblePosition();
						
						for (int i = firstPosition; i <= lastPosition; i++) {
							// not an badge row
							if (getItemViewType(i) == ROW_TYPE_BAR)
								continue;
							
							TVBadge badge = (TVBadge) getItem(i);
							if (badge != null && badge_id.equals(badge.getId())) {
								int childPosition = i - firstPosition;
								BadgeViewHolder viewHolder = 
										(BadgeViewHolder) badge_listview.getChildAt(childPosition).getTag();
								viewHolder.badge.setImageBitmap(bmp);
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
