package com.wildmind.fanwave.media;

import android.graphics.Bitmap;

public interface ImageListener {

	public void retrieveAvatar(final String username, final Bitmap bmp);
	
	public void retrieveBadge(final String badge_id, final boolean scaled, final Bitmap bmp);
	
	public void retrieveAttach(final String token, final boolean is_thumb, final Bitmap bmp);
	
	public void retrieveProgramIcon(final String title, final int sampleBase, final Bitmap bmp);
	
	public void retrieveChannelIcon(final String chcode, final Bitmap bmp);

}
