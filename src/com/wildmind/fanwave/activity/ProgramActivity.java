package com.wildmind.fanwave.activity;

import java.io.BufferedInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.facebook.android.DialogError;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.comment.Attachment;
import com.wildmind.fanwave.comment.Comment;
import com.wildmind.fanwave.comment.CommentListAdapter;
import com.wildmind.fanwave.comment.CommentListAdapter.CommentListListener;
import com.wildmind.fanwave.comment.CommentManager;
import com.wildmind.fanwave.facebook.FBPendingCommand;
import com.wildmind.fanwave.facebook.FacebookManager;
import com.wildmind.fanwave.follow.FollowManager;
import com.wildmind.fanwave.friend.FriendManager;
import com.wildmind.fanwave.media.AttachImageManager;
import com.wildmind.fanwave.media.ImageListener;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.media.ImageManager.SampleBase;
import com.wildmind.fanwave.network.NetworkManager;
import com.wildmind.fanwave.program.ProgramManager;
import com.wildmind.fanwave.program.ProgramMediaAdapter;
import com.wildmind.fanwave.program.TVProgram;
import com.wildmind.fanwave.program.TVProgramApp;
import com.wildmind.fanwave.program.TVProgramExtraInfo;
import com.wildmind.fanwave.program.TVProgramImage;
import com.wildmind.fanwave.program.TVProgramVideo;
import com.wildmind.fanwave.tutroial.TutorialManager;
import com.wildmind.fanwave.user.TVUser;
import com.wildmind.fanwave.util.StringGenerator;
import com.wildmind.fanwave.widget.MessagePoster;
import com.wildmind.fanwave.widget.MessagePoster.MessagePosterListener;
import com.wildmind.fanwave.widget.ProgramRater;
import com.wildmind.fanwave.widget.ProgramRater.ProgramRaterListener;
import com.wildmind.fanwave.widget.ProgramWaver;
import com.wildmind.fanwave.widget.ProgramWaver.ProgramWaverListener;
import com.wildmind.fanwave.widget.SoftKeyboardFrameLayout;
import com.wildmind.fanwave.widget.SoftKeyboardFrameLayout.SoftKeyboardListener;
import com.wildmind.fanwave.xmpp.FWXMPPManager;
import com.wildmind.fanwave.xmpp.muc.FWChatroomListener;
import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.app.ApplicationManager;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Intent input data "program" 		: TVProgram
 * 					 "back_image" 	: Bitmap
 * @author Kencool
 *
 */

