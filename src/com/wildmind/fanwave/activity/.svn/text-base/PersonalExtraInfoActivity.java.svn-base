package com.wildmind.fanwave.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.facebook.FacebookManager;
import com.wildmind.fanwave.friend.FriendManager;
import com.wildmind.fanwave.media.AttachImageManager;
import com.wildmind.fanwave.media.AvatarManager;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.media.MediaFileManager;
import com.wildmind.fanwave.profile.FacebookProfile;
import com.wildmind.fanwave.user.TVUser;
import com.wildmind.fanwave.user.TVUserExtraInfo;
import com.wildmind.fanwave.user.TVUserPrivacy;
import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.animation.AlphaSetter;
import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.comment.AttachImageAdapter;
import com.wildmind.fanwave.comment.Attachment;
import com.wildmind.fanwave.comment.CommentManager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

/**
 * Intent input data "user" : TVUser
 * @author Kencool
 *
 */

public class PersonalExtraInfoActivity extends BaseActivity {

	private final int TAKE_PICUTRE_REQUEST 	= 0;
	private final int PICK_PHOTO_REQUEST 	= 1;
	private final int USE_FACEBOOK_REQUEST	= 2;
	private final int CROP_AVATAR_REQUEST	= 3;
	
	private ImageView 		avatar_imageview;
	private LinearLayout	media_layout;
	private Gallery 		media_gallery;
	private Button			detail_segment_button, intro_segment_button, link_segment_button;
	private ViewFlipper		extrainfo_viewflipper;
	
	/**
	 * Detail UI
	 */
	private TextView	email_textview, gender_textview, birth_textview, location_textview, intro_textview;
	
	private TVUser user = null;
	private String avatar_filepath = null;
	private AttachImageAdapter media_gallery_adapter = null;
	private boolean is_self = false;
	
