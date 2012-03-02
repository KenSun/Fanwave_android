package com.wildmind.fanwave.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.guide.ChannelManager;
import com.wildmind.fanwave.guide.ProviderManager;
import com.wildmind.fanwave.vendor.VendorManager;
import com.wildmind.fanwave.activity.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GuideSettingsProviderActivity extends BaseActivity implements View.OnClickListener{
	
	private Intent mIntent;
	private RelativeLayout region_relativelayout, city_relativelayout, zip_code_relativelayout, provider_relativelayout;
	private TextView textview_region, textview_city, textview_zip, textview_provider;
	private String countryCode, countryName, cityName, zip_name, zip_code, vendorname, zip_obj;
	private LayoutInflater mInflater = null;
	private AlertDialog account_alertdialog = null;
	private int ISREGION 		= 1;
	private int ISCITY 			= 2;
	private int ISZIP 			= 3;
	private int ISPROVIDER 		= 4;
	private ProgressDialog progressDialog = null;
	private int userselect = 0;
	private int viewselect = 0;
	
	ArrayList<HashMap<String, String>> datalist = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// check if activity is destroyed due to abnormal situation
		if (isDestroyed())
			return;
		
		setContentView(R.layout.guide_settings_provider_activity);
		initData();
		initUI();
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mIntent = null;
		account_alertdialog = null;
		region_relativelayout = null;
		city_relativelayout = null;
		zip_code_relativelayout = null;
		provider_relativelayout = null;
		mInflater = null;

		if (datalist != null)
			datalist.clear();
		datalist = null;
		progressDialog = null;
		textview_region = null;
		textview_city = null;
		textview_zip = null;
		textview_provider = null;
		countryCode = null; 
		countryName = null; 
		cityName = null;
		zip_name = null;
		zip_code = null;
		vendorname = null;
		zip_obj = null;
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
	
	private void initData(){
		getDefaultVendor();
	}
	
	private void initUI(){
		mInflater = LayoutInflater.from(this);
		
		//Home Button
		ImageButton back_button = (ImageButton) findViewById(R.id.back_imagebutton);
		back_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		textview_region = (TextView)findViewById(R.id.textview_region);
		textview_region.setText(countryName);
		textview_city = (TextView)findViewById(R.id.textview_city);
		textview_city.setText(cityName);
		textview_zip = (TextView)findViewById(R.id.textview_zip);
		if(zip_name.equals("")||zip_name == null)
			textview_zip.setText("");
		else
			textview_zip.setText(zip_name+", "+zip_code);
		
		textview_provider = (TextView)findViewById(R.id.textview_provider);
		textview_provider.setText(vendorname);

		region_relativelayout = (RelativeLayout)findViewById(R.id.region_relativelayout);
		region_relativelayout.setOnClickListener(this);
		city_relativelayout = (RelativeLayout)findViewById(R.id.city_relativelayout);
		city_relativelayout.setOnClickListener(this);
		zip_code_relativelayout = (RelativeLayout)findViewById(R.id.zip_code_relativelayout);
		zip_code_relativelayout.setOnClickListener(this);
		provider_relativelayout = (RelativeLayout)findViewById(R.id.provider_relativelayout);
		provider_relativelayout.setOnClickListener(this);
	}
	

	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
			case R.id.back_imagebutton:
				GuideSettingsProviderActivity.this.finish();
				break;
			case R.id.region_relativelayout:
				setDialog(ISREGION);
				
				break;
				
			case R.id.city_relativelayout:
				
				if(countryCode.equals("")!=true){
					userselect = ISCITY;
					setDialog(ISCITY);
				}else{
					setDialog(ISREGION);
				}

				break;
				
			case R.id.zip_code_relativelayout:
				if(cityName.equals("")!=true){
					userselect = ISZIP;
					setDialog(ISZIP);
				}else if(countryCode.equals("")!=true){
					userselect = ISCITY;
					setDialog(ISCITY);
				}else{
					setDialog(ISREGION);
				}
					
						
				break;
				
			case R.id.provider_relativelayout:
				
				if(vendorname.equals("")!=true||zip_code.equals("")!=true){
					userselect = ISPROVIDER;
					setDialog(ISPROVIDER);
				}else if(cityName.equals("")!=true){
					userselect = ISZIP;
					setDialog(ISZIP);
				}else if(countryCode.equals("")!=true){
					userselect = ISCITY;
					setDialog(ISCITY);
				}else{
					setDialog(ISREGION);
				}
				
				break;
		}
		
		if(mIntent!=null) startActivity(mIntent);
	}
	
	private void getDefaultVendor(){
		
		countryCode = VendorManager.getCountry();
		countryName = VendorManager.getCountryName();
		cityName = VendorManager.getCityName();
		vendorname = VendorManager.getVendorName();
		zip_code = VendorManager.getPostcode();
		zip_name = VendorManager.getPostname();
		
	}
	
	private void setDialog(final int type){
		
		progressDialog = new ProgressDialog(GuideSettingsProviderActivity.this);
		progressDialog.setMessage(ApplicationManager.getAppContext().getResources().getString(R.string.action_loading));
		progressDialog.show();	

		new Thread (new Runnable () {
			public void run () {
				viewselect = type;
				if(type == ISREGION){
					
					datalist = ProviderManager.getSupportCountry();
					
				}else if(type == ISCITY){
					
					if(countryCode.equals(""))
						if(progressDialog!=null&&progressDialog.isShowing()){
							progressDialog.dismiss();
							return;
						}
					
					datalist = ProviderManager.getRegionName(countryCode);
					
				}else if(type == ISZIP){
					if(zip_obj == null)
						zip_obj = getZip();
					else{
						if(zip_obj.equals(""))
							if(progressDialog!=null&&progressDialog.isShowing()){
								progressDialog.dismiss();
								return;
							}
					}
						
					
				}else if(type == ISPROVIDER){
					if(zip_code.equals("")||countryCode.equals(""))
						if(progressDialog!=null&&progressDialog.isShowing()){
							progressDialog.dismiss();
							return;
						}
					
					datalist = ProviderManager.getProvider(zip_code, countryCode);
				}
				
				GuideSettingsProviderActivity.this.runOnUiThread( new Runnable () {
					public void run () {	
						if(type == ISREGION){
							
							account_alertdialog = initRegionDialog(datalist, type).create();
							
						}else if(type == ISCITY){
							
							if(countryCode.equals(""))
								if(progressDialog!=null&&progressDialog.isShowing()){
									progressDialog.dismiss();
									return;
								};
							
							account_alertdialog = initCityDialog(datalist, type).create();
							
						}else if(type == ISZIP){
							
							if(zip_obj == null||zip_obj.equals(""))
								if(progressDialog!=null&&progressDialog.isShowing()){
									progressDialog.dismiss();
									return;
								}
								
							account_alertdialog = initZipDialog(zip_obj, type).create();
							
						}else if(type == ISPROVIDER){
							if(zip_code.equals("")||countryCode.equals(""))
								if(progressDialog!=null&&progressDialog.isShowing()){
									progressDialog.dismiss();
									return;
								}
							account_alertdialog = initRoviderDialog(datalist, type).create();
						}
						if(progressDialog!=null&&progressDialog.isShowing())
							progressDialog.dismiss();
								
						if(account_alertdialog!=null){
							account_alertdialog.show();
						}
					}
				});		
				
			}
		}).start();
		
		
	}
	
	private void showToast(String msr){
  		Toast.makeText(GuideSettingsProviderActivity.this, msr, Toast.LENGTH_SHORT).show();
	}
	
	
	private AlertDialog.Builder initRegionDialog(final ArrayList<HashMap<String, String>> datalist, final int type){
		AlertDialog.Builder dialog_builder = null;
		dialog_builder = new AlertDialog.Builder(GuideSettingsProviderActivity.this);
		return dialog_builder.setTitle(ApplicationManager.getAppContext().getResources().getString(R.string.guides_provider_country))
					  .setAdapter(getlistadapter(datalist, type), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								countryCode = datalist.get(which).get("countryCode");
								countryName = datalist.get(which).get("countryFull");
								textview_region.setText(countryName);
								cityName = "";
								zip_name = "";
								zip_code = "";
								vendorname = "";
								textview_city.setText("");
								textview_zip.setText("");
								textview_provider.setText("");
								setDialog(ISCITY);
							}
						});
	}
	
	private AlertDialog.Builder initCityDialog(final ArrayList<HashMap<String, String>> datalist, final int type){
		AlertDialog.Builder dialog_builder = null;
		dialog_builder = new AlertDialog.Builder(GuideSettingsProviderActivity.this);
		return dialog_builder
					  .setTitle(ApplicationManager.getAppContext().getResources().getString(R.string.guides_provider_ciy))
					  .setOnKeyListener(getonkeylistener())
					  .setAdapter(getlistadapter(datalist, type), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								cityName = datalist.get(which).get("regionName");
								zip_obj = datalist.get(which).get("zip");
								textview_region.setText(countryName);
								textview_city.setText(cityName);
								zip_name = "";
								zip_code = "";
								vendorname = "";
								textview_zip.setText("");
								textview_provider.setText("");
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
		AlertDialog.Builder	dialog_builder = null;
		dialog_builder = new AlertDialog.Builder(GuideSettingsProviderActivity.this);
		return dialog_builder
					.setTitle(ApplicationManager.getAppContext().getResources().getString(R.string.guides_provider_zip))
					.setOnKeyListener(getonkeylistener())
					.setAdapter(getlistadapter(datalist, type), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								zip_name = datalist.get(which).get("zipName");
								zip_code = datalist.get(which).get("zipCode");
								textview_region.setText(countryName);
								textview_city.setText(cityName);
								textview_zip.setText(zip_name+", "+zip_code);
								vendorname = "";
								textview_provider.setText("");
								setDialog(ISPROVIDER);
							}
						});
	}
	
	private AlertDialog.Builder initRoviderDialog(final ArrayList<HashMap<String, String>> datalist, final int type){
		AlertDialog.Builder dialog_builder = null;
		dialog_builder = new AlertDialog.Builder(GuideSettingsProviderActivity.this);
		return dialog_builder
					  .setTitle(ApplicationManager.getAppContext().getResources().getString(R.string.guides_provider_mso))
					  .setOnKeyListener(getonkeylistener())
					  .setAdapter(getlistadapter(datalist, type), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								ShowMessage(datalist.get(which).get("msoid"), datalist.get(which).get("msoname"));
							}
						});
	}
	
	private String getZip(){
		ArrayList<HashMap<String, String>> datalist = ProviderManager.getRegionName(countryCode);
		for(HashMap<String, String> hashmap: datalist){
			if(hashmap.get("regionName").equals(cityName)){
				return hashmap.get("zip");
			}
		}
		return null;
	}
	
	private OnKeyListener getonkeylistener (){
		return new OnKeyListener (){
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == KeyEvent.ACTION_UP){
					if(userselect != viewselect){
			    		setDialog(viewselect-1);
			    	}
			    	dialog.cancel();
				}
				return false;
			}
			
		};
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
				if(type == ISREGION)
					viewHolder.tv_item.setText(hashmap.get("countryFull"));
				else if(type == ISCITY)
					viewHolder.tv_item.setText(hashmap.get("regionName"));
				else if(type == ISZIP)
					viewHolder.tv_item.setText(hashmap.get("zipName"));
				else if(type == ISPROVIDER)
					viewHolder.tv_item.setText(hashmap.get("msoname"));	
				
				return convertView;
			}
			
			class ViewHolder {
				TextView 	tv_item;
			}
			
		};
		return dialog_adapter;
	}
	 public void ShowMessage(final String vendor_id, final String vendor_name){
		 DialogInterface.OnClickListener sync = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				setMsoSaveChannels(vendor_id, vendor_name);
			}
		};
		DialogInterface.OnClickListener unsync = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				getDefaultVendor();
				
				textview_region.setText(countryName);
				textview_city.setText(cityName);
				textview_zip.setText(zip_name+", "+zip_code);
				textview_provider.setText(vendorname);
				userselect = 0;		
				viewselect = 0;
			}
		};
		new AlertDialog.Builder(this)
						   .setTitle(R.string.guides_program_setting_changemsg_title)
						   .setMessage(R.string.guides_program_setting_changemsg)
						   .setOnKeyListener(getMsoSaveChannelsonkeylistener())
						   .setPositiveButton(R.string.action_confirm, sync)
						   .setNegativeButton(R.string.action_cancel, unsync)
						   .show();
	 }
	 
	 public void setMsoSaveChannels(final String vendorid, final String vendorname){
			textview_region.setText(countryName);
			textview_city.setText(cityName);
			textview_zip.setText(zip_name+", "+zip_code);
			textview_provider.setText(vendorname);
			userselect = 0;		
			viewselect = 0;
			progressDialog = new ProgressDialog(GuideSettingsProviderActivity.this);
			progressDialog.setMessage(ApplicationManager.getAppContext().getResources().getString(R.string.action_loading));
			progressDialog.show();	
			new Thread ( new Runnable () {
				public void run () {
					ChannelManager.sethaveupdate(true);
					VendorManager.saveVendorSettings(countryCode, countryName, cityName, vendorname, vendorid, zip_name, zip_code);
					ChannelManager.getNewChannelList();
					GuideSettingsProviderActivity.this.runOnUiThread( new Runnable () {
						public void run () {	
							if(progressDialog!=null&&progressDialog.isShowing())
								progressDialog.dismiss();
							showToast(ApplicationManager.getAppContext().getResources().getString(R.string.action_update_successfully));
							finish();
						}
					});		
				}
			}).start();
	 }
	 
	 private OnKeyListener getMsoSaveChannelsonkeylistener(){
			return new OnKeyListener (){
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode,
						KeyEvent event) {
					// TODO Auto-generated method stub
					if(event.getAction() == KeyEvent.ACTION_UP){
						getDefaultVendor();
						
						textview_region.setText(countryName);
						textview_city.setText(cityName);
						textview_zip.setText(zip_name+", "+zip_code);
						textview_provider.setText(vendorname);
						userselect = 0;		
						viewselect = 0;
				    	dialog.cancel();
					}
					return false;
				}
				
			};
		}
}
