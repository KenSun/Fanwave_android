package com.wildmind.fanwave.reminder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.network.NetworkManager;
import com.wildmind.fanwave.profile.ReminderProfile;
import com.wildmind.fanwave.util.CalendarGenerator;
import com.wildmind.fanwave.util.DateGenerator;
import com.wildmind.fanwave.util.StringGenerator;
import com.wildmind.fanwave.vendor.VendorManager;
import com.wildmind.fanwave.activity.R;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

public class ReminderManager {

	private static String calendars_url = "";
	private static String events_url = "";
	private static String reminders_url = "";
	
	private static ArrayList<TVReminder> reminders = null;
	
	static {
		if (Build.VERSION.SDK_INT >= 8) {
			calendars_url 	= "content://com.android.calendar/calendars";
			events_url 		= "content://com.android.calendar/events";
			reminders_url 	= "content://com.android.calendar/reminders"; 
		} else {
			calendars_url 	= "content://calendar/calendars";
			events_url 		= "content://calendar/events";
			reminders_url 	= "content://calendar/reminders"; 
		}
	}
	
	/**
	 * Whether reminders are synchronized with server.
	 * @return boolean
	 */
	public static boolean areRemindersSync () {
		boolean sync = true;
		
		ReminderProfile rp = new ReminderProfile(ApplicationManager.getAppContext());
		HashMap<String, String> uncheck_reminders = rp.getAll();
		ArrayList<String> eventIds = getCalendarReminderEventIds();
		
		// get reminder list
		reminders = getReminderList(AccountManager.getCurrentUser().getUsername());
		
		// check reminder
		for (TVReminder reminder:reminders) {
			// check profile
			if (uncheck_reminders.containsKey(reminder.getKey())) {
				// set reminder id
				String id = uncheck_reminders.get(reminder.getKey());
				reminder.setId(id);
				
				// check local calendar
				if (!eventIds.contains(id)) {
					sync = false;
					break;
				} else {
					// remove this checked reminder
					uncheck_reminders.remove(reminder.getKey());
				}
			} else {
				sync = false;
				break;
			}
		}
		
		// if all synchronized with server but still some unchecked reminders in local, these reminders
		// are unsynchronized
		if (sync && uncheck_reminders.size() > 0)
			sync = false;
		
		return sync;
	}
	
	/**
	 * Synchronize server reminders to local.
	 */
	public static void syncReminders () {
		ReminderProfile rp = new ReminderProfile(ApplicationManager.getAppContext());
		
		// remove all local reminders
		//
		removeRemindersFromCalendar(new ArrayList<String>(rp.getAll().values()));
		rp.removeAll();
		
		if (reminders == null)
			reminders = getReminderList(AccountManager.getCurrentUser().getUsername());
		
		// reset reminders from server
		//
		for (TVReminder reminder:reminders) 
			addReminder(reminder, false);
				
		reminders = null;
	}
	
