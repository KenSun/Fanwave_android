package com.wildmind.fanwave.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.guide.ChannelManager;
import com.wildmind.fanwave.guide.DragListAdapter;
import com.wildmind.fanwave.guide.HidderAdapter;
import com.wildmind.fanwave.guide.ProviderManager;
import com.wildmind.fanwave.guide.TVChannel;
import com.wildmind.fanwave.guide.HidderAdapter.HidderGroupListener;
import com.wildmind.fanwave.vendor.VendorManager;
import com.wildmind.fanwave.widget.DragListView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class WizardActivity extends BaseActivity implements HidderGroupListener{
	
	private int ISCITY 			= 0;
	private int ISZIP 			= 1;
	private int ISPROVIDER 		= 2;

	private LayoutInflater			 mInflater = null;
	private ViewFlipper				 wizard_viewflipper = null;
	private ProgressDialog 			 progressDialog = null;
	private AlertDialog 			 account_alertdialog = null;
	private EditText 				 city_edittext, postname_edittext;
	private ListView 				 channel_list_listview;

	private ArrayList<TVChannel> 	hidder_TVChannels =null; 
	private ArrayList<TVChannel> 	reorder_TVChannels =null; 
	private boolean 				select_all = true;

	//hidder ui
	private ListView 				hidder_list_listview = null;
	private HidderAdapter 			hidder_adapter = null;
	private ImageButton				select_all_imagebutton;
	//Reorder ui
	private ListView 				reorder_list_listview = null;
	private DragListAdapter 		reorderadapter = null;

	
	
	private String countryCode = "tw";
	private String countryName = ApplicationManager.getAppContext().getResources().getString(R.string.wizard_location_country);
	private String cityName = "";
	private String zip_name = "";
	private String zip_code = "";
	private String vendorid	= "";
	private String vendorname = "";
	private String zip_obj	= "";
	
	ArrayList<HashMap<String, String>> datalist = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // check if activity is destroyed due to abnormal situation
       
        if (isDestroyed())
			return;
        
        setContentView(R.layout.wizard_activity);
        initData ();
       
        //get flipper
        wizard_viewflipper = (ViewFlipper) findViewById(R.id.wizard_viewflipper);
        mInflater = LayoutInflater.from(this);
        initWizard_intro_UI();
        initWizard_location_UI();
        initWizard_channel_UI();
        initWizard_channel_hidder_UI();
        initWizard_channel_reorder_UI(); 
    }
    
    @Override
    protected void onDestroy() {
		super.onDestroy();

		wizard_viewflipper = null;
		progressDialog = null;
		account_alertdialog =null;
		city_edittext = null;
		postname_edittext = null;
		channel_list_listview = null;
		mInflater = null;
		hidder_list_listview = null;
		reorder_list_listview = null;
		
		if(datalist!=null)
			datalist.clear();
		datalist = null;
		
		if(hidder_TVChannels!=null)
			hidder_TVChannels.clear();
		hidder_TVChannels = null;

		if(reorder_TVChannels!=null)
			reorder_TVChannels.clear();
		reorder_TVChannels = null;
		
		if(hidder_adapter!=null)
			hidder_adapter.clear();
		hidder_adapter = null;
		
		if(reorderadapter!=null)
			reorderadapter.clear();
		reorderadapter = null;
	}
    
	private void initData () {

	}
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			switch(wizard_viewflipper.getDisplayedChild()){
				case 0:
					ShowMessage();
					break;
				default:
					wizard_viewflipper.setDisplayedChild(wizard_viewflipper.getDisplayedChild()-1);

					
			}
	        return false;
	    }
		
		return super.onKeyDown(keyCode, event);
	}


	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		
		AnimationDrawable imv_animation = (AnimationDrawable)((ImageView) (wizard_viewflipper.getChildAt(0)).findViewById(R.id.wizard_ball_processing_imageview)).getBackground();
		if(!imv_animation.isRunning()) imv_animation.start();
	}

    
    private void initWizard_intro_UI(){
    		LinearLayout wizard_intro_view = (LinearLayout) View.inflate(this, R.layout.wizard_intro, null);
    	
    		((Button)wizard_intro_view.findViewById(R.id.wizard_cancel_button)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
						ShowMessage();;
				}
			});
    		
    		((Button)wizard_intro_view.findViewById(R.id.wizard_start_button)).setOnClickListener(getNextClickedListener());
    		
    		wizard_viewflipper.addView(wizard_intro_view, 0);
    	}
    
    private void initWizard_location_UI(){
    		LinearLayout wizard_location_view = (LinearLayout) View.inflate(this, R.layout.wizard_location, null);
    		
    		((Button)wizard_location_view.findViewById(R.id.wizard_pre_button)).setOnClickListener(getPreClickedListener());
    		((Button)wizard_location_view.findViewById(R.id.wizard_next_button)).setOnClickListener(getNextClickedListener());
    		
    		city_edittext = ((EditText)wizard_location_view.findViewById(R.id.city_edittext));
    		city_edittext.setOnTouchListener(getOnTouchListener(ISCITY));
    		
    		postname_edittext = ((EditText)wizard_location_view.findViewById(R.id.postname_edittext));
    		postname_edittext.setOnTouchListener(getOnTouchListener(ISZIP));
    		
    		wizard_viewflipper.addView(wizard_location_view, 1);
    }
    
    private void initWizard_channel_UI(){
    		LinearLayout wizard_channel_view = (LinearLayout) View.inflate(this, R.layout.wizard_channel, null);
    		
    		((Button)wizard_channel_view.findViewById(R.id.wizard_pre_button)).setOnClickListener(getPreClickedListener());
    		((Button)wizard_channel_view.findViewById(R.id.wizard_next_button)).setOnClickListener(getNextClickedListener());
    		channel_list_listview = ((ListView)wizard_channel_view.findViewById(R.id.channel_list_listview));
    		wizard_viewflipper.addView(wizard_channel_view, 2);
    }
    
    private void initWizard_channel_hidder_UI(){
    		LinearLayout wizard_channel_hidder_view = (LinearLayout) View.inflate(this, R.layout.wizard_channel_hidder, null);
    		RelativeLayout senseRegion = (RelativeLayout) wizard_channel_hidder_view.findViewById(R.id.title_sense_region);
    		senseRegion.setClickable(false);
    		((Button)wizard_channel_hidder_view.findViewById(R.id.wizard_pre_button)).setOnClickListener(getPreClickedListener());
    		((Button)wizard_channel_hidder_view.findViewById(R.id.wizard_next_button)).setOnClickListener(getNextClickedListener());
        	hidder_list_listview = (ListView)wizard_channel_hidder_view.findViewById(R.id.wizard_hidder_list_listview);
        	hidder_list_listview.setDivider(null);
        	select_all_imagebutton = ((ImageButton)wizard_channel_hidder_view.findViewById(R.id.setting_imagebutton));
        	select_all_imagebutton.setOnClickListener(getHidderClickListener());
    		wizard_viewflipper.addView(wizard_channel_hidder_view, 3);
    }
    
    public void initWizard_channel_reorder_UI(){
    		LinearLayout wizard_channel_reorder_view = (LinearLayout) View.inflate(this, R.layout.wizard_channel_reorder, null);
    		RelativeLayout senseRegion = (RelativeLayout) wizard_channel_reorder_view.findViewById(R.id.title_sense_region);
    		senseRegion.setClickable(false);
    		((Button)wizard_channel_reorder_view.findViewById(R.id.wizard_pre_button)).setOnClickListener(getPreClickedListener());
    		((Button)wizard_channel_reorder_view.findViewById(R.id.wizard_finish_button)).setOnClickListener(getNextClickedListener());
        	reorder_list_listview = (DragListView)wizard_channel_reorder_view.findViewById(R.id.wizard_reorder_list_draglistview);
        	reorder_list_listview.setDivider(null);
    		((ImageButton)wizard_channel_reorder_view.findViewById(R.id.setting_imagebutton)).setOnClickListener(getReorderClickListener());;
    		wizard_viewflipper.addView(wizard_channel_reorder_view, 4);
    }
    
    private void setVendorandfinish(){
    	progressDialog = new ProgressDialog(WizardActivity.this);
		progressDialog.setMessage(getResources().getString(R.string.action_saving));
		progressDialog.show();
	
	
		new Thread ( new Runnable () {
			public void run () {
				VendorManager.saveVendorSettings(countryCode, countryName, cityName, vendorname, vendorid, zip_name, zip_code);
		    	ChannelManager.getNewChannelList();
			    WizardActivity.this.runOnUiThread( new Runnable () {
						public void run () {
							
							if(progressDialog!=null&&progressDialog.isShowing())
								progressDialog.dismiss();
							progressDialog = null;
							
							setResult(RESULT_OK);
							WizardActivity.this.finish();
					    	

						}
					});				     
			        
				}
			}).start();		
    }
    
    private View.OnClickListener getPreClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v.getId()==R.id.wizard_pre_button){
					if(wizard_viewflipper.getDisplayedChild()>0)
						wizard_viewflipper.setDisplayedChild(wizard_viewflipper.getDisplayedChild()-1);
				}
			}
		};
	}
    
    private View.OnClickListener getNextClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v.getId()==R.id.wizard_next_button || v.getId()==R.id.wizard_start_button){
					
					switch(wizard_viewflipper.getDisplayedChild()){
						case 1:
							if(cityName.equals("")&&zip_code.equals("")){
								cityName = ApplicationManager.getAppContext().getResources().getString(R.string.wizard_location_city_default);
								zip_code = "114";
							}
								
							if(wizard_viewflipper.getDisplayedChild()<wizard_viewflipper.getChildCount())
							{
								setChannelListView(channel_list_listview);
								
							}
							
							vendorid = "";
							vendorname = "";
							break;
						case 2:
							if(vendorid.equals("")&&vendorname.equals("")){
								showToast(ApplicationManager.getAppContext().getResources().getString(R.string.guides_provider_mso));
							}else{
								if(wizard_viewflipper.getDisplayedChild()<wizard_viewflipper.getChildCount())
								{
									UpdatetoServerAndInitHidder();
								}
							}
							break;
						case 3:
							if(wizard_viewflipper.getDisplayedChild()<wizard_viewflipper.getChildCount())
							{
								SaveHidder(hidder_adapter.getChannels());
							}
							break;

						default:
							if(wizard_viewflipper.getDisplayedChild()<wizard_viewflipper.getChildCount())
								wizard_viewflipper.setDisplayedChild(wizard_viewflipper.getDisplayedChild()+1);
							break;
					}
				
				}else if(v.getId()==R.id.wizard_finish_button){
					SaveRecoder(reorderadapter.getList());
				}
			}
		};
	}
    
    private View.OnTouchListener getOnTouchListener(final int type){
    	return new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(type == ISCITY)
					((EditText)v.findViewById(R.id.city_edittext)).setInputType(InputType.TYPE_NULL);
				else if(type == ISZIP)
					((EditText)v.findViewById(R.id.postname_edittext)).setInputType(InputType.TYPE_NULL);
				
				if(event.getAction() == MotionEvent.ACTION_DOWN)
					setDialog(type);
				
				return true;
			}
    		
    	};
    }
    
    private void setDialog(final int type){
    	if (isDestroyed())
    		return;
    	
		progressDialog = new ProgressDialog(WizardActivity.this);
		progressDialog.setMessage(ApplicationManager.getAppContext().getResources().getString(R.string.action_loading));
		progressDialog.show();	
		
		account_alertdialog = null;
		
		new Thread (new Runnable () {
			public void run () {
				
				if(type == ISCITY){
					
					if(countryCode.equals("")){
						countryCode = "tw";
						countryName = ApplicationManager.getAppContext().getResources().getString(R.string.wizard_location_country);
					}
					
					datalist = ProviderManager.getRegionName(countryCode);
					
				}else if(type == ISZIP){
					if(cityName.equals(""))
						cityName = ApplicationManager.getAppContext().getResources().getString(R.string.wizard_location_city_default);
						
					zip_obj = getZip(countryCode, cityName);
					
				}
				
				if (!isDestroyed()) {
					WizardActivity.this.runOnUiThread( new Runnable () {
						public void run () {
							
							if(type == ISCITY){
								
								if(countryCode.equals("")){
									countryCode = "tw";
									countryName = ApplicationManager.getAppContext().getResources().getString(R.string.wizard_location_country);
								}
								
								cityName = ApplicationManager.getAppContext().getResources().getString(R.string.wizard_location_city_default);
								zip_name = ApplicationManager.getAppContext().getResources().getString(R.string.wizard_location_postname_default);
								city_edittext.setText("");
								postname_edittext.setText("");
								account_alertdialog = initCityDialog(datalist, type).create();
								
							}else if(type == ISZIP){
								
								if(zip_obj == null||zip_obj.equals(""))
									return;
									
								account_alertdialog = initZipDialog(zip_obj, type).create();
								
							}
							
							if(progressDialog!=null&&progressDialog.isShowing())
								progressDialog.dismiss();
							
							if(account_alertdialog!=null){
								account_alertdialog.show();
							}
						}
							
					});		
				}
			}
		}).start();
		
		
	}
    
	private String getZip(String countryCode, String cityName){
		ArrayList<HashMap<String, String>> datalist = ProviderManager.getRegionName(countryCode);
		for(HashMap<String, String> hashmap: datalist){
			if(hashmap.get("regionName").equals(cityName)){
				return hashmap.get("zip");
			}
		}
		return null;
	}
	
	private AlertDialog.Builder initCityDialog(final ArrayList<HashMap<String, String>> datalist, final int type){
		AlertDialog.Builder dialog_builder = new AlertDialog.Builder(WizardActivity.this);
		return dialog_builder
					  .setTitle(ApplicationManager.getAppContext().getResources().getString(R.string.guides_provider_ciy))
					  .setAdapter(getlistadapter(datalist, type), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								cityName = datalist.get(which).get("regionName");
								zip_obj = datalist.get(which).get("zip");
								city_edittext.setText(cityName);
								zip_name = "";
								zip_code = "";
								vendorid = "";
								vendorname = "";
								postname_edittext.setText("");
								setDialog(ISZIP);
							}
						});
	}
	
	private AlertDialog.Builder initZipDialog(final String zip, final int type){
		final ArrayList<HashMap<String, String>> datalist = new ArrayList<HashMap<String, String>>();
		try {
			JSONArray array = new JSONArray(zip);
			int len = array.length();
			if (len != 0)
			for (int i = 0; i < len; i++){
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> hashmap = new HashMap<String, String>();
				hashmap.put("zipCode", obj.getString("zipCode"));
				hashmap.put("zipName", obj.getString("zipName"));
				datalist.add(hashmap);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		AlertDialog.Builder dialog_builder = new AlertDialog.Builder(WizardActivity.this);
		return dialog_builder
					.setTitle(ApplicationManager.getAppContext().getResources().getString(R.string.guides_provider_zip))
					.setOnKeyListener(getonkeylistener())
					.setAdapter(getlistadapter(datalist, type), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								zip_name = datalist.get(which).get("zipName");
								zip_code = datalist.get(which).get("zipCode");
								vendorid = "";
								vendorname = "";
								postname_edittext.setText(zip_name);
							}
						});
	}
	
	private void setChannelListView (final ListView Lv) {
		progressDialog = new ProgressDialog(WizardActivity.this);
		progressDialog.setMessage(ApplicationManager.getAppContext().getResources().getString(R.string.action_loading));
		progressDialog.show();	
		new Thread( new Runnable() {
			public void run() {
				if(zip_code.equals("")||countryCode.equals(""))
					return;
				datalist = ProviderManager.getProvider(zip_code, countryCode);
				
				if(datalist.size() == 0){
					WizardActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							if (isDestroyed())
								return;
							
							showToast(ApplicationManager.getAppContext().getResources().getString(R.string.guides_provider_ciy));
							return;
							
						}
					});
					
				}
					
				
				if (!isDestroyed()) {
					WizardActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							if (isDestroyed())
								return;
							
							Lv.setDivider(null);
							Lv.setAdapter(getlistadapter(datalist, ISPROVIDER));
							Lv.setOnItemClickListener(getOnItemClickListener ());
							
							if(progressDialog!=null&&progressDialog.isShowing())
								progressDialog.dismiss();
							
							wizard_viewflipper.setDisplayedChild(wizard_viewflipper.getDisplayedChild()+1);
							
						}
					});
				}
			}
		}).start();
	}

	
	private OnItemClickListener getOnItemClickListener (){
		return new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				vendorid = datalist.get(arg2).get("msoid");
				vendorname = datalist.get(arg2).get("msoname");
				
				for(int i = 0; i < datalist.size(); i++){
		    		((TextView)arg0.getChildAt(i).findViewById(R.id.dialog_textview)).setBackgroundResource(R.drawable.wizard_provider_normal);
		    		((TextView)arg0.getChildAt(i).findViewById(R.id.dialog_textview)).setTextColor(Color.BLACK);
				}
				
	    		((TextView)arg1.findViewById(R.id.dialog_textview)).setBackgroundResource(R.drawable.wizard_provider_press);
	    		((TextView)arg1.findViewById(R.id.dialog_textview)).setTextColor(Color.WHITE);

			}
		};
	}
	
	private OnKeyListener getonkeylistener (){
		return new OnKeyListener (){
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == KeyEvent.ACTION_UP){
			    	setDialog(ISCITY);
			    	dialog.cancel();
				}
				return false;
			}
			
		};
	}
	
	private void showToast(String msr){
  		Toast.makeText(WizardActivity.this, msr, Toast.LENGTH_SHORT).show();
	}
		
	private ListAdapter getlistadapter(final ArrayList<HashMap<String, String>> datalist, final int type){
		ListAdapter dialog_adapter = new BaseAdapter() {

			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return datalist.get(position);
			}
			
			public int getCount() {
				// TODO Auto-generated method stub
				return datalist.size();
			}
	
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder viewHolder;
				if (convertView == null) {
					viewHolder = new ViewHolder();
					convertView = mInflater.inflate(R.layout.provider_dialog_view, null);	
					viewHolder.tv_item = (TextView)convertView.findViewById(R.id.dialog_textview);
					convertView.setTag(viewHolder);
				} else
					viewHolder = (ViewHolder) convertView.getTag();

				viewHolder.tv_item.setText("");		
				HashMap<String, String> hashmap = datalist.get(position);
				if(type == ISCITY)
					viewHolder.tv_item.setText(hashmap.get("regionName"));
				else if(type == ISZIP)
					viewHolder.tv_item.setText(hashmap.get("zipName"));
				else if(type == ISPROVIDER){
					convertView.setPadding(0, 0, 0, 5);
					viewHolder.tv_item.setBackgroundResource(R.drawable.wizard_provider_normal);
					viewHolder.tv_item.setTextColor(Color.BLACK);
					viewHolder.tv_item.setText(hashmap.get("msoname"));	
					
				}
				return convertView;
			}
			
			class ViewHolder {
				TextView 	tv_item;
			}
			
		};
		return dialog_adapter;
	}



	// Hidder 
	
	private void UpdatetoServerAndInitHidder () {
		progressDialog = new ProgressDialog(WizardActivity.this);
		progressDialog.setMessage(ApplicationManager.getAppContext().getResources().getString(R.string.action_loading));
		progressDialog.show();	
		new Thread( new Runnable() {
			public void run() {
				VendorManager.saveVendorSettings(countryCode, countryName, cityName, vendorname, vendorid, zip_name, zip_code);
				ChannelManager.getNewChannelList();
				ChannelManager.UpdatetoServer();
				if (!isDestroyed()) {
					WizardActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							if (isDestroyed())
								return;
							InitHidderViewUi();
							if(progressDialog!=null&&progressDialog.isShowing())
								progressDialog.dismiss();
							
							wizard_viewflipper.setDisplayedChild(wizard_viewflipper.getDisplayedChild()+1);
						}
					});
				}
			}
		}).start();
	}

	
	private void InitHidderViewUi(){

		hidder_TVChannels = ChannelManager.getSortChannelListBydefault();
		hidder_adapter = new HidderAdapter(WizardActivity.this, hidder_list_listview);
		hidder_list_listview.setAdapter(hidder_adapter);
		hidder_adapter.setHidderGroupListener(this);
		hidder_adapter.refreshData(hidder_TVChannels);
		select_all = hidder_adapter.isSelectAll();
		select_all_imagebutton.setSelected(select_all);
	}
	
	 private OnClickListener getHidderClickListener(){ 
		 return new OnClickListener() { 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (hidder_adapter == null)
					return;
				
				if (select_all)
					hidder_adapter.unselectAll();
				else
					hidder_adapter.selectAll();
				
			}
		 };
	 }
	 
	 public void SaveHidder(final ArrayList<TVChannel> TVChannelList){
		 if(TVChannelList.size()!=0){
		 	progressDialog = new ProgressDialog(WizardActivity.this);
			progressDialog.setMessage(getResources().getString(R.string.action_saving));
			progressDialog.show();
			if (!isDestroyed()){

				new Thread ( new Runnable () {
					public void run () {
						ChannelManager.sethaveupdate(true);
				        ChannelManager.setHidder(TVChannelList);
						ChannelManager.UpdatetoServer();
					
				        WizardActivity.this.runOnUiThread( new Runnable () {
							public void run () {
								if (isDestroyed())
									return;
								InitReorderViewUi();
								if(progressDialog!=null&&progressDialog.isShowing())
									progressDialog.dismiss();
								
								wizard_viewflipper.setDisplayedChild(wizard_viewflipper.getDisplayedChild()+1);
							}
				         });						
			           	
					}
				}).start();
			}
	 	}
	 }
	 
	 
	 
	 
	 
	 
	 
	 // reorderr

	private void InitReorderViewUi(){
		reorder_TVChannels = ChannelManager.getSortChannelListByOrder();
    	reorderadapter = new DragListAdapter(WizardActivity.this, reorder_TVChannels, reorder_list_listview);
        reorder_list_listview.setAdapter(reorderadapter);
	}
	
	 private OnClickListener getReorderClickListener(){ 
		 return new OnClickListener() { 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShowMessageforReorder();

			}
		 };
	 }
	 private void SaveRecoder(final ArrayList<TVChannel> TVChannels){

		progressDialog = new ProgressDialog(WizardActivity.this);
		progressDialog.setMessage(getResources().getString(R.string.action_saving));
		progressDialog.show();
	
		new Thread ( new Runnable () {
			public void run () {
				
				 for(int i = 0; i < TVChannels.size(); i++){
						TVChannels.get(i).setOrder((i+1)+"");
					}
				
				ChannelManager.sethaveupdate(true);
			    ChannelManager.setReorder(TVChannels);
			    ChannelManager.UpdatetoServer();
			    WizardActivity.this.runOnUiThread( new Runnable () {
						public void run () {
							
							if(progressDialog!=null&&progressDialog.isShowing())
								progressDialog.dismiss();
							progressDialog = null;
							
							setResult(RESULT_OK);
							WizardActivity.this.finish();
						}
					});				     
			        
				}
			}).start();		
	 }
	 	 
	 public void ShowMessage(){
		setDefault();
		 
		DialogInterface.OnClickListener sync = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				setVendorandfinish();
			}
		};
		DialogInterface.OnClickListener unsync = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
					
			}
		};
		new AlertDialog.Builder(this)
						  	.setTitle(R.string.app_message)
						   .setMessage(
								   ApplicationManager.getAppContext().getResources().getString(R.string.wizard_back) 
								   + " "
								   + cityName 
								   + " " 
								   + zip_name 
								   + " " 
								   + ApplicationManager.getAppContext().getResources().getString(R.string.wizard_back_1)
								   )
						   .setPositiveButton(R.string.action_confirm, sync)
						   .setNegativeButton(R.string.action_cancel, unsync)
						   .show();
	 }
	 
	 public void ShowMessageforReorder(){
		 DialogInterface.OnClickListener sync = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				reorder_TVChannels = ChannelManager.getSortChannelListByNum();
				reorder_list_listview.removeAllViewsInLayout();
				reorderadapter = new DragListAdapter(WizardActivity.this, reorder_TVChannels, reorder_list_listview);
		        reorder_list_listview.setAdapter(reorderadapter);
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
	 
	 public void setDefault(){
			countryCode = "tw";
			countryName = ApplicationManager.getAppContext().getResources().getString(R.string.wizard_location_country);
			cityName = ApplicationManager.getAppContext().getResources().getString(R.string.wizard_location_city_default);
			zip_name = ApplicationManager.getAppContext().getResources().getString(R.string.wizard_location_postname_default);
			zip_code = "114";
			vendorid	= "tw_114_11450_3"; 
			vendorname	= "·s¥x¥_";
			city_edittext.setText("");
			postname_edittext.setText("");
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