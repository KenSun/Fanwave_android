package com.wildmind.fanwave.feed;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.wildmind.fanwave.activity.CommentActivity;
import com.wildmind.fanwave.activity.ImagePresentActivity;
import com.wildmind.fanwave.activity.PersonalActivity;
import com.wildmind.fanwave.comment.Attachment;
import com.wildmind.fanwave.comment.Comment;
import com.wildmind.fanwave.media.AttachImageManager;
import com.wildmind.fanwave.media.ImageListener;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.util.StringGenerator;
//import com.wildmind.fanwave.activity.ImagePresentActivity;
import com.wildmind.fanwave.activity.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FeedListAdapter extends BaseAdapter {

	// wavein, comment, thumb comment, set reminder, gain badge, topfan, more
	private static final int ROW_VIEW_TYPE_COUNT = 7;
	
	private ListView feed_listview;
	
	private Context context;
	private LayoutInflater inflater;
	private ArrayList<Feed> feedlist;
	
	private FeedlistImageListener image_listener = new FeedlistImageListener();
	private ConcurrentHashMap<String, String> avatar_requests = new ConcurrentHashMap<String, String>();
	private ConcurrentHashMap<String, String> badge_requests = new ConcurrentHashMap<String, String>();
	private ConcurrentHashMap<String, String> attach_requests = new ConcurrentHashMap<String, String>();
	
	private boolean avatar_clickable = true;
	private boolean show_more = false;
	
	/**
	 * Constructor
	 * @param listview
	 * @param list
	 * @param context
	 */
	public FeedListAdapter (ListView listview, ArrayList<Feed> list, Context context) {
		this.feed_listview = listview;
		this.feedlist = list;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		
		ImageManager.addImageListener(image_listener);
	}
	
	/**
	 * Set avatar clickable or not. Not effective for current visible rows.
	 * @param clickable
	 */
	public void setAvatarClickable (boolean clickable) {
		this.avatar_clickable = clickable;
	}
	
	public void setMore (boolean is_more) {
		this.show_more = is_more;
	}
	
	/**
	 * Clear all resources.
	 */
	public void clear () {
		ImageManager.removeImageListener(image_listener);
		image_listener = null;
		
		feed_listview = null;
		context = null;
		inflater = null;
		feedlist = null;
		
		avatar_requests.clear();
		avatar_requests = null;
		badge_requests.clear();
		badge_requests = null;
		attach_requests.clear();
		attach_requests = null;
	}
	
	/**
	 * List View Resources Methods
	 */
	@Override
	public int getCount() {
		return feedlist != null ? feedlist.size() + (show_more ? 1 : 0) : 0;
	}

	@Override
	public Object getItem(int position) {
		return feedlist != null ? (position < feedlist.size() ? feedlist.get(position) : null) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public boolean isEnabled (int position) {
		return position < feedlist.size();
	}
	
	@Override
	public int getViewTypeCount () {
		return ROW_VIEW_TYPE_COUNT;
	}
	
	@Override
	public int getItemViewType (int position) {
		// more row
		if (position == feedlist.size())
			return 6;
		
		// feed row
		String feedType = feedlist.get(position).getType();
		if (feedType.equals(WaveinFeed.TYPE))
			return 0;
		else if (feedType.equals(CommentFeed.TYPE))
			return 1;
		else if (feedType.equals(ThumbCommentFeed.TYPE))
			return 2;	
		else if (feedType.equals(SetReminderFeed.TYPE))
			return 3;
		else if (feedType.equals(GainBadgeFeed.TYPE))
			return 4;
		else if (feedType.equals(TopfanFeed.TYPE))
			return 5;
		else
			return IGNORE_ITEM_VIEW_TYPE;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// more row
		if (position == feedlist.size())
			return getMoreView(convertView, parent);
		
		// get feed and feed type
		Feed feed = feedlist.get(position);
		String feedType = feed.getType();
		
		// get feed view
		if (feedType.equals(WaveinFeed.TYPE)) 
			return getWaveinView((WaveinFeed) feed, convertView, position);
		
		else if (feedType.equals(CommentFeed.TYPE))
			return getCommentView((CommentFeed) feed, convertView, position);
			
		else if (feedType.equals(ThumbCommentFeed.TYPE))
			return getThumbCommentView((ThumbCommentFeed) feed, convertView, position);
			
		else if (feedType.equals(SetReminderFeed.TYPE))
			return getSetReminderView((SetReminderFeed) feed, convertView, position);
			
		else if (feedType.equals(GainBadgeFeed.TYPE))
			return getGainBadgeView((GainBadgeFeed) feed, convertView, position);
			
		else if (feedType.equals(TopfanFeed.TYPE))
			return getTopfanView((TopfanFeed) feed, convertView, position);
			
		else
			return null;
	}
	
	private View getMoreView (View view, ViewGroup parent) {
		MoreViewHolder viewHolder;
		if (view == null) {
			view = inflater.inflate(R.layout.row_more, null);
			viewHolder = new MoreViewHolder();
			viewHolder.loading = (ProgressBar) view.findViewById(R.id.loading_progressbar);
			viewHolder.no_more = (TextView) view.findViewById(R.id.no_more_textview);
			viewHolder.no_more.setText(R.string.waves_no_more);
			view.setTag(viewHolder);
		} else
			viewHolder = (MoreViewHolder) view.getTag();
		
		viewHolder.loading.setVisibility(show_more ? View.VISIBLE : View.GONE);
		viewHolder.no_more.setVisibility(show_more ? View.GONE : View.VISIBLE);
		
		return view;
	}

	/**
	 * Get view for wave in feed.
	 * @param feed
	 * @return View
	 */
	private View getWaveinView (WaveinFeed feed, View view, int position) {
		WaveinViewHolder viewHolder;
		if (view == null) {
			view = inflater.inflate(R.layout.feed_row_wavein, null);
			viewHolder = new WaveinViewHolder();
			viewHolder.avatar = (ImageView) view.findViewById(R.id.avatar_imageview);
			viewHolder.nickname = (TextView) view.findViewById(R.id.nickname_textview);
			viewHolder.program_title = (TextView) view.findViewById(R.id.program_title_textview);
			viewHolder.created_time = (TextView) view.findViewById(R.id.created_time_textview);
			view.setTag(viewHolder);
		} else
			viewHolder = (WaveinViewHolder) view.getTag();
		
		// Avatar
		viewHolder.avatar.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.avatar_loading));
		if(avatar_clickable)
			viewHolder.avatar.setOnClickListener(getAvatarImageClickedListener(feed.getOwner(), feed.getNickname()));
		loadAvatar(viewHolder.avatar, feed.getOwner());
		
		// Nickname
		viewHolder.nickname.setText(feed.getNickname());
		
		// Program Title
		viewHolder.program_title.setText("\""+ feed.getProgram().getTitle() + "\"");
		
		// Created Time
		viewHolder.created_time.setText(StringGenerator.distinctTimeStringFromTimeString(feed.getCreatedTime()));
		
		return view;
	}
	
	/**
	 * Get view for comment feed.
	 * @param feed
	 * @return View
	 */
	private View getCommentView (CommentFeed feed, View view, int position) {
		CommentViewHolder viewHolder;
		if (view == null) {
			viewHolder = new CommentViewHolder();
			view = inflater.inflate(R.layout.feed_row_comment, null);
			viewHolder.avatar = (ImageView) view.findViewById(R.id.avatar_imageview);
			viewHolder.attach_image = (ImageView) view.findViewById(R.id.attach_image_imageview);
			viewHolder.nickname = (TextView) view.findViewById(R.id.nickname_textview);
			viewHolder.content = (TextView) view.findViewById(R.id.content_textview);
			viewHolder.attach_link = (TextView) view.findViewById(R.id.attach_link_textview);
			viewHolder.like = (TextView) view.findViewById(R.id.like_count_textview);
			viewHolder.dislike = (TextView) view.findViewById(R.id.dislike_count_textview);
			viewHolder.program_title = (TextView) view.findViewById(R.id.program_title_textview);
			viewHolder.created_time = (TextView) view.findViewById(R.id.created_time_textview);
			viewHolder.cloud = (Button) view.findViewById(R.id.cloud_button);
			view.setTag(viewHolder);
		} else
			viewHolder = (CommentViewHolder) view.getTag();
		
		// Avatar
		viewHolder.avatar.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.avatar_loading));
		if(avatar_clickable)
			viewHolder.avatar.setOnClickListener(getAvatarImageClickedListener(feed.getOwner(), feed.getNickname()));
		loadAvatar(viewHolder.avatar, feed.getOwner());
		
		// Nickname
		viewHolder.nickname.setText(feed.getNickname());
		
		// Content
		viewHolder.content.setText(feed.getComment().getContent());
		
		// Attachment
		if (feed.getComment().getAttaches().size() > 0) {
			Attachment attach = feed.getComment().getAttaches().get(0);
			String attachType = attach.getType();
			
			if (attachType.equals(Attachment.ATTACH_LINK)) {
				// Attach Link
				viewHolder.attach_link.setText(attach.getUrl());
				Linkify.addLinks(viewHolder.attach_link, Linkify.ALL);
				viewHolder.attach_link.setLinksClickable(true);
				viewHolder.attach_link.setFocusable(false);
				viewHolder.attach_link.setVisibility(View.VISIBLE);
				viewHolder.attach_image.setVisibility(View.GONE);
				
			} else if (attachType.equals(Attachment.ATTACH_IMAGE)) {
				// Attach Image
				viewHolder.attach_image.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.attach_default));
				loadAttach(viewHolder.attach_image, attach.getToken(), true);
				viewHolder.attach_image.setOnClickListener(getAttachImageClickedListener(feed.getComment()));
				viewHolder.attach_image.setVisibility(View.VISIBLE);
				viewHolder.attach_link.setVisibility(View.GONE);
				
			} else {
				viewHolder.attach_image.setVisibility(View.GONE);
				viewHolder.attach_link.setVisibility(View.GONE);
			}
		} else {
			viewHolder.attach_image.setVisibility(View.GONE);
			viewHolder.attach_link.setVisibility(View.GONE);
		}
		
		// like, dislike
		viewHolder.like.setText("(" + feed.getComment().getLikeCount() + ")");
		viewHolder.dislike.setText("(" + feed.getComment().getDislikeCount() + ")");
		
		// Program Title
		viewHolder.program_title.setText(feed.getProgram().getTitle());
		
		// Created Time
		viewHolder.created_time.setText(StringGenerator.distinctTimeStringFromTimeString(feed.getCreatedTime()));
		
		// Cloud Button
		viewHolder.cloud.setOnClickListener(getCloudClickedListener(feed.getComment()));
		
		return view;
	}
	
	/**
	 * Get view for thumb comment feed.
	 * @param feed
	 * @return View
	 */
	private View getThumbCommentView (ThumbCommentFeed feed, View view, int position) {
		ThumbCommentViewHolder viewHolder;
		if (view == null) {
			viewHolder = new ThumbCommentViewHolder();
			view = inflater.inflate(R.layout.feed_row_thumb_comment, null);
			viewHolder.avatar = (ImageView) view.findViewById(R.id.avatar_imageview);
			viewHolder.attach_image = (ImageView) view.findViewById(R.id.attach_image_imageview);
			viewHolder.nickname = (TextView) view.findViewById(R.id.nickname_textview);
			viewHolder.liked_nickname = (TextView) view.findViewById(R.id.liked_nickname_textview);
			viewHolder.content = (TextView) view.findViewById(R.id.content_textview);
			viewHolder.attach_link = (TextView) view.findViewById(R.id.attach_link_textview);
			viewHolder.like = (TextView) view.findViewById(R.id.like_count_textview);
			viewHolder.dislike = (TextView) view.findViewById(R.id.dislike_count_textview);
			viewHolder.program_title = (TextView) view.findViewById(R.id.program_title_textview);
			viewHolder.created_time = (TextView) view.findViewById(R.id.created_time_textview);
			viewHolder.cloud = (Button) view.findViewById(R.id.cloud_button);
			view.setTag(viewHolder);
		} else
			viewHolder = (ThumbCommentViewHolder) view.getTag();
		
		// Avatar
		viewHolder.avatar.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.avatar_loading));
		if(avatar_clickable)
			viewHolder.avatar.setOnClickListener(getAvatarImageClickedListener(feed.getOwner(), feed.getNickname()));
		loadAvatar(viewHolder.avatar, feed.getOwner());
				
		// Nickname
		viewHolder.nickname.setText(feed.getNickname());
		
		// Liked Nickname
		viewHolder.liked_nickname.setText(feed.getComment().getNickname());
		
		// Content
		viewHolder.content.setText(feed.getComment().getContent());
				
		// Attachment
		if (feed.getComment().getAttaches().size() > 0) {
			Attachment attach = feed.getComment().getAttaches().get(0);
			String attachType = attach.getType();
					
			if (attachType.equals(Attachment.ATTACH_LINK)) {
				// Attach Link
				viewHolder.attach_link.setText(attach.getUrl());
				Linkify.addLinks(viewHolder.attach_link, Linkify.ALL);
				viewHolder.attach_link.setLinksClickable(true);
				viewHolder.attach_link.setFocusable(false);
				viewHolder.attach_link.setVisibility(View.VISIBLE);
				viewHolder.attach_image.setVisibility(View.GONE);
						
			} else if (attachType.equals(Attachment.ATTACH_IMAGE)) {
				// Attach Image
				viewHolder.attach_image.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.attach_default));
				loadAttach(viewHolder.attach_image, attach.getToken(), true);
				viewHolder.attach_image.setOnClickListener(getAttachImageClickedListener(feed.getComment()));
				viewHolder.attach_image.setVisibility(View.VISIBLE);
				viewHolder.attach_link.setVisibility(View.GONE);
				
			} else {
				viewHolder.attach_image.setVisibility(View.GONE);
				viewHolder.attach_link.setVisibility(View.GONE);
			}
		} else {
			viewHolder.attach_image.setVisibility(View.GONE);
			viewHolder.attach_link.setVisibility(View.GONE);
		}
		
		// like, dislike
		viewHolder.like.setText("(" + feed.getComment().getLikeCount() + ")");
		viewHolder.dislike.setText("(" + feed.getComment().getDislikeCount() + ")");
				
		// Program Title
		viewHolder.program_title.setText(feed.getProgram().getTitle());
				
		// Created Time
		viewHolder.created_time.setText(StringGenerator.distinctTimeStringFromTimeString(feed.getCreatedTime()));
				
		// Cloud Button
		viewHolder.cloud.setOnClickListener(getCloudClickedListener(feed.getComment()));
				
		return view;
	}
	
	/**
	 * Get view for set reminder feed.
	 * @param feed
	 * @return  View
	 */
	private View getSetReminderView (SetReminderFeed feed, View view, int position) {
		SetReminderViewHolder viewHolder;
		if (view == null) {
			view = inflater.inflate(R.layout.feed_row_set_reminder, null);
			viewHolder = new SetReminderViewHolder();
			viewHolder.avatar = (ImageView) view.findViewById(R.id.avatar_imageview);
			viewHolder.nickname = (TextView) view.findViewById(R.id.nickname_textview);
			viewHolder.program_title = (TextView) view.findViewById(R.id.program_title_textview);
			viewHolder.created_time = (TextView) view.findViewById(R.id.created_time_textview);
			view.setTag(viewHolder);
		} else
			viewHolder = (SetReminderViewHolder) view.getTag();
		
		// Avatar
		viewHolder.avatar.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.avatar_loading));
		if(avatar_clickable)
			viewHolder.avatar.setOnClickListener(getAvatarImageClickedListener(feed.getOwner(), feed.getNickname()));
		loadAvatar(viewHolder.avatar, feed.getOwner());
				
		// Nickname
		viewHolder.nickname.setText(feed.getNickname());
				
		// Program Title
		viewHolder.program_title.setText(" \"" + feed.getProgram().getTitle() + "\"");
				
		// Created Time
		viewHolder.created_time.setText(StringGenerator.distinctTimeStringFromTimeString(feed.getCreatedTime()));
				
		return view;
	}
	
	/**
	 * Get view for gain badge feed.
	 * @param feed
	 * @return View
	 */
	private View getGainBadgeView (GainBadgeFeed feed, View view, int position) {
		GainBadgeViewHolder viewHolder;
		if (view == null) {
			view = inflater.inflate(R.layout.feed_row_gain_badge, null);
			viewHolder = new GainBadgeViewHolder();
			viewHolder.avatar = (ImageView) view.findViewById(R.id.avatar_imageview);
			viewHolder.badge_image = (ImageView) view.findViewById(R.id.badge_imageview);
			viewHolder.program_small_icon = (ImageView) view.findViewById(R.id.program_imageview);
			viewHolder.nickname = (TextView) view.findViewById(R.id.nickname_textview);
			viewHolder.badge_title = (TextView) view.findViewById(R.id.badge_title_textview);
			viewHolder.created_time = (TextView) view.findViewById(R.id.created_time_textview);
			viewHolder.program_title = (TextView) view.findViewById(R.id.program_title_textview);
			view.setTag(viewHolder);
		} else
			viewHolder = (GainBadgeViewHolder) view.getTag();
		
		// Avatar
		viewHolder.avatar.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.avatar_loading));
		if(avatar_clickable)
			viewHolder.avatar.setOnClickListener(getAvatarImageClickedListener(feed.getOwner(), feed.getNickname()));
		loadAvatar(viewHolder.avatar, feed.getOwner());
						
		// Nickname
		viewHolder.nickname.setText(feed.getNickname());
		
		// Badge Title
		viewHolder.badge_title.setText(" \""+ feed.getBadgeTitle() + "\" ");
		
		// Badge Image
		viewHolder.badge_image.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.badge_loading));
		loadBadge(viewHolder.badge_image, feed.getBadgeId());
						
		// Created Time
		viewHolder.created_time.setText(StringGenerator.distinctTimeStringFromTimeString(feed.getCreatedTime()));
				
		// Program Small Icon, Program Title
		String programTitle = feed.getProgram().getTitle();
		viewHolder.program_small_icon.setVisibility(programTitle.length() > 0 ? View.VISIBLE : View.INVISIBLE);
		viewHolder.program_title.setText((programTitle.length() > 0 ? programTitle : ""));
				
		return view;
	}
	
	/**
	 * Get view for top fan feed.
	 * @param feed
	 * @return View
	 */
	private View getTopfanView (TopfanFeed feed, View view, int position) {
		TopfanViewHolder viewHolder;
		if (view == null) {
			view = inflater.inflate(R.layout.feed_row_topfan, null);
			viewHolder = new TopfanViewHolder();
			viewHolder.avatar = (ImageView) view.findViewById(R.id.avatar_imageview);
			viewHolder.topfan_image = (ImageView) view.findViewById(R.id.topfan_imageview);
			viewHolder.nickname = (TextView) view.findViewById(R.id.nickname_textview);
			viewHolder.program_title = (TextView) view.findViewById(R.id.program_title_textview);
			viewHolder.created_time = (TextView) view.findViewById(R.id.created_time_textview);
			view.setTag(viewHolder);
		} else
			viewHolder = (TopfanViewHolder) view.getTag();
		
		// Avatar
		viewHolder.avatar.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.avatar_loading));
		if(avatar_clickable)
			viewHolder.avatar.setOnClickListener(getAvatarImageClickedListener(feed.getOwner(), feed.getNickname()));
		loadAvatar(viewHolder.avatar, feed.getOwner());
								
		// Nickname
		viewHolder.nickname.setText(feed.getNickname());
								
		// Program Title
		viewHolder.program_title.setText(" \""+ feed.getProgram().getTitle() + "\"");
		
		// Topfan Image
		viewHolder.topfan_image.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.badge_loading));
		loadBadge(viewHolder.topfan_image, feed.getBadgeId());
								
		// Created Time
		viewHolder.created_time.setText(StringGenerator.distinctTimeStringFromTimeString(feed.getCreatedTime()));
		
		return view;
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
				Intent i = new Intent(context, PersonalActivity.class);
				i.putExtra("username", username);
				i.putExtra("nickname", nickname);
				i.putExtra("back_image", BitmapFactory.decodeResource(context.getResources(), R.drawable.waves_icon));
				context.startActivity(i);
			}
		};
	}
	
	/**
	 * Get callback for cloud.
	 * @param cm
	 * @return View.OnClickListener
	 */
	private View.OnClickListener getCloudClickedListener (final Comment cm) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, CommentActivity.class);
				i.putExtra("comment_id", cm.getId());
				i.putExtra("back_image", BitmapFactory.decodeResource(context.getResources(), R.drawable.waves_icon));
				context.startActivity(i);
			}
		};
	}
	
	/**
	 * Get callback for attached image.
	 * @param attach
	 * @return View.OnClickListener
	 */
	private View.OnClickListener getAttachImageClickedListener (final Comment cm) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, ImagePresentActivity.class);
				i.putExtra("present_type", ImagePresentActivity.ATTACH_IMAGE_PRESENT);
				i.putExtra("attachment_description", cm.getContent());
				i.putExtra("attachment", cm.getAttaches().get(0));
				context.startActivity(i);
			}
		};
	}
	
	/**
	 * Refresh data.
	 * @param list
	 */
	public void refreshData (ArrayList<Feed> list, boolean is_more) {
		feedlist = list;
		show_more = is_more;
		notifyDataSetChanged();
	}
	
	/**
	 * Convert view holders.
	 * @author Kencool
	 *
	 */
	private class MoreViewHolder {
		ProgressBar	loading;
		TextView	no_more;
	}
	
	private class FeedViewHolder {
		ImageView 	avatar;
		TextView 	nickname;
		TextView 	program_title;
		TextView 	created_time;
	}
	private class WaveinViewHolder extends FeedViewHolder {
		
	}
	private class CommentViewHolder extends FeedViewHolder {
		ImageView	attach_image;
		TextView	content;
		TextView	attach_link;
		TextView	like;
		TextView	dislike;
		Button		cloud;
	}
	private class ThumbCommentViewHolder extends FeedViewHolder {
		ImageView	attach_image;
		TextView	liked_nickname;
		TextView	content;
		TextView	attach_link;
		TextView	like;
		TextView	dislike;
		Button		cloud;
	}
	private class SetReminderViewHolder extends FeedViewHolder {
		
	}
	private class GainBadgeViewHolder extends FeedViewHolder {
		ImageView	program_small_icon;
		ImageView	badge_image;
		TextView	badge_title;
	}
	private class TopfanViewHolder extends FeedViewHolder {
		ImageView	topfan_image;
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
	 * Load badge.
	 * @param iv
	 * @param badge_id
	 */
	private void loadBadge (ImageView iv, String badge_id) {
		if (badge_id == null || badge_id.length() == 0)
			return;
		
		if (!ImageManager.drawBadgeImage(iv, badge_id, true))
			badge_requests.put(badge_id, "");
	}
	
	/**
	 * Load attach image.
	 * @param iv
	 * @param token
	 * @param is_thumb
	 */
	private void loadAttach (ImageView iv, String token, boolean is_thumb) {
		if (token == null || token.length() == 0)
			return;
		
		if (!ImageManager.drawAttachImage(iv, token, is_thumb))
			attach_requests.put(AttachImageManager.buildAttachImageKey(token, is_thumb), "");
	}
	
	/**
	 * Draw badge.
	 * @param badge_id
	 * @param bmp
	 */
	private void drawBadge(String badge_id, Bitmap bmp) {
		for (int i = feed_listview.getFirstVisiblePosition(); 
				 i <= feed_listview.getLastVisiblePosition(); 
				 i++) {
				// over feed list position, may be other purpose row
				if (i >= feedlist.size())
					break;
				
				if (feedlist.get(i).getType().equals(GainBadgeFeed.TYPE)) {
					GainBadgeFeed feed = (GainBadgeFeed) feedlist.get(i);
					if(badge_id.equals(feed.getBadgeId())) {
						int childPosition = i - feed_listview.getFirstVisiblePosition();
						GainBadgeViewHolder viewHolder = 
								(GainBadgeViewHolder) feed_listview.getChildAt(childPosition).getTag();
						viewHolder.badge_image.setImageBitmap(bmp);
					}
				} else if (feedlist.get(i).getType().equals(TopfanFeed.TYPE)) {
					TopfanFeed feed = (TopfanFeed) feedlist.get(i);
					if(badge_id.equals(feed.getBadgeId())) {
						int childPosition = i - feed_listview.getFirstVisiblePosition();
						TopfanViewHolder viewHolder = 
								(TopfanViewHolder) feed_listview.getChildAt(childPosition).getTag();
						viewHolder.topfan_image.setImageBitmap(bmp);
					}
				}
			}
	}
	
	/**
	 * Draw attach image.
	 * @param key
	 * @param bmp
	 */
	private void drawAttachImage (String key, Bitmap bmp) {
		int firstPosition = feed_listview.getFirstVisiblePosition(); 
		int lastPosition = feed_listview.getLastVisiblePosition(); 
		for (int i = firstPosition; i <= lastPosition; i++) {
				// over feed list position, may be other purpose row
				if (i >= feedlist.size())
					break;
				
				Attachment attach = null;
				if (feedlist.get(i).getType().equals(CommentFeed.TYPE)) {
					CommentFeed feed = (CommentFeed) feedlist.get(i);
					if (feed.getComment().getAttaches().size() > 0) 
						attach = feed.getComment().getAttaches().get(0);
					
					if (attach != null && 
						attach.getType().equals(Attachment.ATTACH_IMAGE) &&
						key.equals(AttachImageManager.buildAttachImageKey(attach.getToken(), true))) {
						int childPosition = i - firstPosition;
						CommentViewHolder viewHolder = 
								(CommentViewHolder) feed_listview.getChildAt(childPosition).getTag();
						viewHolder.attach_image.setImageBitmap(bmp);
					} 

				} else if (feedlist.get(i).getType().equals(ThumbCommentFeed.TYPE)) {
					ThumbCommentFeed feed = (ThumbCommentFeed) feedlist.get(i);
					if (feed.getComment().getAttaches().size() > 0) 
						attach = feed.getComment().getAttaches().get(0);
					
					if (attach != null && 
						attach.getType().equals(Attachment.ATTACH_IMAGE) &&
						key.equals(AttachImageManager.buildAttachImageKey(attach.getToken(), true))) {
						int childPosition = i - firstPosition;
						ThumbCommentViewHolder viewHolder = 
								(ThumbCommentViewHolder) feed_listview.getChildAt(childPosition).getTag();
						viewHolder.attach_image.setImageBitmap(bmp);
					}
				}
			}
	}
	
	/**
	 * Listener for listening image loading requests.
	 * @author Kencool
	 *
	 */
	private class FeedlistImageListener implements ImageListener {

		public void retrieveAvatar(final String username, final Bitmap bmp) {
			if (avatar_requests == null || !avatar_requests.containsKey(username) || bmp == null)
				return;
			
			if (feed_listview != null) {
				feed_listview.post( new Runnable () {
					public void run () {
						if (feed_listview == null)
							return;
						
						int firstPosition = feed_listview.getFirstVisiblePosition();
						int lastPosition = feed_listview.getLastVisiblePosition();
						// go through current visible rows and check if refresh is need
						//
						for (int i = firstPosition; i <= lastPosition; i++) {
							// over feed list position, may be other purpose row
							if (i >= feedlist.size())
								break;
							
							if (username.equals(feedlist.get(i).getOwner())) {
								int childPosition = i - firstPosition;
								FeedViewHolder viewHolder = 
										(FeedViewHolder) feed_listview.getChildAt(childPosition).getTag();
								viewHolder.avatar.setImageBitmap(bmp);
							}
						}
						
						avatar_requests.remove(username);
					}
				});
			}
		}

		public void retrieveBadge(final String badge_id, boolean scaled, final Bitmap bmp) {
			if (badge_requests == null || !badge_requests.containsKey(badge_id) || !scaled || bmp == null)
				return;
			
			if (feed_listview != null) {
				feed_listview.post(new Runnable () {
					public void run () {
						if (feed_listview == null)
							return;
						
						drawBadge(badge_id, bmp);
						badge_requests.remove(badge_id);
					}
				});
			}
		}

		public void retrieveAttach(final String token, final boolean is_thumb, final Bitmap bmp) {
			final String key = AttachImageManager.buildAttachImageKey(token, is_thumb);
			if (attach_requests == null || !attach_requests.containsKey(key) || bmp == null)
				return;
			
			if (feed_listview != null) {
				feed_listview.post(new Runnable () {
					public void run () {
						if (feed_listview == null)
							return;
						
						drawAttachImage(key, bmp);
						attach_requests.remove(key);
					}
				});
			}
		}

		@Override
		public void retrieveProgramIcon(String title, int sampleBase, Bitmap bmp) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void retrieveChannelIcon(String title, Bitmap bmp) {
			// TODO Auto-generated method stub
			
		}
	}
}
