package util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;

import util.logical.Assert;

public class Strings {

	public static boolean hasContent(String s) {
		if (s == null)
			return false;
		if (s.trim().equals(""))
			return false;
		return true;
	}

	private static final char DEFAULT_QUOTE_CHAR = '\"';

	public static <T> String concat(T[] array, String separator) {
		List<T> ar = Colls.toList(array);
		return concat(ar, separator);
	}

	public static <T> String concat(Iterable<T> list, String separator) {
		return concat(list, separator, (t) -> toString(t));
	}

	public static String toString(Object o) {
		if (o == null)
			return null;
		return o.toString();
	}

	public static <T> String concat(Iterable<T> list, String separator, Function<T, String> toString) {
		String r = "";
		for (T i : list) {
			if (!"".equals(r))
				r += separator;
			r += toString.apply(i);
		}
		return r;
	}

	public static String[] trim(String[] list) {
		String[] r = new String[list.length];
		for (int i = 0; i < list.length; i++) {
			if (list[i] != null)
				r[i] = list[i].trim();
			else
				r[i] = null;
		}
		return r;
	}

	public static String[] unquote(String[] list) {
		return unquote(list, DEFAULT_QUOTE_CHAR);
	}

	public static String[] unquote(String[] list, char quoteChar) {
		String[] r = new String[list.length];
		for (int i = 0; i < list.length; i++) {
			if (list[i] != null)
				r[i] = unquote(list[i], quoteChar);
			else
				r[i] = null;
		}
		return r;
	}


	public static String removeStart(String s, String start) {
		Assert.that(s.startsWith(start), start + " não corresponde ao início de " + s);
		s = s.substring(start.length());
		return s;
	}

	public static String removeEnd(String s, String end) {
		Assert.that(s.endsWith(end), end + " não corresponde ao final de " + s);
		s = s.substring(0, s.length() - end.length());
		return s;
	}

	public static List<String> removeStart(List<String> list, String start) {
		ArrayList<String> r = new ArrayList<String>();
		for (String s : list) {
			if (s.startsWith(start)) {
				s = removeStart(s, start);
			}
			r.add(s);
		}
		return r;
	}

	public static List<String> removeEnd(List<String> list, String end) {
		ArrayList<String> r = new ArrayList<String>();
		for (String s : list) {
			if (s.endsWith(end)) {
				s = removeEnd(s, end);
			}
			r.add(s);
		}
		return r;
	}

	public static String repeat(String s, int times) {
		return repeat(s, times, "");
	}

	public static String repeat(String s, int times, String separator) {
		String r = "";
		for (int i = 1; i <= times; i++) {
			if (!"".equals(r))
				r += separator;
			r += s;
		}
		return r;
	}

	public static String remove(String s, String... parts) {
		for (String p : parts)
			s = s.replace(p, "");

		return s;
	}

	public static String asString(Object s) {
		if (s == null)
			return "null";
		if (s.getClass().isArray())
			return arrayAsString(s);
		return s.toString();
	}
	
