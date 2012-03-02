package com.wildmind.fanwave.activity;

import java.util.ArrayList;
import java.util.Collection;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.friend.FriendManager;
import com.wildmind.fanwave.media.ProgramIconManager;
import com.wildmind.fanwave.media.ImageManager.SampleBase;
import com.wildmind.fanwave.program.ProgramManager;
import com.wildmind.fanwave.splash.SplashGroupAdapter;
import com.wildmind.fanwave.splash.SplashManager;
import com.wildmind.fanwave.splash.SplashGroupAdapter.SplashGroupListener;
import com.wildmind.fanwave.user.TVUser;
import com.wildmind.fanwave.widget.SoftKeyboardFrameLayout;
import com.wildmind.fanwave.widget.SoftKeyboardFrameLayout.SoftKeyboardListener;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Intent input data "program_title" : String
 * 					 "to_fans" : boolean
 * 
 * 		if to_fans = true
 * 					 "pgid" : String
 * @author Kencool
 *
 */

public class SplashProgramActivity extends BaseActivity implements SoftKeyboardListener,
		OnItemClickListener, SplashGroupListener {

	private final int SPLASH_TEXT_MAX_BYTES = 100;
	
	private SoftKeyboardFrameLayout keyboard_listener_layout;
	private TextView		title_textview;
	private ImageButton		select_all_imagebutton;
	private GridView 		splash_gridview;
	private LinearLayout 	poster_layout, poster_entry_layout, loading_indicator;
	private EditText		splash_edittext;
	private Button			splash_button;
	
	private SplashGroupAdapter splash_adapter = null;
	private String 	program_title = null;
	private String	pgid = null;
	private boolean keyboard_showing = false;
	private boolean select_all = false;
	private boolean to_fans = false;
	
	/**
	 * Activity Life Cycle Methods
	 */
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // check if activity is destroyed due to abnormal situation
        if (isDestroyed())
			return;
        
        setContentView(R.layout.splash_program_activity);
        
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
		
		if (keyboard_listener_layout != null)
			keyboard_listener_layout.clear();
		keyboard_listener_layout = null;
		
		select_all_imagebutton = null;
		title_textview = null;
		splash_gridview = null;
		poster_layout = null;
		poster_entry_layout = null;
		loading_indicator = null;
		splash_edittext = null;
		splash_button = null;
		
		if (splash_adapter != null)
			splash_adapter.clear();
		splash_adapter = null;
		
		program_title = null;
		pgid = null;
	}
	
	private void initData () {
		Intent i = getIntent();
		program_title = i.getStringExtra("program_title");
		to_fans = i.getBooleanExtra("to_fans", false);
		if (to_fans)
			pgid = i.getStringExtra("pgid");
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
		ProgramIconManager.drawProgramIcon(back_button, program_title, SampleBase.RIGOROUS_SAMPLED, true);
		
		// Title Text View
		title_textview = (TextView) findViewById(R.id.title_textview);
		updateTitle(0);
		
		// Select All Button
		select_all_imagebutton = (ImageButton) findViewById(R.id.next_imagebutton);
		select_all_imagebutton.setOnClickListener(getSelectAllClickedListener());
		
		// Splash Grid View
		splash_gridview = (GridView) findViewById(R.id.splash_list_gridview);
		splash_gridview.setOnItemClickListener(this);
		splash_adapter = new SplashGroupAdapter(splash_gridview, null, this);
		splash_adapter.setSplashGroupListener(this);
		splash_gridview.setAdapter(splash_adapter);
		
		// Loading Indicator
		loading_indicator = (LinearLayout) findViewById(R.id.loading_indicator);
		
		// Splash Poster Layout
		poster_layout = (LinearLayout) findViewById(R.id.message_splash_poster);
		poster_layout.setVisibility(View.GONE);
		splash_edittext = (EditText) poster_layout.findViewById(R.id.post_edittext);
		splash_edittext.addTextChangedListener(new SplashTextWatcher());
		splash_button = (Button) poster_layout.findViewById(R.id.post_button);
		splash_button.setOnClickListener(getSplashClickedListener());
		
		// Poster Entry Layout
		poster_entry_layout = (LinearLayout) findViewById(R.id.splash_poster_entry);
		
		// Keyboard Listener Layout
		keyboard_listener_layout = (SoftKeyboardFrameLayout) findViewById(R.id.keyboard_listener_layout);
		keyboard_listener_layout.setSoftKeyboardListener(this);
	}

	@Override
	public void onSoftKeyboardShown(boolean isShowing) {
		if (isShowing && !keyboard_showing) {
			// keyboard is going to show up
			keyboard_showing = true;
			onKeyboardShown();
			
		} else if (isShowing && keyboard_showing) {
			// keyboard is already shown
			
		} else if (!isShowing && keyboard_showing) {
			// keyboard is going to hide
			keyboard_showing = false;
			onKeyboardHidden();
			
		} else if (!isShowing && keyboard_showing) {
			// keyboard is already hidden
		}
	}
	
	private void onKeyboardShown () {
		poster_entry_layout.setVisibility(View.GONE);
		poster_layout.setVisibility(View.VISIBLE);
		splash_edittext.requestFocus();
		
		if (splash_gridview != null) {
			splash_gridview.post( new Runnable () {
				public void run () {
					if (splash_gridview == null)
						return;
				}
			});
		}
	}
	
	private void onKeyboardHidden () {
		poster_entry_layout.setVisibility(View.VISIBLE);
		poster_layout.setVisibility(View.GONE);
		splash_edittext.clearFocus();
	}
	
	private void refreshUsers () {
		if (to_fans) {
			showUserLoading();
			getFans();
		} else 
			showUsers(FriendManager.getFriends());
	}
	
	/**
	 * Show user loading.
	 */
	private void showUserLoading () {
		splash_gridview.setVisibility(View.INVISIBLE);
		loading_indicator.setVisibility(View.VISIBLE);
	}
	
	/**
	 * Show users.
	 * @param users
	 */
	private void showUsers (ArrayList<TVUser> users) {
		splash_gridview.setVisibility(View.VISIBLE);
		loading_indicator.setVisibility(View.INVISIBLE);
		
		splash_adapter.refreshData(users);
		if (to_fans)
			splash_adapter.selectAll();
	}
	
	/**
	 * Get fans.
	 */
	private void getFans () {
		new Thread (new Runnable () {
			public void run () {
				final ArrayList<TVUser> fans = ProgramManager.getProgramFollowers(pgid);
				
				// remove self from fan list for avoiding splash to self
				int userIndex = -1;
				for (int i = 0; i < fans.size(); i++) {
					if (fans.get(i).getUsername().equals(AccountManager.getCurrentUser().getUsername())) {
						userIndex = i;
						break;
					}
				}
				if (userIndex != -1)
					fans.remove(userIndex);
				
				if (isDestroyed())
					return;
				
				splash_gridview.post( new Runnable () {
					public void run () {
						if (isDestroyed())
							return;
						
						showUsers(fans);
					}
				});
			}
		}).start();
	}
	
	/**
	 * Update title text view.
	 * @param count
	 */
	private void updateTitle (int count) {
		title_textview.setText(getString(R.string.action_splash) + " " + count + " " + 
					(to_fans ? getString(R.string.splash_group_follower_title) 
							 : getString(R.string.splash_group_friend_title)));
	}
	
	/**
	 * Send splash.
	 * @param message
	 */
	private void sendSplash (final String message) {
		// show loading progress dialog
		final ProgressDialog pd = ProgressDialog.show(this, "", getString(R.string.action_sending));
		new Thread (new Runnable() {
			public void run () {
				Collection<TVUser> users = splash_adapter.getSelectedUsers().values();
				ArrayList<String> usernames = new ArrayList<String>();
				for (TVUser user:users)
					usernames.add(user.getUsername());
				final boolean success = to_fans ? SplashManager.splashFollowers(usernames, message)
												: SplashManager.splashUsers(usernames, message);

				if (!isDestroyed()) {
					splash_gridview.post( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							pd.dismiss();
							if (success) 
								finish();
							else
								Toast.makeText(getApplicationContext(), R.string.splash_user_failed, Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Callback for select all clicked.
	 * @return
	 */
	private View.OnClickListener getSelectAllClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (splash_adapter == null)
					return;
				
				if (select_all)
					splash_adapter.unselectAll();
				else
					splash_adapter.selectAll();
			}
		};
	}
	
	/**
	 * Callback for splash clicked.
	 * @return
	 */
	private View.OnClickListener getSplashClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String content = splash_edittext.getText().toString();
				if (content.length() == 0)
					Toast.makeText(getApplicationContext(), R.string.message_poster_no_content, Toast.LENGTH_SHORT).show();
				else {
					if (splash_adapter.getSelectedUsers().size() > 0)
						sendSplash(content);
					else
						Toast.makeText(getApplicationContext(), R.string.splash_user_required, Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

	@Override
	public void onItemClick(AdapterView<?> gridview, View view, int position, long id) {
		if (splash_adapter != null)
			splash_adapter.clickItem(position, view);
	}
	
	/**
	 * Splash Group Listener Methods
	 */

	@Override
	public void onSelectedCountChanged(int selectedCount) {
		updateTitle(selectedCount);
	}
	
	@Override
	public void onAllSelected() {
		select_all = true;
		select_all_imagebutton.setSelected(select_all);
	}

	@Override
	public void onAllSelectedCancel() {
		select_all = false;
		select_all_imagebutton.setSelected(select_all);
	}
	
	/**
	 * Splash Text Watcher class
	 * @author Kencool
	 *
	 */
	private class SplashTextWatcher implements TextWatcher {
		private String temp_text;
		
		@Override
		public void afterTextChanged(Editable s) {
			String content = splash_edittext.getText().toString();
			int bytes = content.getBytes().length;
			if (bytes > SPLASH_TEXT_MAX_BYTES) {
				splash_edittext.setText(temp_text);
				splash_edittext.setSelection(temp_text.length());
			} else
				temp_text = content;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			temp_text = splash_edittext.getText().toString();
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
		}
	}
}
