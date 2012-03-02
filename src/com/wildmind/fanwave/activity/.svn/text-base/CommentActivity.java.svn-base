package com.wildmind.fanwave.activity;


import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.animation.AlphaSetter;
import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.comment.Attachment;
import com.wildmind.fanwave.comment.Comment;
import com.wildmind.fanwave.comment.CommentInfo;
import com.wildmind.fanwave.comment.CommentManager;
import com.wildmind.fanwave.media.ImageListener;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.user.TVUser;
import com.wildmind.fanwave.util.StringGenerator;
import com.wildmind.fanwave.xmpp.FWXMPPManager;
import com.wildmind.fanwave.xmpp.muc.FWChatroomListener;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Intent input data "chat_jid" 	: String
 * 	 				 "comment_id"  	: String
 * 					 "from_program"	: boolean
 * 					 "back_image" 	: Bitmap
 * @author Eli
 *
 */

public class CommentActivity extends BaseActivity {
	
	private TextView 		comment_like_textview, comment_dislike_textview;
	private LinearLayout 	like_comment_button, dislike_comment_button;
	private LinearLayout 	comment_row_enter_program;
	private LinearLayout 	comment_layout, loading_indicator;

	private ImageView 		avatar_imageview;
	private TextView 		nickname_textview;
	private TextView 		comment_409_textview;
	private TextView 		content_textview;
	private ImageView 		attach_image_imageview;
	private TextView 		attach_link_textview;
	private TextView 		created_time_textview;
	private TextView 		comment_enter_program_textview;
	private String 			chatjid;
	private String 			commentid;
	private CommentInfo 	commentInfo;
	
	private boolean  		from_program = false, avatarOk = false, attachOk = false;
	
	private AvatarImageListener image_listener = new AvatarImageListener();
	private ProgramChatroomListener muc_listener = new ProgramChatroomListener();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// check if activity is destroyed due to abnormal situation
		if (isDestroyed())
			return;
		
		setContentView(R.layout.comment_activity);
		initData();
		initUI();
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
	
		comment_like_textview 	 = null; 
		comment_dislike_textview = null; 
		like_comment_button		 = null; 
		dislike_comment_button	 = null; 

		comment_row_enter_program = null;
		comment_layout = null;
		loading_indicator = null;
		
		avatar_imageview	= null; 
		nickname_textview   = null; 
		comment_409_textview = null;
		content_textview    = null; 
		attach_image_imageview = null; 
		attach_link_textview   = null; 
		created_time_textview  = null; 
		comment_enter_program_textview = null;
		
		if (chatjid != null && chatjid.length() > 0)
			FWXMPPManager.getMucManager().removeChatroomListener(chatjid, muc_listener);
		
		chatjid = null; 
		muc_listener = null;
		
