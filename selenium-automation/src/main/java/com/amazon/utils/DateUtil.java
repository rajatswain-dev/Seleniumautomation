package com.amazon.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * This class will provide all the data required by the suite
 */

public class DateUtil {

	public static String getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static String getCurrentDate(String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static String getTomorrowDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
		return dateFormat.format(tomorrow);
	}

	public static int getMonth() {
		return Calendar.getInstance().get(Calendar.MONTH);
	}

	public static String getMonthString() {
		Calendar cal = Calendar.getInstance();
		return new SimpleDateFormat("MM").format(cal.getTime());
	}

	public static String getCurrentYear() {
		Calendar cal = Calendar.getInstance();
		return new SimpleDateFormat("yy").format(cal.getTime());
	}

	public static String getcurrentYearFull() {
		Calendar cal = Calendar.getInstance();
		return new SimpleDateFormat("yyyy").format(cal.getTime());
	}

	public static long getCurrentTimeStamp() {
		return new Timestamp(System.currentTimeMillis()).getTime();
	}

	public static Date getCurrentDate(String format, long startTime) {
		Date currentDate = null;
		Date date = new Date(startTime);
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String formattedDate = formatter.format(date);
		try {
			currentDate = formatter.parse(formattedDate);
		} catch (ParseException e) {
			System.err.println("Failed to parse date: " + e.getMessage());
		}
		return currentDate;
	}

	public static String getCurrentDateAccordingToTimeZone(String timeZoneCode) {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		df.setTimeZone(TimeZone.getTimeZone(timeZoneCode));
		return df.format(date);
	}

	public static String getModifiedHoursAccordingToTimeZone(int hoursToModify, String timeZoneCode) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, hoursToModify);
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone(timeZoneCode));
		return df.format(date);
	}

	public static String getYesterdayDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		Date yesterday = new Date(today.getTime() - (1000 * 60 * 60 * 24));
		return dateFormat.format(yesterday);
	}

	public static String getDateAfterDays(int days) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, days);
		return dateFormat.format(calendar.getTime());
	}

	public static String getCurrentDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(new Date());
	}

	public static String getCurrentTime() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		return dateFormat.format(new Date());
	}

}
