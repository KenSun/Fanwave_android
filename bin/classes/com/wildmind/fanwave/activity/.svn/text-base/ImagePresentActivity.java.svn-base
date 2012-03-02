package com.wildmind.fanwave.activity;

import com.wildmind.fanwave.comment.Attachment;
import com.wildmind.fanwave.media.ImageManager.SampleBase;
import com.wildmind.fanwave.network.NetworkManager;
import com.wildmind.fanwave.program.TVProgramImage;
import com.wildmind.fanwave.activity.R;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Intent input data "present_type" : int
 * 
 * 		if type = PROGRAM_IMAGE_PRESENT	
 * 					 "program_title" : String
 * 					 "program_image" : TVProgramImage
 * 
 * 		if type = ATTACH_IMAGE_PRESENT
 * 					 "attachment_description" : String
 * 					 "attachment" : Attachment
 * @author Kencool
 *
 */

public class ImagePresentActivity extends BaseActivity {

	public static final int PROGRAM_IMAGE_PRESENT 	= 0;
	public static final int ATTACH_IMAGE_PRESENT 	= 1;
	
	private ImageView 		present_imageview;
	private LinearLayout	loading_indicator;
	
	private Bitmap 	present_image = null;
	private String	title = "";
	private int 	present_type = -1;
	
	private TVProgramImage program_image = null;
	private Attachment attach = null;
	
	/**
	 * Activity Life Cycle Methods
	 * @author Kencool
	 *
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// check if activity is destroyed due to abnormal situation
		if (isDestroyed())
			return;
		
		setContentView(R.layout.image_present_activity);
     	
		initData();
		initUI();
		getImage();
	}
	
	protected void onStart () {
		super.onStart();
		// The activity is about to become visible.
	}
	
	protected void onResume () {
		super.onResume();
		// The activity has become visible (it is now "resumed").
	}
	
	protected void onPause () {
		super.onPause();
		// Another activity is taking focus (this activity is about to be "paused").
	}
	
	protected void onStop () {
		super.onStop();
		// The activity is no longer visible (it is now "stopped")
	}
	
	protected void onDestroy () {
		super.onDestroy();
		// The activity is about to be destroyed.
		
		present_imageview = null;
		loading_indicator = null;
		
		present_image = null;
		title = null;
		program_image = null;
		attach = null;
	}
	
	/**
	 * Initialization Methods
	 * @author Kencool
	 *
	 */
	private void initData() {
		Intent i = getIntent();
		this.present_type = (int) i.getIntExtra("present_type", -1);
		
		switch (present_type) {
		case PROGRAM_IMAGE_PRESENT:
			initProgramImageData(i);
			break;
		case ATTACH_IMAGE_PRESENT:
			initAttachImageData(i);
			break;
		default:
			break;
		}
	}
	
	private void initProgramImageData (Intent i) {
		title = (String) i.getStringExtra("program_title");
		this.program_image = (TVProgramImage) i.getParcelableExtra("program_image");
	}
	
	private void initAttachImageData (Intent i) {
		title = (String) i.getStringExtra("attachment_description");
		this.attach = (Attachment) i.getParcelableExtra("attachment");
	}
	
	private void initUI() {
		// Activity Layout
		FrameLayout activity_layout = (FrameLayout) findViewById(R.id.activity_layout);
		activity_layout.setOnClickListener(new View.OnClickListener () {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		// Image View
		present_imageview = (ImageView) findViewById(R.id.image_present_imageview);
		present_imageview.setVisibility(View.INVISIBLE);
		
		// loading indicator
		//
		loading_indicator = (LinearLayout) findViewById(R.id.loading_indicator);
		
		switch (present_type) {
		
			case PROGRAM_IMAGE_PRESENT:
				initProgramImageUI();
				break;
		
			case ATTACH_IMAGE_PRESENT:
				initAttachImageUI();
				break;
			
			default:
				break;
		}
	}
	
	private void initProgramImageUI () {
		TextView title_textview = (TextView) findViewById(R.id.image_title_textview);
		String imageTitle = "";
		
		// image title
		if (program_image.getTitle().length() > 0)
			imageTitle = program_image.getTitle();
		
		// image description
		if (program_image.getDescription().length() > 0) {
			if (imageTitle.length() > 0)
				imageTitle += ": ";
			imageTitle += program_image.getDescription();
		}
		
		if (imageTitle.length() > 0)
			title = imageTitle;
		title_textview.setText(title);
	}
	
	private void initAttachImageUI () {
		TextView title_textview = (TextView) findViewById(R.id.image_title_textview);
		title_textview.setText(title);
	}
	
	/**
	 * Show image.
	 */
	private void showImage () {
		loading_indicator.setVisibility(View.INVISIBLE);
		present_imageview.setVisibility(View.VISIBLE);
		
		if (present_image == null)
			return;
		present_imageview.setImageBitmap(present_image);
	}
	
	/**
	 * Get image.
	 */
	private void getImage () {
		new Thread (new Runnable () {
			public void run () {
				Bitmap bmp = null;
				
				switch (present_type) {	
				case PROGRAM_IMAGE_PRESENT:
					bmp = NetworkManager.downloadImage(program_image.getImageUrl(), SampleBase.ROUGH_SAMPLED);
					break;
				case ATTACH_IMAGE_PRESENT:
					bmp = NetworkManager.getAttachImage(attach.getToken(), false);
					break;
				default:
					break;
				}
				
				if (!isDestroyed()) {
					present_image = bmp;
					
					present_imageview.post( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							showImage();
						}
					});
				}
			}
		}).start();
	}
	
	/*
	private void setImage(Bitmap bm, ImageView iv){
		DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);    
        int vWidth = dm.widthPixels;
        int vHeight = dm.heightPixels-300;
        double srcWidth = bm.getWidth();
		double srcHeight = bm.getHeight();
        bm=null;

		double destWidth = 0;
		double destHeight = 0;
		double ratio = 0.0;
		
		if(srcWidth >srcHeight) {
			ratio = srcWidth / vWidth;
			destWidth = vWidth;
			destHeight =  (srcHeight / ratio);
		}else if(srcWidth == srcHeight) {
			
			destWidth  =vWidth -100;
			destHeight = vWidth -100;
			
		}else{
			ratio = srcHeight / vHeight;
			destHeight = vHeight;
			destWidth =  (srcWidth / ratio); 
		}
		 LayoutParams para = iv.getLayoutParams();
	     para.height = (int) destHeight;
	     para.width = (int) destWidth;
	     iv.setLayoutParams(para);
		 iv=null;
	}
	*/
	
}
