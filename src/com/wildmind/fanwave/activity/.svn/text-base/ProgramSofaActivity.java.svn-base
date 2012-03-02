package com.wildmind.fanwave.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.wildmind.fanwave.friend.FriendManager;
import com.wildmind.fanwave.media.ImageManager.SampleBase;
import com.wildmind.fanwave.media.ProgramIconManager;
import com.wildmind.fanwave.user.GeneralUserAdapter;
import com.wildmind.fanwave.user.TVUser;
import com.wildmind.fanwave.activity.R;

/**
 * Intent input data "program_title" : String
 * 					 "sofa_users"	 : ArrayList<String>
 * @author Kencool
 *
 */

public class ProgramSofaActivity extends BaseActivity implements OnItemClickListener {

	private ListView sofa_listview;
	private TextView descr_textview;
	
	private String program_title = null;
	private ArrayList<TVUser> sofa_users = null;
	private GeneralUserAdapter user_adapter = null;
	
	/**
	 * Activity Life Cycle Methods
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// check if activity is destroyed due to abnormal situation
		if (isDestroyed())
			return;
		
		setContentView(R.layout.program_sofa_activity);

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
		
		sofa_listview = null;
		descr_textview = null;
		
		if (user_adapter != null)
			user_adapter.clear();
		user_adapter = null;
		
		program_title = null;
		sofa_users = null;
	}
	
	private void initData () {
		Intent i = getIntent();
		program_title = i.getStringExtra("program_title");
		
		ArrayList<String> users = (ArrayList<String>) i.getStringArrayListExtra("sofa_users");
		sofa_users = new ArrayList<TVUser>();
		for (String username:users) {
			TVUser user = FriendManager.getFriend(username);
			if (user != null)
				sofa_users.add(user);
		}
	}
	
	private void initUI () {
		initTitleBarUI();
		initSofaListUI();
		
		// Description Text View
		descr_textview = (TextView) findViewById(R.id.descr_textview);
		
		if (sofa_users == null || sofa_users.size() == 0)
			showDescription();
		else
			showSofaList();
	}
	
	private void initTitleBarUI () {
		// Back Button
		ImageButton back_button = (ImageButton) findViewById(R.id.back_imagebutton);
		
		// Back Image
		ProgramIconManager.drawProgramIcon(back_button, program_title, SampleBase.RIGOROUS_SAMPLED, true);
		
		// Back Listener
		back_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void initSofaListUI () {
		// Sofa List View
		sofa_listview = (ListView) findViewById(R.id.sofa_list_listview);
		sofa_listview.setDivider(null);
		sofa_listview.setDividerHeight(0);
		sofa_listview.setOnItemClickListener(this);
		user_adapter = new GeneralUserAdapter(sofa_listview, sofa_users, this);
		sofa_listview.setAdapter(user_adapter);
	}
	
	/**
	 * Show sofa list view.
	 */
	private void showSofaList () {
		sofa_listview.setVisibility(View.VISIBLE);
		descr_textview.setVisibility(View.GONE);
	}
	
	/**
	 * Show Description.
	 */
	private void showDescription () {
		sofa_listview.setVisibility(View.GONE);
		descr_textview.setVisibility(View.VISIBLE);
	}

	@Override
	public void onItemClick(AdapterView<?> listview, View view, int position, long id) {
		TVUser user = (TVUser) user_adapter.getItem(position);
		if (user != null) {
			Intent i = new Intent(ProgramSofaActivity.this, PersonalActivity.class);
			i.putExtra("username", user.getUsername());
			i.putExtra("nickname", user.getNickname());
			i.putExtra("back_image", BitmapFactory.decodeResource(getResources(), R.drawable.sofalist_icon));
			startActivity(i);
		}
	}
}
