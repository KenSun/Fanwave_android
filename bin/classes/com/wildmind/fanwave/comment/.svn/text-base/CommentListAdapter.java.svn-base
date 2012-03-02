package com.wildmind.fanwave.comment;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.wildmind.fanwave.activity.CommentActivity;
import com.wildmind.fanwave.activity.ImagePresentActivity;
import com.wildmind.fanwave.activity.PersonalActivity;
import com.wildmind.fanwave.media.AttachImageManager;
import com.wildmind.fanwave.media.ImageListener;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.media.ImageManager.SampleBase;
import com.wildmind.fanwave.media.ProgramIconManager;
import com.wildmind.fanwave.util.StringGenerator;
import com.wildmind.fanwave.activity.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

public class CommentListAdapter extends BaseAdapter {

	// comment, more
	private static final int ROW_VIEW_TYPE_COUNT = 7;
	
	private ListView comment_listview;
	
	private Context context;
	private LayoutInflater inflater;

	private String program_title = null;
	private String chat_jid = null;
	private ArrayList<Comment> comments = null;
	private CommentListListener listener = null;
	private CommentListImageListener image_listener = new CommentListImageListener();
	private ConcurrentHashMap<String, String> avatar_requests = new ConcurrentHashMap<String, String>();
	private ConcurrentHashMap<String, String> attach_requests = new ConcurrentHashMap<String, String>();
	
	private boolean show_more = false;

	public CommentListAdapter (ListView listview, String programTitle, ArrayList<Comment> list, 
			Context context) {
		this.comment_listview = listview;
		this.program_title = programTitle;
		this.comments = list;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		
		ImageManager.addImageListener(image_listener);
	}
	
	public void setChatJid (String chatJid) {
		this.chat_jid = chatJid;
	}
	public void setMore (boolean is_more) {
		this.show_more = is_more;
	}
	public void setCommentListListener (CommentListListener listener) {
		this.listener = listener;
	}
	
	/**
	 * Clean up all resources.
	 */
	public void clear() {
		ImageManager.removeImageListener(image_listener);
		image_listener = null;
		
		comment_listview = null;
		context = null;
		inflater = null;
		comments = null;
		listener = null;
		
		avatar_requests.clear();
		avatar_requests = null;
		attach_requests.clear();
		attach_requests = null;
	}

	@Override
	public int getCount() {
		return comments != null ? comments.size() + (show_more ? 1 : 0) : 0;
	}

	@Override
	public Object getItem(int position) {
		return comments != null ? (position < comments.size() ? comments.get(position) : null) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public boolean areAllItemsEnabled () {
		return false;
	}
	
	@Override
	public boolean isEnabled (int position) {
		return false;
	}
	
	@Override
	public int getViewTypeCount () {
		return ROW_VIEW_TYPE_COUNT;
	}
	
	@Override
	public int getItemViewType (int position) {
		// more row
		if (position == comments.size())
			return 1;
		
		return 0;
	}
	
	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		// more row
		if (position == comments.size())
			return getMoreView (convertView, parent);
		
		// comment row
		return getCommentView(position, convertView, parent);
	}
	
	public View getMoreView (View view, ViewGroup parent) {
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

	public View getCommentView (int position, View view, ViewGroup parent) {
		Comment cm = comments.get(position);
		CommentViewHolder viewHolder;
		if (view == null) {
			viewHolder = new CommentViewHolder();
			view = inflater.inflate(R.layout.program_comment_row, null);
			viewHolder.avatar = (ImageView) view.findViewById(R.id.avatar_imageview);
			viewHolder.attach_image = (ImageView) view.findViewById(R.id.attach_image_imageview);
			viewHolder.nickname = (TextView) view.findViewById(R.id.nickname_textview);
			viewHolder.content = (TextView) view.findViewById(R.id.content_textview);
			viewHolder.attach_link = (TextView) view.findViewById(R.id.attach_link_textview);
			viewHolder.like = (TextView) view.findViewById(R.id.like_count_textview);
			viewHolder.dislike = (TextView) view.findViewById(R.id.dislike_count_textview);
			viewHolder.created_time = (TextView) view.findViewById(R.id.created_time_textview);
			viewHolder.cloud = (Button) view.findViewById(R.id.cloud_button);
			view.setTag(viewHolder);
		} else
			viewHolder = (CommentViewHolder) view.getTag();

		// Avatar
		viewHolder.avatar.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.avatar_loading));
		loadAvatar(viewHolder.avatar, cm.getOwner());
		viewHolder.avatar.setOnClickListener(getAvatarImageClickedListener(cm.getOwner(), cm.getNickname()));

		// Nickname
		viewHolder.nickname.setText(cm.getNickname());
		viewHolder.nickname.setOnClickListener(getNicknameClickedListener(cm.getNickname()));

		// Content
		viewHolder.content.setText(cm.getContent());

