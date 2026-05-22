package com.example.orderapp.utils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
	
	public static Timestamp convertStringToTimestamp(String convertDate, String pattern) {
		try {
			LocalDateTime ldt = LocalDateTime.parse(convertDate, DateTimeFormatter.ofPattern(pattern));
			return Timestamp.valueOf(ldt);
		} catch (Exception e) {
			return null;
		}
	}

	public static Timestamp convertStringToTimestamp(String convertDate) {
		try {
			LocalDate date = LocalDate.parse(convertDate);
			return Timestamp.valueOf(date.atStartOfDay());
		} catch (Exception e) {
			return null;
		}
	}

	public static java.sql.Date getSQLDate(String date, String pattern) {
		try {
			LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
			return java.sql.Date.valueOf(localDate);
		} catch (Exception e) {
			return null;
		}
	}

	public static java.sql.Date getSQLDate(String date) {
		try {
			LocalDate localDate = LocalDate.parse(date);
			return java.sql.Date.valueOf(localDate);
		} catch (Exception e) {
			return null;
		}
	}

	public static Timestamp getSQLTimestamp(String date, String pattern) {
	    try {
	        if (pattern.contains("H") || pattern.contains("m") || pattern.contains("s")) {
	            LocalDateTime ldt = LocalDateTime.parse(date, DateTimeFormatter.ofPattern(pattern));
	            return Timestamp.valueOf(ldt);
	        } else {
	            LocalDate ld = LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
	            return Timestamp.valueOf(ld.atStartOfDay());
	        }
	    } catch (Exception e) {
	        return null;
	    }
	}
	
	public static String formatSQLTimestamp(Timestamp timestamp, String pattern) {
	    try {
	        if (timestamp == null) {
	            return null;
	        }
	        LocalDateTime ldt = timestamp.toLocalDateTime();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
	        return ldt.format(formatter);
	    } catch (Exception e) {
	        return null;
	    }
	}

	public static Timestamp getSQLTimestamp(String date) {
		try {
			LocalDateTime ldt = LocalDateTime.parse(date, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
			return Timestamp.valueOf(ldt);
		} catch (Exception e) {
			return null;
		}
	}
	
	

	public static Timestamp getCurrentSQLTimestamp() {
		return Timestamp.from(Instant.now());
	}

	public static java.sql.Date getCurrentSQLDate() {
		return java.sql.Date.valueOf(LocalDate.now());
	}

	public static String getCurrentDateFormatted(String pattern) {
		return LocalDate.now().format(DateTimeFormatter.ofPattern(pattern));
	}

	public static Timestamp getStartOfToday() {
		return Timestamp.valueOf(LocalDate.now().atStartOfDay());
	}

	public static Timestamp getEndOfToday() {
		return Timestamp.valueOf(LocalDate.now().atTime(LocalTime.MAX));
	}

	public static Timestamp getStartOfDay(LocalDate date) {
		return Timestamp.valueOf(date.atStartOfDay());
	}

	public static Timestamp getEndOfDay(LocalDate date) {
		return Timestamp.valueOf(date.atTime(LocalTime.MAX));
	}

	public static long toEpochMillis(String datetime, String pattern, String zoneId) {
		LocalDateTime localDateTime = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern(pattern));
		ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of(zoneId));
		return zonedDateTime.toInstant().toEpochMilli();
	}

	public static String epochToString(Long epochMillis, String zoneId, String pattern) {
		ZonedDateTime dateTime = Instant.ofEpochMilli(epochMillis).atZone(ZoneId.of(zoneId));
		return dateTime.format(DateTimeFormatter.ofPattern(pattern));
	}

	public static String convertToISO8601(String date, String inputPattern, int offsetHours) {
		LocalDateTime localDateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern(inputPattern));
		ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneOffset.ofHours(offsetHours));
		return zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	}

	public static boolean isAfterToday(String dateStr, String pattern) {
		LocalDate inputDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
		return inputDate.isAfter(LocalDate.now());
	}

	public static boolean isBeforeToday(String dateStr, String pattern) {
		LocalDate inputDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
		return inputDate.isBefore(LocalDate.now());
	}

}
