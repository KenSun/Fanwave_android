package com.wildmind.fanwave.reminder;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.wildmind.fanwave.reminder.TVReminder;
import com.wildmind.fanwave.util.StringGenerator;
import com.wildmind.fanwave.widget.ReminderPicker;
import com.wildmind.fanwave.guide.ChannelManager;
import com.wildmind.fanwave.guide.TVChannel;
import com.wildmind.fanwave.media.ImageListener;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.app.ApplicationManager;

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

public class ReminderListAdapter extends BaseAdapter implements ReminderPicker.ReminderPickerListener {

	/**
	 * List type.
	 */
	public static final int TYPE_LIST_SELF 	= 0;
	public static final int TYPE_LIST_OTHER = 1;
	
	/**
	 * Row view type count.
	 * Bar		: 0
	 * Reminder	: 1
	 */
	private final int ROW_VIEW_TYPE_COUNT 	= 2;
	private final int ROW_TYPE_BAR 			= 0;	
	private final int ROW_TYPE_REMINDER 	= 1;
	
	private ListView reminder_listview;
	private Context context;
	private LayoutInflater inflater;
	private int list_type = -1;
	
	private ArrayList<TVReminder> reminders = null;
	private ArrayList<TVReminder> common_reminders = null;
	private ArrayList<TVReminder> uncommon_reminders = null;
	private ReminderListImageListener image_listener = new ReminderListImageListener();
	private ConcurrentHashMap<String, String> pgicon_requests = new ConcurrentHashMap<String, String>();
	
	/**
	 * Constructor
	 * @param listview
	 * @param type
	 * @param context
	 */
	public ReminderListAdapter (ListView listview, int type, Context context) {
		this.reminder_listview = listview;
		this.list_type = type;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		
		ImageManager.addImageListener(image_listener);
	}
	
	/**
	 * Constructor for TYPE_LIST_SELF
	 * @param listview
	 * @param reminders
	 * @param context
	 */
	public ReminderListAdapter (ListView listview, ArrayList<TVReminder> reminders, Context context) {
		this(listview, TYPE_LIST_SELF, context);
		this.reminders = reminders;
	}
	
	/**
	 * Constructor for TYPE_LIST_OTHER
	 * @param listview
	 * @param commons
	 * @param uncommons
	 * @param context
	 */
	public ReminderListAdapter (ListView listview, ArrayList<TVReminder> commons, 
			ArrayList<TVReminder> uncommons, Context context) {
		this(listview, TYPE_LIST_OTHER, context);
		this.common_reminders = commons;
		this.uncommon_reminders = uncommons;
	}
	
	/**
	 * Clear all resources.
	 */
	public void clear () {
		ImageManager.removeImageListener(image_listener);
		image_listener = null;
		
		reminder_listview = null;
		context = null;
		inflater = null;
		reminders = null;
		common_reminders = null;
		uncommon_reminders = null;
		
		pgicon_requests.clear();
		pgicon_requests = null;
	}
	
	@Override
	public int getCount() {
		int count = 0;
		switch (list_type) {
		case TYPE_LIST_SELF:
			count = reminders != null ? reminders.size() : 0;
			break;
			
		case TYPE_LIST_OTHER:
			if (common_reminders != null)
				count += common_reminders.size() > 0 ? common_reminders.size() + 1 : 0;
			if (uncommon_reminders != null)
				count += uncommon_reminders.size() > 0 ? uncommon_reminders.size() + 1 : 0;
			break;
		}
		
		return count;
	}

	@Override
	public Object getItem(int position) {
		Object obj = null;
		switch (list_type) {
		case TYPE_LIST_SELF:
			if (position < reminders.size())
				obj = reminders != null ? reminders.get(position) : null;
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
			return ROW_TYPE_REMINDER;
			
		case TYPE_LIST_OTHER:
			if (position == getCommonBarPosition() || position == getUncommonBarPosition())
				return ROW_TYPE_BAR;
			else
				return ROW_TYPE_REMINDER;
			
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
			return getReminderView((TVReminder)getItem(position), convertView, position, true);
			
		case TYPE_LIST_OTHER:
			if (getItemViewType(position) == ROW_TYPE_BAR)
				return getBarView(position, convertView, parent);
			else {
				int userPosition = 0;
				boolean remindering = getCommonItem(position) != null;
				if (remindering) {
					// user position in common reminders
					userPosition = position - 1;
				} else {
					// user position in uncommon reminders
					userPosition = position - (common_reminders.size() > 0 ? common_reminders.size() + 2 : 1);
				}
				return getReminderView((TVReminder)getItem(position), convertView, userPosition, remindering);
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
		return (common_reminders == null || common_reminders.size() == 0) ? -1 : 0;
	}
	
	/**
	 * Get uncommon bar position.
	 * @return
	 */
	public int getUncommonBarPosition () {
		if (uncommon_reminders == null || uncommon_reminders.size() == 0)
			return -1;
		else 
			return getCommonBarPosition () != -1 ? common_reminders.size() + 1 : 0;
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
		return itemPosition < common_reminders.size() ? common_reminders.get(itemPosition) : null;
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
			return itemPosition < uncommon_reminders.size() ? uncommon_reminders.get(itemPosition) : null;
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
			((TextView) view).setText(ApplicationManager.getAppContext().getString(R.string.personal_reminder_common));
		else if (position == getUncommonBarPosition())
			((TextView) view).setText(ApplicationManager.getAppContext().getString(R.string.personal_reminder_uncommon));
		
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
	private View getReminderView (TVReminder reminder, View view, int position, boolean reminding) {
		ReminderViewHolder viewHolder;
		if (view == null) {
			view = inflater.inflate(R.layout.personal_reminder_row, null);
			viewHolder = new ReminderViewHolder();
			viewHolder.program_icon = (ImageView) view.findViewById(R.id.program_icon_imageview);
			viewHolder.title = (TextView) view.findViewById(R.id.title_textview);
			viewHolder.sub_title = (TextView) view.findViewById(R.id.subtitle_textview);
			viewHolder.modify = (Button) view.findViewById(R.id.modify_button);
			viewHolder.divider = (ImageView) view.findViewById(R.id.divider_imageview);
			view.setTag(viewHolder);
		} else
			viewHolder = (ReminderViewHolder) view.getTag();
		
		// Program Icon
		viewHolder.program_icon.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.program_loading));
		if (reminder.getIconUrl().length() == 0)
			viewHolder.program_icon.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.program_default));
		else
			loadProgramIcon(viewHolder.program_icon, reminder.getTitle(), reminder.getIconUrl());
		