		// Attachment
		if (cm.getAttaches().size() > 0) {
			Attachment attach = cm.getAttaches().get(0);
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
				viewHolder.attach_image.setOnClickListener(getAttachImageClickedListener(cm));
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
		viewHolder.like.setText("(" + cm.getLikeCount() + ")");
		viewHolder.dislike.setText("(" + cm.getDislikeCount() + ")");

		// Created Time
		viewHolder.created_time.setText(StringGenerator.dateStringFromTimeStringWithSeconds(cm.getCreatedTime()));

		// Cloud Button
		viewHolder.cloud.setOnClickListener(getCloudClickedListener(cm));

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
				i.putExtra("back_image", ProgramIconManager.getProgramIconBitmap(program_title, 
							SampleBase.RIGOROUS_SAMPLED, true));
				context.startActivity(i);
			}
		};
	}
	
	private View.OnClickListener getNicknameClickedListener (final String nickname) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null)
					listener.onReply(nickname);
			}
		};
	}
	
	/**
	 * Callback for attached image.
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
	 * Callback for cloud.
	 * @param cm
	 * @return View.OnClickListener
	 */
	private View.OnClickListener getCloudClickedListener (final Comment cm) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, CommentActivity.class);
				if (chat_jid != null) 
					i.putExtra("chat_jid", chat_jid);
				i.putExtra("comment_id", cm.getId());
				i.putExtra("from_program", true);
				i.putExtra("back_image", ProgramIconManager.getProgramIconBitmap(program_title, 
							SampleBase.RIGOROUS_SAMPLED, true));
				context.startActivity(i);
			}
		};
	}
	
	/**
	 * Refresh data.
	 * @param list
	 */
	public void refreshData (ArrayList<Comment> list, boolean is_more) {
		this.comments = list;
		show_more = is_more;
		this.notifyDataSetChanged();
	}
	
	/**
	 * Refresh comment rating.
	 * @param comment_id
	 * @param is_like
	 */
	public void refreshCommentRating (String comment_id, boolean is_like) {
		int len = comments.size();
		for (int i = 0; i < len; i++) {
			Comment cm = comments.get(i);
			if (cm.getId().equals(comment_id)) {
				if (comment_listview.getFirstVisiblePosition() <= i &&
					i <= comment_listview.getLastVisiblePosition()) {
					int childPosition = i - comment_listview.getFirstVisiblePosition();
					CommentViewHolder viewHolder = 
							(CommentViewHolder) comment_listview.getChildAt(childPosition).getTag();
					if (is_like)
						viewHolder.like.setText("(" + cm.getLikeCount() + ")");
					else 
						viewHolder.dislike.setText("(" + cm.getDislikeCount() + ")");
				}
				break;
			}
		}
	}
	
	private class MoreViewHolder {
		ProgressBar	loading;
		TextView	no_more;
	}

	private class CommentViewHolder {
		ImageView avatar;
		ImageView attach_image;
		TextView nickname;
		TextView content;
		TextView attach_link;
		TextView created_time;
		TextView like;
		TextView dislike;
		Button cloud;
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
	 * Listener for listening image loading requests.
	 * @author Kencool
	 *
	 */
	private class CommentListImageListener implements ImageListener {

		@Override
		public void retrieveAvatar(final String username, final Bitmap bmp) {
			if (avatar_requests == null || !avatar_requests.containsKey(username) || bmp == null)
				return;
			
			if (comment_listview != null) {
				comment_listview.post( new Runnable () {
					public void run () {
						if (comment_listview == null)
							return;
						
						// go through current visible rows and check if refresh is need
						//
						for (int i = comment_listview.getFirstVisiblePosition(); 
							 i <= comment_listview.getLastVisiblePosition(); 
							 i++) {
							// over feed list position, may be other purpose row
							if (i >= comments.size())
								break;
							
							if (comments.get(i).getOwner().equals(username)) {
								int childPosition = i - comment_listview.getFirstVisiblePosition();
								CommentViewHolder viewHolder = 
										(CommentViewHolder) comment_listview.getChildAt(childPosition).getTag();
								viewHolder.avatar.setImageBitmap(bmp);
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
		public void retrieveAttach(String token, boolean is_thumb, final Bitmap bmp) {
			final String key = AttachImageManager.buildAttachImageKey(token, is_thumb);
			if (attach_requests == null || !attach_requests.containsKey(key) || bmp == null)
				return;
			
			((Activity) context).runOnUiThread (new Runnable () {
				public void run () {
					if (context == null)
						return;
					
					// go through current visible rows and check if refresh is need
					//
					for (int i = comment_listview.getFirstVisiblePosition(); 
						 i <= comment_listview.getLastVisiblePosition(); 
						 i++) {
						// over feed list position, may be other purpose row
						if (i >= comments.size())
							break;
						
						if (comments.get(i).getAttaches().size() == 0) 
							continue;
						
						Attachment attach = comments.get(i).getAttaches().get(0);
						
						if (attach.getType().equals(Attachment.ATTACH_IMAGE) &&
							key.equals(AttachImageManager.buildAttachImageKey(attach.getToken(), true))) {
							int childPosition = i - comment_listview.getFirstVisiblePosition();
							CommentViewHolder viewHolder = 
									(CommentViewHolder) comment_listview.getChildAt(childPosition).getTag();
							viewHolder.attach_image.setImageBitmap(bmp);
						}
					}
					
					attach_requests.remove(key);
				}
			});
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
	
	public interface CommentListListener {
		
		public void onReply (String nickname);
	}
}
