package com.wildmind.fanwave.activity;


import java.util.ArrayList;
import java.util.Calendar;

import com.wildmind.fanwave.guide.ChannelManager;
import com.wildmind.fanwave.guide.EpgManager;
import com.wildmind.fanwave.guide.PgbyChAdapter;
import com.wildmind.fanwave.guide.TVChannel;
import com.wildmind.fanwave.media.ImageListener;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.program.TVProgram;
import com.wildmind.fanwave.util.CalendarGenerator;
import com.wildmind.fanwave.util.StringGenerator;
import com.wildmind.fanwave.activity.R;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class GuideEpgforPgbyChActivity extends BaseActivity{
	
	private ArrayList<TVProgram> TVProgramList = new  ArrayList<TVProgram>(); 
	private ChannellistImageListener image_listener = new ChannellistImageListener();
	private ListView listview = null;
	private PgbyChAdapter pgbychadapter = null;
	private Calendar now_system_Calendar = null;
	private TextView gudie_epg_time_date;
	private ImageView guide_epg_channel_imageview;
	private LinearLayout gudie_epg_time_linearlayout, loading_indicator;
	private TVChannel channel;

	private int now_sysetm_Year  = 0;
	private int now_sysetm_Month = 0; 
	private int now_sysetm_Day	 = 0; 
	private int now_sysetm_Hour	 = 0; 
	private int now_sysetm_Minute= 0;
	private String localtime = null;
	
		@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// check if activity is destroyed due to abnormal situation
		if (isDestroyed())
			return;
		
		setContentView(R.layout.guide_epg_pgbuch_activity);

		initData();
		initUI();
	}
		
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
		
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
				
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		TVProgramList = null;
		listview = null;
		image_listener = null;	
		if (pgbychadapter != null)
			pgbychadapter.clear();
		pgbychadapter = null;
		if (TVProgramList != null)
			TVProgramList.clear();

		now_system_Calendar = null;
		guide_epg_channel_imageview = null;
		gudie_epg_time_linearlayout = null;
		loading_indicator = null;
		channel = null;
		localtime = null;
		ImageManager.removeImageListener(image_listener);
	}

	public void initData(){	
		Intent i = getIntent();
		channel = (TVChannel) i.getParcelableExtra("channel");
		ImageManager.addImageListener(image_listener);
		now_system_Calendar =CalendarGenerator.calendarFromTimeString(EpgManager.getUserSelectTime());
		setXmlCalendar();
	}

	public void initUI(){
		
		((TextView)findViewById(R.id.title_textview)).setText(channel.getChname());
		ImageButton back_button = (ImageButton) findViewById(R.id.back_imagebutton);
		back_button.setOnClickListener(getClickListener());
		
		gudie_epg_time_linearlayout = (LinearLayout) findViewById(R.id.gudie_epg_time_linearlayout);
	
		guide_epg_channel_imageview = (ImageView) findViewById(R.id.guide_epg_channel_imageview);
		
		// Loading Indicator
		loading_indicator = (LinearLayout) findViewById(R.id.loading_indicator);	
		
		listview = (ListView)findViewById(R.id.gudie_epg_channel_listview);
		pgbychadapter = new PgbyChAdapter(GuideEpgforPgbyChActivity.this, TVProgramList);
		listview.setAdapter(pgbychadapter);
		loadChicon(guide_epg_channel_imageview, channel.getChcode());
		
		
		gudie_epg_time_date = (TextView) findViewById(R.id.gudie_epg_time_date);
		setLocalDateInfo();
		refreshView();
		
	 } 	
	     
	 private OnClickListener getClickListener(){ 
		 return new OnClickListener() { 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch(v.getId()){
					case R.id.back_imagebutton:
						finish();
						break;

				}	

			}
		 };
	 }
	 
		/**
		 * Show list view.
		 */
		private void showList() {
			listview.setVisibility(View.VISIBLE);
			loading_indicator.setVisibility(View.GONE);
		}

		/**
		 * Show  loading view.
		 */
		private void showLoading() {
			listview.setVisibility(View.GONE);
			loading_indicator.setVisibility(View.VISIBLE);
		}
		
	 	/**
		 * Show time
		 */
		private void setLocalDateInfo() {
			gudie_epg_time_date.setText( localtime.substring(0, 4)
										+ "."
										+ localtime.substring(4, 6) 
										+ "." 
										+ localtime.substring(6, 8) 
										+ "\t" 
										+ getweek(localtime));

			gudie_epg_time_linearlayout.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					nowSystemTime();
				}
			});
			
		}
		
		/**
		 * Set local time
		 */
		private void setXmlCalendar() {
			localtime = StringGenerator.timeStringFromCalendar(now_system_Calendar);
		}

		/**
		 *  System Time
		 */
		public void nowSystemTime() {
			long time = System.currentTimeMillis();
			Calendar systemTime = Calendar.getInstance();
			systemTime.setTimeInMillis(time);
			now_sysetm_Year = systemTime.get(Calendar.YEAR);
			now_sysetm_Month = systemTime.get(Calendar.MONTH);
			now_sysetm_Day = systemTime.get(Calendar.DATE);
			now_sysetm_Hour = systemTime.get(Calendar.HOUR);
			if (systemTime.get(Calendar.AM_PM) == Calendar.PM) {
				now_sysetm_Hour += 12;
			}
			now_sysetm_Minute = systemTime.get(Calendar.MINUTE);
			updateDisplay();
		}
		
		/**
		 * Update and Show time
		 */
		public void updateDisplay() {
			StringBuilder time = new StringBuilder();
			time.append(now_sysetm_Year);
			time.append(format(now_sysetm_Month));
			time.append(format(now_sysetm_Day));
			time.append(format(now_sysetm_Hour));
			time.append(format(now_sysetm_Minute));
			String title = now_sysetm_Year
							 +"."
							 +format(now_sysetm_Month + 1)
							 +"."
							 +format(now_sysetm_Day);
			
			SetTimeDialog(title);
		}
		
		/**
		 * Show set time Dialog
		 */
		public void SetTimeDialog(String Title) {
			AlertDialog.Builder dialog_builder = new AlertDialog.Builder(GuideEpgforPgbyChActivity.this);
			dialog_builder.setTitle(Title)
						  .setItems(R.array.timeaialog_pgbych_array, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			    	switch (item) {
			    	case 0:
			    		DatePickerDialog DatePd = new DatePickerDialog(
			    				GuideEpgforPgbyChActivity.this,
								new DatePickerDialog.OnDateSetListener() {

									public void onDateSet(DatePicker view,
											int year, int monthOfYear,
											int dayOfMonth) {
										now_sysetm_Year = year;
										now_sysetm_Month = monthOfYear;
										now_sysetm_Day = dayOfMonth;
										updateDisplay();
									}
								}, now_sysetm_Year, now_sysetm_Month, now_sysetm_Day);
						DatePd.show();
						break;
	
			    	case 1:
			    		nowSystemTime();
						break;
			    	}
			    }
			}).setPositiveButton(getResources().getString(R.string.action_confirm), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                //do things
		        	   
		        	   changeUserSelect();
		        	   
		           }
		    }).setNegativeButton(getResources().getString(R.string.action_cancel), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                //do things
		           }
		    });;
		    AlertDialog	account_alertdialog = dialog_builder.create();
			account_alertdialog.show();

		}
		
		/**
		 * Use user Select
		 */
		private void changeUserSelect() {
			now_system_Calendar.clear();
			now_system_Calendar.set(Calendar.YEAR, now_sysetm_Year);
			now_system_Calendar.set(Calendar.MONTH, now_sysetm_Month);
			now_system_Calendar.set(Calendar.DATE, now_sysetm_Day);
			now_system_Calendar.set(Calendar.HOUR, now_sysetm_Hour);
			now_system_Calendar.set(Calendar.MINUTE, now_sysetm_Minute);
			setXmlCalendar();
			setLocalDateInfo();
			refreshView();
		}
		
		public void refreshView(){
			if (!isDestroyed()) {
				showLoading();
				new Thread ( new Runnable () {
					public void run () {
						String start = localtime.substring(0,8)+"0000";
						String end   = StringGenerator.timeStringShiftWithHours(start, 24);
						TVProgramList = ChannelManager.DownloadProgramlistforChannel(channel.getChcode(), start, end);
						GuideEpgforPgbyChActivity.this.runOnUiThread( new Runnable () {
							public void run () {	
								if (!isDestroyed()) {
								pgbychadapter.refreshData(TVProgramList);
								showList();
								}
							}
						});
					}
				}).start();
			}
		}
		
		
		/**
		 * 
		 * 
		 * @param x
		 * @return 
		 */
		
		public String format(int x) {
			String format = "" + x;
			if (format.length() == 1) {
				format = "0" + x;
			}
			return format;
		}
		
		/**
		 * Get week for time
		 * @param time
		 * @return week
		 */
		private String getweek(String time) {
			String year;
			String month;
			String day;
			year = time.substring(0, 4);
			month = time.substring(4, 6);
			day = time.substring(6, 8);
			Calendar xmlCalendar = null;
			xmlCalendar = Calendar.getInstance();
			xmlCalendar.set(Calendar.YEAR, Integer.valueOf(year));
			xmlCalendar.set(Calendar.MONTH, Integer.valueOf(month)-1);
			xmlCalendar.set(Calendar.DATE, Integer.valueOf(day));

			int weekint = xmlCalendar.get(Calendar.DAY_OF_WEEK);

			return CalendarGenerator.getWeekDayString(weekint);
		} 
		
		
		/**
		 * Load ChannelIcon.
		 * @param chcode
		 */
		private void loadChicon (ImageView iv, String chcode) {
			if (chcode == null || chcode.length() == 0)
				return;
			
			if (ImageManager.drawChannelIcon(iv, chcode))
				ImageManager.removeImageListener(image_listener);
		}
		
		/**
		 * Listener for listening image loading request.
		 * @author 
		 *
		 */
		private class ChannellistImageListener implements ImageListener {

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
			public void retrieveChannelIcon(String title, final Bitmap bmp) {
				// TODO Auto-generated method stub
				if (bmp == null)
					return;
				GuideEpgforPgbyChActivity.this.runOnUiThread(new Runnable () {
					public void run () {
						if (isDestroyed())
							return;
						
						guide_epg_channel_imageview.setImageBitmap(bmp);
						ImageManager.removeImageListener(image_listener);
					}
				});
			}
			
			
		}

}