	/**
	 * Activity Life Cycle Methods
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// check if activity is destroyed due to abnormal situation
		if (isDestroyed())
			return;
		
		setContentView(R.layout.personal_extrainfo_activity);
		
		initData();
		initUI();
		getMedia();
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
		
		avatar_imageview = null;
		media_layout = null;
		media_gallery = null;
		detail_segment_button = null;
		intro_segment_button = null;
		link_segment_button = null;
		
		if (media_gallery_adapter != null)
			media_gallery_adapter.clear();
		media_gallery_adapter = null;
		
		if (extrainfo_viewflipper != null)
			extrainfo_viewflipper.removeAllViews();
		extrainfo_viewflipper = null;
		
		// detail
		email_textview = null;
		gender_textview = null;
		birth_textview = null;
		location_textview = null;
		
		// intro
		intro_textview = null;
		
		user = null;
		avatar_filepath = null;
	}

	private void initData() {
		Intent i = getIntent();
		user = (TVUser) i.getParcelableExtra("user");
		
		is_self = AccountManager.getCurrentUser().getUsername().equals(user.getUsername());
	}

	private void initUI() {
		initTitleBarUI();
		initAvatarUI();
		initPointUI();
		initMediaUI();
		initSegmentUI();
		
		// View Flipper
		extrainfo_viewflipper = (ViewFlipper) findViewById(R.id.extrainfo_viewflipper);
		initDetailUI();
		initIntroUI();
		initLinkUI();
	}
	
	private void initTitleBarUI () {
		// Back Button
		ImageButton back_button = (ImageButton) findViewById(R.id.back_imagebutton);
		back_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		AvatarManager.drawAvatar(back_button, user.getUsername(), true);
		
		// Title
		TextView title = (TextView) findViewById(R.id.title_textview);
		title.setText(getString(R.string.personal_extrainfo_about) + " " + user.getNickname());
	}
	
	private void initAvatarUI () {
		// Avatar Image
		avatar_imageview = (ImageView) findViewById(R.id.avatar_imageview);
		loadAvatar();
		if (is_self)
			avatar_imageview.setOnClickListener(getAvatarClickedListener());
		
		// Avatar Edit
		TextView avatar_edit = (TextView) findViewById(R.id.edit_textview);
		avatar_edit.setVisibility(is_self ? View.VISIBLE : View.INVISIBLE);
	}
	
	private void initPointUI () {
		// wave in
		TextView wavein_point = (TextView) findViewById(R.id.wavein_point_textview);
		wavein_point.setText(String.valueOf(user.getScores().getWavein()));
		
		// follow
		TextView favorite_point = (TextView) findViewById(R.id.favorite_point_textview);
		favorite_point.setText(String.valueOf(user.getScores().getFollow()));
		
		// comment
		TextView comment_point = (TextView) findViewById(R.id.comment_point_textview);
		comment_point.setText(String.valueOf(user.getScores().getComment()));
	}
	
	private void initMediaUI () {
		media_layout = (LinearLayout) findViewById(R.id.media_layout);
		media_layout.setVisibility(View.GONE);
		media_gallery = (Gallery) findViewById(R.id.media_gallery);
	}
	
	private void initSegmentUI () {
		View.OnClickListener listener = getSegmentClickedListener();
		detail_segment_button = (Button) findViewById(R.id.detail_segment_button);
		detail_segment_button.setOnClickListener(listener);
		detail_segment_button.setSelected(true);
		intro_segment_button = (Button) findViewById(R.id.intro_segment_button);
		intro_segment_button.setOnClickListener(listener);
		link_segment_button = (Button) findViewById(R.id.link_segment_button);
		link_segment_button.setOnClickListener(listener);
	}
	
	private void initDetailUI () {
		ScrollView detail_view = (ScrollView) View.inflate(this, R.layout.personal_extrainfo_detail, null);;
		email_textview = (TextView) detail_view.findViewById(R.id.email_textview);
		gender_textview = (TextView) detail_view.findViewById(R.id.gender_textview);
		birth_textview = (TextView) detail_view.findViewById(R.id.birth_textview);
		location_textview = (TextView) detail_view.findViewById(R.id.location_textview);
		
		// set detail values
		//
		email_textview.setText(getEmailValue());
		gender_textview.setText(getGenderValue());
		birth_textview.setText(getBirthValue());
		location_textview.setText(user.getExtraInfo().getLocationCity());
		
		// if is self, details are able to modify
		//
		if (is_self) {
			email_textview.startAnimation(new AlphaSetter(0.3f));
			RelativeLayout gender = (RelativeLayout) detail_view.findViewById(R.id.gender_layout);
			gender.setOnClickListener(getGenderClickedListener());
			RelativeLayout birth = (RelativeLayout) detail_view.findViewById(R.id.birth_layout);
			birth.setOnClickListener(getBirthClickedListener());
			RelativeLayout location = (RelativeLayout) detail_view.findViewById(R.id.location_layout);
			location.setOnClickListener(getLocationClickedListener());
			
			((ImageView)gender.findViewById(R.id.imv_edit)).setVisibility(View.VISIBLE);
			((ImageView)birth.findViewById(R.id.imv_edit)).setVisibility(View.VISIBLE);
			((ImageView)location.findViewById(R.id.imv_edit)).setVisibility(View.VISIBLE);
		} 
		
		extrainfo_viewflipper.addView(detail_view, 0);
	}
	
	private void initIntroUI () {
		FrameLayout intro_view = (FrameLayout) View.inflate(this, R.layout.personal_extrainfo_intro, null);
		if (is_self)
			intro_view.setOnClickListener(getIntroClickedListener());
		intro_textview = (TextView) intro_view.findViewById(R.id.intro_textview);
		updateIntroTextGravity(user.getExtraInfo().getBio());
		
		extrainfo_viewflipper.addView(intro_view, 1);
	}
	
	private void initLinkUI () {
		RelativeLayout link_view = (RelativeLayout) View.inflate(this, R.layout.personal_extrainfo_link, null);
		
		// facebook
		Button facebook = (Button) link_view.findViewById(R.id.link_facebook_button);
		if (!is_self) {
			// is other, we should check whether facebook button is enabled
			boolean enable = true;
			// no facebook id, disable facebook button
			if ((user.getFbid() == null || user.getFbid().length() == 0))
				enable = false;
			// privacy forbidden
			if (user.getPrivacy().getFacebook().equals(TVUserPrivacy.PRIVACY_NONE))
				enable = false;
			// privacy forbidden
			if (user.getPrivacy().getFacebook().equals(TVUserPrivacy.PRIVACY_FRIEND) &&
				!FriendManager.isFriend(user.getUsername()))
				enable = false;
			
			if (enable)
				facebook.setOnClickListener(getFacebookClickedListener());
			else {
				facebook.startAnimation(new AlphaSetter(0.5f));
				facebook.setEnabled(false);
			}
		} else
			facebook.setOnClickListener(getFacebookClickedListener());
		
		// blog
		Button blog = (Button) link_view.findViewById(R.id.link_blog_button);
		if (is_self || user.getExtraInfo().getBlog().length() > 0)
			blog.setOnClickListener(getBlogClickedListener());
		else {
			blog.startAnimation(new AlphaSetter(0.5f));
			blog.setEnabled(false);
		}
		
		// website
		Button website = (Button) link_view.findViewById(R.id.link_website_button);
		if (is_self || user.getExtraInfo().getWebsite().length() > 0)
			website.setOnClickListener(getWebsiteClickedListener());
		else {
			website.startAnimation(new AlphaSetter(0.5f));
			website.setEnabled(false);
		}
		
		extrainfo_viewflipper.addView(link_view, 2);
	}
	
	/**
	 * Load avatar. Here we don't use image listener since avatar is only loaded once. Just try redraw 
	 * after certain interval if failed.
	 */
	private void loadAvatar () {
		if (isDestroyed())
			return;
		
		if (!ImageManager.drawAvatarImage(avatar_imageview, user.getUsername()))
			avatar_imageview.postDelayed(new Runnable () {
				public void run () {
					loadAvatar();
				}
			}, 1000);
	}
	
