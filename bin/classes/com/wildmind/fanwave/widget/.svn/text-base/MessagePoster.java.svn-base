package com.wildmind.fanwave.widget;

import java.io.BufferedInputStream;

import com.wildmind.fanwave.animation.AlphaSetter;
import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.comment.Attachment;
import com.wildmind.fanwave.comment.CommentManager;
import com.wildmind.fanwave.facebook.FBPendingCommand;
import com.wildmind.fanwave.facebook.FacebookManager;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.network.NetworkManager;
import com.wildmind.fanwave.activity.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MessagePoster extends LinearLayout {

	public FrameLayout	attach_layout;
	public LinearLayout attach_link_layout, attach_image_layout;
	public ImageView	attach_imageview;
	public ProgressBar	attach_image_progressbar;
	public TextView		attach_image_textview;
	public TextView		attach_link_textview;
	public EditText 	message_edittext;
	public Button 		attach_button, post_button, facebook_post_button;
	
	private Context		context;
	private String		attach_type = "", link_address = "", image_path = "";
	private Attachment	attach = null;
	private Bitmap		image_thumbnail = null;
	private MessagePosterListener 	listener = null;
	
	private Runnable increase_progress = new Runnable () {
		public void run () {
			// request has been canceled
			if (image_path == null || image_path.length() == 0)
				return;
			
			if (attach_image_progressbar.getProgress() < 60) {
				attach_image_progressbar.setProgress(attach_image_progressbar.getProgress() + 1);
				attach_image_progressbar.postDelayed(increase_progress, 100);
			}else if (attach_image_progressbar.getProgress() < 90) {
				attach_image_progressbar.setProgress(attach_image_progressbar.getProgress() + 1);
				attach_image_progressbar.postDelayed(increase_progress, 200);
			}
		}
	};
	
	/**
	 * Constructor
	 * @param context
	 * @param attrs
	 */
	public MessagePoster(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.context = context;
	}
	
	/**
	 * Initialization
	 */
	public void init () {
		// Attach Layout
		View.OnClickListener attachLayoutListener = getAttachLayoutClickedListener();
		
		attach_layout = (FrameLayout) findViewById(R.id.attach_layout);
		attach_layout.setVisibility(View.INVISIBLE);
		
		attach_link_layout = (LinearLayout) findViewById(R.id.attach_link_layout);
		attach_link_layout.setOnClickListener(attachLayoutListener);
		
		attach_image_layout = (LinearLayout) findViewById(R.id.attach_image_layout);
		attach_image_layout.setOnClickListener(attachLayoutListener);
		
		attach_link_textview = (TextView) findViewById(R.id.attach_link_textview);
		attach_imageview = (ImageView) findViewById(R.id.attach_imageview);
		attach_image_progressbar = (ProgressBar) findViewById(R.id.attach_image_progressbar);
		attach_image_textview = (TextView) findViewById(R.id.attach_image_textview);
		
		// Message Edit Text
		message_edittext = (EditText) findViewById(R.id.message_edittext);
		
		// Attach Button
		attach_button = (Button) findViewById(R.id.attach_button);
		attach_button.setOnClickListener(getAttachButtonClickedListener());
		
		// Post Button
		post_button = (Button) findViewById(R.id.post_button);
		post_button.setOnClickListener(getPostClickedListener());
		facebook_post_button = (Button) findViewById(R.id.facebook_post_button);
		facebook_post_button.setOnClickListener(getFacebookPostClickedListener());
	}
	
	/**
	 * Clear all resources.
	 */
	public void clear () {
		attach_layout = null;
		attach_link_layout = null;
		attach_image_layout = null;
		attach_imageview = null;
		attach_image_progressbar = null;
		attach_image_textview = null;
		attach_link_textview = null;
		message_edittext = null;
		attach_button = null;
		post_button = null;
		facebook_post_button = null;
		
		context = null;
		attach_type = null;
		link_address = null;
		image_path = null;
		attach = null;
		image_thumbnail = null;
		listener = null;
		
		increase_progress = null;
	}
	
	// getters
	//
	public Attachment getAttach () {
		return attach;
	}
	
	// setters
	//
	public void setMessagePosterListener (MessagePosterListener listener) {
		this.listener = listener;
	}
	
	public boolean isUploading () {
		return attach_image_progressbar.getProgress() > 0 && 
			   attach_image_progressbar.getProgress() < 100;
	}
	
	/**
	 * Refresh poster content.
	 */
	public void refreshPost () {
		attach_type = "";
		link_address = "";
		image_path = "";
		image_thumbnail = null;
		attach = null;
		message_edittext.setText("");
		attach_image_progressbar.setProgress(0);
		
		attach_layout.setVisibility(View.INVISIBLE);
		attach_link_layout.setVisibility(View.INVISIBLE);
		attach_image_layout.setVisibility(View.INVISIBLE);
	}
	
	private void enablePost () {
		post_button.clearAnimation();
		post_button.startAnimation(new AlphaSetter(1));
		post_button.setEnabled(true);
		
		facebook_post_button.clearAnimation();
		facebook_post_button.startAnimation(new AlphaSetter(1));
		facebook_post_button.setEnabled(true);
	}
	
	private void disablePost () {
		post_button.clearAnimation();
		post_button.startAnimation(new AlphaSetter(0.5f));
		post_button.setEnabled(false);
		
		facebook_post_button.clearAnimation();
		facebook_post_button.startAnimation(new AlphaSetter(0.5f));
		facebook_post_button.setEnabled(false);
	}
	
	/**
	 * Start progressing.
	 */
	private void startProgressing () {
		resetProgressing();
		attach_image_progressbar.postDelayed(increase_progress, 100);
	}
	
	/**
	 * Finish progressing.
	 * @param success
	 */
	private void finishProgressing (final boolean success) {
		if (success) {
			attach_image_progressbar.setProgress(100);
			attach_image_progressbar.postDelayed( new Runnable () {
				public void run () {
					attach_image_progressbar.setVisibility(View.GONE);
					attach_image_textview.setVisibility(VISIBLE);
					attach_image_textview.setText(ApplicationManager.getAppContext().getString(R.string.attach_finish));
				}
			}, 1000);
		} else {
			attach_image_progressbar.setVisibility(View.GONE);
			attach_image_textview.setVisibility(VISIBLE);
			attach_image_textview.setText(ApplicationManager.getAppContext().getString(R.string.attach_failed));
		}
	}
	
	/**
	 * Reset progressing.
	 */
	private void resetProgressing () {
		attach_image_textview.setVisibility(View.GONE);
		attach_image_progressbar.setVisibility(View.VISIBLE);
		attach_image_progressbar.setProgress(0);
	}
	
	/**
	 * Add attach image.
	 * @param uri
	 */
	public void addAttachImage (Uri uri) {
		if (uri != null) {
        	Cursor cursor = context.getContentResolver().query(uri, 
            		new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
            cursor.moveToFirst();
            image_path = cursor.getString(0);
            cursor.close();
        }
		attachImage();
	}
	
	/**
	 * Add attach image.
	 * @param String
	 */
	public void addAttachImage (String imagePath) {
		image_path = imagePath;
		attachImage();
	}
	
	/**
	 * Attach image.
	 */
	private void attachImage () {
		confirmAttachImage();

		BufferedInputStream fileInputStream = NetworkManager.getFileInputStream(image_path);
		if (fileInputStream == null) {
			cancelAttachImage();
			return;
		}
		
		// calculate sample size
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(fileInputStream, null, opts);
		opts.inSampleSize = NetworkManager.getFitSampleSize(opts, ImageManager.SampleBase.RIGOROUS_SAMPLED);
		
		// get sampled image
		opts.inJustDecodeBounds = false;
		fileInputStream = NetworkManager.getFileInputStream(image_path);
		image_thumbnail = BitmapFactory.decodeStream(fileInputStream, null, opts);
		
		attach_imageview.setImageBitmap(image_thumbnail);
		uploadImage();
	}
	
	/**
	 * Reattach image.
	 */
	private void reattachImage () {
		DialogInterface.OnClickListener itemListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int option) {
				switch (option) {
				case 0:
					cancelAttachImage();
					break;
				case 1:
					if (listener != null)
						listener.onRequestTakePicture();
					break;
				case 2:
					if (listener != null)
						listener.onRequestPickPhoto();
					break;
				default:
					break;
				}
			}
		};
		
		final String [] str_array = context.getResources().getStringArray(R.array.reattach_image_array);
		final int [] id_array = { R.drawable.attach_file_option_delete, R.drawable.attach_file_option_camera, R.drawable.attach_file_optioin_album };
		
		// dialog list adapter
		final LayoutInflater inflater = LayoutInflater.from(context);
		ListAdapter adapter = new BaseAdapter() {
			public long getItemId(int position) {
				return position;
			}
			public Object getItem(int position) {
				return position;
			}
			public int getCount() {
				return str_array.length;
			}
			public View getView(int position, View view, ViewGroup parent) {
				view = inflater.inflate(R.layout.attach_file_option_view, null);
				((ImageView)view.findViewById(R.id.attach_file_dialog_option_imageview)).setImageBitmap(ImageManager.getBitmapFromResources(id_array[position]));
				((TextView)view.findViewById(R.id.attach_file_dialog_option_textview)).setText(str_array[position]);
				
				return view;
			}
		};
		
		// alert dialog
		new AlertDialog.Builder(context)
					   .setTitle(context.getString(R.string.attach_image_add))
					   .setPositiveButton(R.string.action_cancel, null)
					   .setAdapter(adapter, itemListener)
					   .show();
	}
	
	/**
	 * Upload attach image.
	 * @param filePath
	 */
	private void uploadImage () {
		startProgressing();
		
		new Thread (new Runnable () {
			public void run () {
				final String filePath = image_path;
				final Attachment am = CommentManager.postImage(image_path);
				
				if (attach_layout != null) {
					attach_layout.post( new Runnable () {
						public void run () {
							if (attach_layout  == null)
								return;
							if (!filePath.equals(image_path))
								return;
							
							attach = am;
							finishProgressing(attach != null);
							if (attach != null) {
								enablePost();
							} else {
								DialogInterface.OnClickListener retry = new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										uploadImage();
									}
								};
								DialogInterface.OnClickListener cancel = new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										cancelAttachImage();
									}
								}; 	 
								AlertDialog dialog = new AlertDialog.Builder(context)
								   			   .setTitle(R.string.attach_title)
								   			   .setMessage(R.string.attach_image_failed)
								   			   .setPositiveButton(R.string.action_retry, retry)
								   			   .setNegativeButton(R.string.action_cancel, cancel)
								   			   .create();
								dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
								dialog.show();
							}
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Attach a link.
	 */
	private void attachLink () {
		// Link Edit Text
		final EditText linkEditText = new EditText(context);
		linkEditText.setTextSize(14);
		linkEditText.setText(link_address);
				
		// Button
		int positive = link_address.length() == 0 ? R.string.action_confirm : R.string.action_modify;
		int negative = link_address.length() == 0 ? R.string.action_cancel : R.string.action_remove;
		
		DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener () {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				link_address = linkEditText.getText().toString();
				attach_link_textview.setText(link_address);
				if (link_address.length() > 0) 
					confirmAttachLink();
				else
					cancelAttachLink();
			}
		};
		DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener () {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				cancelAttachLink();
			}
		};
		
		// Dialog
		AlertDialog dialog = new AlertDialog.Builder(context)
					   .setTitle(R.string.attach_link_add)
					   .setView(linkEditText)
					   .setPositiveButton(positive, positiveListener)
					   .setNegativeButton(negative, negativeListener)
					   .create();
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		dialog.show();
	}
	
	/**
	 * Confirm attach a link.
	 */
	private void confirmAttachLink () {
		attach_type = Attachment.ATTACH_LINK;
		attach_layout.setVisibility(View.VISIBLE);
		attach_link_layout.setVisibility(View.VISIBLE);
		attach_image_layout.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * Cancel attach a link.
	 */
	private void cancelAttachLink () {
		attach_type = "";
		attach = null;
		link_address = "";
		attach_layout.setVisibility(View.INVISIBLE);
		attach_link_layout.setVisibility(View.INVISIBLE);
	}
	
	private void confirmAttachImage () {
		attach_type = Attachment.ATTACH_IMAGE;
		attach_layout.setVisibility(View.VISIBLE);
		attach_image_layout.setVisibility(View.VISIBLE);
		attach_link_layout.setVisibility(View.INVISIBLE);
		
		disablePost();
	}
	
	private void cancelAttachImage () {
		attach_type = "";
		image_path = "";
		attach = null;
		image_thumbnail = null;
		attach_layout.setVisibility(View.INVISIBLE);
		attach_image_layout.setVisibility(View.INVISIBLE);
		
		resetProgressing();
		enablePost();
	}
	
	/**
	 * Process post.
	 * @param onFacebook
	 */
	private void processPost (boolean onFacebook) {
		String message = message_edittext.getText().toString();
		if (message.length() == 0) {
			Toast.makeText(context, R.string.message_poster_no_content, Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (attach_type.equals(Attachment.ATTACH_LINK)) {
			processPostWithLink(message, onFacebook);
		} else if (attach_type.equals(Attachment.ATTACH_IMAGE)) {
			processPostWithImage(message, onFacebook);
		} else {
			if (listener != null)
				listener.onPost(message, onFacebook);
		}
	}
	
	/**
	 * Process post with link.
	 * @param message
	 * @param onFacebook
	 */
	private void processPostWithLink (String message, boolean onFacebook) {
		if (attach == null) 
			attach = new Attachment(Attachment.ATTACH_LINK);
		attach.setDescription(message);
		attach.setUrl(link_address);
		
		if (listener != null)
			listener.onPostWithLink(message, attach, onFacebook);
	}
	
	/**
	 * Process post with image.
	 * @param message
	 * @param onFacebook
	 */
	private void processPostWithImage (String message, boolean onFacebook) {
		attach.setDescription(message);
		if (listener != null)
			listener.onPostWithImage(message, image_path, attach, onFacebook);
	}
	
	/**
	 * Callback for attach layout clicked.
	 * @return
	 */
	private View.OnClickListener getAttachLayoutClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch(v.getId()) {
				case R.id.attach_link_layout:
					attachLink();
					break;
				case R.id.attach_image_layout:
					reattachImage();
					break;
				default:
					break;
				}
			}
		};
	}
	
	/**
	 * Callback for attach button clicked.
	 * @return
	 */
	private View.OnClickListener getAttachButtonClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (attach_layout.getVisibility() == View.VISIBLE) {
					Toast.makeText(context, R.string.attach_refuse, Toast.LENGTH_SHORT).show();
				} else {
					// item listener
					DialogInterface.OnClickListener itemListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int option) {
							switch (option) {
							case 0:
								attachLink();
								break;
							case 1:
								if (listener != null)
									listener.onRequestTakePicture();
								break;
							case 2:
								if (listener != null)
									listener.onRequestPickPhoto();
								break;
							default:
								break;
							}
						}
					};
					
					final String [] str_array = context.getResources().getStringArray(R.array.attach_image_array);
					final int [] id_array = { R.drawable.attach_file_option_link, R.drawable.attach_file_option_camera, R.drawable.attach_file_optioin_album };
					
					// dialog list adapter
					final LayoutInflater inflater = LayoutInflater.from(context);
					ListAdapter adapter = new BaseAdapter() {
						public long getItemId(int position) {
							return position;
						}
						public Object getItem(int position) {
							return position;
						}
						public int getCount() {
							return str_array.length;
						}
						public View getView(int position, View view, ViewGroup parent) {
							view = inflater.inflate(R.layout.attach_file_option_view, null);
							((ImageView)view.findViewById(R.id.attach_file_dialog_option_imageview)).setImageBitmap(ImageManager.getBitmapFromResources(id_array[position]));
							((TextView)view.findViewById(R.id.attach_file_dialog_option_textview)).setText(str_array[position]);
							
							return view;
						}
					};
					
					// alert dialog
					new AlertDialog.Builder(context)
								   .setTitle(R.string.attach_title)
								   .setPositiveButton(R.string.action_cancel, null)
								   .setAdapter(adapter, itemListener)
								   .show();
				}
			}
		};
	}
	
	/**
	 * Callback for post clicked.
	 * @return
	 */
	private View.OnClickListener getPostClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				processPost(false);
			}
		};
	}
	
	/**
	 * Callback for Facebook post clicked.
	 * @return
	 */
	private View.OnClickListener getFacebookPostClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!FacebookManager.isFacebookActive()) {
					// Button
					DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener () {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (listener != null)
								listener.onFacebookActiveRequest(new PostCommand());
						}
					};
					// Dialog
					new AlertDialog.Builder(context)
								   .setTitle(R.string.app_message)
								   .setMessage(R.string.fb_link_request)
								   .setPositiveButton(R.string.action_confirm, positive)
								   .setNegativeButton(R.string.action_cancel, null)
								   .show();
				} else
					processPost(true);
			}
		};
	}
	
	/**
	 * Facebook Pending Post Command class.
	 * @author Kencool
	 *
	 */
	private class PostCommand implements FBPendingCommand {
		@Override
		public void execute () {
			if (facebook_post_button != null) {
				facebook_post_button.post( new Runnable () {
					public void run () {
						processPost(true);
					}
				});
			}
		}
	}
	
	public interface MessagePosterListener {
		
		public void onRequestPickPhoto();
		
		public void onRequestTakePicture();
		
		public void onPost (String message, boolean onFacebook);
		
		public void onPostWithLink (String message, Attachment attach, boolean onFacebook);
		
		public void onPostWithImage (String message, String imagePath, Attachment attach, boolean onFacebook);
		
		public void onFacebookActiveRequest(FBPendingCommand command);
	}
}
