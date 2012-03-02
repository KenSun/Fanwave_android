package com.wildmind.fanwave.widget;

import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.reminder.ReminderManager;
import com.wildmind.fanwave.reminder.TVReminder;
import com.wildmind.fanwave.activity.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ReminderPicker extends AlertDialog {

	private Context context;
	private TVReminder reminder = null;
	private ReminderPickerListener listener = null;
	
	private boolean is_set = false;
	private int selected_minutes = 5;
	private int selected_recurrence = TVReminder.REMINDER_TYPE_ONCE;

	public ReminderPicker(Context context, TVReminder reminder) {
		super(context);

		this.context = context;
		this.reminder = reminder;
		this.is_set = ReminderManager.isReminderSet(reminder);
	}
	
	public void setReminderPickerListener (ReminderPickerListener listener) {
		this.listener = listener;
	}

	public void prepare() {
		// Title
		setTitle(is_set ? ReminderManager.getReminderLocalSettingsDescription(reminder) : "Set reminder");

		// View
		setView(getContentView());

		// Negative Button
		int negaTitle = is_set ? R.string.action_remove : R.string.action_cancel;
		setButton(BUTTON_NEGATIVE, ApplicationManager.getAppContext().getResources().getString(negaTitle),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (is_set) {
							removeReminder();
						} else {
							if (listener != null)
								listener.onPickerCancel();
						}
							
						dismiss();
					}
				});

		// Positive Button
		int posiTitle = is_set ? R.string.action_modify : R.string.action_confirm;
		setButton(BUTTON_POSITIVE, ApplicationManager.getAppContext().getResources().getString(posiTitle),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (is_set)
							modifyReminder();
						else
							addReminder();

						dismiss();
					}
				});
	}

	private View getContentView() {
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);

		// Remind Text View
		TextView remind = new TextView(context);
		remind.setText("Remind");
		remind.setPadding(15, 15, 15, 15);
		remind.setTextSize(14);
		remind.setTextColor(Color.WHITE);
		layout.addView(remind);

		// Remind Spinner
		Spinner remindSpinner = new Spinner(context);
		remindSpinner.setPadding(15, 15, 15, 15);
		remindSpinner.setPromptId(R.string.remind_prompt);
		ArrayAdapter<CharSequence> remindAdapter = ArrayAdapter.createFromResource(
				context, R.array.remind_array, android.R.layout.simple_spinner_item);
		remindAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		remindSpinner.setAdapter(remindAdapter);
		remindSpinner.setOnItemSelectedListener(new OnRemindItemSelectedListener());
		layout.addView(remindSpinner);

		// Recurrence Text View
		TextView recurrence = new TextView(context);
		recurrence.setText("Recurrence");
		recurrence.setPadding(15, 15, 15, 15);
		recurrence.setTextSize(14);
		recurrence.setTextColor(Color.WHITE);
		layout.addView(recurrence);

		// Recurrence Spinner
		Spinner recurSpinner = new Spinner(context);
		recurSpinner.setPadding(15, 15, 15, 15);
		recurSpinner.setPromptId(R.string.recurrence_prompt);
		ArrayAdapter<CharSequence> recurAdapter = ArrayAdapter.createFromResource(
				context, R.array.recurrence_array, android.R.layout.simple_spinner_item);
		recurAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		recurSpinner.setAdapter(recurAdapter);
		recurSpinner.setOnItemSelectedListener(new OnRecurrenceItemSelectedListener());
		layout.addView(recurSpinner);

		return layout;
	}
	
	private void addReminder () {
		reminder.setRemindTimeBefore(selected_minutes);
		reminder.setReminderType(selected_recurrence);
		
		final ProgressDialog pd = ProgressDialog.show(context, "", context.getResources().getString(R.string.action_setting));
		new Thread (new Runnable () {
			public void run () {
				final boolean success = ReminderManager.addReminder(reminder, true);
				
				if (context != null) {
					((Activity) context).runOnUiThread (new Runnable () {
						public void run () {
							if (context == null)
								return;
							
							pd.dismiss();
							
							if (success) {
								if (listener != null)
									listener.onReminderSet();
							} else
								Toast.makeText(context, R.string.action_setting_failed, Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}
	
	private void modifyReminder () {
		reminder.setRemindTimeBefore(selected_minutes);
		reminder.setReminderType(selected_recurrence);
		
		final ProgressDialog pd = ProgressDialog.show(context, "", context.getResources().getString(R.string.action_setting));
		new Thread (new Runnable () {
			public void run () {
				final boolean success = ReminderManager.modifyReminder(reminder);
				
				if (context != null) {
					((Activity) context).runOnUiThread (new Runnable () {
						public void run () {
							if (context == null)
								return;
							
							pd.dismiss();
							
							if (success) {
								if (listener != null)
									listener.onReminderModified();
							} else
								Toast.makeText(context, R.string.action_setting_failed, Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}
	
	private void removeReminder () {
		final ProgressDialog pd = ProgressDialog.show(context, "", context.getResources().getString(R.string.action_setting));
		new Thread (new Runnable () {
			public void run () {
				final boolean success = ReminderManager.removeReminder(reminder, true);
				
				if (context != null) {
					((Activity) context).runOnUiThread (new Runnable () {
						public void run () {
							if (context == null)
								return;
							
							pd.dismiss();
							
							if (success) {
								if (listener != null)
									listener.onReminderRemoved(reminder);
							} else
								Toast.makeText(context, R.string.action_setting_failed, Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}

	private class OnRemindItemSelectedListener implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
			switch (position) {
			case 0:
				selected_minutes = 5;
				break;
			case 1:
				selected_minutes = 15;
				break;
			case 2:
				selected_minutes = 30;
				break;
			case 3:
				selected_minutes = 60;
				break;
			case 4:
				selected_minutes = 120;
				break;
			default:
				break;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> adapterView) {
			// TODO Auto-generated method stub
			
		}		
	}
	
	private class OnRecurrenceItemSelectedListener implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
			switch (position) {
			case 0:
				selected_recurrence = TVReminder.REMINDER_TYPE_ONCE;
				break;
			case 1:
				selected_recurrence = TVReminder.REMINDER_TYPE_WEEKLY;
				break;
			case 2:
				selected_recurrence = TVReminder.REMINDER_TYPE_WEEKDAY;
				break;
			default:
				break;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> adapterView) {
			// TODO Auto-generated method stub
			
		}
	}
	
	public interface ReminderPickerListener {
		
		public void onReminderSet();
		
		public void onReminderModified();
		
		public void onReminderRemoved(TVReminder reminder);
		
		public void onPickerCancel();
	}
}
