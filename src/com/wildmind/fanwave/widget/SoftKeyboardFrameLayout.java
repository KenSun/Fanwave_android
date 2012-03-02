package com.wildmind.fanwave.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class SoftKeyboardFrameLayout extends FrameLayout {

	private SoftKeyboardListener listener;
	private int original_height = 0;
	
	/**
	 * Constructor
	 * @param context
	 * @param attrs
	 */
	public SoftKeyboardFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void clear () {
		listener = null;
	}
	
	// setter
	//
	public void setSoftKeyboardListener(SoftKeyboardListener listener) {
        this.listener = listener;
    }
	
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int proposedheight = MeasureSpec.getSize(heightMeasureSpec);
	    final int actualHeight = getHeight();
	    
	    if (original_height == 0) {
	    	original_height = actualHeight > 0 ? actualHeight : 0;
	    }
	    
	    if (listener != null) {
	    	listener.onSoftKeyboardShown(original_height != proposedheight);
	    }
	    
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);     
    }

	public interface SoftKeyboardListener {
        public void onSoftKeyboardShown(boolean isShowing);
    }
}