		// Title
		viewHolder.title.setText(reminder.getTitle());
				
		// Subtitle
		TVChannel ch = ChannelManager.getChannel(reminder.getChannelCode());
		String chname = ch != null ? ch.getChname() + " " : "";
		String subtitle = chname + StringGenerator.playTimeStringFromTimeString(reminder.getStartTime());
		viewHolder.sub_title.setText(subtitle);
		
		// Modify
		viewHolder.modify.setVisibility(list_type == TYPE_LIST_SELF ? View.VISIBLE : View.INVISIBLE);
		viewHolder.modify.setOnClickListener(getModifyClickedListener(reminder));
		
		// Divider
		if (list_type == TYPE_LIST_SELF)
			viewHolder.divider.setVisibility(View.VISIBLE);
		else 
			viewHolder.divider.setVisibility(
					reminding && position == common_reminders.size() - 1 && uncommon_reminders.size() > 0
			 								 ? View.INVISIBLE : View.VISIBLE);
		
		return view;
	}
	
	/**
	 * Callback for modify button clicked.
	 * @param reminder
	 * @return
	 */
	private View.OnClickListener getModifyClickedListener (final TVReminder reminder) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ReminderPicker rp = new ReminderPicker(context, reminder);
				rp.setReminderPickerListener(ReminderListAdapter.this);
				rp.prepare();
				rp.show();
			}
		};
	}
	
	/**
	 * Refresh reminder list data of type TYPE_LIST_SELF.
	 * @param reminders
	 */
	public void refreshData (ArrayList<TVReminder> reminders) {
		this.reminders = reminders;
		notifyDataSetChanged();
	}
	
	/**
	 * Refresh reminder list data of type TYPE_LIST_OTHER;
	 * @param commons
	 * @param uncommons
	 */
	public void refreshData (ArrayList<TVReminder> commons, ArrayList<TVReminder> uncommons) {
		this.common_reminders = commons;
		this.uncommon_reminders = uncommons;
		notifyDataSetChanged();
	}
	
	/**
	 * Reminder View Holder
	 * @author Kencool
	 *
	 */
	private class ReminderViewHolder {
		ImageView 	program_icon;
		ImageView	divider;
		TextView	title;
		TextView	sub_title;
		Button		modify;
	}
	
	/**
	 * Image Process Methods
	 */
	
	/**
	 * Load Program icon.
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
	private class ReminderListImageListener implements ImageListener {

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
			
			if (reminder_listview != null) {
				reminder_listview.post (new Runnable () {
					public void run () {
						if (reminder_listview == null)
							return;
						
						// go through current visible rows and check if refresh is need
						//
						for (int i = reminder_listview.getFirstVisiblePosition(); 
							 i <= reminder_listview.getLastVisiblePosition(); 
							 i++) {
							if (getItemViewType(i) == ROW_TYPE_BAR)
								continue;
						
							TVReminder reminder = (TVReminder) getItem(i);
							if (reminder != null && title.equals(reminder.getTitle())) {
								int childPosition = i - reminder_listview.getFirstVisiblePosition();
								ReminderViewHolder viewHolder = 
										(ReminderViewHolder) reminder_listview.getChildAt(childPosition).getTag();
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

	/**
	 * Reminder Picker Listener Methods
	 */
	
	@Override
	public void onReminderSet() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReminderModified() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReminderRemoved(TVReminder reminder) {
		reminders.remove(reminder);
		notifyDataSetChanged();
	}

	@Override
	public void onPickerCancel() {
		// TODO Auto-generated method stub
		
	}

}
