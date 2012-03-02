package com.wildmind.fanwave.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wildmind.fanwave.friend.FriendManager;
import com.wildmind.fanwave.user.IndexedUserAdapter;
import com.wildmind.fanwave.user.TVUser;

public class SplashSearchActivity extends BaseActivity implements OnItemClickListener {

	private EditText	search_edittext;
	private ListView	user_listview;
	private TextView	descr_textview;
	
	private IndexedUserAdapter user_adapter = null;
	private ArrayList<TVUser> all_friends = null;
	private ArrayList<TVUser> searched_friends = null;
	
	/**
	 * Activity Life Cycle Methods
	 */
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // check if activity is destroyed due to abnormal situation
        if (isDestroyed())
			return;
        
        setContentView(R.layout.splash_search_activity);
        
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
		
		if (user_adapter != null)
			user_adapter.clear();
		user_adapter = null;
		
		search_edittext = null;
		user_listview = null;
		descr_textview = null;
		
		all_friends = null;
		searched_friends = null;
	}
	
	private void initData () {
		all_friends = FriendManager.getFriends();
		searched_friends = new ArrayList<TVUser>();
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
		search_edittext.addTextChangedListener(getSearchChangedListener());
		
		// Search Button
		ImageView search = (ImageView) findViewById(R.id.search_imageview);
		search.setOnClickListener(getSearchClickedListener());
		
		// Splash List View
		user_listview = (ListView) findViewById(R.id.splash_list_listview);
		user_listview.setDivider(null);
		user_listview.setDividerHeight(0);
		user_listview.setOnItemClickListener(this);				
		user_adapter = new IndexedUserAdapter(user_listview, all_friends, this);
		user_listview.setAdapter(user_adapter);
		
		// Description Text View
		descr_textview = (TextView) findViewById(R.id.descr_textview);
		
		if (all_friends == null || all_friends.size() == 0)
			showDescription();
		else
			showUserList();
	}
	
	private void showUserList () {
		user_listview.setVisibility(View.VISIBLE);
		descr_textview.setVisibility(View.GONE);
	}
	
	/**
	 * Show Description.
	 */
	private void showDescription () {
		user_listview.setVisibility(View.GONE);
		descr_textview.setVisibility(View.VISIBLE);
	}
	
	/**
	 * Callback for search term changed.
	 * @return
	 */
	private TextWatcher getSearchChangedListener () {
		return new TextWatcher () {
			@Override
			public void afterTextChanged(Editable editable) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (count == 0)
					user_adapter.refreshData(all_friends);
				else {
					searched_friends.clear();
					for (TVUser user:all_friends) {
						if (user.getNickname().contains(s))
							searched_friends.add(user);
					}
					user_adapter.refreshData(searched_friends);
				}
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
				// hide soft keyboard
				InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				mgr.hideSoftInputFromWindow(search_edittext.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		};
	}
	
	@Override
	public void onItemClick(AdapterView<?> listview, View view, int position, long id) {
		TVUser user = (TVUser) user_adapter.getItem(position);
		Intent i = new Intent(SplashSearchActivity.this, SplashPrivateActivity.class);
		i.putExtra("username", user.getUsername());
		i.putExtra("nickname", user.getNickname());
		i.putExtra("focus_typing", true);
		i.putExtra("back_image", BitmapFactory.decodeResource(getResources(), R.drawable.search_title_icon));
		startActivity(i);
	}
}