	private String getEmailValue () {
		String email = "";
		if (is_self || user.getPrivacy().getEmail().equals(TVUserPrivacy.PRIAVCY_ALL) ||
			(user.getPrivacy().getEmail().equals(TVUserPrivacy.PRIVACY_FRIEND) && FriendManager.isFriend(user.getUsername())))
			email = user.getEmail();
		
		return email;
	}
	private String getGenderValue () {
		Log.i("ExtraInfo", "gender = " + user.getPrivacy().getGender());
		String gender = "";
		if (is_self || user.getPrivacy().getGender().equals(TVUserPrivacy.PRIAVCY_ALL) ||
			(user.getPrivacy().getGender().equals(TVUserPrivacy.PRIVACY_FRIEND) && FriendManager.isFriend(user.getUsername()))) {
			gender = user.getExtraInfo().getGender().equals(TVUserExtraInfo.GENDER_MALE) 
					   ? getResources().getString(R.string.extrainfo_gender_male)
					   : getResources().getString(R.string.extrainfo_gender_female);
		}
		
		return gender;
	}
	private String getBirthValue () {
		String birth = user.getExtraInfo().getBirthday();
		if (is_self || user.getPrivacy().getBirth().equals(TVUserPrivacy.PRIVACY_SHOW_YEAR))
			return birth;
		else {
			if (user.getPrivacy().getBirth().equals(TVUserPrivacy.PRIVACY_NO_YEAR) &&
				birth.length() > 4)
				return user.getExtraInfo().getBirthday().substring(0, 5);
			else
				return "";
		}
	}
	
