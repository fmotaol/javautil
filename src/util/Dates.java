package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Dates {

	public static final TimeZone TIMEZONE_GMT = TimeZone.getTimeZone("GMT");

	public static final TimeZone TIMEZONE_BRASIL = TimeZone.getTimeZone("GMT-3:00");

	public static String format(Date date, String fmt) {
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		return sdf.format(date);
	}

	public static Date parse(String s, String fmt) {
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		Date r;
		try {
			r = sdf.parse(s);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return r;
	}

	public static Date shift(Date time, long milliseconds) {
		long t = time.getTime();
		t += milliseconds;
		return new Date(t);
	}

	public static String spentTimeString(Date start, Date end) {
		long spent = spentTime(start, end);
		return spentTimeString(spent);
	}

	public static String spentTimeString(long time) {
		double spent = time;
		if (spent < 1000)
			return (int) spent + "ms";

		spent /= 1000;
		if (spent < 60)
			return (int) spent + "s";

		spent /= 60;
		if (spent < 60)
			return (int) spent + "min";

		spent /= 60;
		if (spent < 24)
			return (int) spent + "h";

		// TODO Auto-generated method stub
		throw new RuntimeException("ainda não implementado");
	}

	public static long spentTime(Date from, Date to) {
		return to.getTime() - from.getTime();
	}

	public static long spentTime(Date from) {
		return spentTime(from, new Date());
	}

	public static String format(Date d) {
		if (d == null)
			return null;

		return defaultDateTimeFormat.format(d);
	}

	public static boolean isRecent(Date time, long milliseconds) {
		if (time == null)
			return false;
		Date now = new Date();
		Date limit = shift(now, -milliseconds);
		return time.after(limit);
	}

	public static Date parseDateTime(String s) {
		s = s.trim();
		if (s.equalsIgnoreCase("now"))
			return new Date();
		if (s.endsWith("before now")) {
			Date ref = new Date();
			s = Strings.removeEnd(s, "before now");
			long p = parsePeriod(s);
			Date r = new Date(ref.getTime() - p);
			return r;
		}
		return parse(s);
	}

	public static String currentTimeText() {
		Date t = new Date();
		return timeText(t);
	}

	private static SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm:ss");

	private static String timeText(Date time) {
		return simpleTimeFormat.format(time);
	}

	public static String formatTime(Date time) {
		return simpleTimeFormat.format(time);
	}

	public static long ONE_SECOND = 1000;
	public static long ONE_MINUTE = ONE_SECOND * 60;
	public static long ONE_HOUR = ONE_MINUTE * 60;
	public static long ONE_DAY = ONE_HOUR * 24;
	public static long ONE_WEEK = ONE_DAY * 7;
	public static long ONE_MONTH = ONE_DAY * 30;
	public static long ONE_YEAR = ONE_DAY * 365;

	public static long parsePeriod(String value) {
		value = value.trim();
		if (value.endsWith("seconds"))
			return parsePeriod(value, "seconds", ONE_SECOND);
		if (value.endsWith("minutes"))
			return parsePeriod(value, "minutes", ONE_MINUTE);
		if (value.endsWith("hours"))
			return parsePeriod(value, "hours", ONE_HOUR);
		if (value.endsWith("days"))
			return parsePeriod(value, "days", ONE_DAY);
		if (value.endsWith("weeks"))
			return parsePeriod(value, "weeks", ONE_WEEK);
		if (value.endsWith("months"))
			return parsePeriod(value, "months", ONE_MONTH);
		if (value.endsWith("years"))
			return parsePeriod(value, "years", ONE_YEAR);

		if (value.equals("1 second"))
			return ONE_SECOND;
		if (value.equals("1 minute"))
			return ONE_MINUTE;
		if (value.equals("1 hour"))
			return ONE_HOUR;
		if (value.equals("1 day"))
			return ONE_DAY;
		if (value.equals("1 week"))
			return ONE_WEEK;
		if (value.equals("1 month"))
			return ONE_MONTH;
		if (value.equals("1 year"))
			return ONE_YEAR;

		throw new RuntimeException("Período não identificado: " + value);
	}

	static long parsePeriod(String value, String periodUnit, long base) {
		String s = Strings.removeEnd(value, periodUnit);
		double d = Double.parseDouble(s);
		return Math.round(d * base);
	}

	public static Date parse(String s) {
		if (s == null)
			return null;
		for (SimpleDateFormat sdf : defaultDateTimeFormats) {
			try {
				Date r = sdf.parse(s);
				return r;
			} catch (ParseException e) {
			}
		}
		throw new RuntimeException("Formato de data desconhecido: " + s);
	}

	private static SimpleDateFormat defaultDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

	public static ArrayList<SimpleDateFormat> defaultDateTimeFormats = new ArrayList<>();

	static {
		defaultDateTimeFormats.add(defaultDateTimeFormat);
		defaultDateTimeFormats.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		defaultDateTimeFormats.add(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
		defaultDateTimeFormats.add(new SimpleDateFormat("yyyy-MM-dd"));
		defaultDateTimeFormats.add(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"));
		defaultDateTimeFormats.add(new SimpleDateFormat("dd/MM/yyyy HH:mm"));
		defaultDateTimeFormats.add(new SimpleDateFormat("dd/MM/yyyy"));
	}

	public static Date now() {
		return new Date();
	}

	public static Date today() {
		return new Date(); //TODO falta arredondar para meia noite
	}

	public static String spentTimeString(Date from) {
		return spentTimeString(from, new Date());
	}

	public static Date changeTimeZone(Date time, String fromTimeZone, String toTimeZone) {
//		TimeZone from = TimeZone.getTimeZone("GMT-3:00");
		TimeZone from = TimeZone.getTimeZone(fromTimeZone);
		TimeZone to = TimeZone.getTimeZone(toTimeZone);
		return changeTimeZone(time, from, to);
	}

	public static Date changeTimeZone(Date time, TimeZone from, TimeZone to) {
		Calendar c = Calendar.getInstance();
		c.setTimeZone(from);
		c.setTime(time);
		c.setTimeZone(to);
		Date r = c.getTime();
		return r;
	}

	public static int compare(Date d1, Date d2) {
		return Long.compare(d1.getTime(), d2.getTime());
	}

	public static long intervalMillis(long i, TimeUnit unit) {
		switch (unit) {
		case NANOSECONDS:
			return i / 1000000;
		case MICROSECONDS:
			return i / 1000;
		case MILLISECONDS:
			return i;
		case SECONDS:
			return i * 1000;
		case MINUTES:
			return i * 60000L;
		case HOURS:
			return i * 60000L * 60;
		case DAYS:
			return i * 60000L * 60 * 24;
//		case WEEKS:
//			return i * 60000L * 60 * 24 * 7;
		}
		throw new RuntimeException("ainda não implementado");
	}

}
