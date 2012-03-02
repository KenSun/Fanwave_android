package com.wildmind.fanwave.widget;

import com.wildmind.fanwave.activity.BaseActivity;
import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.animation.DivergenceAnimation;
import com.wildmind.fanwave.facebook.FBPendingCommand;
import com.wildmind.fanwave.facebook.FacebookManager;
import com.wildmind.fanwave.program.ProgramManager;
import com.wildmind.fanwave.program.TVProgram;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProgramWaver extends FrameLayout {

	private Context			context;
	private LinearLayout 	wave_in_layout, loading_indicator, title_layout;
	private FrameLayout 	action_layout;
	private TextView 		title_textview;
	private ImageView 		ray_imageview, success_imageview;
	
	private TVProgram program = null;
	private ProgramWaverListener listener = null;

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param attrs
	 */
	public ProgramWaver(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	/**
	 * Clear all resources.
	 */
	public void clear() {
		context = null;
		wave_in_layout = null;
		loading_indicator = null;
		title_layout = null;
		action_layout = null;
		title_textview = null;
		ray_imageview = null;
		success_imageview = null;

		program = null;
		listener = null;
	}
	
	public void setProgramWaverListener (ProgramWaverListener listener) {
		this.listener = listener;
	}

	public void init(TVProgram program) {
		this.program = program;

		wave_in_layout = (LinearLayout) findViewById(R.id.wave_in_layout);
		wave_in_layout.setVisibility(View.INVISIBLE);
		loading_indicator = (LinearLayout) findViewById(R.id.loading_indicator);

		// Title
		title_layout = (LinearLayout) findViewById(R.id.title_layout);
		title_textview = (TextView) findViewById(R.id.program_title_textview);
		title_textview.setText(program.getTitle());
		
		// Ray
		ray_imageview = (ImageView) findViewById(R.id.ray_imageview);
		ray_imageview.setVisibility(View.INVISIBLE);

		// Action Layout
		action_layout = (FrameLayout) findViewById(R.id.action_layout);

		// Success Check
		success_imageview = (ImageView) findViewById(R.id.success_check_imageview);
		success_imageview.setVisibility(View.INVISIBLE);
		
		// Wavein Button
		View.OnClickListener wavein_listener = getWaveinClickedListener();
		
		Button wavein = (Button) findViewById(R.id.wavein_button);
		wavein.setOnClickListener(wavein_listener);
		
		Button fb_wavein = (Button) findViewById(R.id.facebook_wavein_button);
		fb_wavein.setOnClickListener(getFacebookWaveinClickedListener());
		
		Button retry = (Button) findViewById(R.id.wavein_retry_button);
		retry.setOnClickListener(wavein_listener);
		
		Button cancel = (Button)findViewById(R.id.wavein_cancel_button);
		cancel.setOnClickListener(getCancelClickedListener());
	}
	
	/**
	 * Show loading view.
	 */
	public void showLoading () {
		wave_in_layout.setVisibility(View.INVISIBLE);
		loading_indicator.setVisibility(View.VISIBLE);
	}
	
	/**
	 * Show waver view.
	 */
	public void showWaver () {
		loading_indicator.setVisibility(View.INVISIBLE);
		wave_in_layout.setVisibility(View.VISIBLE);
		wave_in_layout.startAnimation(new DivergenceAnimation(2, 1, false));
		
		// dynamically resize title text view
		int lines = title_textview.getLineCount();
		if (lines > 1)
			title_textview.setTextSize(title_textview.getTextSize() / lines);
	}
	
	public void hideWaver () {
		wave_in_layout.startAnimation(new DivergenceAnimation(1, 2, true));
		title_textview.setFocusable(false);
	}
	
	public void showWaveinResult (boolean success) {
		//title
		title_layout.setVisibility(success ? View.INVISIBLE : View.VISIBLE);
				
		//success image view
		success_imageview.setVisibility(success ? View.VISIBLE : View.INVISIBLE);
				
		// wave in
		LinearLayout wavein = (LinearLayout) findViewById(R.id.wavein_layout);
		wavein.setVisibility(View.INVISIBLE);
		
		// success text
		TextView success_text = (TextView) findViewById(R.id.wavein_success_textview);
		success_text.setVisibility(success ? View.VISIBLE : View.INVISIBLE);
		
		// retry
		LinearLayout retry = (LinearLayout) findViewById(R.id.wavein_retry_layout);
		retry.setVisibility(success ? View.INVISIBLE : View.VISIBLE);
	}
	
	public void startRayRotating () {
		ray_imageview.setVisibility(View.VISIBLE);
		ray_imageview.clearAnimation();
		if (BaseActivity.getCurrentActivity() != null) {
			Animation anim = AnimationUtils.loadAnimation(BaseActivity.getCurrentActivity(), R.anim.rotate_infinite);
			ray_imageview.startAnimation(anim);
		}
	}
	
	public void stopRayRotating () {
		ray_imageview.clearAnimation();
	}
	
	/**
	 * Wave in.
	 */
	private void wavein () {
		title_layout.setVisibility(View.INVISIBLE);
		action_layout.setVisibility(View.INVISIBLE);
		startRayRotating();
		
		new Thread (new Runnable () {
			public void run () {
				final boolean success = ProgramManager.waveinProgram(program);
				
				if (action_layout != null) {
					action_layout.post(new Runnable () {
						public void run () {
							action_layout.setVisibility(View.VISIBLE);
							showWaveinResult(success);
							stopRayRotating();
							
							if (success && listener != null)
								listener.onWaveinSuccess();
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Callback for wave in button clicked.
	 * @return
	 */
	private View.OnClickListener getWaveinClickedListener() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				wavein();
			}
		};
	}
	
	/**
	 * Callback for facebook wave in button clicked.
	 * @return
	 */
	private View.OnClickListener getFacebookWaveinClickedListener() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!FacebookManager.isFacebookActive()) {
					// Button
					DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener () {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (listener != null)
								listener.onFacebookActiveRequest(new WaveinCommand());
						}
					};
					// Dialog
					new AlertDialog.Builder(context)
								   .setTitle(R.string.app_message)
								   .setMessage(R.string.fb_link_request)
								   .setPositiveButton(R.string.action_confirm, positive)
								   .setNegativeButton(R.string.action_cancel, null)
								   .show();
				} else {
					wavein();
					
					// Facebook post
					new Thread (new Runnable () {
						public void run () {
							FacebookManager.postWavein(program.getTitle());
						}
					}).start();
				}
			}
		};
	}
	
	/**
	 * Callback for cancel button clicked.
	 * @return
	 */
	private View.OnClickListener getCancelClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null)
					listener.onWaveinCancel();
			}
		};
	}
	
	private class WaveinCommand implements FBPendingCommand {
		@Override
		public void execute() {
			if (wave_in_layout != null) {
				wave_in_layout.post( new Runnable () {
					public void run () {
						wavein();
					}
				});
			}
			
			// Facebook post
			new Thread (new Runnable () {
				public void run () {
					FacebookManager.postWavein(program.getTitle());
				}
			}).start();
		}
	}
	
	public interface ProgramWaverListener {
		
		public void onWaveinSuccess ();
		
		public void onWaveinCancel();
		
		public void onFacebookActiveRequest(FBPendingCommand command);
	}
}