	/**
	 * Get user's reminder list.
	 * @param username
	 * @return ArrayList<TVReminder>
	 */
	public static ArrayList<TVReminder> getReminderList(String username) {
		ArrayList<TVReminder> reminders = new ArrayList<TVReminder>();

		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/program/reminder/" + username + "/" + "100";

		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByGet(URI, null);

		if (response.get("code").equals("200")) {
			try {
				JSONArray array = new JSONArray(response.get("data"));
				int len = array.length();
				for (int i = 0; i < len; i++)
					reminders.add(new TVReminder(array.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return reminders;
	}
	
	/**
	 * Add a reminder.
	 * @param reminder
	 * @param upload	whether upload to server
	 */
	public static boolean addReminder(TVReminder reminder, boolean upload) {
		
		// set reminder to calendar
		if (!addReminderToCalendar(reminder))
			return false;
				
		// set reminder to server if need
		if (upload) {
			if (!setReminderToServer(reminder)) {
				removeReminder(reminder, false);
				return false;
			}
		}
		
		// set reminder to profile
		ReminderProfile rp = new ReminderProfile(ApplicationManager.getAppContext());
		rp.setReminderId(reminder.getKey(), reminder.getId());
		
		return true;
	}
	
	/**
	 * Modify a reminder.
	 * @param reminder
	 */
	public static boolean modifyReminder (TVReminder reminder) {
		
		// set reminder to server
		if (!setReminderToServer(reminder))
			return false;
		
		// remove local reminder
		removeReminder(reminder, false);
		
		// add local reminder
		addReminder(reminder, false);
		
		return true;
	}
	
	/**
	 * Remove a reminder.
	 * @param reminder
	 * @param upload	whether upload to server
	 */
	public static boolean removeReminder (TVReminder reminder, boolean upload) {
		
		if (upload) {
			// rmeove from server
			if (!removeReminderFromServer(reminder))
				return false;
		}
				
		ReminderProfile rp = new ReminderProfile(ApplicationManager.getAppContext());
		String eventId = rp.getReminderId(reminder.getKey());
		
		// remove from calendar
		ArrayList<String> eventIds = new ArrayList<String>();
		eventIds.add(eventId);
		removeRemindersFromCalendar(eventIds);
		
		// remove from profile
		rp.removeReminder(reminder.getKey());
		
		return true;
	}
	
	/**
	 * get reminder local settings description.
	 * @param reminder
	 * @return
	 */
	public static String getReminderLocalSettingsDescription (TVReminder reminder) {
		ReminderProfile rp = new ReminderProfile(ApplicationManager.getAppContext());
		String id = rp.getReminderId(reminder.getKey());
		HashMap<String, String> settings = getCalendarReminderInfo(id);
		
		String rrule = RecurrenceRuleParser.parseRule(settings.get("rrule"));
		String minutes = parseReminderMinutes(settings.get("minutes"));
		
		return rrule + " " + minutes;
	}
	
	/**
	 * Whether reminder is already set.
	 * @param reminder
	 * @return boolean
	 */
	public static boolean isReminderSet (TVReminder reminder) {
		boolean set = false;
		ReminderProfile rp = new ReminderProfile(ApplicationManager.getAppContext());
		if (rp.containsReminder(reminder.getKey())) {
			// set reminder id
			String id = rp.getReminderId(reminder.getKey());
			reminder.setId(id);
			
			// check local calendar
			set = isCalendarReminderExist(id);
		}
		
		return set;
	}
	
	/**
	 * Get all event ids of calendar reminders.
	 * @return ArrayList<String>
	 */
	private static ArrayList<String> getCalendarReminderEventIds () {
		ArrayList<String> eventIds = new ArrayList<String>();
		
		ContentResolver cr = ApplicationManager.getAppContext().getContentResolver();
		Cursor cursor = cr.query(Uri.parse(reminders_url), new String[] {"event_id"}, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				eventIds.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		cursor.close();
		
		return eventIds;
	}
	
	/**
	 * Get reminder info of event_id.
	 * @param event_id
	 * @return HashMap<String, String>	current keys : "rrule", "minutes"
	 */
	private static HashMap<String, String> getCalendarReminderInfo (String event_id) {
		HashMap<String, String> info = new HashMap<String, String>();
		
		ContentResolver cr = ApplicationManager.getAppContext().getContentResolver();
		
		// get rrule
		//
		Cursor rruleCursor = cr.query(Uri.withAppendedPath(Uri.parse(events_url), event_id), 
				new String [] {"rrule"}, null, null, null);
		if (rruleCursor.moveToFirst() && rruleCursor.getString(0) != null)
			info.put("rrule", rruleCursor.getString(0));
		
		rruleCursor.close();
		// get minutes
		//
		Cursor minutesCursor = cr.query(Uri.parse(reminders_url), 
				new String[] {"event_id", "minutes"}, null, null, null);
		if (minutesCursor.moveToFirst()) {
			do {
				String eventId = minutesCursor.getString(0);
				String minutes = minutesCursor.getString(1);
				if (eventId != null && minutes != null && eventId.equals(event_id)) {
					info.put("minutes", minutes);
					break;
				}
			} while (minutesCursor.moveToNext());
		}
		minutesCursor.close();
		
		return info;
	}
	
	/**
	 * Remove all reminders with eventIds.
	 * @param eventIds
	 */
	private static void removeRemindersFromCalendar (ArrayList<String> eventIds) {
		Context context = ApplicationManager.getAppContext();
		ContentResolver cr = context.getContentResolver();
		
		// remove reminders
		Cursor cursor = cr.query(Uri.parse(reminders_url), new String[] {"event_id"}, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				String eventId = cursor.getString(0);
				if (eventIds.contains(eventId))
					cr.delete(Uri.parse(reminders_url), "event_id=?", new String[] {eventId});
			} while (cursor.moveToNext());
		}
		cursor.close();
		
		// remove events
		for (String id:eventIds)
			cr.delete(Uri.withAppendedPath(Uri.parse(events_url), id), null, null);
	}
	
	/**
	 * Check if reminder exists by event_id.
	 * @param event_id
	 * @return boolean
	 */
	private static boolean isCalendarReminderExist (String event_id) {
		boolean exist = false;
		ContentResolver cr = ApplicationManager.getAppContext().getContentResolver();
		Cursor cursor = cr.query(Uri.parse(reminders_url), new String[] {"event_id"}, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				if (event_id.equals(cursor.getString(0))) {
					exist = true;
					break;
				}
			} while (cursor.moveToNext());
		}
		cursor.close();
		
		return exist;
	}
	
	/**
	 * Add reminder to google calendar.
	 * @param reminder
	 * @return boolean	false if no google calendar found
	 */
	private static boolean addReminderToCalendar (TVReminder reminder) {
		boolean success = false;
		Context context = ApplicationManager.getAppContext();
		ContentResolver cr = context.getContentResolver();

		// get calendar
		Cursor cursor = cr.query(Uri.parse(calendars_url), 
				new String[] {"_id", "displayName", "_sync_account_type"}, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				// save to google calendar
				if (cursor.getString(2).equals("com.google")) {
					// calendar id
			        String calendar_id = cursor.getString(0);
			        
			        Calendar calendar = Calendar.getInstance();
			        // start time
					calendar.setTime(DateGenerator.dateFromTimeString(reminder.getStartTime()));
					long start = calendar.getTimeInMillis();
					
					// end time
					calendar.setTime(DateGenerator.dateFromTimeString(reminder.getEndTime()));
					long end = calendar.getTimeInMillis();
					
					// insert new event
					ContentValues event = new ContentValues();
					event.put("calendar_id", calendar_id);
					event.put("title", context.getResources().getString(R.string.app_name));
					event.put("description", reminder.getDescription());
					event.put("dtstart", start);
					event.put("dtend", end);
					event.put("allDay", 0);
					event.put("hasAlarm", 1);
					event.put("rrule", parseReminderType(reminder));
					Uri eventUri = cr.insert(Uri.parse(events_url), event);
					
					// get event id
					String id = eventUri.getLastPathSegment();
					reminder.setId(id);
					
					// insert new reminder
					ContentValues remind = new ContentValues();
					remind.put("event_id", id);
					remind.put("method", 1);
					remind.put("minutes", reminder.getMinutesBefore());
					cr.insert(Uri.parse(reminders_url), remind);

					success = true;
					break;
				}
			} while (cursor.moveToNext());
		}
		cursor.close();
		
		return success;
	}
	
	/**
	 * Set reminder to server.
	 * @param reminder
	 * @return
	 */
	private static boolean setReminderToServer (TVReminder reminder) {
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/notification/add";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("notification_key", reminder.getKey());
		bodyMap.put("channel", reminder.getChannelName());
		bodyMap.put("channel_code", reminder.getChannelCode());
		bodyMap.put("program", reminder.getTitle());
		bodyMap.put("pgid", reminder.getPgid());
		bodyMap.put("start_time", reminder.getStartTime());
		bodyMap.put("end_time", reminder.getEndTime());
		bodyMap.put("remind_time", StringGenerator.UTCFromLocal(reminder.getReminderTime()));
		bodyMap.put("reminder_type", String.valueOf(reminder.getReminderType()));
		bodyMap.put("application", ApplicationManager.getAppContext().getString(R.string.app_name));
		bodyMap.put("country", VendorManager.getCountry());
		bodyMap.put("localreminder", "yes");
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);
		
		return response.get("code").equals("200");
	}
	
	private static boolean removeReminderFromServer (TVReminder reminder) {
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/notification/delete";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("notification_key", reminder.getKey());
		bodyMap.put("localreminder", "yes");
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);
		
		return response.get("code").equals("200");
	}
	
