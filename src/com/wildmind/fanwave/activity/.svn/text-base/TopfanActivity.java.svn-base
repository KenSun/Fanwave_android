package com.wildmind.fanwave.activity;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.media.ProgramIconManager;
import com.wildmind.fanwave.media.ImageManager.SampleBase;
import com.wildmind.fanwave.program.ProgramManager;
import com.wildmind.fanwave.program.TopFanAdapter;
import com.wildmind.fanwave.activity.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;


/**
 * Intent input data "program_title" : String
 * 					 "pgid" : String
 * @author Eli
 *
 */

public class TopfanActivity extends BaseActivity {
	
	private LinearLayout loading_indicator;
	private String program_title = null;
	private String pgid;
	private ListView fan_list_listview;
	private TopFanAdapter topfan_adapter = null;
	private String topfan_list = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// check if activity is destroyed due to abnormal situation
		if (isDestroyed())
			return;
		
		setContentView(R.layout.topfan_activity);
		
		initData();
		initUI();
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

		
		if (topfan_adapter != null)
			topfan_adapter.clear();
		topfan_adapter = null;

		program_title = null;
		topfan_list = null;
		
		fan_list_listview = null;
		loading_indicator = null;
	}
	private void initData(){
		Intent i = getIntent();
		program_title = i.getStringExtra("program_title");
		pgid = i.getStringExtra("pgid");
	}
	
	private void initUI(){
		ImageButton back_button = (ImageButton)findViewById(R.id.back_imagebutton);
		back_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		ImageButton next_imagebutton = (ImageButton)findViewById(R.id.next_imagebutton);
		next_imagebutton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(TopfanActivity.this, HowBeTopFanActivity.class);
				startActivity(mIntent);
			}
		});
		
		ProgramIconManager.drawProgramIcon(back_button, program_title, SampleBase.RIGOROUS_SAMPLED, true);
		
		// Loading Indicator
		loading_indicator = (LinearLayout) findViewById(R.id.loading_indicator);

		fan_list_listview = (ListView)findViewById(R.id.fan_list_listview);
		fan_list_listview.setDivider(null);
		fan_list_listview.setDividerHeight(0);
		topfan_adapter = new TopFanAdapter(TopfanActivity.this, fan_list_listview, pgid);
		fan_list_listview.setAdapter(topfan_adapter);
		ProgramFansList(AccountManager.getCurrentUser().getUsername(), pgid, 5);

	}
	
	private void ProgramFansList(final String username,final String pgid,final int number){
		showLoading();
		new Thread ( new Runnable () {
			public void run () {
				if (isDestroyed())
					return;
				
				topfan_list = ProgramManager.getProgramFans(username, pgid, number);
				
				if (isDestroyed())
					return;
				
				TopfanActivity.this.runOnUiThread( new Runnable () {
					public void run () {
						if (isDestroyed())
							return;
						
						
						
						if(topfan_list!=null&&topfan_adapter!=null)
							topfan_adapter.refreshData(topfan_list);
						
						showList();
					}
				});
			}
		}).start();
	}
	/**
	 * Show guide list view.
	 */
	private void showList() {
		if(fan_list_listview!=null&&loading_indicator!=null){
			fan_list_listview.setVisibility(View.VISIBLE);
			loading_indicator.setVisibility(View.GONE);
		}
	}

	/**
	 * Show guide loading view.
	 */
	private void showLoading() {
		if(fan_list_listview!=null&&loading_indicator!=null){
			fan_list_listview.setVisibility(View.GONE);
			loading_indicator.setVisibility(View.VISIBLE);
		}
	}
	
}
