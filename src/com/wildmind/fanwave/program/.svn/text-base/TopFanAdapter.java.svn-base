package com.wildmind.fanwave.program;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.activity.PersonalActivity;
import com.wildmind.fanwave.media.ImageListener;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.app.ApplicationManager;


public class TopFanAdapter extends BaseAdapter {
	private static final int ROW_VIEW_TYPE_COUNT = 3;
	private static final int ROW_VIEW_TOPFAN	 = 0;
	private static final int ROW_VIEW_FAN 		 = 1;
	private static final int ROW_VIEW_MORE 		 = 2;

	private LayoutInflater inflater;
	private ListView listview;
	private Context _context;
	private String pgid;
	private boolean more_topfan;
	private int number = 5;
	private ArrayList<TVProgramFan> topfanlist = null;
	private TopFanListImageListener image_listener = new TopFanListImageListener();
	private ConcurrentHashMap<String, String> avatar_requests = new ConcurrentHashMap<String, String>();
	
	// constructor
	//
	public TopFanAdapter (Context _context, ListView listview, String pgid) {
		this.inflater = LayoutInflater.from(_context);
		this.listview = listview;		
		this._context = _context;		
		this.pgid = pgid;		
		ImageManager.addImageListener(image_listener);
	}
	
	/**
	 * Clear all resources.
	 */
	public void clear () {
		inflater = null;
		listview = null;
		
		if(topfanlist!=null)
			topfanlist.clear();
		topfanlist = null;
		
		_context = null;
		ImageManager.removeImageListener(image_listener);
		image_listener = null;
		
		if(avatar_requests!=null)
			avatar_requests.clear();
		avatar_requests = null;
	}
	

	/**
	 * Refresh data.
	 * @param list
	 * @param is_more
	 */
	public void refreshData (String topfanlist) {
		this.topfanlist = getProgramFan(topfanlist);
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return topfanlist != null ? topfanlist.size() + (more_topfan ? 1 : 0) : 0;
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return topfanlist != null ? topfanlist.get(position) : null;
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public int getViewTypeCount () {
		return ROW_VIEW_TYPE_COUNT;
	}
	
	@Override
	public int getItemViewType (int position) {
		// more row
		if (position == topfanlist.size())
			return ROW_VIEW_MORE;
		
		// fan row
		if (position == 0)
			return ROW_VIEW_TOPFAN;
		else 
			return ROW_VIEW_FAN;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if (position == topfanlist.size())
			return  more(convertView);	
		
		if (position == 0) 
			return topfan(convertView, topfanlist.get(position));
		else
			return fan(convertView, topfanlist.get(position));
		
	}

	private View topfan(View view,  final TVProgramFan tvpfan) {
		TopFanViewHolder viewHolder;
		if (view == null) {
			viewHolder = new TopFanViewHolder();
			view = inflater.inflate(R.layout.topfan_row_topfan, null);
			view.setBackgroundResource(R.drawable.fanwave_row_selector);
			viewHolder.topfan_imageview = (ImageView) view.findViewById(R.id.topfan_imageview);
			viewHolder.topfan_name_textview = (TextView) view.findViewById(R.id.topfan_name_textview);
			viewHolder.credit_score_textview = (TextView) view.findViewById(R.id.credit_score_textview);
			viewHolder.waveins_score_textview = (TextView) view.findViewById(R.id.waveins_score_textview);
			viewHolder.comments_score_textview = (TextView) view.findViewById(R.id.comments_score_textview);	
			view.setTag(viewHolder);
		} else
			viewHolder = (TopFanViewHolder) view.getTag();
				
		loadAvatar(viewHolder.topfan_imageview, tvpfan.getUsername());
		viewHolder.topfan_name_textview.setText(tvpfan.getNickname());
		viewHolder.credit_score_textview.setText(tvpfan.getPoint()+"");
		viewHolder.waveins_score_textview.setText(tvpfan.getWaveinCount()+"");
		viewHolder.comments_score_textview.setText(tvpfan.getCommentCount()+"");
		
		view.setOnClickListener(getAvatarImageClickedListener(tvpfan.getUsername(), tvpfan.getNickname()));
		
		return view;
	}

	private View fan(View view,  final TVProgramFan tvpfan) {
		FanViewHolder viewHolder;
		if (view == null) {
			viewHolder = new FanViewHolder();
			view = inflater.inflate(R.layout.topfan_row_fan, null);
			view.setBackgroundResource(R.drawable.fanwave_row_selector);
			viewHolder.topfan_imageview = (ImageView) view.findViewById(R.id.topfan_imageview);
			viewHolder.topfan_rank_textview = (TextView) view.findViewById(R.id.topfan_rank_textview);
			viewHolder.topfan_name_textview = (TextView) view.findViewById(R.id.topfan_name_textview);
			viewHolder.credit_score_textview = (TextView) view.findViewById(R.id.credit_score_textview);
			viewHolder.waveins_score_textview = (TextView) view.findViewById(R.id.waveins_score_textview);
			viewHolder.comments_score_textview = (TextView) view.findViewById(R.id.comments_score_textview);	
			view.setTag(viewHolder);
		} else
			viewHolder = (FanViewHolder) view.getTag();
				
		viewHolder.topfan_imageview.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.avatar_loading));
		loadAvatar(viewHolder.topfan_imageview, tvpfan.getUsername());
		viewHolder.topfan_name_textview.setText(tvpfan.getNickname());
		
