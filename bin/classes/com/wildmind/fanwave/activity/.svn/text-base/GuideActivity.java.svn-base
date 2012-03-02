package com.wildmind.fanwave.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.wildmind.fanwave.animation.FWOptionAnimation;
import com.wildmind.fanwave.follow.TVFollow;
import com.wildmind.fanwave.guide.ChannelListAdapter;
import com.wildmind.fanwave.guide.EpgManager;
import com.wildmind.fanwave.guide.EpgManager.ChangeAdapterListener;
import com.wildmind.fanwave.guide.EpgManager.ProgramListAdapterListener;
import com.wildmind.fanwave.guide.ChannelManager;
import com.wildmind.fanwave.guide.ListViewAdapter;
import com.wildmind.fanwave.guide.ProgramListAdapter;
import com.wildmind.fanwave.guide.TVChannel;
import com.wildmind.fanwave.hot.HotListAdapter;
import com.wildmind.fanwave.hot.HotManager;
import com.wildmind.fanwave.hot.TVEvent;
import com.wildmind.fanwave.hot.TVHot;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.program.TVProgram;
import com.wildmind.fanwave.reminder.TVReminder;
import com.wildmind.fanwave.util.CalendarGenerator;
import com.wildmind.fanwave.util.StringGenerator;
import com.wildmind.fanwave.vendor.VendorManager;
import com.wildmind.fanwave.widget.ViewFlow.ViewChangeListener;
import com.wildmind.fanwave.widget.ViewFlow;
import com.wildmind.fanwave.activity.R;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.AdapterView.OnItemClickListener;

public class GuideActivity extends BaseActivity implements OnItemClickListener {

	public static final int EPG_SEGMENT 		= 0;
	public static final int FOCUS_SEGMENT 		= 1;
	public static final int ACTIVES_SEGMENT 	= 2;

	
	// UI variables
	//
	private FrameLayout option_layout;
	private LinearLayout loading_indicator;
	private LinearLayout Epg_indicator;
	private ListView guide_list_view;
	private HotListAdapter guide_list_adapter;
	private TextView epg_textview, focus_textview, actives_textview, descr_textview;


	// structure variables
	//
	private ArrayList<TVHot> focus_guide_list = null;
	private ArrayList<TVEvent> actives_guide_list = null;
	private int focus_guide_position = 0;
	private int actives_guide_position = 0;
	private int selected_segment = EPG_SEGMENT;
	private boolean is_onCreate = false;

	private HashMap<String, TVFollow> follow_map = null;
	private HashMap<String, TVReminder> reminder_map = null;	
	//for time & epg ui
	
	private Calendar now_system_Calendar = null;
	
	private int now_sysetm_Year  = 0;
	private int now_sysetm_Month = 0; 
	private int now_sysetm_Day	 = 0; 
	private int now_sysetm_Hour	 = 0; 
	private int now_sysetm_Minute= 0;
	private String localtime = null;
	private int  screenWidth  = 0;
	private int  channelWidth = 0;
	private int  programWidth = 0;
	
	private TextView gudie_epg_provider, gudie_epg_time_date, gudie_epg_time_time;
	private LinearLayout gudie_epg_time_linearlayout;
	private ListView gudie_epg_channel_listview;
	private ViewFlow gudie_epg_program_viewflow;
	private ArrayList<TVChannel> listChannels  = null;
	private ChannelListAdapter channellist_adapter = null;
	private ListViewAdapter viewflow_lv_adapter = null;
	private PgListAdapterListener pgladapterListener = new PgListAdapterListener();
	private AdapterChangeListener dapterchangeListener = new AdapterChangeListener();
	
	private ViewListener listener = new ViewListener();
    private ArrayList<ListView> ListViewList = new ArrayList<ListView>();	
    
