package util;

import java.text.DecimalFormat;
import java.util.Date;

import util.logical.Assert;

public class Util {

	private static DecimalFormat defaultPercentFormat = new DecimalFormat("#,###,###,##0.0%");

	private static DecimalFormat moneyFormat = new DecimalFormat("#,###,###,##0.00");

	public static String formatMoney(Double value) {
		if (value == null)
			return "?.??";
		return moneyFormat.format(value);
	}

	public static String formatPercent(double rel) {
		String r = defaultPercentFormat.format(rel);
		return r;
	}

	private static DecimalFormat defaultValueFormat = new DecimalFormat("###,###,###,###,##0.0#####");

	public static String format(Double value) {
		String r = defaultValueFormat.format(value);
		return r;
	}

	public static String format(double value, String format) {
		DecimalFormat df = new DecimalFormat(format);
		String r = df.format(value);
		return r;
	}

	public static String format(double value, Integer precision) {
		return format(value, precision, true);
	}

	public static String format(double value, Integer precision, boolean thousandsSeparator) {
		if (precision == null)
			return Strings.toString(value);
		Assert.that(precision >= 0);
		String integerPart = thousandsSeparator ? "###,###,###,###,###" : "###############";
		String dp;
		if (precision > 0)
			dp = "." + Strings.repeat("#", precision);
		else
			dp = "";
		String format = integerPart + dp;
		DecimalFormat df = new DecimalFormat(format);
		String r = df.format(value);
		return r;
	}

	@Deprecated
	public static <T> int nulls(T[] items) {
		int r = 0;
		for (T i : items) {
			if (i == null)
				r++;
		}
		return r;
	}

	public static String[] format(double[] values, int[] precisions) {
		Assert.that(values.length == precisions.length);
		String[] r = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			double v = values[i];
			int prec = precisions[i];
			r[i] = format(v, prec);
		}
		return r;
	}

	public static double[] unwrap(Double[] tps) {
		double[] r = new double[tps.length];
		for (int i = 0; i < tps.length; i++) {
			r[i] = tps[i];
		}
		return r;
	}

	public static Double[] wrap(double[] tps) {
		Double[] r = new Double[tps.length];
		for (int i = 0; i < tps.length; i++) {
			r[i] = tps[i];
		}
		return r;
	}

}