	/**
	 * Get media.
	 */
	private void getMedia () {
		new Thread (new Runnable () {
			public void run () {
				final ArrayList<Attachment> images = CommentManager.getUserImageAttachment(user.getUsername());
				
				if (!isDestroyed() && images.size() > 0) {
					media_layout.post(new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							media_layout.setVisibility(View.VISIBLE);
							media_gallery.setOnItemClickListener(getMediaClickedlistener());
							media_gallery_adapter = new AttachImageAdapter(media_gallery, images, PersonalExtraInfoActivity.this);
							media_gallery.setAdapter(media_gallery_adapter);
							
							if (images.size() > 2)
								media_gallery.setSelection(1);
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Take a picture.
	 */
	private void takePicture () {	
		// generate file
		File file = AttachImageManager.generaterFile();
		avatar_filepath = file.getAbsolutePath();
				
		// start activity
		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		startActivityForResult(i, TAKE_PICUTRE_REQUEST);
	}
	
	/**
	 * Pick up a photo.
	 */
	private void pickPhoto () {
		Intent i = new Intent();
		i.setType("image/*");
		i.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(i, "Select Picture"), PICK_PHOTO_REQUEST);
	}
	
	/**
	 * Use Facebook picture.
	 */
	private void useFacebook () {
		// set avatar image to loading
		avatar_imageview.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.avatar_loading));
		
		// get and upload avatar
		new Thread(new Runnable() {
			public void run() {
				final boolean success = FacebookManager.getUserPicture();
				
				if (!isDestroyed()) {
					avatar_imageview.post( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							if (success) {
								ImageManager.drawAvatarImage(avatar_imageview, user.getUsername());
								
								// broadcast refresh avatar
								Intent i = new Intent();
								i.setAction(AvatarManager.BROADCAST_REFRESH_AVATAR);
								ApplicationManager.getAppContext().sendBroadcast(i);
							} else
								Toast.makeText( PersonalExtraInfoActivity.this, 
									R.string.personal_extrainfo_modify_failed, Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}
	
	private void cropAvatar () {
		if (avatar_filepath == null)
			return;
		
		// check file exist
		File file = new File(avatar_filepath);
		if (!file.exists()) {
			Toast.makeText(this, R.string.attach_fetch_image_failed, Toast.LENGTH_SHORT);
			return;
		}
				
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");
        
        // check if crop available
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
        int size = list.size();
        if (size == 0) {
        	// no crop app
        	processAvatarImage();
        } else {
        	// set crop parameters
        	intent.setData(Uri.fromFile(new File(avatar_filepath)));
        	intent.putExtra("outputX", 500);
        	intent.putExtra("outputY", 500);
        	intent.putExtra("aspectX", 1);
        	intent.putExtra("aspectY", 1);
        	intent.putExtra("scale", true);
        	intent.putExtra("return-data", true);
            
        	// open crop app
        	Intent i = new Intent(intent);
            ResolveInfo res = list.get(0);
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            startActivityForResult(i, CROP_AVATAR_REQUEST);
        }
	}
	
	/**
	 * Process avatar image.
	 */
	private void processAvatarImage () {
		// check file exist
		File file = new File(avatar_filepath);
		if (!file.exists()) {
			Toast.makeText(this, R.string.attach_fetch_image_failed, Toast.LENGTH_SHORT);
			return;
		}
		
		// set avatar image to loading
		avatar_imageview.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.avatar_loading));
		
		// upload avatar
		new Thread (new Runnable () {
			public void run () {
				final boolean success = AvatarManager.uploadAvatar(avatar_filepath);
				// delete temp avatar after uploaded
				File file = new File(avatar_filepath);
				if (file.exists())
					file.delete();
				
				if (!isDestroyed()) {
					avatar_imageview.post( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							if (success) {
								AvatarManager.deleteUserAvatar();
								loadAvatar();
							} else
								Toast.makeText( PersonalExtraInfoActivity.this, 
									R.string.personal_extrainfo_modify_failed, Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Update gender.
	 * @param gender
	 */
	private void updateGender (final String gender) {
		// show loading progress dialog
		final ProgressDialog pd = ProgressDialog.show(this, "", getString(R.string.action_saving));
		new Thread (new Runnable () {
			public void run () {
				// update gender
				final TVUserExtraInfo extrainfo = new TVUserExtraInfo(user.getExtraInfo());
				extrainfo.setGender(gender);
				final boolean success = AccountManager.updateExtraInfo(extrainfo);
				
				if (!isDestroyed()) {
					gender_textview.post( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							// dismiss loading progress dialog
							pd.dismiss();
							
							// refresh gender
							if (success) {
								user.setExtraInfo(extrainfo);
								gender_textview.setText(getGenderValue());
							} else
								Toast.makeText(PersonalExtraInfoActivity.this, 
									R.string.personal_extrainfo_modify_failed, Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Update birthday.
	 * @param birthday
	 */
	private void updateBirthday (final String birthday) {
		// show loading progress dialog
		final ProgressDialog pd = ProgressDialog.show(this, "", getString(R.string.action_saving));
		new Thread (new Runnable () {
			public void run () {
				// update birthday
				final TVUserExtraInfo extrainfo = new TVUserExtraInfo(user.getExtraInfo());
				extrainfo.setBirthday(birthday);
				final boolean success = AccountManager.updateExtraInfo(extrainfo);
				
				if (!isDestroyed()) {
					birth_textview.post( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							// dismiss loading progress dialog
							pd.dismiss();
							
							// refresh birthday
							if (success) {
								user.setExtraInfo(extrainfo);
								birth_textview.setText(getBirthValue());
							} else
								Toast.makeText(PersonalExtraInfoActivity.this, 
									R.string.personal_extrainfo_modify_failed, Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Update location.
	 * @param locationCity
	 * @param location
	 */
	private void updateLocation (final String locationCity, final Location location) {
		// show loading progress dialog
		final ProgressDialog pd = ProgressDialog.show(this, "", getString(R.string.action_saving));
		new Thread (new Runnable () {
			public void run () {
				// update location
				final TVUserExtraInfo extrainfo = new TVUserExtraInfo(user.getExtraInfo());
				extrainfo.setLocationCity(locationCity);
				extrainfo.setLocationLatitude(String.valueOf(location.getLatitude()));
				extrainfo.setLocationLatitude(String.valueOf(location.getLongitude()));
				final boolean success = AccountManager.updateExtraInfo(extrainfo);
				
				if (!isDestroyed()) {
					location_textview.post( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							// dismiss loading progress dialog
							pd.dismiss();
							
							// refresh location
							if (success) {
								user.setExtraInfo(extrainfo);
								location_textview.setText(user.getExtraInfo().getLocationCity());
							} else
								Toast.makeText(PersonalExtraInfoActivity.this, 
									R.string.personal_extrainfo_modify_failed, Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Update intro.
	 * @param intro
	 */
	private void updateIntro (final String intro) {
		// show loading progress dialog
		final ProgressDialog pd = ProgressDialog.show(this, "", getString(R.string.action_saving));
		new Thread (new Runnable () {
			public void run () {
				// update intro
				final TVUserExtraInfo extrainfo = new TVUserExtraInfo(user.getExtraInfo());
				extrainfo.setBio(intro);
				final boolean success = AccountManager.updateExtraInfo(extrainfo);
				
				if (!isDestroyed()) {
					intro_textview.post( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							// dismiss loading progress dialog
							pd.dismiss();
							
							// refresh intro
							if (success) {
								user.setExtraInfo(extrainfo);
								updateIntroTextGravity(user.getExtraInfo().getBio());
							} else
								Toast.makeText(PersonalExtraInfoActivity.this, 
									R.string.personal_extrainfo_modify_failed, Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Update blog.
	 * @param blog
	 */
	private void updateBlog (final String blog) {
		// show loading progress dialog
		final ProgressDialog pd = ProgressDialog.show(this, "", getString(R.string.action_saving));
		new Thread (new Runnable () {
			public void run () {
				// update blog
				final TVUserExtraInfo extrainfo = new TVUserExtraInfo(user.getExtraInfo());
				extrainfo.setBlog(blog);
				final boolean success = AccountManager.updateExtraInfo(extrainfo);
				
				if (!isDestroyed()) {
					PersonalExtraInfoActivity.this.runOnUiThread( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							// dismiss loading progress dialog
							pd.dismiss();
							
							// refresh blog
							if (success) 
								user.setExtraInfo(extrainfo);
							else
								Toast.makeText(PersonalExtraInfoActivity.this, 
									R.string.personal_extrainfo_modify_failed, Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Update website.
	 * @param website
	 */
	private void updateWebsite (final String website) {
		// show loading progress dialog
		final ProgressDialog pd = ProgressDialog.show(this, "", getString(R.string.action_saving));
		new Thread (new Runnable () {
			public void run () {
				// update website
				final TVUserExtraInfo extrainfo = new TVUserExtraInfo(user.getExtraInfo());
				extrainfo.setWebsite(website);
				final boolean success = AccountManager.updateExtraInfo(extrainfo);
				
				if (!isDestroyed()) {
					PersonalExtraInfoActivity.this.runOnUiThread( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							// dismiss loading progress dialog
							pd.dismiss();
							
							// refresh website
							if (success) 
								user.setExtraInfo(extrainfo);
							else
								Toast.makeText(PersonalExtraInfoActivity.this, 
									R.string.personal_extrainfo_modify_failed, Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}
	
	private void updateIntroTextGravity(String strIntro){
		if(strIntro.length()>0){
	    		intro_textview.setText(strIntro);
	    		intro_textview.setGravity(android.view.Gravity.LEFT|android.view.Gravity.TOP);
            intro_textview.setTextColor(getResources().getColor(R.color.gray));
	    }
	    else{
	        intro_textview.setText(is_self?getString(R.string.extrainfo_no_result_self_intro):getString(R.string.extrainfo_no_result_intro));
	        intro_textview.setGravity(android.view.Gravity.CENTER);
            intro_textview.setTextColor(getResources().getColor(R.color.no_result_text_color));
	    }
	}
	
	/**
	 * Callback for avatar clicked.
	 * @return
	 */
	private View.OnClickListener getAvatarClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {	
				// item listener
				DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int option) {
						switch (option) {
						case 0:
							takePicture();
							break;
						case 1:
							pickPhoto();
							break;
						case 2:
							useFacebook();
							break;
						default:
							break;
						}
					}
				};
				
				final String [] str_array = getResources().getStringArray(R.array.extrainfo_avatar_array);
				final int [] id_array = { R.drawable.attach_file_option_camera, R.drawable.attach_file_optioin_album, R.drawable.attach_file_option_fb };
				
				// dialog list adapter
				final LayoutInflater inflater = LayoutInflater.from(PersonalExtraInfoActivity.this);
				ListAdapter adapter = new BaseAdapter() {
					public long getItemId(int position) {
						return position;
					}
					public Object getItem(int position) {
						return position;
					}
					public int getCount() {
						FacebookProfile fp = new FacebookProfile(getApplicationContext());
						return fp.isFacebookActive() ? str_array.length : str_array.length - 1;
					}
					public View getView(int position, View view, ViewGroup parent) {
						view = inflater.inflate(R.layout.attach_file_option_view, null);
						((ImageView)view.findViewById(R.id.attach_file_dialog_option_imageview)).setImageBitmap(ImageManager.getBitmapFromResources(id_array[position]));
						((TextView)view.findViewById(R.id.attach_file_dialog_option_textview)).setText(str_array[position]);
						
						return view;
					}
				};
				
				// alert dialog
				new AlertDialog.Builder(PersonalExtraInfoActivity.this)
							   .setTitle(R.string.attach_title)
							   .setPositiveButton(R.string.action_cancel, null)
							   .setAdapter(adapter, listener)
							   .show();
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
				Attachment image = (Attachment) media_gallery_adapter.getItem(position);
				Intent i = new Intent(PersonalExtraInfoActivity.this, ImagePresentActivity.class);
				i.putExtra("present_type", ImagePresentActivity.ATTACH_IMAGE_PRESENT);
				i.putExtra("attachment_description", image.getDescription());
				i.putExtra("attachment", image);
				startActivity(i);
			}
		};	
	}
	
	/**
	 * Callback for segments clicked.
	 * @return
	 */
	private View.OnClickListener getSegmentClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				detail_segment_button.setSelected(v.getId() == R.id.detail_segment_button);
				intro_segment_button.setSelected(v.getId() == R.id.intro_segment_button);
				link_segment_button.setSelected(v.getId() == R.id.link_segment_button);
				switch (v.getId()) {
				case R.id.detail_segment_button:
					extrainfo_viewflipper.setDisplayedChild(0);
					break;
				case R.id.intro_segment_button:
					extrainfo_viewflipper.setDisplayedChild(1);
					break;
				case R.id.link_segment_button:
					extrainfo_viewflipper.setDisplayedChild(2);
					break;
				default:
					break;
				}
			}
		};
	}
	
	/**
	 * Callback for gender clicked.
	 * @return
	 */
	private View.OnClickListener getGenderClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!is_self)
					return;
				
				// item listener
				DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener () {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						final String gender = which == 0 ? TVUserExtraInfo.GENDER_MALE : TVUserExtraInfo.GENDER_FEMALE;
						if (!gender.equals(user.getExtraInfo().getGender())) 
							updateGender(gender);
						
						dialog.dismiss();
					}	
				};
				
				// default checked item
				int checkedItem = user.getExtraInfo().getGender().equals(TVUserExtraInfo.GENDER_MALE) ? 0 : 1;
				
				// alert dialog
				String[] items = new String[] { getResources().getString(R.string.extrainfo_gender_male),
												getResources().getString(R.string.extrainfo_gender_female) };
				new AlertDialog.Builder(PersonalExtraInfoActivity.this)
							   .setTitle(R.string.personal_extrainfo_detail_gender)
							   .setSingleChoiceItems(items, checkedItem, listener)
							   .setPositiveButton(R.string.action_cancel, null)
							   .show();
			}
		};
	}
	
	/**
	 * Callback for birth clicked.
	 * @return
	 */
	private View.OnClickListener getBirthClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!is_self)
					return;
				
				// date set listener
				DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener () {
					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						monthOfYear++;
						final String birthday = String.format("%02d", monthOfYear) + "/" + 
												String.format("%02d", dayOfMonth) + "/" + 
												String.format("%04d", year);
						if (!birthday.equals(user.getExtraInfo().getBirthday()))
							updateBirthday(birthday);
					}
				};
				
				// default date value
				int year;
				int month;
				int day;
				String birth = user.getExtraInfo().getBirthday();
				if (birth.length() > 0) {
					year = Integer.valueOf(birth.substring(6, 10));
					month = Integer.valueOf(birth.substring(0, 2)) - 1;
					day = Integer.valueOf(birth.substring(3, 5));
				} else {
					Calendar cal = Calendar.getInstance();
					year = cal.get(Calendar.YEAR);
					month = cal.get(Calendar.MONTH);
					day = cal.get(Calendar.DATE);
				}
				
				// date picker dialog
				new DatePickerDialog(PersonalExtraInfoActivity.this, listener, year, month, day).show();
			}
		};
	}
	
	/**
	 * Callback for location clicked.
	 * @return
	 */
	private View.OnClickListener getLocationClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!is_self)
					return;
				
				// set locating wording
				location_textview.setText(R.string.extrainfo_locating);
				
				new Thread (new Runnable () {
					public void run () {
						// get location
						LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
						final Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						
						// get city
						String city = "";
						try {
							Geocoder gcd = new Geocoder(PersonalExtraInfoActivity.this, Locale.getDefault());
							List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 3);
							if (addresses.size() > 0) 
								city = addresses.get(0).getLocality();
							
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						// get country
						String country = getResources().getConfiguration().locale.getDisplayCountry();
						
						final String newLocation = city.length() > 0 ? city + ", " + country : "";
						
						if (!isDestroyed()) {
							location_textview.post( new Runnable () {
								public void run () {
									if (isDestroyed())
										return;
									
									location_textview.setText(user.getExtraInfo().getLocationCity());
									showUpdateLocationDialog(newLocation, location);
								}
							});
						}
					}
				}).start();
			}
		};
	}
	
	/**
	 * Show update location dialog.
	 */
	private void showUpdateLocationDialog (final String newLocation, final Location location) {
		// Location Edit Text
		final EditText locationEditText = new EditText(PersonalExtraInfoActivity.this);
		locationEditText.setTextSize(14);
		locationEditText.setText(newLocation);
		
		// Button
		DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener () {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String updateLocation = locationEditText.getText().toString();
				if (!updateLocation.equals(user.getExtraInfo().getLocationCity())) 
					updateLocation(updateLocation, location);
			}
		};
		
		// Dialog
		new AlertDialog.Builder(PersonalExtraInfoActivity.this)
					   .setTitle(R.string.personal_extrainfo_detail_loaction)
					   .setView(locationEditText)
					   .setPositiveButton(R.string.action_confirm, positive)
					   .setNegativeButton(R.string.action_cancel, null)
					   .show();
	}
	
	/**
	 * Callback for intro text clicked.
	 * @return
	 */
	private View.OnClickListener getIntroClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Intro Edit Text
				final EditText introEditText = new EditText(PersonalExtraInfoActivity.this);
				introEditText.setTextSize(14);
				introEditText.setText(user.getExtraInfo().getBio());
				
				// Button
				DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener () {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						updateIntro(introEditText.getText().toString());
					}
				};
				
				// Dialog
				AlertDialog dialog = new AlertDialog.Builder(PersonalExtraInfoActivity.this)
							   .setTitle(R.string.personal_extrainfo_segment_intro)
							   .setView(introEditText)
							   .setPositiveButton(R.string.action_confirm, positive)
							   .setNegativeButton(R.string.action_cancel, null)
							   .create();
				dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
				dialog.show();
			}
		};
	}
	
	/**
	 * Callback for Facebook buttons clicked.
	 * @return
	 */
	private View.OnClickListener getFacebookClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String url = "http://m.facebook.com/profile.php?id=" + user.getFbid();
				Intent i = new Intent(PersonalExtraInfoActivity.this, WebViewActivity.class);
				i.putExtra("url", url);
				startActivity(i);
			}
		};
	}
	
	/**
	 * Callback for blog buttons clicked.
	 * @return
	 */
	private View.OnClickListener getBlogClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!is_self) {
					Intent i = new Intent(PersonalExtraInfoActivity.this, WebViewActivity.class);
					i.putExtra("url", user.getExtraInfo().getBlog());
					startActivity(i);
				} else {
					if (user.getExtraInfo().getBlog().length() == 0)
						showUpdateBlogDialog();
					else {
						// item listener
						DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener () {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (which == 0)
									showUpdateBlogDialog();
								else {
									Intent i = new Intent(PersonalExtraInfoActivity.this, WebViewActivity.class);
									i.putExtra("url", user.getExtraInfo().getBlog());
									startActivity(i);
								}
							}					
						};
						
						// alert dialog
						new AlertDialog.Builder(PersonalExtraInfoActivity.this)
									   .setTitle("Blog")
									   .setPositiveButton(R.string.action_cancel, null)
									   .setItems(R.array.extrainfo_blog_array, listener)
									   .show();
					}
				}
			}
		};
	}
	
	/**
	 * Show update blog dialog.
	 */
	private void showUpdateBlogDialog () {
		LinearLayout view = new LinearLayout(PersonalExtraInfoActivity.this);
		TextView header = new TextView(PersonalExtraInfoActivity.this);
		header.setTextSize(14);
		header.setText("http://");
		view.addView(header);
	
		// Blog Edit Text
		final EditText blogEditText = new EditText(PersonalExtraInfoActivity.this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 
																		 LayoutParams.WRAP_CONTENT);
		blogEditText.setLayoutParams(params);
		blogEditText.setTextSize(14);
		blogEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
		String blog = user.getExtraInfo().getBlog();
		if (blog != null && blog.length() > 0)
			blog = blog.substring(7);
		blogEditText.setText(blog);
		view.addView(blogEditText);
		
		// Button
		DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener () {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String newBlog = blogEditText.getText().toString();
				if (newBlog.length() > 0)
					newBlog = "http://" + newBlog;
				if (!newBlog.equals(user.getExtraInfo().getBlog()))
					updateBlog(newBlog);
			}
		};
		
		// Dialog
		AlertDialog dialog = new AlertDialog.Builder(PersonalExtraInfoActivity.this)
					   .setTitle(R.string.extrainfo_link_blog_update)
					   .setView(view)
					   .setPositiveButton(R.string.action_confirm, positive)
					   .setNegativeButton(R.string.action_cancel, null)
					   .create();
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		dialog.show();
	}
	
	/**
	 * Callback for website buttons clicked.
	 * @return
	 */
	private View.OnClickListener getWebsiteClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!is_self) {
					Intent i = new Intent(PersonalExtraInfoActivity.this, WebViewActivity.class);
					i.putExtra("url", user.getExtraInfo().getWebsite());
					startActivity(i);
				} else {
					if (user.getExtraInfo().getWebsite().length() == 0)
						showUpdateWebsiteDialog();
					else {
						// item listener
						DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener () {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (which == 0)
									showUpdateWebsiteDialog();
								else {
									Intent i = new Intent(PersonalExtraInfoActivity.this, WebViewActivity.class);
									i.putExtra("url", user.getExtraInfo().getWebsite());
									startActivity(i);
								}
							}					
						};
						
						// alert dialog
						new AlertDialog.Builder(PersonalExtraInfoActivity.this)
									   .setTitle("Website")
									   .setPositiveButton(R.string.action_cancel, null)
									   .setItems(R.array.extrainfo_website_array, listener)
									   .show();
					}
				}
			}
		};
	}
	
	/**
	 * Show update website dialog.
	 */
	private void showUpdateWebsiteDialog () {
		LinearLayout view = new LinearLayout(PersonalExtraInfoActivity.this);
		TextView header = new TextView(PersonalExtraInfoActivity.this);
		header.setTextSize(14);
		header.setText("http://");
		view.addView(header);
	
		// Website Edit Text
		final EditText websiteEditText = new EditText(PersonalExtraInfoActivity.this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 
				 														 LayoutParams.WRAP_CONTENT);
		websiteEditText.setLayoutParams(params);
		websiteEditText.setTextSize(14);
		websiteEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
		String website = user.getExtraInfo().getWebsite();
		if (website != null && website.length() > 0)
			website = website.substring(7);
		websiteEditText.setText(website);
		view.addView(websiteEditText);
		
		// Button
		DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener () {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String newWebsite = websiteEditText.getText().toString();
				if (newWebsite.length() > 0)
					newWebsite = "http://" + newWebsite;
				if (!newWebsite.equals(user.getExtraInfo().getWebsite()))
					updateWebsite(newWebsite);
			}
		};
		
		// Dialog
		AlertDialog dialog = new AlertDialog.Builder(PersonalExtraInfoActivity.this)
					   .setTitle(R.string.extrainfo_link_website_update)
					   .setView(view)
					   .setPositiveButton(R.string.action_confirm, positive)
					   .setNegativeButton(R.string.action_cancel, null)
					   .create();
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		dialog.show();
	}
	
	/**
	 * onActivityResult
	 */
	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch(requestCode) {
		
		case TAKE_PICUTRE_REQUEST:
			if (resultCode == RESULT_OK) {
				data = null;
				System.gc();

				avatar_imageview.post( new Runnable () {
					public void run () {
						if (!isDestroyed())
							cropAvatar();
					}
				});
			}
			break;
			
		case PICK_PHOTO_REQUEST:
			if (resultCode == RESULT_OK) {
				final Uri uri = data.getData();
				data = null;
				System.gc();

				avatar_imageview.post( new Runnable () {
					public void run () {
						if (!isDestroyed() && uri != null) {
				        	Cursor cursor = ApplicationManager.getAppContext().getContentResolver().query(uri, 
				            		new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
				            cursor.moveToFirst();
				            avatar_filepath = cursor.getString(0);
				            cursor.close();
				        }
						cropAvatar();
					}
				});
			}
			break;
			
		case USE_FACEBOOK_REQUEST:
			break;
		
		case CROP_AVATAR_REQUEST:
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
                if (bundle != null) {
                    Bitmap bmp = bundle.getParcelable("data");
                    File file = AttachImageManager.generaterFile();
                    
                    // save new file
                    avatar_filepath = file.getAbsolutePath();
                    MediaFileManager.saveImageFile(avatar_filepath, bmp);
                }
                processAvatarImage();
			}
			break;
			
		default:
			break;
		}
	}
}