	/**
	 * Activity Life Cycle Methods
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// check if activity is destroyed due to abnormal situation
		if (isDestroyed())
			return;
		
		setContentView(R.layout.guide_activity);
		is_onCreate = true;
		
		setTitlesWidth();
		
		initData();
		initUI();
		refreshGuides();
	}

	protected void onStart() {
		super.onStart();
		// The activity is about to become visible.		
	}
	
	protected void onRestart() {
		super.onRestart();
		is_onCreate = false;
	}
	protected void onResume() {
		super.onResume();
		// The activity has become visible (it is now "resumed").
		if(!is_onCreate)
			RestartEpgUI();			
	}

	protected void onPause() {
		super.onPause();
		// Another activity is taking focus (this activity is about to be
		// "paused").
	}

	protected void onStop() {
		super.onStop();
		// The activity is no longer visible (it is now "stopped")
	}

	protected void onDestroy() {
		super.onDestroy();
		
		option_layout = null;
		loading_indicator = null;
		guide_list_view = null;
		gudie_epg_provider = null;
		gudie_epg_time_date = null;
		gudie_epg_time_time = null;
		gudie_epg_time_linearlayout = null;
		now_system_Calendar = null;
		
		if (guide_list_adapter != null)
			guide_list_adapter.clear();
		guide_list_adapter = null;
		
		epg_textview = null;
		focus_textview = null;
		actives_textview = null;
		descr_textview = null;

		if (focus_guide_list != null)
			focus_guide_list.clear();
		focus_guide_list = null;
		
		if (actives_guide_list != null)
			actives_guide_list.clear();
		actives_guide_list = null;
		
		if(listChannels!=null)
			listChannels.clear();
		listChannels = null;

		if (viewflow_lv_adapter != null)
			viewflow_lv_adapter.clear();
		viewflow_lv_adapter = null;
		
		if (channellist_adapter != null)
			channellist_adapter.clear();
		channellist_adapter = null;
		
		if (follow_map != null)
			follow_map.clear();
		follow_map = null;
		
		if (reminder_map != null)
			reminder_map.clear();
		reminder_map = null;

		gudie_epg_channel_listview = null;
		
		ViewFlow.removeListener(listener);
		EpgManager.removeListener(pgladapterListener);
		EpgManager.removeChangeListener(dapterchangeListener);
		
		for(ListView lv : ListViewList)
			((ProgramListAdapter) lv.getAdapter()).clear();
		
		if(ListViewList!=null)
			ListViewList.clear();
		
		if (ListViewList != null) {
			for(ListView lv : ListViewList)
				((ProgramListAdapter) lv.getAdapter()).clear();
			ListViewList.clear();
		}

		if (gudie_epg_program_viewflow != null) {
			gudie_epg_program_viewflow.destroyDrawingCache();
			gudie_epg_program_viewflow.clearDisappearingChildren();
			gudie_epg_program_viewflow.clearAnimation();
		}
	}
	
	/**
	 * Set width 
	 */
	private void setTitlesWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		programWidth = (dm.widthPixels/3)*2-10;
		channelWidth = screenWidth - programWidth;
		dm = null;
	}
	
	// Initialization Methods
	//
	private void initData() {

	}

	private void initUI() {
		// Back Button
		ImageButton back_button = (ImageButton) findViewById(R.id.back_imagebutton);
		back_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		
		// Setting Button
		ImageButton setting_button = (ImageButton) findViewById(R.id.setting_imagebutton);
		setting_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent mIntent = new Intent(GuideActivity.this, GuideSettingsActivity.class);
				startActivity(mIntent);
			}
		});
		
		// Option Layout
		option_layout = (FrameLayout) findViewById(R.id.option_layout);

		// Epg Segment Control
		epg_textview = (TextView) findViewById(R.id.epg_textview);
		epg_textview.setOnClickListener(getEpgSegmentClickedListener());

		// Focus Segment Control
		focus_textview = (TextView) findViewById(R.id.focus_textview);
		focus_textview.setOnClickListener(getFocusSegmentClickedListener());

		// Actives Segment Control
		actives_textview = (TextView) findViewById(R.id.actives_textview);
		actives_textview.setOnClickListener(getActivesSegmentClickedListener());
		
		// Guide List View
		guide_list_view = (ListView) findViewById(R.id.guide_list_listview);
		guide_list_view.setDivider(null);
		guide_list_view.setDividerHeight(0);
		guide_list_view.setOnItemClickListener(this);
		guide_list_adapter = new HotListAdapter(this, guide_list_view, HotListAdapter.TYPE_LIST_GUIDE);
		guide_list_view.setAdapter(guide_list_adapter);

		// Loading Indicator
		loading_indicator = (LinearLayout) findViewById(R.id.loading_indicator);
		
		// Description Text View
		descr_textview = (TextView) findViewById(R.id.descr_textview);
				
		// Epg Indicator
		Epg_indicator = (LinearLayout) findViewById(R.id.epg_indicator);
		
		now_system_Calendar = Calendar.getInstance();
		setXmlCalendar(0, 0);
		gudie_epg_provider  = (TextView) findViewById(R.id.gudie_epg_provider);
		gudie_epg_time_linearlayout = (LinearLayout) findViewById(R.id.gudie_epg_time_linearlayout);
		gudie_epg_time_date = (TextView) findViewById(R.id.gudie_epg_time_date);
		gudie_epg_time_time = (TextView) findViewById(R.id.gudie_epg_time_time);
		setLocalDateInfo(localtime);

		gudie_epg_channel_listview = (ListView) findViewById(R.id.gudie_epg_channel_listview);
		
		gudie_epg_program_viewflow = (ViewFlow) findViewById(R.id.gudie_epg_program_viewflow);		
	}

	/**
	 * Reload feeds.
	 */
	public void refreshGuides() {
		showGuideLoading();
		
		focus_guide_list = null;
		actives_guide_list = null;
		focus_guide_position = 0;
		actives_guide_position = 0;
		getEpgGuide();
		getFacusGuide();
		getActivesGuide();
	}
	
	/**
	 * Get latest weekly hot.
	 */
	private void getEpgGuide () {
		initEpgUI();
		
		Epg_indicator.postDelayed( new Runnable () {
			public void run () {
				showEpgGuide(true);
			}
		}, 500);
	}
	
	/**
	 * Get latest weekly hot.
	 */
	private void getFacusGuide () {
		new Thread( new Runnable() {
			public void run() {
				final ArrayList<TVHot> array = HotManager.getRecommendation();
				
				if (!isDestroyed()) {
					GuideActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							if (isDestroyed())
								return;
							focus_guide_list = array;
							if (selected_segment == FOCUS_SEGMENT)
								showFocusGuide(true);
						}
					});
				}
			}
		}).start();
	}

	/**
	 * Get latest upcoming hot.
	 */
	private void getActivesGuide () {
		new Thread( new Runnable() {
			public void run() {
				final ArrayList<TVEvent> array = HotManager.getHotEvents();
				
				if (!isDestroyed()) {
					GuideActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							if (isDestroyed())
								return;
							
							actives_guide_list = array;
							if (selected_segment == ACTIVES_SEGMENT)
								showActivesGuide(true);
						}
					});
				}
			}
		}).start();
	}

	
	/**
	 * Get callback for focus segment control.
	 * 
	 * @return
	 */
	private View.OnClickListener getEpgSegmentClickedListener() {
		return new View.OnClickListener() {
			// @Override
			public void onClick(View v) {
				if (selected_segment == EPG_SEGMENT)
					return;
				else if (selected_segment == FOCUS_SEGMENT)
					focus_guide_position = guide_list_view.getFirstVisiblePosition();
				else if (selected_segment == ACTIVES_SEGMENT)
					actives_guide_position = guide_list_view.getFirstVisiblePosition();

				// animating option bar
				FWOptionAnimation anim = new FWOptionAnimation(3, selected_segment, EPG_SEGMENT);
				final ImageView iv = (ImageView) option_layout.findViewById(R.id.option_imageview);
				iv.setImageDrawable(null);
				iv.postDelayed(new Runnable() {
					public void run() {
						iv.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.guide_activity_epg_icon));
					}
				}, anim.getInterval() * 150);
				option_layout.startAnimation(anim);

				// refresh 
				selected_segment = EPG_SEGMENT;
				showEpgGuide(true);
//				if (focus_guide_list != null)
//					showEpgGuide(true);
//				else 
//					showGuideLoading();
			}
		};
	}
	
	
	
	/**
	 * Get callback for focus segment control.
	 * 
	 * @return
	 */
	private View.OnClickListener getFocusSegmentClickedListener() {
		return new View.OnClickListener() {
			// @Override
			public void onClick(View v) {
				if (selected_segment == FOCUS_SEGMENT)
					return;
				else if (selected_segment == ACTIVES_SEGMENT)
					actives_guide_position = guide_list_view.getFirstVisiblePosition();

				// animating option bar
				FWOptionAnimation anim = new FWOptionAnimation(3, selected_segment, FOCUS_SEGMENT);
				final ImageView iv = (ImageView) option_layout.findViewById(R.id.option_imageview);
				iv.setImageDrawable(null);
				iv.postDelayed(new Runnable() {
					public void run() {
						iv.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.guide_activity_focus_icon));
					}
				}, anim.getInterval() * 150);
				option_layout.startAnimation(anim);

				// refresh 
				selected_segment = FOCUS_SEGMENT;
				if (focus_guide_list != null)
					showFocusGuide(true);
				else 
					showGuideLoading();
			}
		};
	}
	

	/**
	 * Get callback for actives segment control.
	 * 
	 * @return
	 */
	private View.OnClickListener getActivesSegmentClickedListener() {
		return new View.OnClickListener() {
			// @Override
			public void onClick(View v) {
				if (selected_segment == ACTIVES_SEGMENT)
					return;
				else if (selected_segment == FOCUS_SEGMENT)
					actives_guide_position = guide_list_view.getFirstVisiblePosition();

				// animating option bar
				FWOptionAnimation anim = new FWOptionAnimation(3, selected_segment, ACTIVES_SEGMENT);
				final ImageView iv = (ImageView) option_layout.findViewById(R.id.option_imageview);
				iv.setImageDrawable(null);
				iv.postDelayed(new Runnable() {
					public void run() {
						iv.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.guide_activity_actives_icon));
					}
				}, anim.getInterval() * 150);
				option_layout.startAnimation(anim);

				// refresh 
				selected_segment = ACTIVES_SEGMENT;
				if (actives_guide_list != null)
					showActivesGuide(true);
				else 
					showGuideLoading();
			}
		};
	}
	
	
	/**
	 * Show guide list view.
	 */
	private void showGuideList() {
		guide_list_view.setVisibility(View.VISIBLE);
		loading_indicator.setVisibility(View.GONE);
		Epg_indicator.setVisibility(View.GONE);
		descr_textview.setVisibility(View.GONE);
	}

	/**
	 * Show guide loading view.
	 */
	private void showGuideLoading() {
		guide_list_view.setVisibility(View.GONE);
		loading_indicator.setVisibility(View.VISIBLE);
		Epg_indicator.setVisibility(View.GONE);
		descr_textview.setVisibility(View.GONE);
	}
	
	/**
	 * Show Description.
	 */
	private void showDescription (String descr) {
		guide_list_view.setVisibility(View.GONE);
		loading_indicator.setVisibility(View.GONE);
		Epg_indicator.setVisibility(View.GONE);
		descr_textview.setVisibility(View.VISIBLE);
		
		descr_textview.setText(descr);
	}
	
	/**
	 * Show epg.
	 * @param reposition whether reposition to last position
	 */
	private void showEpgGuide (boolean reposition) {
		guide_list_view.setVisibility(View.GONE);
		loading_indicator.setVisibility(View.GONE);
		Epg_indicator.setVisibility(View.VISIBLE);
		descr_textview.setVisibility(View.GONE);
	}
	
	
	/**
	 * Show focus.
	 * @param reposition whether reposition to last position
	 */
	private void showFocusGuide (boolean reposition) {
		if (focus_guide_list == null || focus_guide_list.size() == 0)
			showDescription(getString(R.string.guides_no_recommend));
		else {
			showGuideList();
			guide_list_adapter.refreshData(focus_guide_list, null, HotManager.Hot, false);
			if (reposition)
				guide_list_view.setSelection(focus_guide_position);
		}
	}
	
	/**
	 * Show actives.
	 * @param reposition whether reposition to last position
	 */
	private void showActivesGuide (boolean reposition) {
		if (actives_guide_list == null || actives_guide_list.size() == 0)
			showDescription(getString(R.string.guides_no_activity));
		else {
			showGuideList();
			guide_list_adapter.refreshData(null, actives_guide_list, HotManager.Event, true);
			if (reposition)
				guide_list_view.setSelection(actives_guide_position);
		}
	}

	
	
	/**
	 * Callback for wave list view clicked.
	 */
	// @Override
	public void onItemClick(AdapterView<?> listview, View view, int position, long id) {
		
		switch (selected_segment) {
		
		case EPG_SEGMENT:

			break;
		case FOCUS_SEGMENT:
			if (position < focus_guide_list.size())
				openProgramActivity(focus_guide_list.get(position));
			break;
		case ACTIVES_SEGMENT:
			if (position < actives_guide_list.size())
				openProgramActivity(actives_guide_list.get(position));
			break;
		}
	}
	

	/**
	 * Open program activity with TVProgram tp.
	 * @param tp
	 */
	public void openProgramActivity (TVProgram tp) {
		if (tp.getTitle().length() == 0)
			return;
		
		Intent i = new Intent(GuideActivity.this, ProgramActivity.class);
		i.putExtra("program", tp);
		i.putExtra("back_image", BitmapFactory.decodeResource(getResources(), R.drawable.guide_icon));
		startActivity(i);
	}
	

	/**
	 * Save Follow List to Map
	 */
	public void SaveFollowListtoMap( ArrayList<TVFollow> followlist){
		if(follow_map!=null)
			follow_map.clear();
		else
			follow_map = new HashMap<String, TVFollow>();
		
		for(TVFollow tvfollow : followlist)
			follow_map.put(tvfollow.getPgid(), tvfollow);
	}

	/**
	 * Save Reminder List to Map
	 */
	public void SaveReminderListtoMap(ArrayList<TVReminder> reminderlist){
		if(reminder_map!=null)
			reminder_map.clear();
		else
			reminder_map = new HashMap<String, TVReminder>();
		
		for(TVReminder tvreminder : reminderlist)
			reminder_map.put(tvreminder.getPgid(), tvreminder);
	}


	
	/**
	 * init Epg UI
	 */
	private void initEpgUI() {
		
		gudie_epg_provider.setText(VendorManager.getCityName()+"."+VendorManager.getPostname());
		
		//channels
		listChannels = EpgManager.getListChannels();
		channellist_adapter = new ChannelListAdapter(GuideActivity.this, gudie_epg_channel_listview, listChannels);
		gudie_epg_channel_listview.setAdapter(channellist_adapter);
		
		// programs
		ViewFlow.addListener(listener);
		EpgManager.addListener(pgladapterListener);
		EpgManager.addChangeListener(dapterchangeListener);
		viewflow_lv_adapter = new ListViewAdapter(ListViewList);

		setNewViewFlow(localtime);
		gudie_epg_program_viewflow.setAdapter(viewflow_lv_adapter, 1);
		gudie_epg_channel_listview.setLayoutParams(EpgManager.getParams(channelWidth, EpgManager.changedip(6*listChannels.size(), GuideActivity.this)));
		gudie_epg_program_viewflow.setLayoutParams(EpgManager.getParams(programWidth, EpgManager.changedip(6*listChannels.size(), GuideActivity.this)));	
		
		gudie_epg_channel_listview.setLayoutParams(EpgManager.getParams(channelWidth, EpgManager.changedip(84*listChannels.size(), GuideActivity.this)-listChannels.size()));
		gudie_epg_program_viewflow.setLayoutParams(EpgManager.getParams(programWidth, EpgManager.changedip(84*listChannels.size(), GuideActivity.this)-listChannels.size()));	
		gudie_epg_time_time.setText("NOW");
	}
	
	/**
	 * init Restart Epg UI
	 */
	private void RestartEpgUI() {
		if(!ChannelManager.gethaveupdate())			
			return;
		

		if (selected_segment == EPG_SEGMENT)
			showGuideLoading();

		ChannelManager.sethaveupdate(false);
		gudie_epg_provider.setText(VendorManager.getCityName()+"."+VendorManager.getPostname());
		
		if(listChannels!=null){
			listChannels.clear();
		}
							
		if(ListViewList!=null&&ListViewList.size()!=0){
			for(ListView lv : ListViewList)
				((ProgramListAdapter) lv.getAdapter()).clear();

			viewflow_lv_adapter.clear();
			ListViewList.clear();
		}
		//channels
		listChannels = EpgManager.getListChannels();
		channellist_adapter.refreshData(listChannels);
		// programs
		refreshView();
		gudie_epg_channel_listview.setLayoutParams(EpgManager.getParams(channelWidth, EpgManager.changedip(84*listChannels.size(), GuideActivity.this)-listChannels.size()));
		gudie_epg_program_viewflow.setLayoutParams(EpgManager.getParams(programWidth, EpgManager.changedip(84*listChannels.size(), GuideActivity.this)-listChannels.size()));		

		Epg_indicator.postDelayed( new Runnable () {
			public void run () {
				if (selected_segment == EPG_SEGMENT)
					showEpgGuide(true);
			}
		}, 500);
	}
	
	
	
	/**
	 * Show time
	 */
	private void setLocalDateInfo(String localtime) {
		if(localtime == null)
			return;
		
		EpgManager.setUserSelectTime(localtime);

		gudie_epg_time_date.setText( localtime.substring(0, 4)
									+ "."
									+ localtime.substring(4, 6) 
									+ "." 
									+ localtime.substring(6, 8) 
									+ "\t" 
									+ getweek(localtime));
		if(isNow(localtime))
			gudie_epg_time_time.setText("NOW");
		else
			gudie_epg_time_time.setText(format(Integer.parseInt(localtime.substring(8, 10))) 
									+ ":"
									+ format(Integer.parseInt(localtime.substring(10, 12))));
		gudie_epg_time_linearlayout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				nowSystemTime();
			}
		});
		
	}
	
	/**
	 * Set local time
	 * 
	 * @param diffHour
	 * @param diffmin
	 */
	private void setXmlCalendar(int diffHour, int diffmin) {
		now_system_Calendar.add(Calendar.HOUR, diffHour);
		now_system_Calendar.add(Calendar.MINUTE, diffmin);
		localtime = StringGenerator.timeStringFromCalendar(now_system_Calendar);
	}
	
	private boolean isNow(String time){
		if(time.equals(StringGenerator.timeStringFromCalendar(Calendar.getInstance())))
			return true;
		else
			return false;
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
		updateDisplay(time);
	}
	
	/**
	 * Update and Show time
	 */
	public void updateDisplay(long longtime) {
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
						 +format(now_sysetm_Day)
						 +"\t"
						 +format(now_sysetm_Hour) 
						 + ":" 
						 + format(now_sysetm_Minute);
		
		SetTimeDialog(title, longtime);
	}
	
	/**
	 * Show set time Dialog
	 */
	public void SetTimeDialog(String Title, final long longtime) {
		
		
		
		AlertDialog.Builder dialog_builder = new AlertDialog.Builder(GuideActivity.this);
		dialog_builder.setTitle(Title)
					  .setItems(R.array.timeaialog_array, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	switch (item) {
		    	case 0:
		    		DatePickerDialog DatePd = new DatePickerDialog(
		    				GuideActivity.this,
							new DatePickerDialog.OnDateSetListener() {

								public void onDateSet(DatePicker view,
										int year, int monthOfYear,
										int dayOfMonth) {
									now_sysetm_Year = year;
									now_sysetm_Month = monthOfYear;
									now_sysetm_Day = dayOfMonth;
									updateDisplay(longtime);
								}
							}, now_sysetm_Year, now_sysetm_Month, now_sysetm_Day);
					DatePd.show();
					break;
					
		    	case 1:
		    		
		    		TimePickerDialog timePD = new TimePickerDialog(
		    				GuideActivity.this,
							new TimePickerDialog.OnTimeSetListener() {

								public void onTimeSet(TimePicker view,
										int hourOfDay, int minute) {
									now_sysetm_Hour = hourOfDay;
									now_sysetm_Minute = minute;
									updateDisplay(longtime);
								}
							}, now_sysetm_Hour, now_sysetm_Minute, true);
					timePD.show();
					break;
					
		    	case 2:
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
	 *  diffHour(+1 OR -1), Set time and refresh
	 * 
	 * @param diffHour
	 */
	private ListView adjustHour(int diffHour, boolean isAdjust, int diffmin) {
		if (isAdjust) {
			setXmlCalendar(0, diffmin);
		} 
		EpgManager.setUserSelectTime(localtime);
		setLocalDateInfo(localtime);
		String Time = null;
		if(diffmin < 0){
			Time = StringGenerator.timeStringShiftWithMinutes(localtime, -30); 
		}else if(diffmin > 0){
			Time = StringGenerator.timeStringShiftWithMinutes(localtime, 30); 
		}

		ProgramListAdapter Adapter = new ProgramListAdapter(GuideActivity.this, listChannels, new ArrayList<TVProgram>(), Time);
		ListView new_listview = getListView(); 						
		new_listview.setAdapter(Adapter);
	    loadProgramListAdapter(Adapter, Time);

		return new_listview;
	}
	
	private void setNewViewFlow(String time){
		if (!isDestroyed()) {

			final String perMin = StringGenerator.timeStringShiftWithMinutes(time, -30); 
			final String nowMin = time;
			final String nextMin = StringGenerator.timeStringShiftWithMinutes(time, 30); 
			final String nextnextMin = StringGenerator.timeStringShiftWithMinutes(time, 60); 
			ListView perlistview = getListView(); 
			ListView nowlistview = getListView(); 
			ListView nextlistview= getListView(); 
			final ProgramListAdapter per_adapter  = new ProgramListAdapter(GuideActivity.this, listChannels, null, perMin);
			final ProgramListAdapter now_adapter  = new ProgramListAdapter(GuideActivity.this, listChannels, null, nowMin);
			final ProgramListAdapter next_adapter = new ProgramListAdapter(GuideActivity.this, listChannels, null, nextMin);
		    perlistview.setAdapter(per_adapter);
		    nowlistview.setAdapter(now_adapter);
		    nextlistview.setAdapter(next_adapter);
			ListViewList.add(perlistview);
			ListViewList.add(nowlistview);
			ListViewList.add(nextlistview);


			new Thread ( new Runnable () {
				public void run () {
					if (!isDestroyed()) {
					    loadProgramListAdapter(now_adapter, nowMin);
					    loadProgramListAdapter(next_adapter,nextMin);
					    loadProgramListAdapter(per_adapter, perMin);
					    loadProgramListAdapter(null, nextnextMin);
					}
				}
			}).start();
			
		}
		
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
		setXmlCalendar(0, 0);
		setLocalDateInfo(localtime);
		refreshView();
	}
	
	public void refreshView(){
				ListViewList.clear();
				setNewViewFlow(localtime);
				viewflow_lv_adapter.refreshData(ListViewList);
	}
	
	/**
	 * Set listview and return
	 * @retuen listview
	 */
	public ListView getListView(){
		ListView lv= new ListView(GuideActivity.this); 	
		lv.setDividerHeight(EpgManager.changedip(2, GuideActivity.this));
		lv.setDivider(getResources().getDrawable(R.drawable.guide_line));
		lv.setCacheColorHint(00000000);
		lv.setFocusable(false);
		return lv;
	}
	
	
	
	/**
	 * viewFlow Listener
	 * @param tp
	 */
	 private class ViewListener implements ViewChangeListener {
			@Override
			public void getViewPosition(int position) {
				// TODO Auto-generated method stub
				if (!isDestroyed()) {

					if(position == 0){
						ListViewList.add(0, adjustHour(0, true, -30));
						((ProgramListAdapter) ListViewList.get(3).getAdapter()).setChannellist(null);
						ListViewList.remove(3);
						GuideActivity.this.runOnUiThread( new Runnable () {
								public void run () {	
									viewflow_lv_adapter.refreshData(ListViewList);
									gudie_epg_program_viewflow.setSelection(1);
								}
						 });
					}else if(position == viewflow_lv_adapter.getCount()-1){
						ListViewList.add(adjustHour(0, true, 30));
						((ProgramListAdapter) ListViewList.get(0).getAdapter()).setChannellist(null);
						ListViewList.remove(0);
						 GuideActivity.this.runOnUiThread( new Runnable () {
								public void run () {	
									viewflow_lv_adapter.refreshData(ListViewList);
									gudie_epg_program_viewflow.setSelection(1);
								}
						 });
					}
				}
		
			}
		}
		
		/**
		 * Load ProgramListAdapter.
		 * @param adapter
		 * @param Time
		 */
		private void loadProgramListAdapter (ProgramListAdapter adapter, String Time) {
			if (Time.length() == 0)
				return;
			
			if (!EpgManager.getListTVProgramforTime(GuideActivity.this, adapter, Time));

		}
		
		/**
		 * set ProgramList Adapter Listener
		 * @param tp
		 */
		 private class PgListAdapterListener implements ProgramListAdapterListener {

			@Override
			public void getViewProgramList(final String Time) {
				// TODO Auto-generated method stub
				for(final ListView lv : ListViewList){
					String AdapterTime = ((ProgramListAdapter) lv.getAdapter()).getTime().substring(0,10)+"00";
					if(AdapterTime.equals(Time)){
						if (!isDestroyed()) {
							GuideActivity.this.runOnUiThread( new Runnable () {
								public void run () {	
									((ProgramListAdapter) lv.getAdapter()).refreshData(listChannels, EpgManager.getListTVProgramlist().get(Time));
								}
							});
						}
					}
				}
			}
				
		}
		 
	/**
	 * set ProgramList Adapter Listener
	 * @param tp
	 */
	 private class AdapterChangeListener implements ChangeAdapterListener {

		@Override
		public void isChange() {
			// TODO Auto-generated method stub
			for (ListView lv : ListViewList)
				((ProgramListAdapter)lv.getAdapter()).notifyDataSetChanged();
		}
			
	}
			 
		 
	
}