	/**
	 * Parse reminder type.
	 * @param reminder
	 * @return
	 */
	private static String parseReminderType (TVReminder reminder) {
		String parsedString = null;
		
		switch (reminder.getReminderType()) {
		case TVReminder.REMINDER_TYPE_ONCE:
			break;
		case TVReminder.REMINDER_TYPE_WEEKLY:
			Calendar cal = CalendarGenerator.calendarFromTimeString(reminder.getReminderTime());
			int day = cal.get(Calendar.DAY_OF_WEEK);
			parsedString = EventRecurrenceRule.getWeeklyRule(day);
			break;
		case TVReminder.REMINDER_TYPE_WEEKDAY:
			parsedString = EventRecurrenceRule.WEEKDAY;
			break;
		default:
			break;
		}
		
		return parsedString;
	}
	
	/**
	 * Parse reminder minutes.
	 * @param minutesStr
	 * @return
	 */
	public static String parseReminderMinutes (String minutesStr) {
		if (minutesStr == null)
			return null;
		
		Context context = ApplicationManager.getAppContext();
		String parsedString = "";
		int minutes = Integer.valueOf(minutesStr);
		int week = minutes/(60*24*7);
		int day = (minutes - week*60*24*7)/(60*24);
		int hr = (minutes - (day *60*24 + week*60*24*7))/60;
		int min = minutes % 60;
		
		if (week > 0)
			parsedString += week + context.getResources().getString(R.string.week);
		if (day > 0)
			parsedString += day + context.getResources().getString(R.string.day);
		if (hr > 0)
			parsedString += hr + context.getResources().getString(R.string.hour);
		if (min > 0)
			parsedString += min + context.getResources().getString(R.string.minute);
		
		return parsedString;
	}
	
