package com.wildmind.fanwave.activity;

import java.util.ArrayList;

import com.wildmind.fanwave.program.ProgramManager;
import com.wildmind.fanwave.program.ProgramSearchAdapter;
import com.wildmind.fanwave.program.TVProgram;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

public class ProgramSearchActivity extends BaseActivity implements OnItemClickListener {

	private EditText		search_edittext;
	private ListView		program_listview;
	private LinearLayout	loading_indicator;
	private TextView		descr_textview;
	
	private ProgramSearchAdapter 	program_adapter = null;
	private ArrayList<TVProgram> 	programs = null;
	private String 					search_term = null;
	
	/**
	 * Activity Life Cycle Methods
	 */
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // check if activity is destroyed due to abnormal situation
        if (isDestroyed())
			return;
        
        setContentView(R.layout.program_search_activity);
 
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
		
		if (program_adapter != null)
			program_adapter.clear();
		program_adapter = null;
		
		search_edittext = null;
		program_listview = null;
		loading_indicator = null;
		descr_textview = null;
		
		programs = null;
		search_term = null;
	}
	
	private void initUI () {
		// Back Button
		ImageButton back_button = (ImageButton) findViewById(R.id.back_imagebutton);
		back_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		// Search
		search_edittext = (EditText) findViewById(R.id.search_edittext);
		search_edittext.setText(search_term);
		search_edittext.setOnEditorActionListener(getSearchActionListener());
		search_edittext.requestFocus();
		
		// Search Button
		ImageView search = (ImageView) findViewById(R.id.search_imageview);
		search.setOnClickListener(getSearchClickedListener());
		
		// Program List View
		program_listview = (ListView) findViewById(R.id.program_list_listview);
		program_listview.setDivider(null);
		program_listview.setDividerHeight(0);
		program_listview.setOnItemClickListener(this);				
		program_adapter = new ProgramSearchAdapter(program_listview, null, this);
		program_listview.setAdapter(program_adapter);
		
		// Loading Indicator
		loading_indicator = (LinearLayout) findViewById(R.id.loading_indicator);
		loading_indicator.setVisibility(View.INVISIBLE);
		
		// Description Text View
		descr_textview = (TextView) findViewById(R.id.descr_textview);
		descr_textview.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * Refresh programs.
	 */
	private void refreshPrograms () {
		showLoading();
		programs = null;
		getPrograms();
	}
	
	/**
	 * Show program list view.
	 */
	private void showProgramList () {
		program_listview.setVisibility(View.VISIBLE);
		loading_indicator.setVisibility(View.GONE);
		descr_textview.setVisibility(View.GONE);
	}
	
	/**
	 * Show loading view.
	 */
	public void showLoading () {
		loading_indicator.setVisibility(View.VISIBLE);
		program_listview.setVisibility(View.GONE);
		descr_textview.setVisibility(View.GONE);
	}
	
	/**
	 * Show Description.
	 */
	private void showDescription () {
		program_listview.setVisibility(View.GONE);
		loading_indicator.setVisibility(View.GONE);
		descr_textview.setVisibility(View.VISIBLE);
	}
	
	/**
	 * show programs.
	 */
	public void showPrograms () {
		if (programs == null || programs.size() == 0) 
			showDescription();
		else {
			showProgramList();
			
			program_adapter.refreshData(programs);
			program_listview.startAnimation(AnimationUtils.loadAnimation(this, R.anim.list_drag_down));
		}
	}
	
	/**
	 * Get programs.
	 */
	public void getPrograms () {
		new Thread (new Runnable () {
			public void run () {
				programs = ProgramManager.searchPrograms(search_term);
				
				if (!isDestroyed()) {
					program_listview.post( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							showPrograms();
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Search programs.
	 */
	private void searchPrograms () {
		search_term = search_edittext.getText().toString();
		if (search_term.length() > 0) {
			// hide soft keyboard
			InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			mgr.hideSoftInputFromWindow(search_edittext.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		
			refreshPrograms();
		}
	}
	
	/**
	 * Callback for search action.
	 * @return
	 */
	private OnEditorActionListener getSearchActionListener () {
		return new OnEditorActionListener () {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (event != null && event.getAction() == KeyEvent.ACTION_DOWN) {
					searchPrograms();
					return true;
				}
				return false;
			}
		};
	}
	
	/**
	 * Callback for search clicked.
	 * @return
	 */
	private View.OnClickListener getSearchClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				searchPrograms();
			}
		};
	}
	
	@Override
	public void onItemClick(AdapterView<?> listview, View view, int position, long id) {
		TVProgram tp = (TVProgram) program_adapter.getItem(position);
		Intent i = new Intent(ProgramSearchActivity.this, ProgramActivity.class);
		i.putExtra("program", tp);
		i.putExtra("back_image", BitmapFactory.decodeResource(getResources(), R.drawable.search_title_icon));
		startActivity(i);
	}
}
