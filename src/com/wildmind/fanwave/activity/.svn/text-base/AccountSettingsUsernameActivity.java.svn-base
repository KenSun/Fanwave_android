package com.wildmind.fanwave.activity;


import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.app.ApplicationManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AccountSettingsUsernameActivity extends BaseActivity implements View.OnClickListener{

	private ImageView back_imagebutton;
	private Button update_button;
	private EditText  nickname_edittext;
	private ProgressDialog progressDialog = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// check if activity is destroyed due to abnormal situation
		if (isDestroyed())
			return;
		
		setContentView(R.layout.account_settings_username_activity);
		initData();
		initUI();
		EventListenerHandle();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		back_imagebutton = null;
		update_button = null;
		nickname_edittext = null;
		progressDialog = null;
	}

	private void initData(){	
		 
	}
	
	private void initUI(){
		//Home Button
		back_imagebutton 	= (ImageView)findViewById(R.id.back_imagebutton);
		update_button 		= (Button)findViewById(R.id.update_button);
		nickname_edittext 	= (EditText)findViewById(R.id.nickname_edittext);
		nickname_edittext.setText(AccountManager.getCurrentUser().getNickname());
		nickname_edittext.addTextChangedListener(mTextWatcher);
	}
	
	private void EventListenerHandle(){
		back_imagebutton.setOnClickListener(this);
		update_button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.back_imagebutton:
				AccountSettingsUsernameActivity.this.finish();
				break;
			case R.id.update_button:
				if(!nickname_edittext.getText().toString().equals(AccountManager.getCurrentUser().getNickname()))
					if(length(nickname_edittext.getText().toString()) < 4)
						showToast(ApplicationManager.getAppContext().getResources().getString(R.string.sign_up_err_nickname));
					else
						enterPassword();
				break;
		}
	}
	private void showToast(String msr){
  		Toast.makeText(AccountSettingsUsernameActivity.this, msr, Toast.LENGTH_SHORT).show();
	}
	public static double length(String value) {
        double valueLength = 0;

        String chinese = "[\u0391-\uFFE5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
        }
        return valueLength;
    }
	private void enterPassword(){
		
		updateNickname(nickname_edittext.getText().toString());
		
//		//  Edit Text
//		final EditText moodEditText = new EditText(AccountSettingsUsernameActivity.this);
//		moodEditText.setTextSize(14);
//		moodEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
//		// Button
//		DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener () {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				if(moodEditText.getText().toString().equals(AccountManager.getCurrentUser().getPassword()))
//					updateNickname(nickname_edittext.getText().toString());
//				else
//					showToast(ApplicationManager.getAppContext().getResources().getString(R.string.sign_up_err_password));
//
//			}
//		};
//		
//		// Dialog
//		AlertDialog dialog = new AlertDialog.Builder(AccountSettingsUsernameActivity.this)
//					   .setTitle(R.string.sign_up_err_password_null)
//					   .setView(moodEditText)
//					   .setPositiveButton(R.string.action_confirm, positive)
//					   .setNegativeButton(R.string.action_cancel, null)
//					   .create();
//		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//		dialog.show();
	}
	
	private void updateNickname(final String nickname){
		
	 	progressDialog = new ProgressDialog(AccountSettingsUsernameActivity.this);
		progressDialog.setMessage(getResources().getString(R.string.action_loading));
		progressDialog.show();
		
		new Thread ( new Runnable () {
			public void run () {
				final boolean recode = AccountManager.updateNickname(nickname);
				AccountSettingsUsernameActivity.this.runOnUiThread( new Runnable () {
					public void run () {
						if(progressDialog!=null&&progressDialog.isShowing())
							progressDialog.dismiss();
						
						if(recode){
				      		showToast(ApplicationManager.getAppContext().getResources().getString(R.string.action_update_successfully));
				      		finish();
						}else {
				      		showToast(ApplicationManager.getAppContext().getResources().getString(R.string.sign_up_failed));
						}
					}
				});
			}
		}).start();
	}
	
	TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart ;
        private int editEnd ;
        @Override
        public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                int arg3) {
            temp = s;
        }
       
        @Override
        public void onTextChanged(CharSequence s, int arg1, int arg2,
                int arg3) {
           
        }
       
        @Override
        public void afterTextChanged(Editable s) {
            editStart = nickname_edittext.getSelectionStart();
            editEnd = nickname_edittext.getSelectionEnd();
            if (length(temp.toString()) > 20) {
                s.delete(editStart-1, editEnd);
                int tempSelection = editStart;
                nickname_edittext.setText(s);
                nickname_edittext.setSelection(tempSelection);
            }
        }
    };
}