		viewHolder.topfan_rank_textview.setText(tvpfan.getRank()+"");
		viewHolder.credit_score_textview.setText(tvpfan.getPoint()+"");
		viewHolder.waveins_score_textview.setText(tvpfan.getWaveinCount()+"");
		viewHolder.comments_score_textview.setText(tvpfan.getCommentCount()+"");
		
		view.setOnClickListener(getAvatarImageClickedListener(tvpfan.getUsername(), tvpfan.getNickname()));
		return view;
	}
	
	private View more(View view) {
		final MoreViewHolder viewHolder;
		if (view == null) {
			viewHolder = new MoreViewHolder();
			view = inflater.inflate(R.layout.topfan_row_more, null);
			view.setBackgroundResource(R.drawable.fanwave_row_selector);
			viewHolder.topfan_more_textview = (TextView) view.findViewById(R.id.topfan_more_textview);
			viewHolder.loading_indicator = (LinearLayout) view.findViewById(R.id.loading_indicator);
			view.setTag(viewHolder);
		} else
			viewHolder = (MoreViewHolder) view.getTag();
		
		viewHolder.topfan_more_textview.setVisibility(View.VISIBLE);
		viewHolder.loading_indicator.setVisibility(View.GONE);
		
		view.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if(viewHolder.loading_indicator.getVisibility() == View.GONE){
					viewHolder.topfan_more_textview.setVisibility(View.GONE);
					viewHolder.loading_indicator.setVisibility(View.VISIBLE);
					new Thread ( new Runnable () {
						public void run () {
							if( number == 5){
								number = 100;
								topfanlist = getProgramFan(ProgramManager.getProgramFans(AccountManager.getCurrentUser().getUsername(), pgid, number));
							}else{
								number = number+100;
								topfanlist = getProgramFan(ProgramManager.getProgramFans(AccountManager.getCurrentUser().getUsername(), pgid, number));
							}
							if(_context!=null)
								((Activity) _context).runOnUiThread( new Runnable () {
									public void run () {
										notifyDataSetChanged();
									}
								});
							else{
								if(topfanlist!=null){
									topfanlist.clear();
									topfanlist = null;
								}
							}
								
						}
					}).start();
					
				
					
				}

			}
		});
		return view;
	}

	private class TopFanViewHolder {
		ImageView 	topfan_imageview;
		TextView 	topfan_name_textview;
		TextView 	credit_score_textview;
		TextView 	waveins_score_textview;
		TextView 	comments_score_textview;
	}
	
	private class FanViewHolder  extends TopFanViewHolder{
		TextView 	topfan_rank_textview;
	}

	private class MoreViewHolder {
		TextView 	topfan_more_textview;
		LinearLayout 	loading_indicator;
	}
	
	/**
	 * Get callback for avatar image.
	 * @param username
	 * @return View.OnClickListener
	 */
	private View.OnClickListener getAvatarImageClickedListener (final String username, final String nickname) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(_context, PersonalActivity.class);
				i.putExtra("username", username);
				i.putExtra("nickname", nickname);
				i.putExtra("back_image", BitmapFactory.decodeResource(ApplicationManager.getAppContext().getResources(),
																	  R.drawable.topfan_icon));
				_context.startActivity(i);
			}
		};
	}
	
	private ArrayList<TVProgramFan> getProgramFan(String response){
		ArrayList<TVProgramFan> fanlist = new ArrayList<TVProgramFan> ();

		try {
			JSONObject obj = new JSONObject(response);
			if(obj.has("rank")){
				JSONArray arrayFan = new JSONArray(obj.getString("rank"));
				int len = arrayFan.length();

				for (int i = 0; i < len; i++) {
					TVProgramFan fan = new TVProgramFan(arrayFan.getJSONObject(i));
					fanlist.add(fan);
				}
			}
			
			if(fanlist.size() == number+1){
				more_topfan = true;
				fanlist.remove(number);
			}else{
				more_topfan = false;
			}
			
			if(obj.has("me")){
				JSONObject meobj = new JSONObject(obj.getString("me"));
				TVProgramFan fan = new TVProgramFan(meobj);

				if(fan.getRank() > number)
					fanlist.add(fan);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return fanlist;

	}
	
	
	/**
	 * Image Process Methods
	 */
	
	/**
	 * Load avatar.
	 * @param iv
	 * @param username
	 */
	private void loadAvatar (ImageView iv, String username) {
		if (username == null || username.length() == 0)
			return;
		
		if (!ImageManager.drawAvatarImage(iv, username))
			avatar_requests.put(username, "");
	}
	
	/**
	 * Listener for listening image loading request.
	 * @author Kencool
	 *
	 */
	private class TopFanListImageListener implements ImageListener {

		@Override
		public void retrieveAvatar(final String username, final Bitmap bmp) {
			// TODO Auto-generated method stub
			if (avatar_requests == null || !avatar_requests.containsKey(username) || bmp == null)
				return;
			
			if (listview != null) {
				listview.post (new Runnable () {
					public void run () {
						if (listview == null)
							return;
						
						int firstPosition = listview.getFirstVisiblePosition();
						int lastPosition = listview.getLastVisiblePosition();
						// go through current visible rows and check if refresh is need
						//
						for (int i = firstPosition; i <= lastPosition; i++) {
							// over feed list position, may be other purpose row
							if (i == topfanlist.size())
								break;

							if (username.equals(topfanlist.get(i).getUsername())) {
								int childPosition = i - firstPosition;
								if(i!=0){
									FanViewHolder viewHolder = (FanViewHolder) listview.getChildAt(childPosition).getTag();
									viewHolder.topfan_imageview.setImageBitmap(bmp);
								}else{
									TopFanViewHolder viewHolder = (TopFanViewHolder) listview.getChildAt(childPosition).getTag();
									viewHolder.topfan_imageview.setImageBitmap(bmp);
								}

							}
						}
						
						avatar_requests.remove(username);
					}
				});
			}
		}

		@Override
		public void retrieveBadge(String badge_id, boolean scaled, Bitmap bmp) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void retrieveAttach(String token, boolean is_thumb, Bitmap bmp) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void retrieveProgramIcon(final String title, int sampleBase, Bitmap bmp) {
			
		}

		@Override
		public void retrieveChannelIcon(String title, Bitmap bmp) {
			// TODO Auto-generated method stub
			
		}
	}
}
