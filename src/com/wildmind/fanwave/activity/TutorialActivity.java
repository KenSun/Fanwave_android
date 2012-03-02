package com.wildmind.fanwave.activity;

import com.wildmind.fanwave.tutroial.TutorialAdapter;
import com.wildmind.fanwave.tutroial.TutorialAdapter.TutorialListener;
import com.wildmind.fanwave.tutroial.TutorialManager;
import com.wildmind.fanwave.widget.ViewFlow;

import android.content.Intent;
import android.os.Bundle;


/**
 * Intent input data 
 */

public class TutorialActivity extends BaseActivity implements TutorialListener {
	
	private String class_name;
	private ViewFlow tutorial_viewflow;
    private int[] view_ids = null;	
    private TutorialAdapter tutorial_adapter = null;
    
	/**
	 * Activity life cycle
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// check if activity is destroyed due to abnormal situation
		if (isDestroyed())
			return;
		
		setContentView(R.layout.tutorial_activity);
		
		initData();
		initUI();
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

		tutorial_viewflow = null;
		class_name = null;
		view_ids = null;	
		
		if (tutorial_adapter != null)
			tutorial_adapter.clear();
		tutorial_adapter = null;
	}
	
	private void initData () {
		Intent i = getIntent();
		class_name = i.getStringExtra("class_name");
		getSharedPreferences("Tutorial", 0).edit().putBoolean(class_name, true).commit();
		view_ids = TutorialManager.getTutorialArray(class_name);
	}
	
	private void initUI () {
		tutorial_viewflow = (ViewFlow)findViewById(R.id.tutorial_viewflow);
		tutorial_adapter = new TutorialAdapter(view_ids, this);
		tutorial_adapter.setTutorialListener(this);
		tutorial_viewflow.setAdapter(tutorial_adapter);
	}

	@Override
	public void onFinish() {
		finish();
	}
	
}
