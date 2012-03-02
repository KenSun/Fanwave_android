package com.wildmind.fanwave.tutroial;

import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.media.ImageManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TutorialAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private int[] view_ids;
	
	private TutorialListener listener = null;
	
	/**
	 * Constructor
	 * @param ids
	 * @param context
	 */
	public TutorialAdapter (int[] ids, Context context) {
		view_ids = ids;
		inflater = LayoutInflater.from(context);
	}
	
	/**
	 * Clear all resources.
	 */
	public void clear () {
		inflater = null;
		listener = null;
	}
	
	public void setTutorialListener (TutorialListener listener) {
		this.listener = listener;
	}
	
	@Override
	public int getCount() {
		return view_ids.length;
	}

	@Override
	public Object getItem(int position) {
		return view_ids[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder viewHolder;
		if (view == null) {
			view = inflater.inflate(R.layout.tutorial_view, null);
			viewHolder = new ViewHolder();
			viewHolder.tutorial = (ImageView) view.findViewById(R.id.tutorial_imageview);
			viewHolder.start = (Button) view.findViewById(R.id.ok_button);
			viewHolder.page = (TextView) view.findViewById(R.id.number_textview);
			
			// start
			viewHolder.start.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (listener != null)
						listener.onFinish();
				}
			});
			
			view.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) view.getTag();

		// Image View
		/*
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inPurgeable = true;
		try {
			BitmapFactory.Options.class.getField("inNativeAlloc").setBoolean(opts,true);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		Bitmap bmp = BitmapFactory.decodeResource(ApplicationManager.getAppContext().getResources(), 
												  view_ids[position], 
												  opts);
		*/										  
		Bitmap bmp = ImageManager.getBitmapFromResources(view_ids[position]);
		viewHolder.tutorial.setImageBitmap(bmp);
		
		// Page
		viewHolder.page.setText((position + 1) + "/" + view_ids.length);
		
		// Button
		viewHolder.start.setVisibility(position == view_ids.length - 1 ? View.VISIBLE : View.GONE);
		
		return view;
	}

	private class ViewHolder {
		ImageView 	tutorial;
		Button 		start;
		TextView	page;
	}
	
	/**
	 * Tutorial Listener class
	 * @author Kencool
	 *
	 */
	public interface TutorialListener {
		
		public void onFinish();
	}
}
