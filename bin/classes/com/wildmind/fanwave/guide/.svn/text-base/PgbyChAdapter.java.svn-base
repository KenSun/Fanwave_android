package com.wildmind.fanwave.guide;

import java.util.ArrayList;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.activity.ProgramActivity;
import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.program.TVProgram;
import com.wildmind.fanwave.reminder.ReminderManager;
import com.wildmind.fanwave.reminder.TVReminder;
import com.wildmind.fanwave.util.StringGenerator;
import com.wildmind.fanwave.widget.ReminderPicker;
import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.follow.FollowManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PgbyChAdapter extends BaseAdapter implements ReminderPicker.ReminderPickerListener {
	
	private ArrayList<TVProgram> pglist = null;
	private LayoutInflater inflater;
	private Context _context;

	
	// constructor
	//
	public PgbyChAdapter ( Context _context, ArrayList<TVProgram> pglist) {
		this.pglist = pglist;
		this._context = _context;
		this.inflater = LayoutInflater.from(_context);
	}
	
	/**
	 * Clear all resources.
	 */
	public void clear () {
		inflater = null;
		
		pglist.clear();
		pglist = null;
	}
	
	/**
	 * Refresh data.
	 * @param ArrayList
	 */
	public void refreshData (ArrayList<TVProgram> ArrayList) {
		this.pglist = ArrayList;
		this.notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pglist.size();
	}

	@Override
	public TVProgram getItem(int position) {
		// TODO Auto-generated method stub
		return pglist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final TVProgram tvpg = pglist.get(position);
			
		EpgViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new EpgViewHolder();
			convertView = inflater.inflate(R.layout.guide_epg_list_item, null);
			viewHolder.program_sub 		= (TextView) convertView.findViewById(R.id.guide_epg_program_detail_textview);
			viewHolder.program_name 	= (TextView) convertView.findViewById(R.id.guide_epg_program_title_textview);
			viewHolder.program_time 	= (TextView) convertView.findViewById(R.id.guide_epg_program_time_textview);
			viewHolder.follow 			= (ImageView) convertView.findViewById(R.id.guide_epg_follow_imageview);
			viewHolder.reminder 		= (ImageView) convertView.findViewById(R.id.guide_epg_reminder_imageview);
			viewHolder.hot 				= (ImageView) convertView.findViewById(R.id.guide_epg_hot_imageview);
			viewHolder.films			= (ImageView) convertView.findViewById(R.id.guide_epg_films_imageview);
			viewHolder.add 				= (Button) convertView.findViewById(R.id.guide_epg_add_imagebutton);

			convertView.setTag(viewHolder);
		} else
			viewHolder = (EpgViewHolder) convertView.getTag();
		
		if(tvpg!=null)
			convertView.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					openProgramActivity(tvpg);
				}
				
			});

		
		viewHolder.program_time.setText("");
		viewHolder.program_sub.setText("");
		viewHolder.program_name.setText(ApplicationManager.getAppContext().getResources().getString(R.string.guides_noprogram));
		viewHolder.follow.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.guide_epg_program_follow_n));
		viewHolder.reminder.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.guide_epg_program_reminder_n));
		viewHolder.hot.setImageBitmap(null);
		viewHolder.films.setImageBitmap(null);
		
		
		viewHolder.program_time.setText( tvpg.getStartTime().substring(8, 10)
										+":"
										+tvpg.getStartTime().substring(10, 12)
										+"~"
										+tvpg.getEndTime().substring(8, 10)
										+":"
										+tvpg.getEndTime().substring(10, 12)
										);
						
		viewHolder.program_name.setText(tvpg.getTitle());
		if(tvpg.getSubTitle().equals("")||tvpg.getSubTitle().equals("none"))
			viewHolder.program_sub.setVisibility(View.GONE);
		else{
			viewHolder.program_sub.setVisibility(View.VISIBLE);
			viewHolder.program_sub.setText(tvpg.getSubTitle());
		}				
						
