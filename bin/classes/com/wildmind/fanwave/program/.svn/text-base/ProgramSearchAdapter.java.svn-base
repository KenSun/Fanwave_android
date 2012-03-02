package com.wildmind.fanwave.program;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.follow.FollowManager;
import com.wildmind.fanwave.guide.ChannelManager;
import com.wildmind.fanwave.guide.TVChannel;
import com.wildmind.fanwave.media.ImageListener;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.util.StringGenerator;

import android.content.Context;
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
import android.widget.TextView;

public class ProgramSearchAdapter extends BaseAdapter {

	/**
	 * Row view type count.
	 * Bar		: 0
	 * Program	: 1
	 */
	private final int ROW_VIEW_TYPE_COUNT 	= 2;
	private final int ROW_TYPE_BAR 			= 0;	
	private final int ROW_TYPE_PROGRAM	 	= 1;
	
	private ListView program_listview;
	private Context context;
	private LayoutInflater inflater;
	
	private ArrayList<TVProgram> follow_programs = new ArrayList<TVProgram>();
	private ArrayList<TVProgram> unfollow_programs = new ArrayList<TVProgram>();
	private ProgramImageListener image_listener = new ProgramImageListener();
	//private HashMap<String ,String> follow_requesting = new HashMap<String, String>();
	private ConcurrentHashMap<String, String> pgicon_requests = new ConcurrentHashMap<String, String>();
	
	/**
	 * Constructor
	 * @param listview
	 * @param programs
	 * @param context
	 */
	public ProgramSearchAdapter (ListView listview, ArrayList<TVProgram> programs, Context context) {
		this.program_listview = listview;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		
		filterPrograms(programs);
		ImageManager.addImageListener(image_listener);
	}
	
	/**
	 * Filter out follow and unfollow programs.
	 * @param programs
	 */
	public void filterPrograms (ArrayList<TVProgram> programs) {
		follow_programs.clear();
		unfollow_programs.clear();
		
		if (programs == null)
			return;
		
		for (TVProgram tp:programs) {
			if (FollowManager.isFollowing(tp.getPgid()))
				follow_programs.add(tp);
			else
				unfollow_programs.add(tp);
		}
	}
	
	/**
	 * Clear all resources.
	 */
	public void clear () {
		ImageManager.removeImageListener(image_listener);
		image_listener = null;
		
		program_listview = null;
		context = null;
		inflater = null;
		
		follow_programs = null;
		unfollow_programs = null;
		//follow_requesting = null;
		
		pgicon_requests.clear();
		pgicon_requests = null;
	}

	@Override
	public int getCount() {
		int count = 0;
		if (follow_programs != null)
			count += follow_programs.size() > 0 ? follow_programs.size() + 1 : 0;
		if (unfollow_programs != null)
			count += unfollow_programs.size() > 0 ? unfollow_programs.size() + 1 : 0;
			
		return count;
	}

