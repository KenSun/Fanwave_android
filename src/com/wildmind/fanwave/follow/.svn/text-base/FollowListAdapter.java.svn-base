package com.wildmind.fanwave.follow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.wildmind.fanwave.animation.AlphaSetter;
import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.media.ImageListener;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.activity.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

public class FollowListAdapter extends BaseAdapter {

	/**
	 * List type.
	 */
	public static final int TYPE_LIST_SELF 	= 0;
	public static final int TYPE_LIST_OTHER = 1;
	
	/**
	 * Row view type count.
	 * Bar		: 0
	 * Follow	: 1
	 */
	private final int ROW_VIEW_TYPE_COUNT = 2;
	private final int ROW_TYPE_BAR 		= 0;	
	private final int ROW_TYPE_FOLLOW 	= 1;
	
	private ListView follow_listview;
	private Context context;
	private LayoutInflater inflater;
	private int list_type = -1;
	
	private ArrayList<TVFollow> follows = null;
	private ArrayList<TVFollow> common_follows = null;
	private ArrayList<TVFollow> uncommon_follows = null;
	private FollowListImageListener image_listener = new FollowListImageListener();
	private HashMap<String ,String> follow_requesting = new HashMap<String, String>();
	private ConcurrentHashMap<String, String> pgicon_requests = new ConcurrentHashMap<String, String>();
	
	/**
	 * Constructor
	 * @param listview
	 * @param type
	 * @param context
	 */
	public FollowListAdapter (ListView listview, int type, Context context) {
		this.follow_listview = listview;
		this.list_type = type;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		
		ImageManager.addImageListener(image_listener);
	}
	
	/**
	 * Constructor for self type.
	 * @param listview
	 * @param follows
	 * @param context
	 */
	public FollowListAdapter (ListView listview, ArrayList<TVFollow> follows, Context context) {
		this(listview, TYPE_LIST_SELF, context);
		this.follows = follows;
	}
	
	/**
	 * Constructor for other type.
	 * @param listview
	 * @param commons
	 * @param uncommons
	 * @param context
	 */
	public FollowListAdapter (ListView listview, ArrayList<TVFollow> commons, 
			ArrayList<TVFollow> uncommons, Context context) {
		this(listview, TYPE_LIST_OTHER, context);
		this.common_follows = commons;
		this.uncommon_follows = uncommons;
	}
	
	/**
	 * Clear all resources.
	 */
	public void clear () {
		ImageManager.removeImageListener(image_listener);
		image_listener = null;
		
		follow_listview = null;
		context = null;
		inflater = null;
		follows = null;
		common_follows = null;
		uncommon_follows = null;
		follow_requesting = null;
		
		pgicon_requests.clear();
		pgicon_requests = null;
	}
	
	@Override
	public int getCount() {
		int count = 0;
		switch (list_type) {
		
		case TYPE_LIST_SELF:
			count = follows != null ? follows.size() : 0;
			break;
			
		case TYPE_LIST_OTHER:
			if (common_follows != null)
				count += common_follows.size() > 0 ? common_follows.size() + 1 : 0;
			if (uncommon_follows != null)
				count += uncommon_follows.size() > 0 ? uncommon_follows.size() + 1 : 0;
			break;
		}
		
		return count;
	}

