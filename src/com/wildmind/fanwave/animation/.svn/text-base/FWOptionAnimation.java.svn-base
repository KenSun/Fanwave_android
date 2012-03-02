package com.wildmind.fanwave.animation;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class FWOptionAnimation extends TranslateAnimation {
	
	public static final float OPTION_COUNT_3_RATIO_BASE = 0.32f;
	
	public static final long OPTION_COUNT_3_DURATION_BASE = 250;
	
	private float ratio_base = 0;
	private int interval = 0;
	
	public FWOptionAnimation(int optionCount, int fromOption, int toOption) {
		super( Animation.RELATIVE_TO_PARENT, fromOption * OPTION_COUNT_3_RATIO_BASE, 
			   Animation.RELATIVE_TO_PARENT, toOption * OPTION_COUNT_3_RATIO_BASE, 
			   Animation.RELATIVE_TO_PARENT, 0, 
			   Animation.RELATIVE_TO_PARENT, 0 );
		
		switch (optionCount) {
		case 3:
			ratio_base = OPTION_COUNT_3_RATIO_BASE;
			break;
		default:
			break;
		}
		
		interval = Math.abs(fromOption - toOption);
		setDuration(interval * OPTION_COUNT_3_DURATION_BASE);
		setFillAfter(true);
	}
	
	public float getRatioBase () {
		return ratio_base;
	}
	
	public int getInterval () {
		return interval;
	}
	
	public long getDuration () {
		return interval * OPTION_COUNT_3_DURATION_BASE;
	}
}