		ImageManager.removeImageListener(image_listener);
		image_listener = null;
		commentid = null;
		commentInfo = null;
	}
	private void initData(){
		Intent i = getIntent();
		chatjid = i.getStringExtra("chat_jid");
		commentid =  i.getStringExtra("comment_id");
		from_program = i.getBooleanExtra("from_program", false);
		
		ImageManager.addImageListener(image_listener);
		if (chatjid != null && chatjid.length() > 0)
			FWXMPPManager.getMucManager().addChatroomListener(chatjid, muc_listener);
		
		
	}
	
	private void initUI(){
		initTitleBarUI();
		
		View.OnClickListener listener = getclicklistener ();
		
		like_comment_button = (LinearLayout) findViewById(R.id.like_comment_button);
		like_comment_button.setOnClickListener(listener);
		
		dislike_comment_button = (LinearLayout) findViewById(R.id.dislike_comment_button);
		dislike_comment_button.setOnClickListener(listener);
		
		comment_layout = (LinearLayout) findViewById(R.id.comment_layout);
		
		// Loading Indicator
		loading_indicator = (LinearLayout) findViewById(R.id.loading_indicator);
		
		comment_like_textview  = (TextView) findViewById(R.id.comment_like_textview);
		comment_like_textview.setText(ApplicationManager.getAppContext().getResources().getString(R.string.comment_like)+" (0)");

		comment_dislike_textview = (TextView) findViewById(R.id.comment_dislike_textview);
		comment_dislike_textview.setText(ApplicationManager.getAppContext().getResources().getString(R.string.comment_dislike)+" (0)");
		
		comment_409_textview = (TextView) findViewById(R.id.comment_409_textview);
		
		comment_row_enter_program = (LinearLayout) findViewById(R.id.comment_row_enter_program);
		comment_enter_program_textview = (TextView) findViewById(R.id.comment_enter_program_textview);
		
		if(from_program)
			comment_row_enter_program.setVisibility(View.GONE);
		else{
			comment_row_enter_program.setVisibility(View.VISIBLE);
			comment_row_enter_program.setOnClickListener(getclicklistener());
		}
		
		avatar_imageview = (ImageView) findViewById(R.id.avatar_imageview);

		nickname_textview = (TextView) findViewById(R.id.nickname_textview);
		
		content_textview = (TextView) findViewById(R.id.content_textview);

		attach_image_imageview = (ImageView) findViewById(R.id.attach_image_imageview);
		attach_link_textview = (TextView) findViewById(R.id.attach_link_textview);
		
		created_time_textview = (TextView) findViewById(R.id.created_time_textview);
		
		
		getCommentInfo(commentid);
		
		
		

	}
	
	/**
	 * Show Comment.
	 */
	private void showComment() {
		loading_indicator.setVisibility(View.GONE);
		comment_layout.setVisibility(View.VISIBLE);
		if(!commentInfo.isRated()){
			like_comment_button.setClickable(true);
			dislike_comment_button.setClickable(true);
			like_comment_button.startAnimation(new AlphaSetter(1f));
			like_comment_button.setEnabled(true);
			dislike_comment_button.startAnimation(new AlphaSetter(1f));
			dislike_comment_button.setEnabled(true);
			comment_409_textview.setVisibility(View.INVISIBLE);
		}else{
			comment_409_textview.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * Show comment loading view.
	 */
	private void showCommentLoading() {
		loading_indicator.setVisibility(View.VISIBLE);
		comment_layout.setVisibility(View.GONE);
		like_comment_button.setClickable(false);
		like_comment_button.startAnimation(new AlphaSetter(0.5f));
		like_comment_button.setEnabled(false);
		dislike_comment_button.setClickable(false);
		dislike_comment_button.startAnimation(new AlphaSetter(0.5f));
		dislike_comment_button.setEnabled(false);
	}
	
	
	
	/**
	 */
	private void setCommentView() {
		loadAvatar(avatar_imageview, commentInfo.getComment().getOwner());
		avatar_imageview.setOnClickListener(getAvatarImageClickedListener(commentInfo.getComment().getOwner(), commentInfo.getComment().getNickname()));
		comment_enter_program_textview.setText(commentInfo.getProgram().getTitle());
		nickname_textview.setText(commentInfo.getComment().getNickname());
		content_textview.setText(commentInfo.getComment().getContent());
		comment_like_textview.setText(ApplicationManager.getAppContext().getResources().getString(R.string.comment_like)+" ("+commentInfo.getComment().getLikeCount()+")");
		comment_dislike_textview.setText(ApplicationManager.getAppContext().getResources().getString(R.string.comment_dislike)+" ("+commentInfo.getComment().getDislikeCount()+")");
		created_time_textview.setText(StringGenerator.distinctTimeStringFromTimeString(commentInfo.getComment().getCreatedTime()));
		
		if(commentInfo.getComment().getAttaches().size() == 0){
			attach_image_imageview.setVisibility(View.GONE);
			attach_link_textview.setVisibility(View.GONE);
		}else if(commentInfo.getComment().getAttaches().get(0).getType().equals(Attachment.ATTACH_IMAGE)){
			attach_image_imageview.setVisibility(View.VISIBLE);
			attach_link_textview.setVisibility(View.GONE);
			loadAttach(attach_image_imageview, commentInfo.getComment().getAttaches().get(0).getToken(), true);
			attach_image_imageview.setOnClickListener(getAttachImageClickedListener(commentInfo.getComment()));

		}else if(commentInfo.getComment().getAttaches().get(0).getType().equals(Attachment.ATTACH_LINK)){
			attach_image_imageview.setVisibility(View.GONE);
			attach_link_textview.setVisibility(View.VISIBLE);
			attach_link_textview.setText(commentInfo.getComment().getAttaches().get(0).getUrl());
			Linkify.addLinks(attach_link_textview, Linkify.ALL);
		}
		showComment();
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
				Intent i = new Intent(CommentActivity.this, PersonalActivity.class);
				i.putExtra("username", username);
				i.putExtra("nickname", nickname);
				i.putExtra("back_image", BitmapFactory.decodeResource(getResources(), R.drawable.waves_icon));
				startActivity(i);
			}
		};
	}
	
	
	private void initTitleBarUI () {
		// back button
		ImageButton back_button = (ImageButton)findViewById(R.id.back_imagebutton);
		back_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		// back image
		Bitmap bmp = getIntent().getParcelableExtra("back_image");
		if (bmp != null)
			back_button.setImageBitmap(bmp);
	}
	
	private View.OnClickListener getclicklistener (){
		return new OnClickListener (){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch(v.getId()){
					case R.id.like_comment_button:
							SendLike_disLike(true);
						break;	
						
					case R.id.dislike_comment_button:
							SendLike_disLike(false);
						break;	
						
					case R.id.comment_row_enter_program:
							Intent i = new Intent(CommentActivity.this, ProgramActivity.class);
							i.putExtra("program", commentInfo.getProgram());
							i.putExtra("back_image", BitmapFactory.decodeResource(getResources(), R.drawable.comment_icon));
							startActivity(i);
						break;	
				}
	

			}
		};
	}
	
	
	/**
	 * send like dislike
	 *   true Like   false  disLike
	 * 
	 * @param boolean
	 * @param rating
	 * @return boolean
	 */
	private void SendLike_disLike(final boolean like){
		if (!isDestroyed()) {
			
			like_comment_button.setClickable(false);
			like_comment_button.startAnimation(new AlphaSetter(0.5f));
			like_comment_button.setEnabled(false);
			dislike_comment_button.setClickable(false);
			dislike_comment_button.startAnimation(new AlphaSetter(0.5f));
			dislike_comment_button.setEnabled(false);
			
			new Thread ( new Runnable () {
				public void run () {
					final boolean type = CommentManager.rateComment(chatjid, commentInfo.getComment().getId(), like);
					if (chatjid != null && chatjid.length() > 0){
						
					}else
						CommentActivity.this.runOnUiThread(new Runnable () {
							public void run () {
								if (isDestroyed())
									return;

								
								if(type&&like){
									if(commentInfo!=null)
										commentInfo.getComment().addLike(1);
									if(comment_409_textview!=null)
										comment_409_textview.setVisibility(View.VISIBLE);
									if(comment_like_textview!=null)
										comment_like_textview.setText(ApplicationManager.getAppContext().getResources().getString(R.string.comment_like)+" ("+commentInfo.getComment().getLikeCount()+")");
								}else if(type&&!like){
									if(commentInfo!=null)
										commentInfo.getComment().addDislike(1);
									if(comment_409_textview!=null)
										comment_409_textview.setVisibility(View.VISIBLE);
									if(comment_dislike_textview!=null)
										comment_dislike_textview.setText(ApplicationManager.getAppContext().getResources().getString(R.string.comment_dislike)+" ("+commentInfo.getComment().getDislikeCount()+")");
								}else{
									/*
									showToast(ApplicationManager.getAppContext().getResources().getString(R.string.comment_message_ratecommect_fail));
									comment_409_textview.setVisibility(View.INVISIBLE);
									like_comment_button.setClickable(true);
									like_comment_button.startAnimation(new AlphaSetter(1f));
									like_comment_button.setEnabled(true);
									dislike_comment_button.setClickable(true);
									dislike_comment_button.startAnimation(new AlphaSetter(1f));
									dislike_comment_button.setEnabled(true);
									*/
								}
								

									
							}
						});						

				}
			}).start();
		}
		
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
				Intent i = new Intent(CommentActivity.this, ImagePresentActivity.class);
				i.putExtra("present_type", ImagePresentActivity.ATTACH_IMAGE_PRESENT);
				i.putExtra("attachment_description", cm.getContent());
				i.putExtra("attachment", cm.getAttaches().get(0));
				startActivity(i);
			}
		};
	}
	
	/**
	 * Load avatar.
	 * @param iv
	 * @param username
	 */
	private void loadAvatar (ImageView iv, String username) {
		if (username == null || username.length() == 0)
			return;
		
		avatarOk = ImageManager.drawAvatarImage(iv, username);
		
		if (avatarOk&&attachOk)
			ImageManager.removeImageListener(image_listener);
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
		
		attachOk = ImageManager.drawAttachImage(iv, token, is_thumb);
		
		if (avatarOk&&attachOk)
			ImageManager.removeImageListener(image_listener);
	}
	
	
	/**
	 * Listener for listening image loading request.
	 * @author 
	 *
	 */
	private class AvatarImageListener implements ImageListener {

		@Override
		public void retrieveAvatar(final String username, final Bitmap bmp) {
			// TODO Auto-generated method stub
			if (bmp == null)
				return;
			
			if(commentInfo==null)
				return;
			
			if (isDestroyed())
				return;
			
			if(username.equals(commentInfo.getComment().getOwner()))
				CommentActivity.this.runOnUiThread(new Runnable () {
					public void run () {
						if (isDestroyed())
							return;
						
						avatar_imageview.setImageBitmap(bmp);
						avatarOk = true;
						
						if (avatarOk&&attachOk)
							ImageManager.removeImageListener(image_listener);
					}
				});
		}

		@Override
		public void retrieveBadge(String badge_id, boolean scaled, Bitmap bmp) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void retrieveAttach(String token, boolean is_thumb, final Bitmap bmp) {
			// TODO Auto-generated method stub
			
			if (bmp == null)
				return;
			
			if(commentInfo==null)
				return;
			
			if (isDestroyed())
				return;
			
			if(!isDestroyed()&&token.equals(commentInfo.getComment().getAttaches().get(0).getToken()))
				CommentActivity.this.runOnUiThread(new Runnable () {
					public void run () {
						if (isDestroyed())
							return;
						
						attach_image_imageview.setImageBitmap(bmp);
						attachOk = true;
						
						if (avatarOk&&attachOk)
							ImageManager.removeImageListener(image_listener);
					}
				});
		}

		@Override
		public void retrieveProgramIcon(String title, int sampleBase, Bitmap bmp) {
		}

		@Override
		public void retrieveChannelIcon(String title, final Bitmap bmp) {
			// TODO Auto-generated method stub
		}
		
		
	}
	
	/**
	 * Listener for listening chat room events. 
	 *
	 */
	private class ProgramChatroomListener implements FWChatroomListener {

		@Override
		public void receiveUserJoined(TVUser user) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void receiveUserLeft(TVUser user) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void receiveComment(Comment comment) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void receivePostRating(String comment_id, String username,
				final boolean is_like) {
			// TODO Auto-generated method stub
			if (isDestroyed())
				return;
			
			if (commentInfo.getComment().getId().equals(comment_id)) {
				if (is_like)
					commentInfo.getComment().addLike(1);
				else
					commentInfo.getComment().addDislike(1);
				
				if (isDestroyed())
					return;
				
				CommentActivity.this.runOnUiThread(new Runnable () {
					public void run () {
						if (isDestroyed())
							return;
						
						if(comment_409_textview!=null)
							comment_409_textview.setVisibility(View.VISIBLE);
						if(is_like){
							if(comment_like_textview!=null)
								comment_like_textview.setText(ApplicationManager.getAppContext().getResources().getString(R.string.comment_like)+" ("+commentInfo.getComment().getLikeCount()+")");
						}else {
							if(comment_dislike_textview!=null)
								comment_dislike_textview.setText(ApplicationManager.getAppContext().getResources().getString(R.string.comment_dislike)+" ("+commentInfo.getComment().getDislikeCount()+")");
						}
						
						
							
					}			
				});
			}
				
		}

		@Override
		public void receivePostRatingFailure(String comment_id, String err_msg) {
			
		}

		@Override
		public void receiveAttachment(Comment comment) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void receiveCommentFailure(String err_msg) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void chatroomReconnected() {
			// TODO Auto-generated method stub
			
		}
	}
	
	
	
	
	
	 public void getCommentInfo(final String commentid){
		if (!isDestroyed()){
			showCommentLoading();


			new Thread ( new Runnable () {
				public void run () {
					
					commentInfo = CommentManager.getCommentInfo(commentid);
					
					CommentActivity.this.runOnUiThread( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							setCommentView();
							
								
						}
				   });						
			           	
				}
			}).start();
		}
	 }
}
