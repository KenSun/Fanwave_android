package com.wildmind.fanwave.guide;

import java.util.ArrayList;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.activity.GuideActivity;
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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProgramListAdapter extends BaseAdapter  implements ReminderPicker.ReminderPickerListener {
	
	private String time;
	private boolean islonding = true;
	private Context context;
	private LayoutInflater inflater;
	private ArrayList<TVChannel> Channellist;
	private ArrayList<TVProgram> Programlist;
	
	// constructor
	//
	public ProgramListAdapter ( Context _context, ArrayList<TVChannel> tvclist,
			ArrayList<TVProgram> tvplist, 
			String time) {
		this.time = time;
		this.Channellist = tvclist;
		this.Programlist = tvplist;
		this.context = _context;
		this.inflater = LayoutInflater.from(_context);

	}
	
	// instance variable setters
	//
	public void setChannellist (ArrayList<TVChannel> list) {
		this.Channellist = list;
		
		if(list == null);
		clear ();
	}
	
	public void setProgramlist (ArrayList<TVProgram> list) {
		this.Programlist = list;
	}
	
	public String getTime () {
		return time;
	}


	/**
	 * Clear all resources.
	 */
	public void clear () {
		context = null;
		inflater = null;
		
		if(Programlist!=null)
			Programlist.clear();
		if(Channellist!=null)
			Channellist.clear();
		
		Programlist = null;
		Channellist = null;
	}
	

	/**
	 * Refresh data.
	 * @param list
	 * @param is_more
	 */
	public void refreshData ( ArrayList<TVChannel> tvclist, ArrayList<TVProgram> tvplist) {
		islonding = false;
		this.Channellist = tvclist;
		
		if(tvplist!=null)
			this.Programlist = tvplist;

		this.notifyDataSetChanged();
	}
	
	/**
	 * List View Resources Methods
	 */
	@Override
	public int getCount() {
		return Channellist!=null? Channellist.size() : 0;
	}

	@Override
	public TVChannel getItem(int position) {
		return Channellist!= null? Channellist.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getItemViewType (int position) {	
			return position;	
		}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// get more view
		final TVChannel tvc = Channellist.get(position);
		EpgViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new EpgViewHolder();
			convertView = inflater.inflate(R.layout.guide_epg_list_item_pg, null);
			viewHolder.program_Layout	= (RelativeLayout) convertView.findViewById(R.id.guide_epg_icon_relativelayout);
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
		
		viewHolder.hot.setVisibility(View.GONE);
		viewHolder.films.setVisibility(View.GONE);
		viewHolder.program_time.setText("");
		viewHolder.program_sub.setText("");
		viewHolder.program_name.setText(ApplicationManager.getAppContext().getResources().getString(R.string.action_loading));
		
		viewHolder.follow.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.guide_epg_program_follow_n));
		viewHolder.reminder.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.guide_epg_program_reminder_n));
		viewHolder.hot.setImageBitmap(null);
		viewHolder.films.setImageBitmap(null);
		
		boolean havedata = false;
		
		if(Programlist != null){		
			for (final TVProgram tvpg : Programlist) {
				if (tvc.getChcode().equals(tvpg.getChannelCode())) {
					havedata = true;
					if(Double.valueOf(tvpg.getStartTime()) <= Double.valueOf(time)&&Double.valueOf(tvpg.getEndTime()) >= Double.valueOf(time)){	
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
							viewHolder.program_sub.setText("");
						else{
							viewHolder.program_sub.setText(tvpg.getSubTitle());
						}
						viewHolder.program_Layout.setOnClickListener(new OnClickListener(){		
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								openProgramActivity(tvpg);
		
							}});
						
//						if(tvpg.isHot())
//							viewHolder.hot.setImageResource(R.drawable.guide_epg_program_hot_y);
//						else
//							viewHolder.hot.setImageResource(R.drawable.guide_epg_program_hot_n);
//
//						if(tvpg.hasVideo())
//							viewHolder.films.setImageResource(R.drawable.guide_epg_program_films_y);
//						else
//							viewHolder.films.setImageResource(R.drawable.guide_epg_program_films_n);
						
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
						viewHolder.add.setEnabled(true);
						viewHolder.add.setOnClickListener(getAddClickedListener (tvpg));
					}
				}
			}
			if(!havedata&&!islonding){
				viewHolder.program_name.setText(ApplicationManager.getAppContext().getResources().getString(R.string.guides_noprogram));
				viewHolder.add.setEnabled(false);
			}
		}
		return convertView;
	}
	
	/**
	 * Show set Dialog
	 */
	public void SetDialog(final TVProgram tvpg) {
		final TVReminder reminder = new TVReminder(tvpg, AccountManager.getCurrentUser().getUsername(), null, -1, null, 0);

		AlertDialog.Builder dialog_builder = new AlertDialog.Builder(context);
		dialog_builder.setTitle(tvpg.getTitle())
					  .setItems(getItems(tvpg, reminder), new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	switch (item) {
		    	case 0:
	    			final ProgressDialog progressDialog = new ProgressDialog((GuideActivity)context);
					progressDialog.setMessage(ApplicationManager.getAppContext().getResources().getString(R.string.action_setting));
					progressDialog.show();	

		    		new Thread( new Runnable() {
		    			public void run() {
	
				    		if(FollowManager.isFollowing(tvpg.getPgid())){
				    			if(FollowManager.unfollowProgram(tvpg)){
				    				((GuideActivity)context).runOnUiThread(new Runnable() {
			    						public void run() {
			    							if(progressDialog!=null&&progressDialog.isShowing())
												progressDialog.dismiss();
			    							 EpgManager.getViewProgramList();
			    						}
			    					});	
				    			}else{
									Toast.makeText(context, R.string.program_unfollow_failed, Toast.LENGTH_SHORT).show();
				    			}
				    				
				    		}else
				    			if(FollowManager.followProgram(tvpg)){
				    				((GuideActivity)context).runOnUiThread(new Runnable() {
			    						public void run() {
			    							if(progressDialog!=null&&progressDialog.isShowing())
												progressDialog.dismiss();
			    							 EpgManager.getViewProgramList();
			    						}
			    					});
				    			}else{
									Toast.makeText(context, R.string.program_follow_failed, Toast.LENGTH_SHORT).show();
				    			}
				    		
		    			}
		    		}).start();
		    		
					break;
					
		    	case 1:
		    		ReminderPicker rp = new ReminderPicker(context, reminder);
					rp.setReminderPickerListener(ProgramListAdapter.this);
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
				SetDialog(tvpg);
			}
		};
	}
	
	
	public CharSequence[] getItems( TVProgram tvpg, TVReminder reminder){

		if( Double.valueOf(StringGenerator.getCurrentTimeString()) < Double.valueOf(tvpg.getStartTime())){
			CharSequence[] items = {FollowManager.isFollowing(tvpg.getPgid())? ApplicationManager.getAppContext().getResources().getString(R.string.personal_action_unfollow) : ApplicationManager.getAppContext().getResources().getString(R.string.guides_program_follow), 
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
		RelativeLayout program_Layout;

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
	 * Open program activity with TVProgram tvpg.
	 * @param tvpg
	 */
	public void openProgramActivity (TVProgram tvpg) {
		if (tvpg.getTitle().length() == 0)
			return;
		
		Intent intent = new Intent();
		intent.setClass(context, ProgramActivity.class);
		intent.putExtra("program", tvpg);
		intent.putExtra("back_image", BitmapFactory.decodeResource(context.getResources(), R.drawable.guide_icon));
		context.startActivity(intent);
	}

	@Override
	public void onReminderSet() {
		// TODO Auto-generated method stub
		EpgManager.getViewProgramList();
	}

	@Override
	public void onReminderModified() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReminderRemoved(TVReminder reminder) {
		// TODO Auto-generated method stub
		EpgManager.getViewProgramList();
	}

	@Override
	public void onPickerCancel() {
		// TODO Auto-generated method stub
		
	}


	
}