	@Override
	public Object getItem(int position) {
		Object obj = null;
		if (getItemViewType(position) != ROW_TYPE_BAR) {
			obj = getFollowItem(position);
			if (obj == null)
				obj = getUnfollowItem(position);
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
		if (position == getFollowBarPosition() || position == getUnfollowBarPosition())
			return ROW_TYPE_BAR;
		else
			return ROW_TYPE_PROGRAM;
	}
	
	@Override
	public boolean isEnabled (int position) {
		return getItemViewType(position) != ROW_TYPE_BAR;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (getItemViewType(position) == ROW_TYPE_BAR)
			return getBarView(position, convertView, parent);
		else {
			int userPosition = 0;
			boolean following = getFollowItem(position) != null;
			if (following) {
				// user position in follow programs
				userPosition = position - 1;
			} else {
				// user position in unfollow programs
				userPosition = position - (follow_programs.size() > 0 ? follow_programs.size() + 2 : 1);
			}
			return getProgramView((TVProgram)getItem(position), convertView, userPosition, following);
		}
	}
	
	/**
	 * Get follow bar position.
	 * @return int
	 */
	public int getFollowBarPosition () {
		return follow_programs.size() == 0 ? -1 : 0;
	}
	
	/**
	 * Get unfollow bar position.
	 * @return int
	 */
	public int getUnfollowBarPosition () {
		if (unfollow_programs.size()== 0)
			return -1;
		else 
			return getFollowBarPosition() != -1 ? follow_programs.size() + 1 : 0;
	}
	
	/**
	 * Get follow item for position.
	 * If no follow items or position is not in follow section, return null
	 * @param position
	 * @return Object or null
	 */
	public Object getFollowItem (int position) {
		if (getFollowBarPosition() == -1)
			return null;
		
		int itemPosition = position - 1;
		return itemPosition < follow_programs.size() ? follow_programs.get(itemPosition) : null;
	}
	
	/**
	 * Get unfollow item for position.
	 * If no unfollow items or position is not in unfollow section, return null
	 * @param position
	 * @return Object or null
	 */
	public Object getUnfollowItem (int position) {
		if (getUnfollowBarPosition() == -1)
			return null;
		
		int itemPosition = position - (getUnfollowBarPosition() + 1);
		if (itemPosition < 0)
			return null;
		else
			return itemPosition < unfollow_programs.size() ? unfollow_programs.get(itemPosition) : null;
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
		
		if (position == getFollowBarPosition()) 
			((TextView) view).setText(R.string.program_search_follows);
		else if (position == getUnfollowBarPosition())
			((TextView) view).setText(R.string.program_search_unfollows);
		
		return view;
	}
	
	/**
	 * Get reminder row view.
	 * @param reminder
	 * @param view
	 * @param position
	 * @param reminding
	 * @return
	 */
	private View getProgramView (TVProgram program, View view, int position, boolean following) {
		ProgramViewHolder viewHolder;
		if (view == null) {
			view = inflater.inflate(R.layout.personal_reminder_row, null);
			viewHolder = new ProgramViewHolder();
			viewHolder.program_icon = (ImageView) view.findViewById(R.id.program_icon_imageview);
			viewHolder.title = (TextView) view.findViewById(R.id.title_textview);
			viewHolder.sub_title = (TextView) view.findViewById(R.id.subtitle_textview);
			viewHolder.follow = (Button) view.findViewById(R.id.modify_button);
			viewHolder.divider = (ImageView) view.findViewById(R.id.divider_imageview);
			
			// set follow button hidden
			viewHolder.follow.setVisibility(View.GONE);
			
			view.setTag(viewHolder);
		} else
			viewHolder = (ProgramViewHolder) view.getTag();
		
		// Program Icon
		viewHolder.program_icon.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.program_loading));
		if (program.getIconUrl().length() == 0)
			viewHolder.program_icon.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.program_default));
		else
			loadProgramIcon(viewHolder.program_icon, program.getTitle(), program.getIconUrl());
		
		// Title
		viewHolder.title.setText(program.getTitle());
				
		// Subtitle
		TVChannel ch = ChannelManager.getChannel(program.getChannelCode());
		String chname = ch != null ? ch.getChname() + " " : "";
		String subtitle = chname + StringGenerator.playTimeStringFromTimeString(program.getStartTime());
		viewHolder.sub_title.setText(subtitle);
		
		// Modify
		/*
		viewHolder.follow.setVisibility(following ? View.INVISIBLE : View.VISIBLE);
		viewHolder.follow.setEnabled(!follow_requesting.containsKey(program.getPgid()));
		viewHolder.follow.setOnClickListener(getFollowClickedListener(position));
		viewHolder.follow.clearAnimation();
		if (follow_requesting.containsKey(program.getPgid())) 
			viewHolder.follow.startAnimation(new AlphaSetter(0.5f));
		*/
		// Divider
		viewHolder.divider.setVisibility(
				following && position == follow_programs.size() - 1 && unfollow_programs.size() > 0
			 							 ? View.INVISIBLE : View.VISIBLE );
		
		return view;
	}
	
	/**
	 * Callback for follow button clicked.
	 * @return
	 */
	/*
	private View.OnClickListener getFollowClickedListener (final int position) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final TVProgram program = unfollow_programs.get(position);
				follow_requesting.put(program.getPgid(), "");
				
				// disable following program
				notifyDataSetChanged();
				
				new Thread (new Runnable () {
					public void run () {
						final boolean success = FollowManager.followProgram(program);
						
						if (program_listview != null) {
							program_listview.post( new Runnable () {
								public void run () {
									if (program_listview == null)
										return;
									
									if (success) {
										ArrayList<TVProgram> unfollows = new ArrayList<TVProgram>();
										for (TVProgram tp:unfollow_programs) {
											if (tp.getPgid().equals(program.getPgid()))
												follow_programs.add(tp);
											else
												unfollows.add(tp);
										}
										unfollow_programs = unfollows;
									}
									follow_requesting.remove(program.getPgid());
									notifyDataSetChanged();
								}
							});
						}
					}
				}).start();
			}
		};
	}
	*/
	
	/**
	 * Refresh data.
	 * @param programs
	 */
	public void refreshData (ArrayList<TVProgram> programs) {
		filterPrograms(programs);
		notifyDataSetChanged();
	}
	
	/**
	 * Program View Holder
	 * @author Kencool
	 *
	 */
	private class ProgramViewHolder {
		ImageView 	program_icon;
		ImageView	divider;
		TextView	title;
		TextView	sub_title;
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
	private class ProgramImageListener implements ImageListener {

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
			
			if (program_listview != null) {
				program_listview.post (new Runnable () {
					public void run () {
						if (program_listview == null)
							return;
						
						// go through current visible rows and check if refresh is need
						//
						for (int i = program_listview.getFirstVisiblePosition(); 
							 i <= program_listview.getLastVisiblePosition(); 
							 i++) {
							if (getItemViewType(i) == ROW_TYPE_BAR)
								continue;
						
							TVProgram program = (TVProgram) getItem(i);
							if (program != null && title.equals(program.getTitle())) {
								int childPosition = i - program_listview.getFirstVisiblePosition();
								ProgramViewHolder viewHolder = 
										(ProgramViewHolder) program_listview.getChildAt(childPosition).getTag();
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