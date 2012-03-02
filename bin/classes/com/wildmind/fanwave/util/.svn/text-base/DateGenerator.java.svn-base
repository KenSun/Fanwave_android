package com.wildmind.fanwave.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateGenerator {
	
	/**
	 * Get date from time string.
	 * @param timeString	Format: yyyyMMddHHmmss or yyyyMMddHHmm
	 * @return Date or null if timeString is invalid
	 */
	public static Date dateFromTimeString (String timeString) {
		Date date = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			date = formatter.parse(timeString.length() == 14 ? timeString : timeString + "00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
}
