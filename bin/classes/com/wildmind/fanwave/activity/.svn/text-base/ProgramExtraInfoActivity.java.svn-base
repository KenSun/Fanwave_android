package com.wildmind.fanwave.activity;

import java.util.ArrayList;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.media.ProgramIconManager;
import com.wildmind.fanwave.media.ImageManager.SampleBase;
import com.wildmind.fanwave.program.ProgramManager;
import com.wildmind.fanwave.program.ProgramReminderAdapter;
import com.wildmind.fanwave.program.TVProgram;
import com.wildmind.fanwave.program.TVProgramExtraInfo;
import com.wildmind.fanwave.program.TVProgramImage;
import com.wildmind.fanwave.reminder.TVReminder;
import com.wildmind.fanwave.util.StringGenerator;
import com.wildmind.fanwave.vendor.VendorManager;
import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.animation.AlphaSetter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * Intent input data "extra_info" : TVProgramExtraInfo
 * 					 "country"	  : String
 * @author Kencool
 *
 */

public class ProgramExtraInfoActivity extends BaseActivity implements OnItemClickListener {

	private ImageView 		program_icon_imageview;
	private Button			reminder_segment_button, intro_segment_button, link_segment_button;
	private ViewFlipper		extrainfo_viewflipper;
	private ListView		reminder_listview;
	private LinearLayout	loading_indicator;
	
	private String	country = null;
	private TVProgramExtraInfo	extra_info = null;
	private ProgramReminderAdapter reminder_adapter = null;
	
	/**
	 * Activity Life Cycle Methods
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// check if activity is destroyed due to abnormal situation
		if (isDestroyed())
			return;
		
		setContentView(R.layout.program_extrainfo_activity);

		initData();
		initUI();
		getReminders();
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
		
		program_icon_imageview = null;
		reminder_segment_button = null;
		intro_segment_button = null;
		link_segment_button = null;
		
		if (extrainfo_viewflipper != null)
			extrainfo_viewflipper.removeAllViews();
		extrainfo_viewflipper = null;
		reminder_listview = null;
		loading_indicator = null;
		
		if (reminder_adapter != null)
			reminder_adapter.clear();
		reminder_adapter = null;
		
		country = null;
		extra_info = null;
	}
	
	private void initData () {
		Intent i = getIntent();
		country = i.getStringExtra("country");
		extra_info = (TVProgramExtraInfo) i.getParcelableExtra("extra_info");
	}
	
	private void initUI () {
		initTitleBarUI();
		initProgramIconUI();
		initPointUI();
		initSegmentUI();
		
		// View Flipper
		extrainfo_viewflipper = (ViewFlipper) findViewById(R.id.extrainfo_viewflipper);
		initReminderUI();
		initIntroUI();
		initLinkUI();
	}
	
	private void initTitleBarUI () {
		// Back Button
		ImageButton back_button = (ImageButton) findViewById(R.id.back_imagebutton);
		
		// Back Image
		ProgramIconManager.drawProgramIcon(back_button, extra_info.getTitle(), SampleBase.RIGOROUS_SAMPLED, true);
		
		// Back Listener
		back_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		// Title
		TextView title = (TextView) findViewById(R.id.title_textview);
		title.setText(extra_info.getTitle());
	}
	
	private void initProgramIconUI () {
		program_icon_imageview = (ImageView) findViewById(R.id.program_icon_imageview);
		if (extra_info.getIconUrl().length() == 0)
			program_icon_imageview.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.program_default));
		else {
			loadProgramIcon();
			program_icon_imageview.setOnClickListener(getProgramIconClickedListener());
		}
	}
	
	private void initPointUI () {
		// wave in
		TextView wavein_point = (TextView) findViewById(R.id.wavein_point_textview);
		wavein_point.setText(String.valueOf(extra_info.getWaveinCount()));
		
		// follow
		TextView favorite_point = (TextView) findViewById(R.id.favorite_point_textview);
		favorite_point.setText(String.valueOf(extra_info.getFollowerCount()));
		
		// comment
		TextView comment_point = (TextView) findViewById(R.id.comment_point_textview);
		comment_point.setText(String.valueOf(extra_info.getCommentCount()));
	}
	
	private void initSegmentUI () {
		View.OnClickListener listener = getSegmentClickedListener();
		reminder_segment_button = (Button) findViewById(R.id.reminder_segment_button);
		reminder_segment_button.setOnClickListener(listener);
		reminder_segment_button.setSelected(true);
		intro_segment_button = (Button) findViewById(R.id.intro_segment_button);
		intro_segment_button.setOnClickListener(listener);
		link_segment_button = (Button) findViewById(R.id.link_segment_button);
		link_segment_button.setOnClickListener(listener);
	}
	
	private void initReminderUI () {
		FrameLayout reminder_view = (FrameLayout) View.inflate(this, R.layout.program_extrainfo_reminder, null);
		loading_indicator = (LinearLayout) reminder_view.findViewById(R.id.loading_indicator);
		reminder_listview = (ListView) reminder_view.findViewById(R.id.reminder_list_listview);
		reminder_listview.setDivider(null);
		reminder_listview.setDividerHeight(0);
		reminder_listview.setOnItemClickListener(this);
		reminder_adapter = new ProgramReminderAdapter(reminder_listview, null, this);
		reminder_listview.setAdapter(reminder_adapter);
		
		extrainfo_viewflipper.addView(reminder_view, 0);
	}
	
	private void initIntroUI () {
		FrameLayout intro_view = (FrameLayout) View.inflate(this, R.layout.program_extrainfo_intro, null);
		TextView intro = (TextView) intro_view.findViewById(R.id.intro_textview);
		intro.setText(extra_info.getDescription());
		
		extrainfo_viewflipper.addView(intro_view, 1);
	}
	
	private void initLinkUI () {
		RelativeLayout link_view = (RelativeLayout) View.inflate(this, R.layout.program_extrainfo_link, null);
	
		View.OnClickListener listener = getLinkClickedListener();
		
		// google
		ImageButton google = (ImageButton) link_view.findViewById(R.id.link_google_button);
		google.setOnClickListener(listener);
		
		// youtube
		ImageButton youtube = (ImageButton) link_view.findViewById(R.id.link_youtube_button);
		youtube.setOnClickListener(listener);
		
		// website
		Button website = (Button) link_view.findViewById(R.id.link_website_button);
		if (extra_info.getLink().length() > 0) 
			website.setOnClickListener(listener);
		else {
			website.startAnimation(new AlphaSetter(0.5f));
			website.setEnabled(false);
		}
		
		extrainfo_viewflipper.addView(link_view, 2);
	}
	
	/**
	 * Load program icon. Here we don't use image listener since program icon is only loaded once. Just 
	 * try redraw after certain interval if failed.
	 */
	private void loadProgramIcon () {
		if (isDestroyed())
			return;
		
		if (!ImageManager.drawProgramIcon(program_icon_imageview, extra_info.getTitle(),
				extra_info.getIconUrl(), ImageManager.SampleBase.RIGOROUS_SAMPLED))
			program_icon_imageview.postDelayed(new Runnable () {
				public void run () {
					loadProgramIcon();
				}
			}, 1000);
	}
	