	public static <T> String asString(Callable<T> c) {
		try {
			return asString(c.call());
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	

	public static String arrayAsString(Object array) {
		if (array instanceof double[])
			return Arrays.toString((double[]) array);
		if (array instanceof float[])
			return Arrays.toString((float[]) array);
		if (array instanceof boolean[])
			return Arrays.toString((boolean[]) array);
		if (array instanceof byte[])
			return Arrays.toString((byte[]) array);
		if (array instanceof char[])
			return Arrays.toString((char[]) array);
		if (array instanceof short[])
			return Arrays.toString((short[]) array);
		if (array instanceof int[])
			return Arrays.toString((int[]) array);
		if (array instanceof long[])
			return Arrays.toString((long[]) array);
		if (array instanceof Object[])
			return Arrays.toString((Object[]) array);

		// TODO Auto-generated method stub
		throw new RuntimeException("ainda não implementado");
	}

	public static String quote(String value) {
		return quote(value, DEFAULT_QUOTE_CHAR);
	}

	private static String quote(String text, char quoteChar) {
//		if (isQuoted(text))
//			return text;
		text = text.replace(quoteChar + "", "\\" + quoteChar);
		text = quoteChar + text + quoteChar;
		return text;
	}

	/**
	 * Unquote the given string if it is quoted; single quotes are unescaped. If the
	 * given string is not quoted, it is returned without any modification.
	 *
	 * @param value The string to unquote.
	 * @return The unquoted string.
	 */
	public static String unquote(String value) {
		return unquote(value, DEFAULT_QUOTE_CHAR);
	}

	/**
	 * Unquotes text and unescapes non surrounding quotes. {@code String.replace()}
	 * is a bit too inefficient (see JAVA-67, JAVA-1262).
	 *
	 * @param text      The text
	 * @param quoteChar The character to use as a quote.
	 * @return The text with surrounding quotes removed and non surrounding quotes
	 *         unescaped (i.e. '' becomes ')
	 */
	public static String unquote(String text, char quoteChar) {
		if (!isQuoted(text, quoteChar))
			return text;

		if (text.length() == 2)
			return "";

		String search = emptyQuoted(quoteChar);
		int nbMatch = 0;
		int start = -1;
		do {
			start = text.indexOf(search, start + 2);
			// ignore the second to last character occurrence, as the last character is a
			// quote.
			if (start != -1 && start != text.length() - 2)
				++nbMatch;
		} while (start != -1);

		// no escaped quotes found, simply remove surrounding quotes and return.
		if (nbMatch == 0)
			return text.substring(1, text.length() - 1);

		// length of the new string will be its current length - the number of
		// occurrences.
		int newLength = text.length() - nbMatch - 2;
		char[] result = new char[newLength];
		int newIdx = 0;
		// track whenever a quoteChar is encountered and the previous character is not a
		// quoteChar.
		boolean firstFound = false;
		for (int i = 1; i < text.length() - 1; i++) {
			char c = text.charAt(i);
			if (c == quoteChar) {
				if (firstFound) {
					// The previous character was a quoteChar, don't add this to result, this action
					// in
					// effect removes consecutive quotes.
					firstFound = false;
				} else {
					// found a quoteChar and the previous character was not a quoteChar, include in
					// result.
					firstFound = true;
					result[newIdx++] = c;
				}
			} else {
				// non quoteChar encountered, include in result.
				result[newIdx++] = c;
				firstFound = false;
			}
		}
		return new String(result);
	}

	/**
	 * Return {@code true} if the given string is surrounded by single quotes, and
	 * {@code false} otherwise.
	 *
	 * @param value The string to inspect.
	 * @return {@code true} if the given string is surrounded by single quotes, and
	 *         {@code false} otherwise.
	 */
	public static boolean isQuoted(String value) {
		return isQuoted(value, DEFAULT_QUOTE_CHAR);
	}

	/**
	 * Return {@code true} if the given string is surrounded by the quote character
	 * given, and {@code false} otherwise.
	 *
	 * @param value The string to inspect.
	 * @return {@code true} if the given string is surrounded by the quote
	 *         character, and {@code false} otherwise.
	 */
	private static boolean isQuoted(String value, char quoteChar) {
		if (value == null)
			return false;
		if (value.length() <= 1)
			return false;
		if (value.charAt(0) != quoteChar)
			return false;
		return value.charAt(value.length() - 1) == quoteChar;
	}

	/**
	 * @param quoteChar " or '
	 * @return A quoted empty string.
	 */
	private static String emptyQuoted(char quoteChar) {
		// don't handle non quote characters, this is done so that these are interned
		// and don't create
		// repeated empty quoted strings.
		assert quoteChar == '"' || quoteChar == '\'';
		if (quoteChar == '"')
			return "\"\"";
		else
			return "''";
	}

	public static String[] splitAt(String s, int splitPos) {
		String s0 = s.substring(0, splitPos);
		String s1 = s.substring(splitPos);
		String[] r = new String[] { s0, s1 };
		return r;
	}

	public static boolean equals(String a, String b, boolean ignoreCase) {
		if (ignoreCase) {
			a = a.toLowerCase();
			b = b.toLowerCase();
		}
		return a.equals(b);
	}

	public static boolean contains(String[] list, String value, boolean ignoreCase) {
		for (String s : list) {
			if (equals(s, value, ignoreCase))
				return true;
		}
		return false;
	}

	public static boolean containsAny(String s, String... values) {
		for (String v : values) {
			if (s.contains(v))
				return true;
		}
		return false;
	}

	public static List<String> sortByLength(List<String> list, int direction) {
		ArrayList<String> r = new ArrayList<String>();
		r.addAll(list);
		r.sort(new Comparator<String>() {

			@Override
			public int compare(String s1, String s2) {
				int r = Maths.sign(s1.length() - s2.length());
				r *= direction;
				return r;
			}
		});
		return r;
	}

	public static HashMap<String, String> splitProperties(String declaration) {
		return splitProperties(declaration, "=", ";");
	}
	
	public static HashMap<String, String> splitProperties(String declaration, String attribution, String splitter) {
		HashMap<String, String> r = new HashMap<>();
		String[] props = declaration.split(splitter);
		for (String d : props) {
			String s = d.trim();
			String[] ss = s.split(attribution, 2);
			Assert.that(ss.length == 2);
			Assert.notNulls(ss[0], ss[1]);
			r.put(ss[0].trim(), ss[1].trim());
		}
		return r;
	}
	
	public static String formatText(String msg, Object... params) {
		String orig = msg;
		boolean coded = false;
		for (Object p : params) {
			String s = formattedParam(p);
			if (s.contains("$")) {
				s = s.replace("$", "\\$");
			}
			if (s.contains("%")) {
				s = s.replace("%", MSG_CODE);
				coded = true;
			}
			String msgAnt = msg;
			msg = msg.replaceFirst("%", s);
			if (msg.equals(msgAnt))
				throw new RuntimeException(orig + " - Existem mais parâmetros do que referências: ");
		}
		if (coded)
			msg = msg.replace(MSG_CODE, "%");
		return msg;
	}

	private static final String MSG_CODE = "_WERKJHASFIUPUASKJQWE_";


	private static String formattedParam(Object p) {
		if (p == null)
			return "null";
		if (p instanceof String)
			return (String) p;
		if (p instanceof Number)
			return format((Number) p);
		if (p instanceof Date)
			return Dates.format((Date) p);
		if (p.getClass().isArray()) {
			return formatArray((Object[]) p);
		}

//			return formattedMap((Map) p);
		return p.toString();
	}

	private static <T> String formatArray(T[] array) {
		return "{" + Strings.concat(array, ", ") + "}";
	}

	@Deprecated
	private static String formattedMap(Map map) {
		String r = "";
		for (Object k : map.keySet()) {
			String sk = k.toString();
			Object v = map.get(k);
			String sv = v.toString();
			String s;
			if (sk.contains(" ") || sv.contains(" ")) {
				s = sk + " = " + sv;
				if (!"".equals(r))
					r += "; ";
			} else {
				s = sk + "=" + sv;
				if (!"".equals(r))
					r += ";";
			}
			r += s;
		}
		return r;
	}

	@Deprecated
	public static String formattedDouble(double value) {
		return format(value);
	}

	public static String format(Number value) {
		if (value instanceof Double)
			return format((double) (Double) value);
		return value.toString();
	}

	public static String format(double value) {
		return defaultNumberFormat.format(value);
	}

	private static DecimalFormat defaultNumberFormat = new DecimalFormat("#,###,###,##0.00#####");

	static {
		DecimalFormatSymbols s = new DecimalFormatSymbols();
		s.setDecimalSeparator('.');
		s.setGroupingSeparator(',');
		defaultNumberFormat.setDecimalFormatSymbols(s);
	}


}