//		if(tvpg.isHot())
//			viewHolder.hot.setImageResource(R.drawable.guide_epg_program_hot_y);
//		else
//			viewHolder.hot.setImageResource(R.drawable.guide_epg_program_hot_n);
//
//						
//		if(tvpg.hasVideo())
//			viewHolder.films.setImageResource(R.drawable.guide_epg_program_films_y);
//		else
//			viewHolder.films.setImageResource(R.drawable.guide_epg_program_films_n);


		if(getFollow(tvpg)){
			viewHolder.follow.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.guide_epg_program_follow_y));
		}else{
			viewHolder.follow.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.guide_epg_program_follow_n));
		}
	
		if(getReminder(tvpg)){
			viewHolder.reminder.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.guide_epg_program_reminder_y));
		}else{
			viewHolder.reminder.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.guide_epg_program_reminder_n));
		}
		
		
		viewHolder.add.setOnClickListener(getAddClickedListener (tvpg));

		
		return convertView;
	}

	/**
	 * Show set Dialog
	 */
	public void SetDialog(final TVProgram tvpg, String nowtime) {
		final TVReminder reminder = new TVReminder(tvpg, AccountManager.getCurrentUser().getUsername(), null, -1, null, 0);

		AlertDialog.Builder dialog_builder = new AlertDialog.Builder(_context);
		dialog_builder.setTitle(tvpg.getTitle())
					  .setItems(getItems(nowtime, tvpg, reminder), new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	switch (item) {
		    	case 0:
		    		if(FollowManager.isFollowing(tvpg.getPgid())){
		    			if(FollowManager.unfollowProgram(tvpg))
		    				notifyDataSetChanged();
		    			else
							Toast.makeText(_context, R.string.program_unfollow_failed, Toast.LENGTH_SHORT).show();
		    		}else
		    			if(FollowManager.followProgram(tvpg))
		    				notifyDataSetChanged();
		    			else
							Toast.makeText(_context, R.string.program_follow_failed, Toast.LENGTH_SHORT).show();

					break;
					
		    	case 1:
		    		ReminderPicker rp = new ReminderPicker(_context, reminder);
					rp.setReminderPickerListener(PgbyChAdapter.this);
					rp.prepare();
					rp.show();

					break;

		    	}
		    }
	    }).setNegativeButton(ApplicationManager.getAppContext().getResources().getString(R.string.action_cancel), new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	                //do things
	           }
	    });
	    AlertDialog	account_alertdialog = dialog_builder.create();
		account_alertdialog.show();

	}
	
	private View.OnClickListener getAddClickedListener (final TVProgram tvpg) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SetDialog(tvpg, StringGenerator.getCurrentTimeString());
			}
		};
	}
	
	
	public CharSequence[] getItems(String nowtime, TVProgram tvpg, TVReminder reminder){

		if(Double.valueOf(tvpg.getStartTime()) > Double.valueOf(nowtime)){
			CharSequence[] items = {FollowManager.isFollowing(tvpg.getPgid())? ApplicationManager.getAppContext().getResources().getString(R.string.personal_action_unfollow) : ApplicationManager.getAppContext().getResources().getString(R.string.personal_action_follow), 
					ReminderManager.isReminderSet(reminder) ? ApplicationManager.getAppContext().getResources().getString(R.string.guides_program_reminder_modify) : ApplicationManager.getAppContext().getResources().getString(R.string.guides_program_reminder_set)};
			return items;
		}else{
			CharSequence[] items = {FollowManager.isFollowing(tvpg.getPgid())? ApplicationManager.getAppContext().getResources().getString(R.string.personal_action_unfollow) : ApplicationManager.getAppContext().getResources().getString(R.string.personal_action_follow)};
			return items;
		}		
	}
	
	/**
	 * Get Follow for tvpg
	 * @param TVProgram
	 * @return boolean
	 */
	public boolean getFollow(TVProgram tvpg){
		return FollowManager.isFollowing(tvpg.getPgid());		
	}
	
	
	
	/**
	 * Get Reminder for tvpg
	 * @param TVProgram
	 * @return boolean
	 */
	public boolean getReminder(TVProgram tvpg){
		TVReminder reminder = new TVReminder(tvpg, AccountManager.getCurrentUser().getUsername(), null, -1, null, 0);
		return ReminderManager.isReminderSet(reminder);	
	}
	
	
	private class EpgViewHolder {
		TextView 	program_name;
		TextView 	program_sub;
		TextView 	program_time;
		ImageView 	follow;
		ImageView 	reminder;
		ImageView 	hot;
		ImageView 	films;
		Button      add;
	}
	
	/**
	 * Open program activity with TVProgram tp.
	 * @param tp
	 */
	public void openProgramActivity (TVProgram tp) {
		if (tp.getTitle().length() == 0)
			return;
		
		Intent i = new Intent(_context, ProgramActivity.class);
		i.putExtra("program", tp);
		i.putExtra("back_image", BitmapFactory.decodeResource(_context.getResources(), R.drawable.channel_icon));
		_context.startActivity(i);
	}

	@Override
	public void onReminderSet() {
		// TODO Auto-generated method stub
		notifyDataSetChanged();
	}

	@Override
	public void onReminderModified() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReminderRemoved(TVReminder reminder) {
		// TODO Auto-generated method stub
		notifyDataSetChanged();
	}

	@Override
	public void onPickerCancel() {
		// TODO Auto-generated method stub
		
	}
 
	
		
}
