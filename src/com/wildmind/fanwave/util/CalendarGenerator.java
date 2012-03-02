package com.wildmind.fanwave.util;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;

import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.activity.R;

public class CalendarGenerator {

	/**
	 * Get calendar from time string
	 * @param timeString	Format: yyyyMMddHHmmss or yyyyMMddHHmm
	 * @return Calendar or null if timeString is invalid
	 */
	public static Calendar calendarFromTimeString (String timeString) {
		Date date = DateGenerator.dateFromTimeString(timeString);
		Calendar calendar = Calendar.getInstance();
		if (date != null) {
			calendar.setTime(date);
		}
		
		return date != null ? calendar : null;
	}
	
	/**
	 * Get day of week string.
	 * @param calendarWeekDay
	 * @return String
	 */
	public static String getWeekDayString (int calendarWeekDay) {
		Context context = ApplicationManager.getAppContext();
		switch (calendarWeekDay) {
		case Calendar.MONDAY:
			return context.getResources().getString(R.string.time_monday);
		case Calendar.TUESDAY:
			return context.getResources().getString(R.string.time_tuesday);
		case Calendar.WEDNESDAY:
			return context.getResources().getString(R.string.time_wednesday);
		case Calendar.THURSDAY:
			return context.getResources().getString(R.string.time_thursday);
		case Calendar.FRIDAY:
			return context.getResources().getString(R.string.time_friday);
		case Calendar.SATURDAY:
			return context.getResources().getString(R.string.time_saturday);
		case Calendar.SUNDAY:
			return context.getResources().getString(R.string.time_sunday);
		default:
			return "";
		}
	}
}