	/**
	 * Get reminders.
	 */
	private void getReminders () {
		new Thread (new Runnable () {
			public void run () {
				String utcTime = StringGenerator.UTCFromLocal(StringGenerator.getCurrentTimeString());
				ArrayList<TVProgram> replays = ProgramManager.getProgramReplays(extra_info.getPgid(),
												country, utcTime, VendorManager.getVendorId());
				final ArrayList<TVReminder> reminders = new ArrayList<TVReminder>();
				for (TVProgram program:replays)
					reminders.add(new TVReminder(program, AccountManager.getCurrentUser().getUsername(),
												 null, -1, null, 0));
				if (!isDestroyed()) {
					reminder_listview.post( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							loading_indicator.setVisibility(View.INVISIBLE);
							reminder_adapter.refreshData(reminders);
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Callback for program icon clicked.
	 * @return
	 */
	private View.OnClickListener getProgramIconClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TVProgramImage tpi = new TVProgramImage();
				tpi.setTitle(extra_info.getTitle());
				tpi.setImageUrl(extra_info.getIconUrl());
				
				Intent i = new Intent(ProgramExtraInfoActivity.this, ImagePresentActivity.class);
				i.putExtra("present_type", ImagePresentActivity.PROGRAM_IMAGE_PRESENT);
				i.putExtra("program_title", extra_info.getTitle());
				i.putExtra("program_image", tpi);
				startActivity(i);
			}
		};
	}
	
	/**
	 * Callback for segments clicked.
	 * @return
	 */
	private View.OnClickListener getSegmentClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				reminder_segment_button.setSelected(v.getId() == R.id.reminder_segment_button);
				intro_segment_button.setSelected(v.getId() == R.id.intro_segment_button);
				link_segment_button.setSelected(v.getId() == R.id.link_segment_button);
				switch (v.getId()) {
				case R.id.reminder_segment_button:
					extrainfo_viewflipper.setDisplayedChild(0);
					break;
				case R.id.intro_segment_button:
					extrainfo_viewflipper.setDisplayedChild(1);
					break;
				case R.id.link_segment_button:
					extrainfo_viewflipper.setDisplayedChild(2);
					break;
				default:
					break;
				}
			}
		};
	}
	
	/**
	 * Callback for link buttons clicked.
	 * @return
	 */
	private View.OnClickListener getLinkClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = null;
				
				switch (v.getId()) {
				case R.id.link_google_button:
					i = new Intent(ProgramExtraInfoActivity.this, WebViewActivity.class);
					i.putExtra("url", "http://www.google.com/search?q=" + extra_info.getTitle());
					break;
				case R.id.link_youtube_button:
					i = new Intent(Intent.ACTION_SEARCH);
					i.setPackage("com.google.android.youtube");
					i.putExtra("query", extra_info.getTitle());
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					break;
				case R.id.link_website_button:
					i = new Intent(ProgramExtraInfoActivity.this, WebViewActivity.class);
					i.putExtra("url", extra_info.getLink());
					break;
				default:
					break;
				}
				
				if (i != null)
					startActivity(i);
			}
		};
	}

	@Override
	public void onItemClick(AdapterView<?> listview, View view, int position, long id) {
		// TODO Auto-generated method stub
		
	}
}
