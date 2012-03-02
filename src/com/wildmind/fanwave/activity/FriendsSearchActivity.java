package com.wildmind.fanwave.activity;

import java.util.ArrayList;

import com.wildmind.fanwave.user.FriendedUserAdapter;
import com.wildmind.fanwave.user.TVUser;
import com.wildmind.fanwave.user.UserManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * Intent input data "search_term" : String
 * @author Kencool
 *
 */

public class FriendsSearchActivity extends BaseActivity implements OnItemClickListener {

	private EditText		search_edittext;
	private ListView		user_listview;
	private LinearLayout	loading_indicator;
	private TextView		descr_textview;
	
	private FriendedUserAdapter user_adapter = null;
	private ArrayList<TVUser> 	users = null;
	private String 				search_term = null;
	
	/**
	 * Activity Life Cycle Methods
	 */
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // check if activity is destroyed due to abnormal situation
        if (isDestroyed())
			return;
        
        setContentView(R.layout.friends_search_activity);
        
        initData();
        initUI();
        refreshUsers();
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
		
		if (user_adapter != null)
			user_adapter.clear();
		user_adapter = null;
		
		search_edittext = null;
		user_listview = null;
		loading_indicator = null;
		descr_textview = null;
		
		users = null;
		search_term = null;
	}
	
	private void initData () {
		Intent i = getIntent();
		search_term = i.getStringExtra("search_term");
	}
	
	private void initUI () {
		// Back Button
		ImageButton back_button = (ImageButton) findViewById(R.id.back_imagebutton);
		back_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		// Search
		search_edittext = (EditText) findViewById(R.id.search_edittext);
		search_edittext.setText(search_term);
		search_edittext.setOnEditorActionListener(getSearchActionListener());
		
		// Search Button
		ImageView search = (ImageView) findViewById(R.id.search_imageview);
		search.setOnClickListener(getSearchClickedListener());
				
		// User List View
		user_listview = (ListView) findViewById(R.id.user_list_listview);
		user_listview.setDivider(null);
		user_listview.setDividerHeight(0);
		user_listview.setOnItemClickListener(this);				
		user_adapter = new FriendedUserAdapter( user_listview, null, getString(R.string.friend_search_friend_title),
												getString(R.string.friend_search_nonfriend_title), this);
		user_adapter.filterOutSelf(true);
		user_listview.setAdapter(user_adapter);
		
		// Loading Indicator
		loading_indicator = (LinearLayout) findViewById(R.id.loading_indicator);
		
		// Description Text View
		descr_textview = (TextView) findViewById(R.id.descr_textview);
	}
	
	/**
	 * Refresh users.
	 */
	private void refreshUsers () {
		showLoading();
		users = null;
		getUsers();
	}
	
	/**
	 * Show user list view.
	 */
	private void showUserList () {
		user_listview.setVisibility(View.VISIBLE);
		loading_indicator.setVisibility(View.GONE);
		descr_textview.setVisibility(View.GONE);
	}
	
	/**
	 * Show loading view.
	 */
	private void showLoading () {
		loading_indicator.setVisibility(View.VISIBLE);
		user_listview.setVisibility(View.INVISIBLE);
		descr_textview.setVisibility(View.GONE);
	}
	
	/**
	 * Show Description.
	 */
	private void showDescription () {
		user_listview.setVisibility(View.GONE);
		loading_indicator.setVisibility(View.GONE);
		descr_textview.setVisibility(View.VISIBLE);
	}
	
	/**
	 * Show users.
	 */
	private void showUsers () {
		if (users == null || users.size() == 0)
			showDescription();
		else {
			showUserList();
			
			user_adapter.refreshData(users);
			user_listview.startAnimation(AnimationUtils.loadAnimation(this, R.anim.list_drag_down));
		}
	}
	
	/**
	 * Get users.
	 */
	private void getUsers () {
		new Thread (new Runnable () {
			public void run () {
				final ArrayList<TVUser> array = UserManager.getUsersByNickname(search_term);

				if (!isDestroyed()) {
					user_listview.post( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							users = array;
							showUsers();
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Search Users.
	 */
	private void searchUsers () {
		search_term = search_edittext.getText().toString();
		if (search_term.getBytes().length < 4)
			Toast.makeText(FriendsSearchActivity.this, R.string.sign_up_err_nickname,
							Toast.LENGTH_LONG).show();
		else {
			// hide soft keyboard
			InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			mgr.hideSoftInputFromWindow(search_edittext.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			
			refreshUsers();
		}
	}
	
	/**
	 * Callback for search action.
	 * @return
	 */
	private OnEditorActionListener getSearchActionListener () {
		return new OnEditorActionListener () {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (event != null && event.getAction() == KeyEvent.ACTION_DOWN) {
					searchUsers();
					return true;
				}
				return false;
			}
		};
	}
	
	/**
	 * Callback for search clicked.
	 * @return
	 */
	private View.OnClickListener getSearchClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				searchUsers();
			}
		};
	}
	
	@Override
	public void onItemClick(AdapterView<?> listview, View view, int position, long id) {
		TVUser user = (TVUser) user_adapter.getItem(position);
		Intent i = new Intent(FriendsSearchActivity.this, PersonalActivity.class);
		i.putExtra("username", user.getUsername());
		i.putExtra("nickname", user.getNickname());
		i.putExtra("back_image", BitmapFactory.decodeResource(getResources(), R.drawable.search_title_icon));
		startActivity(i);
	}
}
