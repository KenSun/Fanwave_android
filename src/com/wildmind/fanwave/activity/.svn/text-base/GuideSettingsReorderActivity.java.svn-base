package com.wildmind.fanwave.activity;

import java.util.ArrayList;

import com.wildmind.fanwave.guide.ChannelManager;
import com.wildmind.fanwave.guide.DragListAdapter;
import com.wildmind.fanwave.guide.TVChannel;
import com.wildmind.fanwave.widget.DragListView;
import com.wildmind.fanwave.activity.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class GuideSettingsReorderActivity extends BaseActivity {
	
	private DragListAdapter adapter = null;
	private ArrayList<TVChannel> groupKey= new ArrayList<TVChannel>();
	private ArrayList<TVChannel> TVChannel_List = new ArrayList<TVChannel>(); 
	private ProgressDialog progressDialog = null;
	private DragListView dragListView = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// check if activity is destroyed due to abnormal situation
		if (isDestroyed())
			return;
		
		setContentView(R.layout.guide_settings_reorder_activity);
        initUI();
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dragListView = null;
		if (adapter != null)
			adapter.clear();
		adapter = null;
		if(groupKey!=null)
		groupKey.clear();
		groupKey = null;
		progressDialog = null;
		if (TVChannel_List != null)
			TVChannel_List.clear();
		TVChannel_List = null;
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
	
	
	private void initUI(){
		//Home Button
    	
    	TVChannel_List = ChannelManager.getSortChannelListByOrder();
    	
    	dragListView = (DragListView)findViewById(R.id.guide_list_draglistview);
    	dragListView.setDivider(null);
        adapter = new DragListAdapter(GuideSettingsReorderActivity.this, TVChannel_List, dragListView);
        dragListView.setAdapter(adapter);
        
		ImageButton back_button = (ImageButton) findViewById(R.id.back_imagebutton);
		back_button.setOnClickListener(getClickListener());
		ImageButton refreshbutton = (ImageButton) findViewById(R.id.setting_imagebutton);
		refreshbutton.setOnClickListener(getClickListener());
		
    } 	

    
	 private OnClickListener getClickListener(){ 
		 return new OnClickListener() { 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch(v.getId()){
					case R.id.back_imagebutton:
						
						SaveandFinish(adapter.getList());
						
						break;
					case R.id.setting_imagebutton:
						
						ShowMessageforReorder();
				        
						break;
				}	

			}
		 };
	 }
	    
	 public void SaveandFinish(final ArrayList<TVChannel> TVChannelList){
		 
		for(int i = 0 ; i < TVChannelList.size(); i++)
			TVChannelList.get(i).setOrder((i+1)+"");
		
		 if(ChannelManager.isChange(TVChannelList)){
			progressDialog = new ProgressDialog(GuideSettingsReorderActivity.this);
			progressDialog.setMessage(getResources().getString(R.string.action_saving));
			progressDialog.show();
	
	
			new Thread ( new Runnable () {
				public void run () {
					ChannelManager.sethaveupdate(true);
				    ChannelManager.setReorder(TVChannelList);
				    ChannelManager.UpdatetoServer();
			        GuideSettingsReorderActivity.this.runOnUiThread( new Runnable () {
							public void run () {
								
								if(progressDialog!=null&&progressDialog.isShowing())
									progressDialog.dismiss();
								progressDialog = null;
								finish();
	
							}
						});				     
				        
					}
				}).start();
		 }else
			 finish();
	 }

	 
	//overlay the KeyDown 
	 public boolean onKeyDown(int keyCode, KeyEvent event) { 
       if (keyCode == KeyEvent.KEYCODE_BACK) { 
               //do what you want 
    	   SaveandFinish(adapter.getList());
     	  return true; 
       
       }
       return super.onKeyDown(keyCode, event); 
   } 	    

	 public void ShowMessageforReorder(){
		 DialogInterface.OnClickListener sync = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				TVChannel_List = ChannelManager.getSortChannelListByNum();
		    	dragListView.removeAllViewsInLayout();
		        adapter = new DragListAdapter(GuideSettingsReorderActivity.this, TVChannel_List, dragListView);
		        dragListView.setAdapter(adapter);
			}
		};
		DialogInterface.OnClickListener unsync = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
					
			}
		};
		new AlertDialog.Builder(this)
						  	.setTitle(R.string.app_message)
						   .setMessage(R.string.app_use_default)
						   .setPositiveButton(R.string.action_confirm, sync)
						   .setNegativeButton(R.string.action_cancel, unsync)
						   .show();
	 }
}
