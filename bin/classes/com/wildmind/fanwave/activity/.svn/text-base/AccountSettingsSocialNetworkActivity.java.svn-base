package com.wildmind.fanwave.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.facebook.android.DialogError;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.facebook.FacebookManager;

public class AccountSettingsSocialNetworkActivity extends BaseActivity {
	
	private static final String[] social_titles = {"Facebook"};
	private static final int SOCIAL_FACEBOOK = 0;
	
	private final int LOGIN_FACEBOOK_REQUEST = 0;
	
	private ListView social_listview;
	
	private SocialAdapter social_adapter = new SocialAdapter();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// check if activity is destroyed due to abnormal situation
		if (isDestroyed())
			return;
		
		setContentView(R.layout.account_settings_socialnetwork_activity);
		
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
		super.onDestroy();
		
		social_listview = null;
		
		social_adapter = null;
	}
	
	private void initUI () {
		// Home Button
		ImageButton back_imagebutton = (ImageButton) findViewById(R.id.back_imagebutton);
		back_imagebutton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		social_listview = (ListView)findViewById(R.id.account_socialnetwork_connection_listview);
		social_listview.setAdapter(social_adapter);
	}
	
	private class SocialAdapter extends BaseAdapter {
		
		@Override
		public long getItemId(int position) {
			return position;
		}			
		@Override
		public Object getItem(int position) {
			return social_titles[position];
		}	
		@Override
		public int getCount() {
			return social_titles.length;
		}
		@Override
		public View getView(int position, View view, ViewGroup parent) {
			ViewHolder viewHolder;
			if (view == null) {
				LayoutInflater mInflater = LayoutInflater.from(AccountSettingsSocialNetworkActivity.this);
				view = mInflater.inflate(R.layout.account_settings_socialnetwork_listitem, null);
				viewHolder = new ViewHolder();
				viewHolder.title = (TextView) view.findViewById(R.id.socailnetwork_title_textview);
				viewHolder.subtitle = (TextView) view.findViewById(R.id.socialnetwork_value_textview);
				viewHolder.toggle = (ToggleButton) view.findViewById(R.id.socialnetwork_toggle_button);
				view.setTag(viewHolder);
			} else
				viewHolder = (ViewHolder) view.getTag();
			
			fillViewHolder(position, viewHolder);
			
			return view;
		}	
		
		private class ViewHolder {
			TextView 		title;
			TextView 		subtitle;
			ToggleButton 	toggle;
		}
		
		private void fillViewHolder (int position, ViewHolder viewHolder) {
			// title
			viewHolder.title.setText(social_titles[position]);
			
			// subtitle, toggle
			switch (position) {
			case SOCIAL_FACEBOOK:
				if (FacebookManager.isFacebookAccount()) {
					viewHolder.subtitle.setText(FacebookManager.getName());
					viewHolder.toggle.setVisibility(View.INVISIBLE);
				} else {
					viewHolder.subtitle.setText(FacebookManager.isFacebookActive() ? FacebookManager.getName() : "");
					viewHolder.toggle.setVisibility(View.VISIBLE);
					viewHolder.toggle.setChecked(FacebookManager.isFacebookActive());
					viewHolder.toggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							if (isChecked) {
								FacebookDialogListener listener = new FacebookDialogListener();
								FacebookManager.login(AccountSettingsSocialNetworkActivity.this, 
														 LOGIN_FACEBOOK_REQUEST, listener);
							} else {
								FacebookManager.logout();
								AccountManager.getCurrentUser().setFbid("");
							}
						}
					});
				}
					
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * Listener for listening Facebook dialog.
	 * @author Kencool
	 *
	 */
	private class FacebookDialogListener implements DialogListener {
		@Override
		public void onCancel() {
			social_adapter.notifyDataSetChanged();
		}
		@Override
		public void onComplete(Bundle bundle) {

			FacebookManager.loadFacebookService();
			FacebookManager.setLinkProfile();
			
			social_adapter.notifyDataSetChanged();
		}
		@Override
		public void onError(DialogError error) {
			social_adapter.notifyDataSetChanged();
		}
		@Override
		public void onFacebookError(FacebookError facebookError) {
			social_adapter.notifyDataSetChanged();
		}
	}
	
	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case LOGIN_FACEBOOK_REQUEST:
			FacebookManager.getFacebook().authorizeCallback(requestCode, resultCode, data);
			break;
		}
	}
}
