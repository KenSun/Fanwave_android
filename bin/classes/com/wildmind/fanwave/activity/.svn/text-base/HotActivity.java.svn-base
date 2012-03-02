package com.wildmind.fanwave.activity;

import java.util.ArrayList;

import com.wildmind.fanwave.animation.FWOptionAnimation;
import com.wildmind.fanwave.hot.HotManager;
import com.wildmind.fanwave.hot.TVHot;
import com.wildmind.fanwave.hot.HotListAdapter;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.program.TVProgram;
import com.wildmind.fanwave.activity.R;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class HotActivity extends BaseActivity implements OnItemClickListener {

	public static final int NOW_SEGMENT 		= 0;
	public static final int WEEKLY_SEGMENT 		= 1;
	public static final int UPCOMING_SEGMENT 	= 2;

	// UI variables
	//
	private FrameLayout option_layout;
	private LinearLayout loading_indicator;
	private ListView hot_list_view;
	private HotListAdapter hot_list_adapter;
	private TextView upcoming_textview, now_textview, weekly_textview, descr_textview;

	// structure variables
	//
	private ArrayList<TVHot> now_hot_list = null;
	private ArrayList<TVHot> weekly_hot_list = null;
	private ArrayList<TVHot> upcoming_hot_list = null;
	private int now_hot_position = 0;
	private int weekly_hot_position = 0;
	private int upcoming_hot_position = 0;
	private int selected_segment = NOW_SEGMENT;

	/**
	 * Activity Life Cycle Methods
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// check if activity is destroyed due to abnormal situation
		if (isDestroyed())
			return;
		
		setContentView(R.layout.hot_activity);

		initData();
		initUI();
		refreshHots();
	}

	protected void onStart() {
		super.onStart();
	}

	protected void onResume() {
		super.onResume();
	}

	protected void onPause() {
		super.onPause();
	}

	protected void onStop() {
		super.onStop();
	}

	protected void onDestroy() {
		super.onDestroy();
		if (now_hot_list != null)
			now_hot_list.clear();
		now_hot_list = null;
		
		if (weekly_hot_list != null)
			weekly_hot_list.clear();
		weekly_hot_list = null;
		
		if (upcoming_hot_list != null)
			upcoming_hot_list.clear();
		upcoming_hot_list = null;
		
		if (hot_list_adapter != null)
			hot_list_adapter.clear();
		hot_list_adapter = null;
		
		option_layout = null;
		loading_indicator = null;
		hot_list_view = null;
		upcoming_textview = null;
		now_textview = null;
		weekly_textview = null;
		descr_textview = null;
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

		// Option Layout
		option_layout = (FrameLayout) findViewById(R.id.option_layout);

		// Reminder Segment Control
		upcoming_textview = (TextView) findViewById(R.id.upcoming_textview);
		upcoming_textview.setOnClickListener(getUpcomingSegmentClickedListener());

		// Nowplaying Segment Control
		now_textview = (TextView) findViewById(R.id.now_textview);
		now_textview.setOnClickListener(getNowSegmentClickedListener());

		// Mixed Segment Control
		weekly_textview = (TextView) findViewById(R.id.weekly_textview);
		weekly_textview.setOnClickListener(getWeeklySegmentClickedListener());

		// Hot List View
		hot_list_view = (ListView) findViewById(R.id.hot_list_listview);
		hot_list_view.setDivider(null);
		hot_list_view.setDividerHeight(0);
		hot_list_view.setOnItemClickListener(this);
		hot_list_adapter = new HotListAdapter(this, hot_list_view, HotListAdapter.TYPE_LIST_HOT);
		hot_list_view.setAdapter(hot_list_adapter);

		// Loading Indicator
		loading_indicator = (LinearLayout) findViewById(R.id.loading_indicator);
		
		// Description Text View
		descr_textview = (TextView) findViewById(R.id.descr_textview);
	}

	/**
	 * Reload feeds.
	 */
	public void refreshHots() {
		showHotLoading();
		
		now_hot_list = null;
		weekly_hot_list = null;
		upcoming_hot_list = null;
		now_hot_position = 0;
		weekly_hot_position = 0;
		upcoming_hot_position = 0;

		getNowHot();
		getWeeklyHot();
		getUpcomingHot();
	}

	/**
	 * Show hot list view.
	 */
	private void showHotList() {
		hot_list_view.setVisibility(View.VISIBLE);
		loading_indicator.setVisibility(View.GONE);
        descr_textview.setVisibility(View.GONE);
	}

	/**
	 * Show hot loading view.
	 */
	private void showHotLoading() {
		hot_list_view.setVisibility(View.GONE);
		loading_indicator.setVisibility(View.VISIBLE);
        descr_textview.setVisibility(View.GONE);
	}
	
	/**
	 * Show Description.
	 */
	private void showDescription (String descr) {
		hot_list_view.setVisibility(View.GONE);
		loading_indicator.setVisibility(View.GONE);
		descr_textview.setVisibility(View.VISIBLE);
		
		descr_textview.setText(descr);
	}
	
	/**
	 * Show now hot.
	 * @param reposition whether reposition to last position
	 */
	private void showNowHot (boolean reposition) {
		if (now_hot_list == null || now_hot_list.size() == 0)
			showDescription(getString(R.string.hot_no_result_now));
		else{
			showHotList();
			hot_list_adapter.refreshData(now_hot_list, null, HotManager.Hot, false);
			if (reposition)
				hot_list_view.setSelection(now_hot_position);
		}
	}
	
	/**
	 * Show weekly hot.
	 * @param reposition whether reposition to last position
	 */
	private void showWeeklyHot (boolean reposition) {
		if (weekly_hot_list == null || weekly_hot_list.size() == 0)
			showDescription(getString(R.string.hot_no_result_weekly));
		else{
			showHotList();
			hot_list_adapter.refreshData(weekly_hot_list, null, HotManager.Hot, false);
			if (reposition)
				hot_list_view.setSelection(weekly_hot_position);
		}
	}
	
	/**
	 * Show upcoming hot.
	 * @param reposition whether reposition to last position
	 */
	private void showUpcomingHot (boolean reposition) {
		if (upcoming_hot_list == null || upcoming_hot_list.size() == 0)
			showDescription(getString(R.string.hot_no_result_upcoming));
		else{
			showHotList();
			hot_list_adapter.refreshData(upcoming_hot_list, null, HotManager.Hot, true);
			if (reposition)
				hot_list_view.setSelection(upcoming_hot_position);
		}
	}

	/**
	 * Get latest now hot.
	 */
	private void getNowHot () {
		new Thread( new Runnable() {
			public void run() {
				final ArrayList<TVHot> array = HotManager.getNowHot();
				
				if (!isDestroyed()) {
					HotActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							if (isDestroyed())
								return;
							now_hot_list = array;
							if (selected_segment == NOW_SEGMENT)
								showNowHot(true);
						}
					});
				}
			}
		}).start();
	}

	/**
	 * Get latest weekly hot.
	 */
	private void getWeeklyHot () {
		new Thread( new Runnable() {
			public void run() {
				final ArrayList<TVHot> array = HotManager.getWeeklyHot();
				
				if (!isDestroyed()) {
					HotActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							if (isDestroyed())
								return;
							weekly_hot_list = array;
							if (selected_segment == WEEKLY_SEGMENT)
								showWeeklyHot(true);
						}
					});
				}
			}
		}).start();
	}

	/**
	 * Get latest upcoming hot.
	 */
	private void getUpcomingHot () {
		new Thread( new Runnable() {
			public void run() {
				final ArrayList<TVHot> array = HotManager.getUpcomingHot();
				
				if (!isDestroyed()) {
					HotActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							if (isDestroyed())
								return;
							upcoming_hot_list = array;
							if (selected_segment == UPCOMING_SEGMENT)
								showUpcomingHot(true);
						}
					});
				}
			}
		}).start();
	}

	/**
	 * Get callback for now segment control.
	 * 
	 * @return
	 */
	private View.OnClickListener getNowSegmentClickedListener() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (selected_segment == NOW_SEGMENT)
					return;
				else if (selected_segment == WEEKLY_SEGMENT)
					weekly_hot_position = hot_list_view.getFirstVisiblePosition();
				else if (selected_segment == UPCOMING_SEGMENT)
					upcoming_hot_position = hot_list_view.getFirstVisiblePosition();
				
				// animating option bar
				FWOptionAnimation anim = new FWOptionAnimation(3, selected_segment, NOW_SEGMENT);
				final ImageView iv = (ImageView) option_layout.findViewById(R.id.option_imageview);
				iv.setImageDrawable(null);
				iv.postDelayed(new Runnable() {
					public void run() {
						iv.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.hot_activity_now_icon));
					}
				}, anim.getInterval() * 150);
				option_layout.startAnimation(anim);

				// refresh hot
				selected_segment = NOW_SEGMENT;
				if (now_hot_list != null)
					showNowHot(true);
				else
					showHotLoading();

			}
		};
	}

	/**
	 * Get callback for weekly segment control.
	 * 
	 * @return
	 */
	private View.OnClickListener getWeeklySegmentClickedListener() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (selected_segment == WEEKLY_SEGMENT)
					return;
				else if (selected_segment == NOW_SEGMENT)
					now_hot_position = hot_list_view.getFirstVisiblePosition();
				else if (selected_segment == UPCOMING_SEGMENT)
					weekly_hot_position = hot_list_view.getFirstVisiblePosition();
				
				// animating option bar
				FWOptionAnimation anim = new FWOptionAnimation(3, selected_segment, WEEKLY_SEGMENT);
				final ImageView iv = (ImageView) option_layout.findViewById(R.id.option_imageview);
				iv.setImageDrawable(null);
				iv.postDelayed(new Runnable() {
					public void run() {
						iv.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.hot_activity_upcoming_icon));
					}
				}, anim.getInterval() * 150);
				option_layout.startAnimation(anim);

				// refresh hot
				selected_segment = WEEKLY_SEGMENT;
				if (weekly_hot_list != null)
					showWeeklyHot(true);
				else
					showHotLoading();
			}
		};
	}

	/**
	 * Get callback for upcoming segment control.
	 * 
	 * @return
	 */
	private View.OnClickListener getUpcomingSegmentClickedListener() {
		return new View.OnClickListener() {
			// @Override
			public void onClick(View v) {
				if (selected_segment == UPCOMING_SEGMENT)
					return;
				else if (selected_segment == NOW_SEGMENT)
					now_hot_position = hot_list_view.getFirstVisiblePosition();
				else if (selected_segment == WEEKLY_SEGMENT)
					weekly_hot_position = hot_list_view.getFirstVisiblePosition();

				// animating option bar
				FWOptionAnimation anim = new FWOptionAnimation(3, selected_segment, UPCOMING_SEGMENT);
				final ImageView iv = (ImageView) option_layout.findViewById(R.id.option_imageview);
				iv.setImageDrawable(null);
				iv.postDelayed(new Runnable() {
					public void run() {
						iv.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.hot_activity_weekly_icon));
					}
				}, anim.getInterval() * 150);
				option_layout.startAnimation(anim);

				// refresh hot
				selected_segment = UPCOMING_SEGMENT;
				if (upcoming_hot_list != null)
					showUpcomingHot(true);
				else 
					showHotLoading();
			}
		};
	}

	/**
	 * Callback for wave list view clicked.
	 */
	// @Override
	public void onItemClick(AdapterView<?> listview, View view, int position, long id) {
		
		switch (selected_segment) {
		
		case NOW_SEGMENT:
			if (position < now_hot_list.size())
				openProgramActivity(now_hot_list.get(position));
			break;
		case WEEKLY_SEGMENT:
			if (position < weekly_hot_list.size())
				openProgramActivity(weekly_hot_list.get(position));
			break;
		case UPCOMING_SEGMENT:
			if (position < upcoming_hot_list.size())
				openProgramActivity(upcoming_hot_list.get(position));
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
		
		Intent i = new Intent(HotActivity.this, ProgramActivity.class);
		i.putExtra("program", tp);
		i.putExtra("back_image", BitmapFactory.decodeResource(getResources(), R.drawable.hot_icon));
		startActivity(i);
	}
	
	/**
	 * Menu Control Methods
	 */
	
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.personal_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		refreshHots();
		return true;
	}
}
