package com.wildmind.fanwave.animation;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

public class DivergenceAnimation extends AnimationSet {

	public DivergenceAnimation (float fromSize, float endSize, boolean divergence) {
		super(true);
		ScaleAnimation scale = new ScaleAnimation(fromSize, endSize, fromSize, endSize, 
												  Animation.RELATIVE_TO_PARENT, 0.5f, 
												  Animation.RELATIVE_TO_PARENT, 0.5f);
		AlphaAnimation alpha = divergence ? new AlphaAnimation(1, 0) : new AlphaAnimation(0, 1);
		
		addAnimation(scale);
		addAnimation(alpha);
		setDuration(500);
	}
	
	public void setDuration (int duration) {
		super.setDuration(duration);
	}
}