	/**
	 * Android event recurrence rule definition.
	 * @author Kencool
	 *
	 */
	public static class EventRecurrenceRule {
		public static final String DAILY = "FREQ=DAILY;WKST=SU";
		public static final String WEEKDAY = "FREQ=WEEKLY;WKST=SU;BYDAY=MO,TU,WE,TH,FR";
		
		/**
		 * Days of week.
		 */
		public static final String SUNDAY 		= "SU";
		public static final String MONDAY 		= "MO";
		public static final String TUESDAY 		= "TU";
		public static final String WEDNESDAY 	= "WE";
		public static final String THURSDAY 	= "TH";
		public static final String FRIDAY 		= "FR";
		public static final String SATURDAY 	= "SA";
		
		/**
		 * Get weekly rule.
		 * @param day	Calendar definition day  ex: Sunday=1, Monday=2, ...., Saturday=7
		 * @return
		 */
		public static String getWeeklyRule (int day) {
			String dayString = "";
			switch (day) {
			case Calendar.SUNDAY:
				dayString = SUNDAY;
				break;
			case Calendar.MONDAY:
				dayString = MONDAY;
				break;
			case Calendar.TUESDAY:
				dayString = TUESDAY;
				break;
			case Calendar.WEDNESDAY:
				dayString = WEDNESDAY;
				break;
			case Calendar.THURSDAY:
				dayString = THURSDAY;
				break;
			case Calendar.FRIDAY:
				dayString = FRIDAY;
				break;
			case Calendar.SATURDAY:
				dayString = SATURDAY;
				break;
			}
			return "FREQ=WEEKLY;WKST=SU;BYDAY=" + dayString;
		}
	}
	
	/**
	 * RecurrenceRuleParser class
	 * Recurrence rule:	FREQ=XXXXX;WKST=XX;BYDAY=XX,XX,../BYMONTHDAY=00;BYMONTH=00
	 * @author Kencool
	 *
	 */
	public static class RecurrenceRuleParser {
		
		/**
		 * Parse recurrence rule.
		 * @param rule
		 * @return
		 */
		public static String parseRule (String rule) {
			Context context = ApplicationManager.getAppContext();
			
			if (rule == null)
				return context.getResources().getString(R.string.once);
			else if (rule.equals(EventRecurrenceRule.DAILY))
				return context.getResources().getString(R.string.daily);
			else if (rule.equals(EventRecurrenceRule.WEEKDAY))
				return context.getResources().getString(R.string.weekday);
			else {
				return parseFrequency(rule) + " " + parseTime(rule);
			}
		}
		
