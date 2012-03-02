package com.wildmind.fanwave.widget;

import com.wildmind.fanwave.animation.AlphaSetter;
import com.wildmind.fanwave.program.ProgramManager;
import com.wildmind.fanwave.program.TVProgram;
import com.wildmind.fanwave.program.TVProgramExtraInfo;
import com.wildmind.fanwave.activity.BaseActivity;
import com.wildmind.fanwave.activity.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class ProgramRater extends LinearLayout {

	private LinearLayout 	title_layout, retry_layout;
	private FrameLayout 	action_layout;
	private RatingBar 		rating_bar;
	private ImageView 		ray_imageview, star_imageview, success_imageview;
	private Button 			rating_button;
	private TextView 		success_textview;

	private TVProgram program;
	private TVProgramExtraInfo extra_info;
	private ProgramRaterListener listener = null;

	/**
	 * Constructor
	 * @param context
	 * @param attrs
	 */
	public ProgramRater(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * Clear all resources.
	 */
	public void clear() {
		title_layout = null;
		action_layout = null;
		retry_layout = null;
		rating_bar = null;
		ray_imageview = null;
		star_imageview = null;
		success_imageview = null;
		rating_button = null;
		
		program = null;
		extra_info = null;
		listener = null;
	}
	
	public void setProgramRaterListener (ProgramRaterListener listener) {
		this.listener = listener;
	}

	public void init (TVProgram program) {
		this.program = program;
		
		// Title Layout
		title_layout = (LinearLayout) findViewById(R.id.title_layout);
		TextView title = (TextView) findViewById(R.id.program_title_textview);
		title.setText(program.getTitle());

		// Action Layout
		action_layout = (FrameLayout) findViewById(R.id.action_layout);

		// Rating Bar
		rating_bar = (RatingBar) findViewById(R.id.rating_bar);
		rating_bar.setOnRatingBarChangeListener(getRatingBarChangedListener());

		// Retry Layout
		retry_layout = (LinearLayout) findViewById(R.id.retry_layout);

		// Ray
		ray_imageview = (ImageView) findViewById(R.id.ray_imageview);
		ray_imageview.setVisibility(View.INVISIBLE);
				
		// Star
		star_imageview = (ImageView) findViewById(R.id.star_imageview);
		star_imageview.setOnClickListener(getStarClickedListener());
		
		// Success Check
		success_imageview = (ImageView) findViewById(R.id.success_check_imageview);
		success_textview = (TextView) findViewById(R.id.rating_success_textview);

		// Rating Button
		View.OnClickListener listener = getRatingClickedListener();
		rating_button = (Button) findViewById(R.id.rating_button);
		rating_button.setOnClickListener(listener);
		Button retry = (Button) findViewById(R.id.retry_button);
		retry.setOnClickListener(listener);
	}
	
	public boolean isPrepared () {
		return extra_info != null;
	}
	
	public void prepare (TVProgramExtraInfo extrainfo) {
		this.extra_info = extrainfo;
	}
	
	public void show () {
		title_layout.setVisibility(View.VISIBLE);
		action_layout.setVisibility(View.VISIBLE);
		rating_button.setVisibility(View.VISIBLE);
		retry_layout.setVisibility(View.INVISIBLE);
		success_imageview.setVisibility(View.INVISIBLE);
		success_textview.setVisibility(View.INVISIBLE);
		
		// set program rating
		rating_bar.setRating((float) extra_info.getUserRating());
		rating_button.clearAnimation();
		rating_button.startAnimation(new AlphaSetter(extra_info.getUserRating() >= 1 ? 1 : 0.5f));
		rating_button.setEnabled(extra_info.getUserRating() >= 1);
	}
	
	public void showRatingResult (boolean success) {
		rating_button.clearAnimation();
		rating_button.setVisibility(View.INVISIBLE);
		action_layout.setVisibility(View.VISIBLE);
		title_layout.setVisibility(success ? View.INVISIBLE : View.VISIBLE);
		success_imageview.setVisibility(success ? View.VISIBLE : View.INVISIBLE);
		success_textview.setVisibility(success ? View.VISIBLE : View.INVISIBLE);
		retry_layout.setVisibility(success ? View.INVISIBLE : View.VISIBLE);
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
	 * Callback for star clicked.
	 * @return
	 */
	private View.OnClickListener getStarClickedListener() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (rating_bar.getRating() < 5)
					rating_bar.setRating(rating_bar.getRating() + 1);
			}
		};
	}
	
	/**
	 * Callback for rating button clicked.
	 * @return
	 */
	private View.OnClickListener getRatingClickedListener() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				title_layout.setVisibility(View.INVISIBLE);
				action_layout.setVisibility(View.INVISIBLE);
				startRayRotating();
				
				new Thread (new Runnable () {
					public void run () {
						final boolean success = ProgramManager.rateProgram(program, (int) rating_bar.getRating());
						
						if (rating_bar != null) {
							rating_bar.post( new Runnable () {
								public void run () {
									showRatingResult(success);
									stopRayRotating();
									
									if (success && listener != null)
										listener.onRate((int) rating_bar.getRating());
								}
							});
						}
					}
				}).start();
			}
		};
	}
	
	/**
	 * Callback for rating bar changed.
	 * @return
	 */
	private RatingBar.OnRatingBarChangeListener getRatingBarChangedListener () {
		return new RatingBar.OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				rating_button.clearAnimation();
				rating_button.startAnimation(new AlphaSetter(rating >= 1 ? 1 : 0.5f));
				rating_button.setEnabled(rating >= 1);
			}
		};
	}
	
	public interface ProgramRaterListener {
		
		public void onRate (int ratingPoint);
	}
}
