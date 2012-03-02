package com.wildmind.fanwave.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.user.TVUserPrivacy;
import com.wildmind.fanwave.activity.R;

public class AccountSettingsPrivacyActivity extends BaseActivity implements View.OnClickListener{
	private ImageView back_imagebutton;
	private ListView privacy_settings_listview;
	private ArrayList<PrivacySettings> privacy_setting_list = new ArrayList<PrivacySettings>();
	private TVUserPrivacy tvuPrivacy = null;
	private PrivacyAdapter privacyAdapter = null;
	public int check = 0;

	//Type
	public static final int ALL 		=  0;
	public static final int FRIEND 	 	=  1;
	public static final int NONE 		=  2;
	
	public static final int SHOW_YEAR 	=  0;
	public static final int NO_YEAR 	=  1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// check if activity is destroyed due to abnormal situation
		if (isDestroyed())
			return;
		
		setContentView(R.layout.account_settings_privacy_activity);
		
		initData();
		initUI();
		EventListenerHandle();
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
		back_imagebutton = null;
		privacy_settings_listview = null;
		if(privacy_setting_list!=null)
			privacy_setting_list.clear();
		privacy_setting_list = null;
		privacyAdapter = null;
		tvuPrivacy = null;
		
	}
	
	private void initData(){
		tvuPrivacy = new TVUserPrivacy(AccountManager.getCurrentUser().getPrivacy());
		SetKeys(tvuPrivacy);
	}
	
	private void initUI(){
		//Home Button
		back_imagebutton = (ImageView)findViewById(R.id.back_imagebutton);
		
		privacy_settings_listview = (ListView)findViewById(R.id.account_privacy_settings_listview);
		privacyAdapter = new PrivacyAdapter();
		privacy_settings_listview.setAdapter(privacyAdapter);
			
			
		
	}
	private void EventListenerHandle(){
		back_imagebutton.setOnClickListener(this);
		
		privacy_settings_listview.setOnItemClickListener( getOnItemClickListener ());
	}
	
	/**
	 * Callback for Item clicked.
	 * @return
	 */
	private OnItemClickListener getOnItemClickListener () {
		return new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, final View arg1, final int position,
					 long arg3) {
				
				final PrivacySettings psettings = privacy_setting_list.get(position);
				
				String[] items = null;
				
				if(psettings.getKey().equals("seebirth"))
					items = getResources().getStringArray(R.array.privacy_values_birthday_array);
				else
					items = getResources().getStringArray(R.array.privacy_values_array);
				
				check = psettings.getPrivacy();
		        AlertDialog privacy_setting_dialog = new AlertDialog.Builder(AccountSettingsPrivacyActivity.this)
		        			.setTitle(privacy_setting_list.get(position).getTitle())
		        			.setSingleChoiceItems(items, psettings.getPrivacy(), new DialogInterface.OnClickListener (){
			                    public void onClick(DialogInterface dialog, int whichButton) {
			                        /* User clicked on a radio button do some stuff */
			                    	check = whichButton;  
			                    }
			                })
			                .setNegativeButton(getResources().getString(R.string.action_cancel), new DialogInterface.OnClickListener() {
			                    public void onClick(DialogInterface dialog, int whichButton) {
			                        /* User clicked No so do some stuff */
			                    }
			                }).create();
		    	privacy_setting_dialog.setButton(getResources().getString(R.string.action_confirm), getdialogOnClickListener(arg0, arg1, psettings, position));
				privacy_setting_dialog.show();				

			}
		};
	}

	private OnClickListener getdialogOnClickListener (AdapterView<?> arg0, final View arg1, final PrivacySettings privacysetting, final int position) {
		return new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String[] items = null;
				String Privacy = "";
				int usercheck = check;
				if(privacysetting.getKey().equals("seebirth")){
					items = getResources().getStringArray(R.array.privacy_values_birthday_array);
					if(usercheck == 0){
						Privacy = TVUserPrivacy.PRIVACY_SHOW_YEAR;
					}else if(usercheck == 1){
						Privacy = TVUserPrivacy.PRIVACY_NO_YEAR;
					}else if(usercheck == 2){
						Privacy = TVUserPrivacy.PRIVACY_NONE;
					}
				}else{
					items = getResources().getStringArray(R.array.privacy_values_array);
					if(usercheck == 0){
						Privacy = TVUserPrivacy.PRIAVCY_ALL;
					}else if(usercheck == 1){
						Privacy = TVUserPrivacy.PRIVACY_FRIEND;
					}else if(usercheck == 2){
						Privacy = TVUserPrivacy.PRIVACY_NONE;
					}
				}
				
		    	((TextView)arg1.findViewById(R.id.privacy_value_textview)).setText(items[usercheck]);

				setPrivacy(privacysetting.getKey(), Privacy);
				Update(privacysetting, arg1, items[privacysetting.getPrivacy()], position, Privacy);

			}
		};
	}
	
	
	private void SetKeys(TVUserPrivacy tvuPrivacy){
		String[] items =  getResources().getStringArray(R.array.privacy_array);

		privacy_setting_list.add(new PrivacySettings(items[0]		, "pokeme"		, tvuPrivacy.getSplash()));
		privacy_setting_list.add(new PrivacySettings(items[1]		, "seereminder" , tvuPrivacy.getReminder()));
		privacy_setting_list.add(new PrivacySettings(items[2]		, "seeemail"	, tvuPrivacy.getEmail()));
		privacy_setting_list.add(new PrivacySettings(items[3]		, "seebirth"	, tvuPrivacy.getBirth()));
		privacy_setting_list.add(new PrivacySettings(items[4]		, "seefacebook" , tvuPrivacy.getFacebook()));
		privacy_setting_list.add(new PrivacySettings(items[5]		, "seesex"		, tvuPrivacy.getGender()));
	}
	
	public class PrivacySettings{
		
		private String 	title = "";
		private String 	key = "";
		private String 	privacy = "";
		
		public PrivacySettings (String title, String key, String privacy) {
			this.title = title;
			this.key = key;
			this.privacy = privacy;
		}
		
		public String getTitle () {
			return this.title;
		}
		
		public String getKey () {
			return this.key;
		}
		
		public void setPrivacy (String privacy) {
			this.privacy = privacy;
		}
		
		public int getPrivacy () {
			if(privacy.equals(TVUserPrivacy.PRIAVCY_ALL))
				return ALL;
			else if(privacy.equals(TVUserPrivacy.PRIVACY_FRIEND))
				return FRIEND;
			else if(privacy.equals(TVUserPrivacy.PRIVACY_NONE))
				return NONE;
			else if(privacy.equals(TVUserPrivacy.PRIVACY_NO_YEAR))
				return NO_YEAR;
			else if(privacy.equals(TVUserPrivacy.PRIVACY_SHOW_YEAR))
				return SHOW_YEAR;
			return ALL;
		}
	}
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
			case R.id.back_imagebutton:
				AccountSettingsPrivacyActivity.this.finish();
				break;
		}
		
	}

	private void Update(final PrivacySettings privacysetting, final View v, final String text, final int position, final String privacy){
		new Thread ( new Runnable () {
			public void run () {
				final boolean recode = AccountManager.updatePrivacy(tvuPrivacy);
				AccountSettingsPrivacyActivity.this.runOnUiThread( new Runnable () {
					public void run () {
						if(recode){
				      		showToast(privacysetting.getTitle(), ApplicationManager.getAppContext().getResources().getString(R.string.action_update_successfully));
				      		if (!isDestroyed())
				      			privacy_setting_list.get(position).setPrivacy(privacy);
						}else {
				      		showToast(privacysetting.getTitle(), ApplicationManager.getAppContext().getResources().getString(R.string.sign_up_failed));
				      		((TextView)v.findViewById(R.id.privacy_value_textview)).setText(text);
						}
					}
				});
			}
		}).start();
	}
	
	private void showToast(String Title, String msr){
  		Toast.makeText(AccountSettingsPrivacyActivity.this, Title + " : "+ msr, Toast.LENGTH_SHORT).show();
	}

	private TVUserPrivacy setPrivacy(String Key, String privacy){
		if(Key.equals("pokeme"))
			tvuPrivacy.setSplash(privacy);
		else if(Key.equals("seereminder"))
			tvuPrivacy.setReminder(privacy);
		else if(Key.equals("seeemail"))
			tvuPrivacy.setEmail(privacy);
		else if(Key.equals("seesex"))
			tvuPrivacy.setGender(privacy);
		else if(Key.equals("seebirth"))
			tvuPrivacy.setBirth(privacy);
		else if(Key.equals("seefacebook"))
			tvuPrivacy.setFacebook(privacy);
		
		return tvuPrivacy;
		
	}
	

	public class PrivacyAdapter extends BaseAdapter{

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView==null){
				LayoutInflater mInflater = LayoutInflater.from(AccountSettingsPrivacyActivity.this);
				convertView = mInflater.inflate(R.layout.account_settings_privacy_listitem, null);
				
				TextView tv_title = (TextView) convertView.findViewById(R.id.privacy_title_textview);
				TextView tv_detail = (TextView) convertView.findViewById(R.id.privacy_value_textview);
				PrivacySettings psettings = privacy_setting_list.get(position);
				
				tv_title.setText(psettings.getTitle());
				String[] items = null;
				
				if(psettings.getKey().equals("seebirth")){
					items = getResources().getStringArray(R.array.privacy_values_birthday_array);
				}else{
					items = getResources().getStringArray(R.array.privacy_values_array);
				}
				
				if(psettings.getPrivacy()== 0){
					tv_detail.setText(items[0]);
				}else if(psettings.getPrivacy() == 1){
					tv_detail.setText(items[1]);
				}else if(psettings.getPrivacy() == 2){
					tv_detail.setText(items[2]);
				}
			}
			
			
			return convertView;
		}
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return privacy_setting_list.size();
		}
		
	}

	
}