		/**
		 * Parse recurrence rule frequency.
		 * daily 	= "FREQ=DAILY"
		 * weekly 	= "FREQ=WEEKLY"
		 * monthly 	= "FREQ=MONTHLY"
		 * yearly 	= "FREQ=YEARLY"
		 * @param rule
		 * @return
		 */
		private static String parseFrequency (String rule) {
			Context context = ApplicationManager.getAppContext();
			
			String freqSubString = rule.substring(rule.indexOf("FREQ"));
			if (freqSubString.substring(5, 7).equals("DA"))
				return context.getResources().getString(R.string.daily);
			else if (freqSubString.substring(5, 7).equals("WE"))
				return context.getResources().getString(R.string.weekly);
			else if (freqSubString.substring(5, 7).equals("MO"))
				return context.getResources().getString(R.string.monthly);
			else if (freqSubString.substring(5, 7).equals("YE"))
				return context.getResources().getString(R.string.yearly);
			else return null;
		}
		
		private static String parseTime (String rule) {
			String day = parseDay(rule);
			if (day != null)
				return day;
			
			String month = parseMonth(rule);
			if (month != null) {
				String monthDay = parseMonthDay(rule);
				return "(" + month + "/" + monthDay +  ")";
			}
			
			String monthDay = parseMonthDay(rule);
			if (monthDay != null)
				return "(" + monthDay + ")";
			
			return null;	
		}
		
		/**
		 * Parse day.
		 * @param rule
		 * @return String	ex: 日,ㄧ,二...	
		 * 						Sun,Mon,Tue,...
		 * 						Second(Tue)		
		 * 						第三個(二)
		 */
		private static String parseDay (String rule) {
			Context context = ApplicationManager.getAppContext();
			String parsedString = "";
			int start = rule.indexOf("BYDAY=");
			if (start == -1)
				return null;
			
			String daySubString = rule.substring(start);
			int end = daySubString.indexOf(';');
			daySubString = end != -1 ? daySubString.substring(6, end)
									 : daySubString.substring(6);
			String [] days = daySubString.split(",");
			for (int i = 0; i < days.length; i++) {
				if (days[i].length() == 3) {
					String num = days[i].substring(0, 1);
					if (num.equals("1"))
						parsedString += context.getResources().getString(R.string.first);
					else if (num.equals("2"))
						parsedString += context.getResources().getString(R.string.second);
					else if (num.equals("3"))
						parsedString += context.getResources().getString(R.string.third);
					else if (num.equals("4"))
						parsedString += context.getResources().getString(R.string.fourth);
					
					String day = days[i].substring(1, 3);
					parsedString += "(" + displayNameForDayRule(day) + ")";
				} else
					parsedString += displayNameForDayRule(days[i]);
				
				if (i != days.length - 1)
					parsedString += ",";
			}
			
			return parsedString;
		}
		
		/**
		 * Parse month day.
		 * @param rule
		 * @return String	ex: 18
		 */
		private static String parseMonthDay (String rule) {
			int start = rule.indexOf("BYMONTHDAY=");
			if (start == -1)
				return null;
			
			String monthSubString = rule.substring(start);
			int end = monthSubString.indexOf(';');
			monthSubString = end != -1 ? monthSubString.substring(11, end)
									   : monthSubString.substring(11);
			return monthSubString;
		}
		
		/**
		 * Parse month.
		 * @param rule
		 * @return String	ex: 10
		 */
		private static String parseMonth (String rule) {
			int start = rule.indexOf("BYMONTH=");
			if (start == -1)
				return null;
			
			String monthSubString = rule.substring(start);
			int end = monthSubString.indexOf(';');
			monthSubString = end != -1 ? monthSubString.substring(8, end)
									   : monthSubString.substring(8);
			return monthSubString;
		}
		
		private static String displayNameForDayRule (String rule) {
			Context context = ApplicationManager.getAppContext();
			
			if (rule.equals(EventRecurrenceRule.SUNDAY))
				return context.getResources().getString(R.string.time_sunday);
			else if (rule.equals(EventRecurrenceRule.MONDAY))
				return context.getResources().getString(R.string.time_monday);
			else if (rule.equals(EventRecurrenceRule.TUESDAY))
				return context.getResources().getString(R.string.time_tuesday);
			else if (rule.equals(EventRecurrenceRule.WEDNESDAY))
				return context.getResources().getString(R.string.time_wednesday);
			else if (rule.equals(EventRecurrenceRule.THURSDAY))
				return context.getResources().getString(R.string.time_thursday);
			else if (rule.equals(EventRecurrenceRule.FRIDAY))
				return context.getResources().getString(R.string.time_friday);
			else if (rule.equals(EventRecurrenceRule.SATURDAY))
				return context.getResources().getString(R.string.time_saturday);
			else
				return null;
		}
	}
}