public class ProgramActivity extends BaseActivity implements SoftKeyboardListener, 
	MessagePosterListener, ProgramWaverListener, ProgramRaterListener, CommentListListener {

	private final int PICK_PHOTO_REQUEST 	= 0;
	private final int TAKE_PICUTRE_REQUEST 	= 1;
	private final int TUTORIAL_REQUEST 		= 2;
	private final int LOGIN_FACEBOOK_REQUEST = 11;
	
	/**
	 * Wavein UI
	 */
	private ProgramWaver	program_waver;
	
	/**
	 * Rating UI
	 */
	private ProgramRater	program_rater;
	
	/**
	 * Program UI
	 */
	private ImageButton		next_imagebutton;
	private LinearLayout 	info_layout;
	private ImageButton		follow_imagebutton, rate_imagebutton;
	private ImageView		program_icon_imageview;
	private ImageView		topfan_imageview;
	private TextView 		rating_textview;
	
	/**
	 * Viewer UI
	 */
	private LinearLayout	viewer_layout;
	private TextView		viewer_count_textview;
	private ImageView[]		viewer_avatar_images = {null,null,null,null,null};
	
	/**
	 * Media UI
	 */
	private Gallery		media_gallery;
	
	/**
	 * Comment UI
	 */
	private ListView		comment_listview;
	private LinearLayout	comment_loading_indicator;
	private TextView		no_comments_textview;
	private ScaleGestureDetector comment_scale_detector;
	
	/**
	 * Message Poster UI
	 */
	private SoftKeyboardFrameLayout poster_layout;
	private MessagePoster	message_poster;
	private FrameLayout		comment_post_short_layout, comment_post_layout, notification_layout;
	private TextView		notification_textview;
	private EditText		comment_entry_edittext;

	private TVProgram 			program = null;
	private TVProgramExtraInfo 	extra_info = null;
	private ProgramMediaAdapter	media_gallery_adapter = null;
	private CommentManager 		comment_manager = null;
	private CommentListAdapter 	comments_adapter = null;
	private String 				chat_jid = null;
	private String				last_message = null;
	private String				last_attach_picutre_path = null;
	
	private boolean tutorial_showing = false;
	private boolean waver_pending = false;
	private boolean follow_loading = false;
	private boolean viewer_refreshing = false;
	private boolean keyboard_showing = false;
	private boolean comment_loading = false;
	private boolean comment_loading_more = false;
	private boolean comment_scaled = false;
	private boolean wavein_auto_send = false;
	private boolean muc_reconnecting = false;
	private int		viewer_count = 0;
	
	private CopyOnWriteArrayList<String> muc_friends_orders = new CopyOnWriteArrayList<String>();
	private ConcurrentHashMap<String, TVUser> muc_friends = new ConcurrentHashMap<String, TVUser>();
	private ConcurrentHashMap<String, ImageView> avatar_requests = new ConcurrentHashMap<String, ImageView>();
	private ProgramImageListener image_listener = new ProgramImageListener();
	private ProgramChatroomListener muc_listener = new ProgramChatroomListener();
	private ProgramReceiver program_receiver = null;
	
	/**
	 * UI thread handler.
	 */
	private Handler handler = new Handler();
	
	/**
	 * Roll media gallery runnable.
	 */
	private Runnable roll_media = new Runnable () {
		public void run () {
			media_gallery.onScroll(null, null, 1, 0);
			handler.postDelayed(roll_media, 50);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// check if activity is destroyed due to abnormal situation
		if (isDestroyed())
			return;
		
		setContentView(R.layout.program_activity);
     	
		initData();
		initUI();
		initReceiver();
		
		// show tutorial if need
     	if(!TutorialManager.everShowTutorial(getClass().getName())) {
     		tutorial_showing = true;
     		showTutorial(getClass().getName(), TUTORIAL_REQUEST);
     	}
     	
     	// setup chat room
		refreshExtraInfo(!tutorial_showing);
     	refreshComments();
		connectMuc();
	}
	
	protected void onStart() {
		super.onStart();
	}

	protected void onResume() {
		super.onResume();

		if (media_gallery_adapter != null)
			startMediaMarquee();
	}

	protected void onPause() {
		super.onPause();
		
		stopMediaMarquee();
	}

	protected void onStop() {
		super.onStop();
	}

	protected void onDestroy() {
		super.onDestroy();
		
		if (FWXMPPManager.isConnected())
			leaveMuc();
		
		handler.removeCallbacks(roll_media);
		handler = null;
		roll_media = null;
		
		ImageManager.removeImageListener(image_listener);
		image_listener = null;
		
		if (avatar_requests != null)
			avatar_requests.clear();
		avatar_requests = null;
		
		if (media_gallery_adapter != null)
			media_gallery_adapter.clear();
		media_gallery_adapter = null;
		
		if (comments_adapter != null)
			comments_adapter.clear();
		comments_adapter = null;
		
		if (comment_manager != null)
			comment_manager.clear();
		comment_manager = null;
		
		program = null;
		extra_info = null;
		chat_jid = null;
		last_message = null;
		last_attach_picutre_path = null;
		muc_friends_orders = null;
		muc_friends = null;
		muc_listener = null;
		
		// wavein UI
		if (program_waver != null)
			program_waver.clear();
		program_waver = null;
		
		// rater UI
		if (program_rater != null)
			program_rater.clear();
		program_rater = null;
		
		// title bar UI
		next_imagebutton = null;
		
		// info UI
		info_layout = null;
		follow_imagebutton = null;
		rate_imagebutton = null;
		program_icon_imageview = null;
		topfan_imageview = null;
		rating_textview = null;
		
		// viewer UI
		viewer_layout = null;
		viewer_count_textview = null;
		viewer_avatar_images = null;
		
		// media UI
		media_gallery = null;
		
		// comment UI
		comment_listview = null;
		comment_loading_indicator = null;
		no_comments_textview = null;
		comment_scale_detector = null;
		
		// poster UI
		if (poster_layout != null)
			poster_layout.clear();
		poster_layout = null;
		if (message_poster != null)
			message_poster.clear();
		message_poster = null;
		comment_post_layout = null;
		comment_post_short_layout = null;
		notification_layout = null;
		notification_textview = null;
		comment_entry_edittext = null;
	}
	
	@Override
	public void onSoftKeyboardShown(boolean isShowing) {
		if (isShowing && !keyboard_showing) {
			// keyboard is going to show up
			keyboard_showing = true;
			onKeyboardShown();
			
		} else if (isShowing && keyboard_showing) {
			// keyboard is already shown
			
		} else if (!isShowing && keyboard_showing) {
			// keyboard is going to hide
			keyboard_showing = false;
			onKeyboardHidden();
			
		} else if (!isShowing && keyboard_showing) {
			// keyboard is already hidden
		}
	}
	
	private void onKeyboardShown () {
		comment_post_layout.setVisibility(View.VISIBLE);
		comment_post_short_layout.setVisibility(View.GONE);
		info_layout.setVisibility(View.GONE);
		viewer_layout.setVisibility(View.GONE);
		media_gallery.setVisibility(View.GONE);
		
		message_poster.message_edittext.requestFocus();
		
		// temporarily fix comment list view unbound problem
		comment_listview.postDelayed( new Runnable () {
			public void run () {
				comments_adapter.notifyDataSetChanged();
			}
		}, 1);
	}
	
	private void onKeyboardHidden () {
		comment_post_layout.setVisibility(View.GONE);
		comment_post_short_layout.setVisibility(View.VISIBLE);
		if (!comment_scaled) {
			info_layout.setVisibility(View.VISIBLE);
			viewer_layout.setVisibility(View.VISIBLE);
		}
		
		if (extra_info != null) {
			boolean has_media = extra_info.getImages().size() + extra_info.getVideos().size() + 
								extra_info.getApps().size() > 0;
			media_gallery.setVisibility(has_media ? View.VISIBLE : View.GONE);
		}
		
		message_poster.message_edittext.clearFocus();
		
		// temporarily fix comment list view unbound problem
		comment_listview.postDelayed( new Runnable () {
			public void run () {
				comments_adapter.notifyDataSetChanged();
			}
		}, 1);
	}

	private void initData() {
		Intent i = getIntent();
		program = (TVProgram) i.getParcelableExtra("program");
		comment_manager = new CommentManager(program);
		
		ImageManager.addImageListener(image_listener);
	}

	private void initUI() {
		initTitleBarUI();
		initWaverUI();
		initRaterUI();
		initInfoUI();
		initFanActionUI();
		initViewerUI();
		initMediaUI();
		initCommentUI();
		initPosterUI();
	}
	
	private void initReceiver () {
		program_receiver = new ProgramReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(FWXMPPManager.BROADCAST_USER_WILL_RECONNECT_XMPP);
		ApplicationManager.getAppContext().registerReceiver(program_receiver, intentFilter);
	}

	/**
	 * Initialize title bar UI.
	 */
	private void initTitleBarUI() {
		// Back Button
		ImageButton back_button = (ImageButton) findViewById(R.id.back_imagebutton);
		back_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				processBack();
			}
		});
		
		// Back Image
		Bitmap bmp = getIntent().getParcelableExtra("back_image");
		if (bmp != null)
			back_button.setImageBitmap(bmp);

		// Title
		TextView title = (TextView) findViewById(R.id.title_textview);
		title.setText(program.getTitle());

		// Next Button
		next_imagebutton = (ImageButton) findViewById(R.id.next_imagebutton);
		next_imagebutton.setOnClickListener(getNextPageClickedListener());
		next_imagebutton.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * Initialize wave in view UI.
	 */
	private void initWaverUI () {
		// Waver Layout
		program_waver = (ProgramWaver) findViewById(R.id.program_waver);
		program_waver.init(program);
		program_waver.setProgramWaverListener(this);
		program_waver.setVisibility(View.INVISIBLE);
	}

	/**
	 * Initialize rating UI.
	 */
	private void initRaterUI () {
		// Rating Layout
		program_rater = (ProgramRater) findViewById(R.id.program_rater);
		program_rater.init(program);
		program_rater.setProgramRaterListener(this);
		program_rater.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * Initialize info UI.
	 */
	private void initInfoUI() {
		// Info Layout
		info_layout = (LinearLayout) findViewById(R.id.program_info_layout);
		
		// Program Icon
		program_icon_imageview = (ImageView) findViewById(R.id.program_icon_imageview);
		
		// Topfan Avatar
		topfan_imageview = (ImageView) findViewById(R.id.topfan_imageview);
	}

	/**
	 * Initialize fan action UI.
	 */
	private void initFanActionUI() {
		View.OnClickListener listener = getFanActionClickedListener();
		
		// Follow
		follow_imagebutton = (ImageButton) findViewById(R.id.follow_imagebutton);
		follow_imagebutton.setOnClickListener(listener);
				
		// Rate
		rate_imagebutton = (ImageButton) findViewById(R.id.rate_imagebutton);
		rate_imagebutton.setOnClickListener(listener);
				
		// Splash
		ImageButton splash = (ImageButton) findViewById(R.id.splash_imagebutton);
		splash.setOnClickListener(listener);
		
		// Topfan
		FrameLayout topfan = (FrameLayout) findViewById(R.id.topfan_layout);
		topfan.setOnClickListener(listener);
	}

	private void initViewerUI() {
		// Viewer Layout
		viewer_layout = (LinearLayout) findViewById(R.id.views_layout);
		viewer_layout.setOnClickListener(getViewerClickedListener());
		
		// Viewer Count
		viewer_count_textview = (TextView) findViewById(R.id.viewer_count_textview);
		viewer_count_textview.setText("???");
		
		// Viewer avatars
		viewer_avatar_images[0] = (ImageView) findViewById(R.id.viewer_1_avatar);
		viewer_avatar_images[1] = (ImageView) findViewById(R.id.viewer_2_avatar);
		viewer_avatar_images[2] = (ImageView) findViewById(R.id.viewer_3_avatar);
		viewer_avatar_images[3] = (ImageView) findViewById(R.id.viewer_4_avatar);
		viewer_avatar_images[4] = (ImageView) findViewById(R.id.viewer_5_avatar);
		
		viewer_avatar_images[0].setVisibility(View.INVISIBLE);
		viewer_avatar_images[1].setVisibility(View.INVISIBLE);
		viewer_avatar_images[2].setVisibility(View.INVISIBLE);
		viewer_avatar_images[3].setVisibility(View.INVISIBLE);
		viewer_avatar_images[4].setVisibility(View.INVISIBLE);
	}

	private void initMediaUI() {
		// Media Gallery
		media_gallery = (Gallery) findViewById(R.id.program_media_gallery);
		media_gallery.setVisibility(View.GONE);
	}

	private void initCommentUI() {		
		// Comment List
		comment_listview = (ListView) findViewById(R.id.comment_listview);
		comment_listview.setDivider(null);
		comment_listview.setDividerHeight(0);
		comment_listview.setOnScrollListener(getCommentListScrollListener());
		comments_adapter = new CommentListAdapter(comment_listview, program.getTitle(), null, this);
		comments_adapter.setCommentListListener(this);
		comment_listview.setAdapter(comments_adapter);
		comment_listview.setVisibility(View.INVISIBLE);
		
		// Comment Scale Gesture Listener
		comment_scale_detector = new ScaleGestureDetector(this, new CommentScaleListener());
		comment_listview.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				comment_scale_detector.onTouchEvent(event);
				return false;
			}
		});
		
		// Comment Loading Indicator
		comment_loading_indicator = (LinearLayout) findViewById(R.id.comment_loading_indicator);
		comment_loading_indicator.setVisibility(View.VISIBLE);
		
		// No Comments Text View
		no_comments_textview = (TextView) findViewById(R.id.no_comments_textview);
		no_comments_textview.setVisibility(View.INVISIBLE);
	}

	private void initPosterUI() {
		poster_layout = (SoftKeyboardFrameLayout) findViewById(R.id.program_poster);
		poster_layout.setSoftKeyboardListener(this);
		
		// Comment Poster Layout
		comment_post_short_layout = (FrameLayout) poster_layout.findViewById(R.id.comment_post_short_layout);
		comment_entry_edittext = (EditText) comment_post_short_layout.findViewById(R.id.post_edittext);
		
		comment_post_layout = (FrameLayout) poster_layout.findViewById(R.id.comment_post_layout);
		comment_post_layout.setVisibility(View.GONE);
		message_poster = (MessagePoster) comment_post_layout.findViewById(R.id.message_poster);
		message_poster.init();
		message_poster.setMessagePosterListener(this);
		
		notification_layout = (FrameLayout) poster_layout.findViewById(R.id.notification_layout);
		notification_layout.setVisibility(View.GONE);
		RelativeLayout layout = (RelativeLayout) notification_layout.findViewById(R.id.message_notification);
		notification_textview = (TextView) layout.findViewById(R.id.notification_textview);
		notification_textview.setText(R.string.program_notify_joining);
	}
	
	private void refreshProgram () {
		notification_textview.setText(R.string.program_notify_joining);
		notification_textview.setOnClickListener(null);
		refreshComments();
		connectMuc();
	}
	
	/**
	 * Reload extra info.
	 */
	private void refreshExtraInfo (boolean show_loading) {
		if (show_loading)
			showExtraInfoLoading();
		getExtraInfo();
	}
	
	/**
	 * Reload viewers.
	 */
	private void refreshViewers () {
		if (!viewer_refreshing) {
			viewer_refreshing = true;
			viewer_layout.postDelayed(new Runnable () {
				public void run () {
					showViewers();
				}
			}, 2000);
			
		}
	}
	
	/**
	 * Reload comments.
	 */
	private void refreshComments () {
		showCommentLoading();
		comment_loading = false;
		comment_loading_more = false;
		getComments();
	}
	
	/**
	 * Show extra info loading view. Currently using wave in loading view.
	 */
	private void showExtraInfoLoading () {
		program_waver.setVisibility(View.VISIBLE);
		program_waver.showLoading();
	}
	
	/**
	 * Show extra info.
	 */
	private void showExtraInfo () {
		if (extra_info.getPgid().length() == 0 || extra_info.isWavein())
			dismissWaver();
		else {
			// if tutorial is showing, pending present waver until activity is resumed
			if (tutorial_showing)
				waver_pending = true;
			else
				presentWaver();
		}
		showInfo();
		showMedia();
	}
	
	/**
	 * Present wave in view.
	 */
	private void presentWaver () {
		program_waver.setVisibility(View.VISIBLE);
		program_waver.showWaver();
	}
	
	/**
	 * Dismiss wave in view.
	 */
	private void dismissWaver () {
		program_waver.setVisibility(View.INVISIBLE);
		next_imagebutton.setVisibility(View.VISIBLE);
		
		// if muc is not joined yet, show waiting
		if (!FWXMPPManager.getMucManager().isJoinedMUC(chat_jid))
			notification_layout.setVisibility(View.VISIBLE);
	}
	
	/**
	 * Present program rater.
	 */
	private void presentRater () {
		program_rater.setVisibility(View.VISIBLE);
		program_rater.show();
	}
	
	/**
	 * Dismiss program rater.
	 */
	private void dismissRater () {
		program_rater.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * Show program info.
	 */
	private void showInfo () {
		// Program Icon
		if (extra_info.getIconUrl().length() == 0)
			program_icon_imageview.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.program_default));
		else {
			loadProgramIcon(program_icon_imageview, program.getTitle(), extra_info.getIconUrl());
			program_icon_imageview.setOnClickListener(getProgramIconClickedListener());
		}
		
		// Total Rating
		rating_textview = (TextView) findViewById(R.id.total_rating_textview);
		rating_textview.setText(String.valueOf(extra_info.getTotalRating()));
		
		// Follow
		follow_imagebutton.setSelected(extra_info.isFollowing());
		
		// Rate
		rate_imagebutton.setSelected(extra_info.getUserRating() > 0);
		
		// Topfan avatar
		if (extra_info.getTopfan().length() == 0)
			topfan_imageview.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.avatar_default));
		else
			loadAvatar(topfan_imageview, extra_info.getTopfan());
		
		// Viewers Count
		viewer_count += extra_info.getVisitors();
		viewer_count_textview.setText(String.valueOf(viewer_count));
	}
	
	/**
	 * Show program media.
	 */
	private void showMedia () {
		ArrayList<TVProgramImage> images = extra_info.getImages();
		ArrayList<TVProgramVideo> videos = extra_info.getVideos();
		ArrayList<TVProgramApp>		apps = extra_info.getApps();
		int len = images.size() + videos.size() + apps.size();
		if (len > 0) {
			media_gallery.setOnItemClickListener(getMediaClickedlistener());
			media_gallery.setOnTouchListener(getMediaTouchedListener());
			media_gallery_adapter = new ProgramMediaAdapter(media_gallery, apps, images, videos, this);
			media_gallery.setAdapter(media_gallery_adapter);
			media_gallery.setSelection(((media_gallery_adapter.getCount() / len) / 2) * len); 
			startMediaMarquee();
		}
	}
	
	/**
	 * Show viewers.
	 */
	private void showViewers () {
		if (isDestroyed())
			return;
		
		ArrayList<TVUser> friends = new ArrayList<TVUser>();
		int i = 0;
		for (String username:muc_friends_orders) {
			if (i==5)
				break;
			if (muc_friends.containsKey(username))
				friends.add(muc_friends.get(username));
			i++;
		}
		viewer_refreshing = false;
		
		int len = friends.size();
		for (i = 0; i < 5; i++) {
			ImageView iv = viewer_avatar_images[i];
			if (i < len) {
				iv.setVisibility(View.VISIBLE);
				iv.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.avatar_loading));
				iv.setTag(friends.get(i).getUsername());
				loadAvatar(iv, friends.get(i).getUsername());
			} else
				iv.setVisibility(View.INVISIBLE);
		}
	}
	
	/**
	 * Show comment list loading view.
	 */
	private void showCommentLoading () {
		comment_listview.setVisibility(View.INVISIBLE);
		comment_loading_indicator.setVisibility(View.VISIBLE);
		no_comments_textview.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * Show comment list view.
	 */
	private void showCommentList () {
		comment_listview.setVisibility(View.VISIBLE);
		comment_loading_indicator.setVisibility(View.INVISIBLE);
		no_comments_textview.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * Show comments.
	 */
	private void showComments () {
		showCommentList();
		
		ArrayList<Comment> list = comment_manager.getCommentOrderedList();
		comments_adapter.refreshData(list, comment_manager.isMoreComment());
		no_comments_textview.setVisibility(list.size() > 0 ? View.INVISIBLE : View.VISIBLE);
	}
	
	/**
	 * Start rolling media gallery.
	 */
	private void startMediaMarquee () {
		handler.removeCallbacks(roll_media);
		handler.postDelayed(roll_media, 5000);
	}
	
	/**
	 * Stop rolling media gallery.
	 */
	private void stopMediaMarquee () {
		handler.removeCallbacks(roll_media);
	}
	
	/**
	 * Get extra info.
	 */
	private void getExtraInfo () {
		new Thread (new Runnable () {
			public void run () {
				TVProgramExtraInfo tpei = null;
				do {
					if (isDestroyed())
						return;
					tpei = ProgramManager.getProgramExtraInfo(program.getCountry(), program.getPgid());
				} while (tpei == null);
				
				if (!isDestroyed()) {
					extra_info = tpei;
					extra_info.setTitle(program.getTitle());
					
					ProgramActivity.this.runOnUiThread( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							program_rater.prepare(extra_info);
							showExtraInfo();
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Get comments.
	 */
	private void getComments () {
		if (comment_loading)
			return;
		comment_loading = true;
		
		new Thread (new Runnable () {
			public void run () {
				comment_manager.getComments(20);
				
				if (!isDestroyed()) {
					if (!comment_loading)
						return;
					comment_loading = false;
					
					comment_listview.post( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							showComments();
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Get more comments.
	 */
	private void getMoreComments () {
		if (comment_loading_more)
			return;
		comment_loading_more = true;
		
		new Thread (new Runnable () {
			public void run () {
				comment_manager.getMoreComments(20);
				
				if (!isDestroyed()) {
					comment_listview.post( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							if (!comment_loading_more)
								return;
							comment_loading_more = false;
							showComments();
						}
					});
				}
			}
		}).start();
	}
	/**
	 * Connect XMPP muc.
	 */
	private void connectMuc () {
		new Thread ( new Runnable () {
			public void run () {
				String jid = ProgramManager.getProgramJid(program.getTitle(), program.getCountry());
				
				if (!isDestroyed()) {
					chat_jid = jid;
					comments_adapter.setChatJid(chat_jid);
					
					ProgramActivity.this.runOnUiThread(new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							if (chat_jid != null && chat_jid.length() > 0)
								joinMuc();
							else {
								notification_textview.setText(R.string.program_notify_retry);
								notification_textview.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										refreshProgram();
									}
								});
							}
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Join XMPP muc.
	 */
	private void joinMuc () {
		if (!FWXMPPManager.getMucManager().buildMuc(chat_jid, AccountManager.getCurrentUser(), program, muc_listener))
			FWXMPPManager.getMucManager().addChatroomListener(chat_jid, muc_listener);
		
		// if muc is reconnecting, we leave join muc task for reconnecting process
		if (muc_reconnecting) {
			notification_layout.setVisibility(View.VISIBLE);
			notification_textview.setText(R.string.program_notify_reconnect);
			return;
		}
		
		new Thread (new Runnable () {
			public void run () {
				boolean success = false;
				int retry = 0;
				do {
					if (muc_reconnecting)
						return;
					
					success = FWXMPPManager.getMucManager().joinMUC(chat_jid);
					retry++;
				} while (!success && retry <= 3);
				
				final boolean mucSuccess = success;
				if (!isDestroyed()) {
					notification_layout.post( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							if (mucSuccess)
								processMucJoined();
							else {
								notification_textview.setText(R.string.program_notify_retry);
								notification_textview.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										refreshProgram();
									}
								});
							}
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Leave XMPP muc.
	 */
	private void leaveMuc () {
		if (FWXMPPManager.getMucManager() != null)
			FWXMPPManager.getMucManager().leaveMUC(chat_jid, muc_listener);
	}
	
	/**
	 * Process muc joined success task.
	 */
	private void processMucJoined () {
		notification_layout.setVisibility(View.GONE);
		notification_textview.setText(R.string.program_notify_sending);
		
		// auto send wave in message if need
		if (wavein_auto_send) {
			wavein_auto_send = false;
			FWXMPPManager.getMucManager().sendComment(chat_jid, "Wave in !!!");
		}
	}
	
	/**
	 * Process follow or unfollow action.
	 */
	private void processFollow () {
		if (follow_loading)
			return;
		follow_loading = true;
		follow_imagebutton.setSelected(!extra_info.isFollowing());
		
		new Thread (new Runnable () {
			public void run () {
				final boolean success = extra_info.isFollowing()
									  ? FollowManager.unfollowProgram(program)
									  : FollowManager.followProgram(program);
									  
				if (!isDestroyed()) {
					ProgramActivity.this.runOnUiThread( new Runnable () {
						public void run () {
							follow_loading = false;
							int msg_id = -1;
							if (success) {
								extra_info.setFollowing(!extra_info.isFollowing());
								msg_id = extra_info.isFollowing() 
									   ? R.string.program_follow_success
									   : R.string.program_unfollow_success;
							} else {
								follow_imagebutton.setSelected(extra_info.isFollowing());
								msg_id = extra_info.isFollowing() 
									   ? R.string.program_unfollow_failed
						 	 		   : R.string.program_follow_failed;
							}

							Toast.makeText(getApplicationContext(), msg_id, Toast.LENGTH_SHORT).show();
						}
					});
				}
				
			}
		}).start();
	}
	
	/**
	 * Process splash friends or followers action.
	 */
	private void processSplash () {
		final Intent i = new Intent(ProgramActivity.this, SplashProgramActivity.class);
		i.putExtra("program_title", program.getTitle());
		
		if (!extra_info.getTopfan().equals(AccountManager.getCurrentUser().getUsername())) {
			i.putExtra("to_fans", false);
			startActivity(i);
		} else {
			// item listener
			DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener () {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (which == 0) {
						i.putExtra("to_fans", true);
						i.putExtra("pgid", program.getPgid());
					} else 
						i.putExtra("to_fans", false);
					startActivity(i);
				}					
			};
			
			// alert dialog
			new AlertDialog.Builder(this)
						   .setTitle(R.string.main_page_splash)
						   .setPositiveButton(R.string.action_cancel, null)
						   .setItems(R.array.program_splash_array, listener)
						   .show();
		}
	}
	
	/**
	 * Process top fan action.
	 */
	private void processTopfan () {
		Intent i = null;
		if(extra_info.getTopfan().length() == 0){
			i = new Intent(ProgramActivity.this, HowBeTopFanActivity.class);
		}else{
			i = new Intent(ProgramActivity.this, TopfanActivity.class);
			i.putExtra("program_title", program.getTitle());
			i.putExtra("pgid", program.getPgid());
		}
		startActivity(i);
	}
	
	/**
	 * Process back action.
	 */
	private void processBack () {
		if (program_rater.getVisibility() == View.VISIBLE)
			dismissRater();
		else {
			if (!message_poster.isUploading() && message_poster.getAttach() == null)
				finish();
			else {
				DialogInterface.OnClickListener leave = new  DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				}; 	 
				AlertDialog dialog = new AlertDialog.Builder(ProgramActivity.this)
				   			   .setTitle(R.string.attach_title)
				   			   .setMessage(R.string.attach_pending)
				   			   .setPositiveButton(R.string.action_confirm, leave)
				   			   .setNegativeButton(R.string.action_cancel, null)
				   			   .create();
				if (keyboard_showing)
					dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
				dialog.show();
			}
		}
	}

	/**
	 * Callback for next page clicked.
	 * @return
	 */
	private View.OnClickListener getNextPageClickedListener() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (extra_info == null)
					return;
				
				Intent i = new Intent(ProgramActivity.this, ProgramExtraInfoActivity.class);
				i.putExtra("extra_info", extra_info);
				i.putExtra("country", program.getCountry());
				startActivity(i);
			}
		};
	}
	
	/**
	 * Callback for program icon clicked.
	 * @return
	 */
	private View.OnClickListener getProgramIconClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TVProgramImage tpi = new TVProgramImage();
				tpi.setTitle(program.getTitle());
				tpi.setImageUrl(extra_info.getIconUrl());
				
				Intent i = new Intent(ProgramActivity.this, ImagePresentActivity.class);
				i.putExtra("present_type", ImagePresentActivity.PROGRAM_IMAGE_PRESENT);
				i.putExtra("program_title", program.getTitle());
				i.putExtra("program_image", tpi);
				startActivity(i);
			}
		};
	}
	
	/**
	 * Callback for fan action clicked.
	 * @return
	 */
	private View.OnClickListener getFanActionClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				
				case R.id.follow_imagebutton:
					processFollow();
					break;
					
				case R.id.rate_imagebutton:
					if (program_rater.isPrepared())
						presentRater();
					break;
					
				case R.id.splash_imagebutton: 
					processSplash();
					break;
					
				case R.id.topfan_layout: 
					processTopfan();
					break;
					
				default: 
					break;
				}
			}
		};
	}
	
	/**
	 * Callback for viewer clicked.
	 * @return
	 */
	private View.OnClickListener getViewerClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ProgramActivity.this, ProgramSofaActivity.class);
				i.putExtra("program_title", program.getTitle());
				i.putStringArrayListExtra("sofa_users", new ArrayList<String>(muc_friends_orders));
				startActivity(i);
			}
		};
	}
	
	/**
	 * Callback for media item clicked.
	 * @return
	 */
	private OnItemClickListener getMediaClickedlistener () {
		return new OnItemClickListener () {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				position = media_gallery_adapter.decodeOriginPosition(position);
				if (media_gallery_adapter.isAppItem(position)) {
					TVProgramApp app = media_gallery_adapter.getAppItem(position);
					Intent i = new Intent(ProgramActivity.this, WebViewActivity.class);
					i.putExtra("url", app.getUrl());
					startActivity(i);
					
				} else if (media_gallery_adapter.isImageItem(position)) {
					TVProgramImage image = media_gallery_adapter.getImageItem(position);
					Intent i = new Intent(ProgramActivity.this, ImagePresentActivity.class);
					i.putExtra("present_type", ImagePresentActivity.PROGRAM_IMAGE_PRESENT);
					i.putExtra("program_title", program.getTitle());
					i.putExtra("program_image", image);
					startActivity(i);
					
				} else if (media_gallery_adapter.isVideoItem(position)) {
					TVProgramVideo video = media_gallery_adapter.getVideoItem(position);
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(video.getVideoUri()));
					startActivity(i);
				}
			}
		};
	}
	
	private View.OnTouchListener getMediaTouchedListener() {
		return new View.OnTouchListener () {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					stopMediaMarquee();
					break;
				case MotionEvent.ACTION_UP:
					startMediaMarquee();
					break;
				default:
					break;
				}
				return false;
			}
			
		};
	}
	
	/**
	 * Callback for comment list scrolled.
	 * @return
	 */
	private AbsListView.OnScrollListener getCommentListScrollListener () {
		return new AbsListView.OnScrollListener () {
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem + visibleItemCount > totalItemCount - 2) 
					if (comment_manager.isMoreComment())
						getMoreComments();
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
		};
	}
	
	/**
	 * Listener for listening chat room events. 
	 * @author Kencool
	 *
	 */
	private class ProgramChatroomListener implements FWChatroomListener {

		@Override
		public void receiveUserJoined(final TVUser user) {
			if (isDestroyed())
				return;
			
			viewer_count++;
			if (!isDestroyed()) {
				viewer_count_textview.post(new Runnable () {
					public void run () {
						if (isDestroyed())
							return;
						
						viewer_count_textview.setText(String.valueOf(viewer_count));
						
						if (!FriendManager.isFriend(user.getUsername()))
							return;
						
						muc_friends_orders.add(0, user.getUsername());
						muc_friends.put(user.getUsername(), user);
						refreshViewers();
					}
				});
			}
		}

		@Override
		public void receiveUserLeft(TVUser user) {
			if (isDestroyed())
				return;
			
			if (!muc_friends_orders.contains(user.getUsername()))
				return;
			
			muc_friends_orders.remove(user.getUsername());
			muc_friends.remove(user.getUsername());
			if (!isDestroyed()) {
				viewer_count_textview.post( new Runnable () {
					public void run () {
						if (isDestroyed())
							return;
						
						refreshViewers();
					}
				});
			}
		}
		
		@Override
		public void receiveComment(final Comment comment) {
			if (comment_loading)
				return;
			
			if (!isDestroyed()) {
				no_comments_textview.post( new Runnable () {
					public void run () {
						if (isDestroyed())
							return;
						
						// dismiss notification layout if receive last message
						if (comment.getOwner().equals(AccountManager.getCurrentUser().getUsername()) &&
							comment.getContent().equals(last_message)) {
							last_message = null;
							notification_layout.setVisibility(View.GONE);
							comment_entry_edittext.setText("");
						}
						
						comment.setCreatedTime(StringGenerator.getCurrentTimeStringWithSeconds());
						comment_manager.addComment(comment);
						ArrayList<Comment> list = comment_manager.getCommentOrderedList();
						comments_adapter.refreshData(list, comment_manager.isMoreComment());
						
						if (no_comments_textview.getVisibility() == View.VISIBLE)
							no_comments_textview.setVisibility(View.INVISIBLE);
					}
				});
			}
		}
		
		@Override
		public void receiveCommentFailure(String err_msg) {
			Toast.makeText(ProgramActivity.this, err_msg, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void receivePostRating(final String comment_id, String username, final boolean is_like) {
			if (isDestroyed())
				return;
			
			Comment cm = comment_manager.getComment(comment_id);
			if (cm != null) {
				if (is_like)
					cm.addLike(1);
				else
					cm.addDislike(1);
				
				if (!isDestroyed()) {
					no_comments_textview.post( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							comments_adapter.refreshCommentRating(comment_id, is_like);
						}
					});
				}
			}
		}

		@Override
		public void receivePostRatingFailure(String comment_id, String err_msg) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void receiveAttachment(final Comment comment) {
			if (comment_loading)
				return;
			
			if (!isDestroyed()) {
				no_comments_textview.post( new Runnable () {
					public void run () {
						if (isDestroyed())
							return;
						
						// dismiss notification layout if receive last message
						if (comment.getOwner().equals(AccountManager.getCurrentUser().getUsername()) &&
							comment.getContent().equals(last_message)) {
							last_message = null;
							notification_layout.setVisibility(View.GONE);
						}
									
						comment.setCreatedTime(StringGenerator.getCurrentTimeStringWithSeconds());
						comment_manager.addComment(comment);
						ArrayList<Comment> list = comment_manager.getCommentOrderedList();
						comments_adapter.refreshData(list, comment_manager.isMoreComment());
					}
				});
			}
		}

		@Override
		public void chatroomReconnected() {
			muc_reconnecting = false;
			if (!isDestroyed()) {
				notification_layout.post( new Runnable () {
					public void run () {
						if (isDestroyed())
							return;
						
						processMucJoined();
					}
				});
			}
		}
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
			avatar_requests.put(username, iv);
	}
	
	/**
	 * Load ProgramIcon.
	 * @param title
	 * @param icon_url
	 */
	private void loadProgramIcon (ImageView iv, String title, String icon_url) {
		if (icon_url == null || icon_url.length() == 0)
			return;
		
		ImageManager.drawProgramIcon(iv, title, icon_url, ImageManager.SampleBase.RIGOROUS_SAMPLED);
	}
	
	/**
	 * Listener for listening image loading requests.
	 * @author Kencool
	 *
	 */
	private class ProgramImageListener implements ImageListener {

		@Override
		public void retrieveAvatar(final String username, final Bitmap bmp) {
			if (avatar_requests == null || !avatar_requests.containsKey(username))
				return;
			
			if (!isDestroyed()) {
				ProgramActivity.this.runOnUiThread( new Runnable () {
					public void run () {
						if (isDestroyed())
							return;
						
						// Topfan
						if (extra_info != null && username.equals(extra_info.getTopfan()))
							topfan_imageview.setImageBitmap(bmp);
						
						// viewers
						ImageView iv = avatar_requests.get(username);
						if (iv != null) {
							String request = (String) iv.getTag();
							if (username.equals(request))
								iv.setImageBitmap(bmp);
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
		public void retrieveProgramIcon(String title, int sampleBase, final Bitmap bmp) {
			if (!title.equals(program.getTitle()) || 
				sampleBase != ImageManager.SampleBase.RIGOROUS_SAMPLED)
				return;
			
			if (!isDestroyed()) {
				ProgramActivity.this.runOnUiThread( new Runnable () {
					public void run () {
						if (isDestroyed())
							return;
						program_icon_imageview.setImageBitmap(bmp);
					}
				});
			}
		}

		@Override
		public void retrieveChannelIcon(String chcode, Bitmap bmp) {
			// TODO Auto-generated method stub
			
		}
	}
	
	
	/**
	 * Program Waver Listener Methods
	 */
	
	@Override
	public void onWaveinSuccess() {
		if (!isDestroyed()) {
			program_waver.postDelayed( new Runnable () {
				public void run () {
					if (isDestroyed())
						return;
					
					program_waver.hideWaver();
					dismissWaver();
					
					// auto send wave in message to chat room
					/*
					if (FWXMPPManager.getMucManager().isJoinedMUC(chat_jid))
						FWXMPPManager.getMucManager().sendComment(chat_jid, "Wave in !!!");
					else
						// pending auto send task to join muc process (joinMUC)
						wavein_auto_send = true;
						*/
				}
			}, 1000);
		}
	}

	@Override
	public void onWaveinCancel() {
		if (!isDestroyed()) {
			program_waver.post( new Runnable () {
				public void run () {
					if (isDestroyed())
						return;
					
					program_waver.hideWaver();
					dismissWaver();
				}
			});
		}
	}
	
	@Override
	public void onFacebookActiveRequest(FBPendingCommand command) {
		FacebookDialogListener listener = new FacebookDialogListener(command);
		if (FacebookManager.login(this, LOGIN_FACEBOOK_REQUEST, listener))
			listener.onComplete(null);
	}
	
	/**
	 * Program Rater Listener Methods
	 */
	
	@Override
	public void onRate(int ratingPoint) {
		extra_info.setUserRating(ratingPoint);
		
		if (extra_info.getTotalRating() == 0) {
			extra_info.setTotalRating(ratingPoint);
			rating_textview.setText(String.valueOf(ratingPoint));
		}
		
		rate_imagebutton.setSelected(true);
		program_rater.postDelayed(new Runnable () {
			public void run () {
				dismissRater();
			}
		}, 1000);
	}
	
	/**
	 * Message Poster Listener Methods
	 */
	
	@Override
	public void onRequestPickPhoto() {
		Intent i = new Intent();
		i.setType("image/*");
		i.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(i, "Select Picture"), PICK_PHOTO_REQUEST);
	}
	
	@Override
	public void onRequestTakePicture() {
		// create directory if not exist
		String dirPath = Environment.getExternalStorageDirectory() + "/Fanwave/Attach/User";
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		// generate file
		File file = AttachImageManager.generaterFile();
		last_attach_picutre_path = file.getAbsolutePath();
		
		// start activity
		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		startActivityForResult(i, TAKE_PICUTRE_REQUEST);
	}
	
	@Override
	public void onPost(final String message, boolean onFacebook) {
		last_message = message;
		
		// send comment
		if (FWXMPPManager.getMucManager().sendComment(chat_jid, message)) {
			message_poster.refreshPost();
			
			// Facebook post
			if (onFacebook) {
				new Thread (new Runnable () {
					public void run () {
						FacebookManager.postComment(program.getTitle(), message, null, null);
					}
				}).start();
			}
		}
		
		// hide soft keyboard
		InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(message_poster.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		
		// show waiting bar
		message_poster.postDelayed( new Runnable () {
			public void run () {
				if (last_message != null)
					notification_layout.setVisibility(View.VISIBLE);
			}
		}, 500);
	}
	
	@Override
	public void onPostWithLink (final String message, final Attachment attach, final boolean onFacebook) {
		last_message = message;
		
		new Thread (new Runnable () {
			public void run () {
				// post link
				final Attachment am = CommentManager.postLink(attach.getUrl());
				
				if (!isDestroyed()) {
					message_poster.post( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							if (am != null) {
								attach.setUrl(am.getUrl());
								attach.setToken(am.getToken());
								
								// send comment
								if (FWXMPPManager.getMucManager().sendAttachment(chat_jid, attach)) {
									message_poster.refreshPost();
									
									// Facebook post
									if (onFacebook) {
										new Thread (new Runnable () {
											public void run () {
												FacebookManager.postComment(program.getTitle(), message, 
																			am.getUrl(), null);											}
										}).start();
									}
								}
							} else {
								new AlertDialog.Builder(ProgramActivity.this)
											   .setTitle(R.string.attach_title)
											   .setMessage(R.string.attach_link_failed)
											   .setPositiveButton(R.string.action_confirm, null)
											   .show();
							}
						}
					});
				}
			}
		}).start();
		
		// hide soft keyboard
		InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(message_poster.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				
		// show waiting bar
		message_poster.postDelayed( new Runnable () {
			public void run () {
				if (last_message != null)
					notification_layout.setVisibility(View.VISIBLE);
			}
		}, 500);
	}
	
	@Override
	public void onPostWithImage (final String message, final String imagePath, Attachment attach, 
			boolean onFacebook) {
		last_message = message;
		
		if (FWXMPPManager.getMucManager().sendAttachment(chat_jid, attach)) {
			message_poster.refreshPost();
			
			// Facebook post
			if (onFacebook) {
				new Thread (new Runnable () {
					public void run () {
						processFacebookImagePost(message, imagePath);
					}
				}).start();
			}
		}
		
		// hide soft keyboard
		InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(message_poster.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				
		// show waiting bar
		message_poster.postDelayed( new Runnable () {
			public void run () {
				if (last_message != null)
					notification_layout.setVisibility(View.VISIBLE);
			}
		}, 500);
	}
	
	/**
	 * Process Facebook image post.
	 * @param message
	 * @param imagePath
	 */
	private void processFacebookImagePost (String message, String imagePath) {
		
		// get file input stream
		BufferedInputStream fileInputStream = NetworkManager.getFileInputStream(imagePath);
		if (fileInputStream == null)
			return;
		
		// calculate sample size
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(fileInputStream, null, opts);
		opts.inSampleSize = NetworkManager.getFitSampleSize(opts, SampleBase.MEDIUM_SAMPLED);
		
		// get sampled image
		opts.inJustDecodeBounds = false;
		fileInputStream = NetworkManager.getFileInputStream(imagePath);
		Bitmap bmp = BitmapFactory.decodeStream(fileInputStream, null, opts);
		
		// Facebook post
		FacebookManager.postComment(program.getTitle(), message, null, bmp);
		bmp.recycle();
		bmp = null;
	}
	
	/**
	 * Comment List Listener Methods
	 */
	@Override
	public void onReply(String nickname) {
		if (notification_layout.getVisibility() != View.VISIBLE) {
			message_poster.message_edittext.setText("@" + nickname + " ");
			comment_entry_edittext.setText("@" + nickname + " ");
			comment_entry_edittext.requestFocus();
		}
	}
	
	/**
	  * onKeyDown Touch Event Method
	  * @param keyCode
	  * @param event
	  * @return boolean
	  */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { 
			processBack();
			return true;
		}
		return super.onKeyDown(keyCode, event); 
	}
	
	/**
	 * onActivityResult
	 */
	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		
		case PICK_PHOTO_REQUEST: {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
			if (resultCode == RESULT_OK) {
				final Uri uri = data.getData();
				data = null;
				System.gc();

				message_poster.post( new Runnable () {
					public void run () {
						if (message_poster != null)
							message_poster.addAttachImage(uri);
					}
				});
			}
		}
			break;
			
		case TAKE_PICUTRE_REQUEST: {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
			if (resultCode == RESULT_OK) {
				data = null;
				System.gc();

				message_poster.post( new Runnable () {
					public void run () {
						if (message_poster != null) {
							message_poster.addAttachImage(last_attach_picutre_path);
						}
					}
				});
			}
		}
			break;
			
		case TUTORIAL_REQUEST:
			tutorial_showing = false;
			if (extra_info == null)
				showExtraInfoLoading();
			else {
				if (waver_pending) {
					waver_pending = false;
					presentWaver();
				}
			}
			break;
			
		case LOGIN_FACEBOOK_REQUEST:
			if (resultCode == RESULT_OK)
				FacebookManager.getFacebook().authorizeCallback(requestCode, resultCode, data);
			break;
			
		default:
			break;
		}
	}
	
	/**
	 * Listener for listening Facebook dialog.
	 * @author Kencool
	 *
	 */
	private class FacebookDialogListener implements DialogListener {
		
		private FBPendingCommand command = null;
		
		public FacebookDialogListener (FBPendingCommand command) {
			this.command = command;
		}
		
		@Override
		public void onCancel() {
		}
		@Override
		public void onComplete(Bundle bundle) {
			FacebookManager.loadFacebookService();
			FacebookManager.setLinkProfile();
			
			if (command != null)
				command.execute();
		}
		@Override
		public void onError(DialogError error) {
			new AlertDialog.Builder(ProgramActivity.this)
						   .setTitle("Facebook Error")
						   .setMessage(error.getMessage())
						   .setNeutralButton(R.string.action_confirm, null)
						   .show();
		}
		@Override
		public void onFacebookError(FacebookError facebookError) {
		}
	}
	
	/**
	 * Comment scale listener.
	 * @author Kencool
	 *
	 */
	private class CommentScaleListener implements OnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			return true;
		}

		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			return true;
		}

		@Override
		public void onScaleEnd(ScaleGestureDetector detector) {
			comment_scaled = detector.getScaleFactor() >= 1;
			info_layout.setVisibility(comment_scaled ? View.GONE : View.VISIBLE);
			viewer_layout.setVisibility(comment_scaled ? View.GONE : View.VISIBLE);
		}	
	}
	
	/**
	 * Program Activity Broadcast Receiver class
	 * @author Kencool
	 *
	 */
	private class ProgramReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// get action
			String action = intent.getAction();
			
			if (action.equals(FWXMPPManager.BROADCAST_USER_WILL_RECONNECT_XMPP)) {
				if (!isDestroyed()) {
					notification_textview.post( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							// we only wait for reconnecting muc if chat jid is already retrieved
							if (chat_jid != null) {
								notification_layout.setVisibility(View.VISIBLE);
								notification_textview.setText(R.string.program_notify_reconnect);
							}
							refreshComments();
							muc_friends_orders.clear();
							muc_friends.clear();
							refreshViewers();
							muc_reconnecting = true;
						}
					});
				}
			}
		}
		
	}
}
