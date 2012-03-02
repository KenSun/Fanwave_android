package com.wildmind.fanwave.program;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.wildmind.fanwave.media.ImageListener;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.reminder.ReminderManager;
import com.wildmind.fanwave.reminder.TVReminder;
import com.wildmind.fanwave.util.StringGenerator;
import com.wildmind.fanwave.widget.ReminderPicker;
import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.app.ApplicationManager;

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

public class ProgramReminderAdapter extends BaseAdapter implements ReminderPicker.ReminderPickerListener {

	private Context context;
	private LayoutInflater inflater;
	private ListView replay_listview;
	
	private ArrayList<TVReminder> replays = null;
	private ConcurrentHashMap<String, String> chicon_requests = new ConcurrentHashMap<String, String>();
	
	private ReplaysImageListener image_listener = new ReplaysImageListener();
	
	public ProgramReminderAdapter (ListView listview, ArrayList<TVReminder> replays, Context context) {
		this.replay_listview = listview;
		this.replays = replays;
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
		
		context = null;
		inflater = null;
		replay_listview = null;
		replays = null;
		
		chicon_requests.clear();
		chicon_requests = null;
	}
	
	@Override
	public int getCount() {
		return replays != null ? replays.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return replays != null ? replays.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public boolean areAllItemsEnabled () {
		return false;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		TVReminder reminder = replays.get(position);
		ViewHolder viewHolder;
		if (view == null) {
			view = inflater.inflate(R.layout.program_extrainfo_reminder_row, null);
			viewHolder = new ViewHolder();
			viewHolder.channel_icon = (ImageView) view.findViewById(R.id.channel_imageview);
			viewHolder.channel = (TextView) view.findViewById(R.id.channel_textview);
			viewHolder.title = (TextView) view.findViewById(R.id.title_textview);
			viewHolder.sub_title = (TextView) view.findViewById(R.id.subtitle_textview);
			viewHolder.action = (Button) view.findViewById(R.id.action_button);
			view.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) view.getTag();
		
		// Channel Icon
		loadChannelIcon(viewHolder.channel_icon, reminder.getChannelCode());
		
		// Channel Name
		viewHolder.channel.setText(reminder.getChannelName());
		
		// Title
		String title = reminder.getSubTitle();
		if (title.length() > 0 && !title.equals("none")) {
			viewHolder.title.setVisibility(View.VISIBLE);
			viewHolder.title.setText(title);
		} else
			viewHolder.title.setVisibility(View.GONE);
		
		// Subtitle
		viewHolder.sub_title.setText(StringGenerator.playTimeStringFromTimeString(reminder.getStartTime()));

		// Action
		viewHolder.action.setText(ReminderManager.isReminderSet(reminder) 
				? ApplicationManager.getAppContext().getResources().getString(R.string.reminder_modify) 
				: ApplicationManager.getAppContext().getResources().getString(R.string.reminder_set));
		viewHolder.action.setOnClickListener(getReminderClickedListener(reminder));
		
		return view;
	}
	
	private View.OnClickListener getReminderClickedListener (final TVReminder reminder) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ReminderPicker rp = new ReminderPicker(context, reminder);
				rp.setReminderPickerListener(ProgramReminderAdapter.this);
				rp.prepare();
				rp.show();
			}
		};
	}
	
	/**
	 * Refresh replays data.
	 * @param replays
	 */
	public void refreshData (ArrayList<TVReminder> replays) {
		this.replays = replays;
		notifyDataSetChanged();
	}
	
	private class ViewHolder {
		ImageView	channel_icon;
		TextView	channel;
		TextView	title;
		TextView	sub_title;
		Button		action;
	}
	
	/**
	 * Image Process Methods
	 */
	
	/**
	 * Load channel icon.
	 * @param iv
	 * @param chcode
	 */
	private void loadChannelIcon (ImageView iv, String chcode) {
		if (chcode == null || chcode.length() == 0)
			return;
		
		if (!ImageManager.drawChannelIcon(iv, chcode))
			chicon_requests.put(chcode, "");
	}
	
	/**
	 * Listen for listening image requests.
	 * @author Kencool
	 *
	 */
	private class ReplaysImageListener implements ImageListener {

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
		public void retrieveProgramIcon(String title, int sampleBase, Bitmap bmp) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void retrieveChannelIcon(final String chcode, final Bitmap bmp) {
			if (chicon_requests == null || !chicon_requests.containsKey(chcode) || bmp == null)
				return;
			
			if (replay_listview != null) {
				replay_listview.post( new Runnable () {
					public void run () {
						int firstPosition = replay_listview.getFirstVisiblePosition();
						int lastPosition = replay_listview.getLastVisiblePosition();
						
						for (int i = firstPosition; i <= lastPosition; i++) {
							if (i >= replays.size())
								break;
							
							if (chcode.equals(replays.get(i).getChannelCode())) {
								int childPosition = i - firstPosition;
								ViewHolder viewHolder = 
										(ViewHolder) replay_listview.getChildAt(childPosition).getTag();
								viewHolder.channel_icon.setImageBitmap(bmp);
							}
						}
						
						chicon_requests.remove(chcode);
					}
				});
			}
		}
	}

	@Override
	public void onReminderSet() {
		notifyDataSetChanged();
	}

	@Override
	public void onReminderModified() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReminderRemoved(TVReminder reminder) {
		notifyDataSetChanged();
	}

	@Override
	public void onPickerCancel() {
		// TODO Auto-generated method stub
		
	}
}