	@Override
	public Object getItem(int position) {
		Object obj = null;
		switch (list_type) {
		case TYPE_LIST_SELF:
			if (position < follows.size())
				obj = follows != null ? follows.get(position) : null;
			break;
			
		case TYPE_LIST_OTHER:
			if (getItemViewType(position) != ROW_TYPE_BAR) {
				obj = getCommonItem(position);
				if (obj == null)
					obj = getUncommonItem(position);
			}
			break;
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
		switch (list_type) {
		
		case TYPE_LIST_SELF:
			return ROW_TYPE_FOLLOW;
			
		case TYPE_LIST_OTHER:
			if (position == getCommonBarPosition() || 
				position == getUncommonBarPosition())
				return ROW_TYPE_BAR;
			else
				return ROW_TYPE_FOLLOW;
			
		default:
			return -1;
		}
	}
	
	@Override
	public boolean isEnabled (int position) {
		switch (list_type) {
		
		case TYPE_LIST_SELF:
			return true;
			
		case TYPE_LIST_OTHER:
			return getItemViewType(position) != ROW_TYPE_BAR;
			
		default:
			return false;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		switch (list_type) {
		
		case TYPE_LIST_SELF:
			return getFollowView((TVFollow)getItem(position), convertView, position, true);
			
		case TYPE_LIST_OTHER:
			if (getItemViewType(position) == ROW_TYPE_BAR)
				return getBarView(position, convertView, parent);
			else {
				int userPosition = 0;
				boolean following = getCommonItem(position) != null;
				if (following) {
					// user position in common follows
					userPosition = position - 1;
				} else {
					// user position in uncommon follows
					userPosition = position - (common_follows.size() > 0 ? common_follows.size() + 2 : 1);
				}
				return getFollowView((TVFollow)getItem(position), convertView, userPosition, following);
			}
			
		default:
			return null;
		}
	}

	/**
	 * Get common bar position.
	 * @return
	 */
	public int getCommonBarPosition () {
		return (common_follows == null || common_follows.size() == 0) ? -1 : 0;
	}
	
	/**
	 * Get uncommon bar position.
	 * @return
	 */
	public int getUncommonBarPosition () {
		if (uncommon_follows == null || uncommon_follows.size() == 0)
			return -1;
		else {
			if (common_follows == null || common_follows.size() == 0)
				return 0;
			else
				return common_follows.size() + 1;
		}
	}
	
	/**
	 * Get common item for position.
	 * If no common items or position is not in common section, return null
	 * @param position
	 * @return Object or null
	 */
	public Object getCommonItem (int position) {
		if (getCommonBarPosition() == -1)
			return null;
		
		int itemPosition = position - 1;
		return itemPosition < common_follows.size() ? common_follows.get(itemPosition) : null;
	}
	
	/**
	 * Get uncommon item for position.
	 * If no uncommon items or position is not in uncommon section, return null
	 * @param position
	 * @return Object or null
	 */
	public Object getUncommonItem (int position) {
		if (getUncommonBarPosition() == -1)
			return null;
		
		int itemPosition = position - (getUncommonBarPosition() + 1);
		if (itemPosition < 0)
			return null;
		else
			return itemPosition < uncommon_follows.size() ? uncommon_follows.get(itemPosition) : null;
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
		
		if (position == getCommonBarPosition()) 
			((TextView) view).setText(ApplicationManager.getAppContext().getString(R.string.personal_follow_common));
		else if (position == getUncommonBarPosition())
			((TextView) view).setText(ApplicationManager.getAppContext().getString(R.string.personal_follow_uncommon));
		
		return view;
	}
	
	/**
	 * Get follow row view.
	 * @param follow
	 * @param view
	 * @param parent
	 * @return
	 */
	private View getFollowView (TVFollow follow, View view, int position, boolean following) {
		FollowViewHolder viewHolder;
		if (view == null) {
			view = inflater.inflate(R.layout.personal_follow_row, null);
			viewHolder = new FollowViewHolder();
			viewHolder.program_icon = (ImageView) view.findViewById(R.id.program_icon_imageview);
			viewHolder.title = (TextView) view.findViewById(R.id.title_textview);
			viewHolder.rating = (RatingBar) view.findViewById(R.id.rating_bar);
			viewHolder.follow = (Button) view.findViewById(R.id.follow_button);
			viewHolder.divider = (ImageView) view.findViewById(R.id.divider_imageview);
			view.setTag(viewHolder);
		} else
			viewHolder = (FollowViewHolder) view.getTag();
		
		// Program Icon
		viewHolder.program_icon.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.program_loading));
		if (follow.getIconUrl().length() == 0)
			viewHolder.program_icon.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.program_default));
		else
			loadProgramIcon(viewHolder.program_icon, follow.getTitle(), follow.getIconUrl());
		
		// Title
		viewHolder.title.setText(follow.getTitle());
		
		// Rating
		viewHolder.rating.setRating((float) follow.getTotalRating());
		
		// Follow, Divider
		viewHolder.follow.setEnabled(!follow_requesting.containsKey(follow.getPgid()));
		viewHolder.follow.clearAnimation();
		if (follow_requesting.containsKey(follow.getPgid())) 
			viewHolder.follow.startAnimation(new AlphaSetter(0.5f));
		
		switch (list_type) {
		case TYPE_LIST_SELF:
			viewHolder.follow.setVisibility(View.VISIBLE);
			viewHolder.follow.setText(R.string.personal_action_unfollow);
			viewHolder.follow.setOnClickListener(getUnfollowClickedListener(position));
			viewHolder.divider.setVisibility(View.VISIBLE);
			break;
		case TYPE_LIST_OTHER:
			viewHolder.follow.setVisibility(following ? View.INVISIBLE : View.VISIBLE);
			viewHolder.follow.setText(R.string.personal_action_follow);
			viewHolder.follow.setOnClickListener(getFollowClickedListener(position));
			viewHolder.divider.setVisibility(
					following && position == common_follows.size() - 1 && uncommon_follows.size() > 0
											 ? View.INVISIBLE : View.VISIBLE);
			break;
		default:
			break;
		}
		
		return view;
	}
	
	/**
	 * Callback for follow button clicked.
	 * @return
	 */
	private View.OnClickListener getFollowClickedListener (final int position) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final TVFollow follow = uncommon_follows.get(position);
				follow_requesting.put(follow.getPgid(), "");
				v.clearAnimation();
				v.setAnimation(new AlphaSetter(0.5f));
				v.setEnabled(false);
				
				new Thread (new Runnable () {
					public void run () {
						final boolean success = FollowManager.followProgram(follow);
						
						if (follow_listview != null) {
							follow_listview.post( new Runnable () {
								public void run () {
									if (follow_listview == null)
										return;
									
									if (success) {
										uncommon_follows.remove(position);
										common_follows.add(follow);
									}
									follow_requesting.remove(follow.getPgid());
									notifyDataSetChanged();
								}
							});
						}
					}
				}).start();
			}
		};
	}
	
	/**
	 * Callback for unfollow button clicked.
	 * @return
	 */
	private View.OnClickListener getUnfollowClickedListener (final int position) {
		return new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				final TVFollow follow = follows.get(position);
				// button
				DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						follow_requesting.put(follow.getPgid(), "");
						v.clearAnimation();
						v.setAnimation(new AlphaSetter(0.5f));
						v.setEnabled(false);
						
						new Thread (new Runnable () {
							public void run () {
								final boolean success = FollowManager.unfollowProgram(follow);
								
								if (follow_listview != null) {
									follow_listview.post( new Runnable () {
										public void run () {
											if (follow_listview == null)
												return;
											
											if (success)
												follows.remove(position);
											follow_requesting.remove(follow.getPgid());
											notifyDataSetChanged();
										}
									});
								}
							}
						}).start();
					}
				};
				
				// alert dialog
				new AlertDialog.Builder(context)
							   .setTitle(follow.getTitle())
							   .setMessage(context.getResources().getString(R.string.personal_remove_follow_description))
							   .setPositiveButton(R.string.action_confirm, positive)
							   .setNegativeButton(R.string.action_cancel, null)
							   .show();
			}
		};
	}
	
	/**
	 * Refresh follow list data of type TYPE_LIST_SELF.
	 * @param follows
	 */
	public void refreshData (ArrayList<TVFollow> follows) {
		this.follows = follows;
		notifyDataSetChanged();
	}
	
	/**
	 * Refresh follow list data of type TYPE_LIST_OTHER;
	 * @param commons
	 * @param uncommons
	 */
	public void refreshData (ArrayList<TVFollow> commons, ArrayList<TVFollow> uncommons) {
		this.common_follows = commons;
		this.uncommon_follows = uncommons;
		notifyDataSetChanged();
	}
	
	/**
	 * Follow view holder.
	 * @author Kencool
	 *
	 */
	private class FollowViewHolder {
		ImageView 	program_icon;
		ImageView	divider;
		TextView	title;
		RatingBar	rating;
		Button		follow;
	}
	
	/**
	 * Image Process Methods
	 */
	
	/**
	 * Load ProgramIcon.
	 * @param title
	 * @param pgicon_url
	 */
	private void loadProgramIcon (ImageView iv, String title, String pgicon_url) {
		if (pgicon_url == null || pgicon_url.length() == 0)
			return;
		
		if (!ImageManager.drawProgramIcon(iv, title, pgicon_url, ImageManager.SampleBase.RIGOROUS_SAMPLED));
			pgicon_requests.put(title, "");
	}
	
	/**
	 * Listener for listening image requests.
	 * @author Kencool
	 *
	 */
	private class FollowListImageListener implements ImageListener {

		@Override
		public void retrieveAvatar(String username, Bitmap bmp) {
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
		public void retrieveProgramIcon(final String title, final int sampleBase, final Bitmap bmp) {
			if (pgicon_requests == null || !pgicon_requests.containsKey(title) || bmp == null)
				return;
			
			if (follow_listview != null) {
				follow_listview.post (new Runnable () {
					public void run () {
						if (follow_listview == null)
							return;
						
						// go through current visible rows and check if refresh is need
						//
						for (int i = follow_listview.getFirstVisiblePosition(); 
							 i <= follow_listview.getLastVisiblePosition(); 
							 i++) {
							if (getItemViewType(i) == ROW_TYPE_BAR)
								continue;
						
							TVFollow follow = (TVFollow) getItem(i);
							if (follow != null && title.equals(follow.getTitle())) {
								int childPosition = i - follow_listview.getFirstVisiblePosition();
								FollowViewHolder viewHolder = 
										(FollowViewHolder) follow_listview.getChildAt(childPosition).getTag();
								viewHolder.program_icon.setImageBitmap(bmp);
							}
						}
						
						pgicon_requests.remove(title);
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
