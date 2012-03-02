package com.wildmind.fanwave.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;

import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.activity.R;

public class StringGenerator {

	/**
	 * Get string decoded by hex with hexString.
	 * @param hexString
	 * @return String
	 */
	public static String stringFromHexString(String hexString) {
		String resultStr = "";
		String charHex = "";
		int charsCount = hexString.length() / 2;
		byte charsData[] = new byte[charsCount];
		char byte_chars[] = {'\0', '\0', '\0'};
		
		try {
			for (int i = 0; i < charsCount; i++) {
				charHex = hexString.substring(i*2, i*2 + 2);
				byte_chars[0] = charHex.charAt(0);
				byte_chars[1] = charHex.charAt(1);
				charsData[i] = (byte) Integer.parseInt(charHex, 16);
			}
			resultStr = new String(charsData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultStr;
	}
	
	/**
	 * Get string encoded by hex with string.
	 * @param string
	 * @return
	 */
	public static String hexStringFromString (String string) {
		String hexString = "";
		try {
	    	byte[] bytes;
	    	bytes = string.getBytes("UTF-8");
			for(byte b:bytes)
				hexString += Integer.toHexString(b & 0xff);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	
		return hexString;
	}
	
	/**
	 * Get difference presented time string before now.
	 * @param pastTimeString	Format: yyyyMMddHHmmss
	 * @param _context
	 * @return String
	 */
	/**
	public static String differenceTimeStringBeforeNow(String pastTimeString) {
		if (pastTimeString.length() != 12 && pastTimeString.length() != 14) {
			return "";
		}
		
		// create past calendar
		//
		Date pastDate = DateGenerator.dateFromTimeString(pastTimeString);
		GregorianCalendar pastCalendar = new GregorianCalendar();
		pastCalendar.setTime(pastDate);
		
		// create now calendar
		//
		Date nowDate = new Date();
		GregorianCalendar nowCalendar = new GregorianCalendar();
		nowCalendar.setTime(nowDate);
		
		// get difference between now and past in seconds
		//
		int interval = (int) ( nowCalendar.getTimeInMillis() - pastCalendar.getTimeInMillis() ) / 1000;

		// calculate difference
		//
		int days = interval / 86400;
		int hours = (interval - days*86400) / 3600;
		int minutes = ((interval - days*86400) - hours*3600)/60;
		int seconds = ((interval - days*86400) - hours*3600)- minutes*60;
		
		// create difference time string
		//
		String diff_str = "";
		if (days > 0) 
			diff_str += days + " " + ApplicationManager.getAppContext().getResources().getString(R.string.Time_day) + " ";
		if (hours > 0)
			diff_str += hours + " " + ApplicationManager.getAppContext().getResources().getString(R.string.Time_Hr) + " ";
		if (minutes > 0)
			diff_str += minutes + " " + ApplicationManager.getAppContext().getResources().getString(R.string.Time_Min) + " ";
		if (seconds > 0)
			diff_str += seconds + " " + ApplicationManager.getAppContext().getResources().getString(R.string.Time_sec) + " ";
		diff_str += ApplicationManager.getAppContext().getResources().getString(R.string.Time_Ago);
		
		return diff_str;
	}*/
	
	/**
	 * Get time string from date.
	 * @param date
	 * @return String	Format: yyyyMMddHHmm
	 */
	public static String timeStringFromDate (Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
		return formatter.format(date);
	}
	
	/**
	 * Get time string from date with second field.
	 * @param date
	 * @return String	Format: yyyyMMddHHmmss
	 */
	public static String timeStringFromDateWithSeconds (Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		return formatter.format(date);
	}
	
	/**
	 * Get time string from calendar.
	 * @param calendar
	 * @return String	Format: yyyyMMddHHmm
	 */
	public static String timeStringFromCalendar (Calendar calendar) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
		return formatter.format(calendar.getTime());
	}
	
	/**
	 * Get time string from calendar with second field.
	 * @param calendar
	 * @return String	Format: yyyyMMddHHmmss
	 */
	public static String timeStringFromCalendarWithSeconds (Calendar calendar) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		return formatter.format(calendar.getTime());
	}
	
	/**
	 * Get date string from time string.
	 * @param timeString Format: yyyyMMddHHmm
	 * @return String 	 Format: yyyy/MM/dd HH:mm
	 */
	public static String dateStringFromTimeString (String timeString) {
		String year = timeString.substring(0, 4);
		String month = timeString.substring(4, 6);
		String day = timeString.substring(6, 8);
		String hour = timeString.substring(8, 10);
		String minute = timeString.substring(10, 12);
		
		return year + "/" + month + "/" + day + " " + hour + ":" + minute;
	}
	
	/**
	 * Get date string from time string with second field.
	 * @param timeString Format: yyyyMMddHHmmss
	 * @return String 	 Format: yy/MM/dd HH:mm:ss
	 */
	public static String dateStringFromTimeStringWithSeconds (String timeString) {
		String year = timeString.substring(2, 4);
		String month = timeString.substring(4, 6);
		String day = timeString.substring(6, 8);
		String hour = timeString.substring(8, 10);
		String minute = timeString.substring(10, 12);
		String second = timeString.substring(12, 14);
		
		return year + "/" + month + "/" + day + " " + hour + ":" + minute + ":" + second;
	}
	
	/**
	 * Get playing time string from time string.
	 * @param timeString Format: yyyyMMddHHmmss or yyyyMMddHHmm
	 * @return String  	 Format: MM/dd (Day) HH:mm
	 */
	public static String playTimeStringFromTimeString (String timeString) {
		Calendar calendar = CalendarGenerator.calendarFromTimeString(timeString.length() == 14
																	 ? timeString : timeString + "00");
		String month = timeString.substring(4, 6);
		String day = timeString.substring(6, 8);
		String hour = timeString.substring(8, 10);
		String minute = timeString.substring(10, 12);
		String weekDay = calendar != null 
					   ? CalendarGenerator.getWeekDayString(calendar.get(Calendar.DAY_OF_WEEK)) : "";
		
		return month + "/" + day + weekDay + hour + ":" + minute;
	}
	
	/**
	 * Get distinct time string from time string.
	 * @param timeString
	 * @return String 	Format: Today HH:mm:ss if today
	 * 							Yesterday HH:mm:ss if yesterday
	 * 							yyyy/MM/dd else
	 */
	public static String distinctTimeStringFromTimeString (String timeString) {
		String year = timeString.substring(0, 4);
		String month = timeString.substring(4, 6);
		String day = timeString.substring(6, 8);
		String hour = timeString.substring(8, 10);
		String minute = timeString.substring(10, 12);
		String second = timeString.length() == 14 ? timeString.substring(12, 14) : "00";
		
		// today, yesterday 00:00:00 time value
		String dailyTimeString = getCurrentTimeString().substring(0, 8) + "000000";
		Date todayDate = DateGenerator.dateFromTimeString(dailyTimeString);
		long todayTime = todayDate.getTime();
		long yesterdayTime = todayTime - 24 * 60 * 60 * 1000;
		
		// request time value
		Date date = DateGenerator.dateFromTimeString(timeString);
		long time = date.getTime();
		
		Context context = ApplicationManager.getAppContext();
		String disString = "";
		if (time <= yesterdayTime) 
			disString = year + "/" + month + "/" + day;
		else if (yesterdayTime < time && time <= todayTime)
			disString = context.getResources().getString(R.string.time_yesterday) + " " + 
						hour + ":" + minute + ":" + second;
		else if (todayTime < time)
			disString = context.getResources().getString(R.string.time_today) + " " + 
						hour + ":" + minute + ":" + second;
		
		return disString;
	}
	
	/**
	 * Get time string with minutes shifted.
	 * @param timeString	Format: yyyyMMddHHmmss or yyyyMMddHHmm
	 * @param minutes
	 * @return String	Format: yyyyMMddHHmmss or yyyyMMddHHmm
	 */
	public static String timeStringShiftWithMinutes (String timeString, int minutes) {
		boolean withSecond = timeString.length() == 14;
		Date date = DateGenerator.dateFromTimeString(withSecond ? timeString : timeString + "00");
		Date newDate = new Date(date.getTime() + minutes * 60 * 1000);
		
		return withSecond ? timeStringFromDateWithSeconds(newDate) : timeStringFromDate(newDate);
	}
	
	/**
	 * Get time string with hours shifted.
	 * @param timeString	Format: yyyyMMddHHmmss or yyyyMMddHHmm
	 * @param hours
	 * @return String	Format: yyyyMMddHHmmss or yyyyMMddHHmm
	 */
	public static String timeStringShiftWithHours (String timeString, int hours) {
		boolean withSecond = timeString.length() == 14;
		Date date = DateGenerator.dateFromTimeString(withSecond ? timeString : timeString + "00");
		Date newDate = new Date(date.getTime() + hours * 60 * 60 * 1000);
		
		return withSecond ? timeStringFromDateWithSeconds(newDate) : timeStringFromDate(newDate);
	}
	
	/**
	 * Get hourly time string with hours shifted.
	 * @param timeString	Format: yyyyMMddHHmmss or yyyyMMddHHmm
	 * @param hours
	 * @return String	Format: yyyyMMddHH0000 or yyyyMMddHH00
	 */
	public static String hourlyTimeStringShiftWithHours (String timeString, int hours) {
		boolean withSecond = timeString.length() == 14;
		String hourlyString = timeString.substring(0, 10) + "0000";
		Date date = DateGenerator.dateFromTimeString(hourlyString);
		Date newDate = new Date(date.getTime() + hours * 60 * 60 * 1000);
		
		return withSecond ? timeStringFromDateWithSeconds(newDate) : timeStringFromDate(newDate);
	}
	
	/**
	 * Get daily time string with days shifted.
	 * @param timeString	Format: yyyyMMddHHmmss or yyyyMMddHHmm
	 * @param days
	 * @return String	Format: yyyyMMdd000000 or yyyyMMdd0000
	 */
	public static String dailyTimeStringShiftWithDays (String timeString, int days) {
		boolean withSecond = timeString.length() == 14;
		String dailyString = timeString.substring(0, 8) + "000000";
		Date date = DateGenerator.dateFromTimeString(dailyString);
		Date newDate = new Date(date.getTime() + days * 24 * 60 * 60 * 1000);
		
		return withSecond ? timeStringFromDateWithSeconds(newDate) : timeStringFromDate(newDate);
	}
	
	/**
	 * Get current utc time string.
	 * @return
	 */
	public static String getCurrentUTCTimeString () {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		formatter.setTimeZone(TimeZone.getTimeZone("gmt"));
		return formatter.format(new Date());
	}
	
	/**
	 * Get time string for now.
	 * @return String	Format: yyyyMMddHHmm
	 */
	public static String getCurrentTimeString () {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
		return formatter.format(new Date());
	}
	
	/**
	 * Get time string for now with second field.
	 * @return String	Format: yyyyMMddHHmmss
	 */
	public static String getCurrentTimeStringWithSeconds () {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		return formatter.format(new Date());
	}
	
	/**
	 * Get date string of current time.
	 * @return String Format: yyyy/MM/dd HH:mm:ss
	 */
	public static String getCurrentDateStringWithSeconds () {
		return dateStringFromTimeStringWithSeconds(getCurrentTimeStringWithSeconds());
	}
	
	/**
	 * Get UTC time string from local time string.
	 * @param timeString	Format: yyyyMMddHHmmss or yyyyMMddHHmm
	 * @return String	Format: yyyyMMddHHmmss or yyyyMMddHHmm
	 */
	public static String UTCFromLocal (String timeString) {
		boolean withSecond = timeString.length() == 14;
		SimpleDateFormat formatter = new SimpleDateFormat(withSecond ? "yyyyMMddHHmmss" : "yyyyMMddHHmm",
														  Locale.getDefault());
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		return formatter.format(DateGenerator.dateFromTimeString(timeString));
	}
	
	/**
	 * Whether is Chinese word.
	 * @param string
	 * @return
	 */
	public static boolean isChinese (String string) {
		String chinese = "[\u0391-\uFFE5]";
		return string.matches(chinese);
	}
}
