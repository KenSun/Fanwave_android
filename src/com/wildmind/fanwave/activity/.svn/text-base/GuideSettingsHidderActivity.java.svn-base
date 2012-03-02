package com.wildmind.fanwave.activity;

import java.util.ArrayList;

import com.wildmind.fanwave.guide.ChannelManager;
import com.wildmind.fanwave.guide.HidderAdapter;
import com.wildmind.fanwave.guide.HidderAdapter.HidderGroupListener;
import com.wildmind.fanwave.guide.TVChannel;
import com.wildmind.fanwave.activity.R;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

public class GuideSettingsHidderActivity extends BaseActivity implements HidderGroupListener{
	
	private ArrayList<TVChannel> TVChannel_List =null; 
	private ListView 		guide_list_listview = null;
	private ImageButton		select_all_imagebutton;
	private HidderAdapter 	hidder_adapter = null;
	private boolean 		select_all = true;
	private ProgressDialog  progressDialog = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// check if activity is destroyed due to abnormal situation
		if (isDestroyed())
			return;
		
		setContentView(R.layout.guide_settings_hidder_activity);
		
		initUI();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		select_all_imagebutton = null;
		guide_list_listview = null;
		if (TVChannel_List != null)
			TVChannel_List.clear();
		TVChannel_List = null;
		if (hidder_adapter != null)
			hidder_adapter.clear();
		hidder_adapter = null;
		progressDialog = null;

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
				
		super.onStop();
	}

	public void initUI(){
		ImageButton back_button = (ImageButton) findViewById(R.id.back_imagebutton);
		back_button.setOnClickListener(getClickListener());
		select_all_imagebutton = (ImageButton)findViewById(R.id.setting_imagebutton);
		select_all_imagebutton.setOnClickListener(getClickListener());
		
		TVChannel_List = ChannelManager.getSortChannelListBydefault();
		guide_list_listview = (ListView)findViewById(R.id.guide_list_listview);
		guide_list_listview.setDivider(null);
    	hidder_adapter = new HidderAdapter(GuideSettingsHidderActivity.this, guide_list_listview);
    	hidder_adapter.setHidderGroupListener(this);
    	guide_list_listview.setAdapter(hidder_adapter);
    	hidder_adapter.refreshData(TVChannel_List);
    	select_all = hidder_adapter.isSelectAll();
    	select_all_imagebutton.setSelected(select_all);

	 } 	
	     
	 private OnClickListener getClickListener(){ 
		 return new OnClickListener() { 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch(v.getId()){
					case R.id.back_imagebutton:
						SaveandFinish(hidder_adapter.getChannels());
						break;
					case R.id.setting_imagebutton:
						if (hidder_adapter == null)
							return;
					
						if (select_all)
							hidder_adapter.unselectAll();
						else
							hidder_adapter.selectAll();
						
						break;
				}	

			}
		 };
	 }

	 
	 
	 public void SaveandFinish(final ArrayList<TVChannel> TVChannelList){
		 if(TVChannelList.size()!=0&&ChannelManager.isChange(TVChannelList)){
		 	progressDialog = new ProgressDialog(GuideSettingsHidderActivity.this);
			progressDialog.setMessage(getResources().getString(R.string.action_saving));
			progressDialog.show();
			new Thread ( new Runnable () {
				public void run () {
					ChannelManager.sethaveupdate(true);
			        ChannelManager.setHidder(TVChannelList);
					ChannelManager.UpdatetoServer();
					
					if(progressDialog!=null&&progressDialog.isShowing())
						progressDialog.dismiss();
		           	finish();
				}
			}).start();
	 	}else
	 		finish();
	 }
	 
		 
	//overlay the KeyDown 
	 public boolean onKeyDown(int keyCode, KeyEvent event) { 
       if (keyCode == KeyEvent.KEYCODE_BACK) { 
               //do what you want 
		  SaveandFinish(hidder_adapter.getChannels());
     	  return true; 
       
       }
       return super.onKeyDown(keyCode, event); 
    }


	@Override
	public void onAllSelected() {
		// TODO Auto-generated method stub
		select_all = true;
		select_all_imagebutton.setSelected(select_all);
	}

	@Override
	public void onAllSelectedCancel() {
		// TODO Auto-generated method stub
		select_all = false;
		select_all_imagebutton.setSelected(select_all);
	} 	    

   
}
