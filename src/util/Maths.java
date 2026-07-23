package util;

import util.logical.Assert;
import util.logical.Check;

public class Maths {

	public enum RoundType {
		FLOOR, HALF, CEIL
	};

	public static double round(double value, int digits) {
		return round(value, digits, RoundType.HALF);
	}

	public static double round(double value, int digits, RoundType roundType) {
		Assert.that(digits >= 0);
		double base = Math.pow(10, digits);
		value = value * base;
		value = round(value, roundType);
		value = value / base;
		return value;
	}

	public static double roundByStep(double value, double step, RoundType roundType) {
		Assert.that(step >= 0);
		if (step == 0)
			return value;
		double d = value / step;
		d = round(d, roundType);
		value = d * step;
		return value;
	}

	public static double round(double value, RoundType roundType) {
		switch (roundType) {
		case FLOOR:
			value = Math.floor(value);
		case HALF:
			value = Math.round(value);
		case CEIL:
			value = Math.ceil(value);
		}
		return value;
	}

	public static double relativeDifference(double ref, double value) {
		return (value - ref) / ref;
	}

	public static boolean similarValues(Double value, Double reference, double relativeTolerance) {
		if (value == null)
			return false;
		if (reference == null)
			return false;

		double rd = Maths.relativeDifference(reference, value);
		rd = Math.abs(rd);
		return rd <= relativeTolerance;

	}

	public static int random(int limit) {
		double d = Math.random() * limit;
		return (int) Math.round(d);
	}

	public static int random(int from, int to) {
		return (int) random((long) from, (long) to);
	}

	public static long random(long from, long to) {
		Assert.that(from < to);
		long limit = to - from;
		double d = Math.random() * limit;
		long r = (int) Math.round(d);
		return from + r;
	}

	public static <N extends Number> N sum(N... values) {
		return sum((N[]) values);
	}

	public static <N extends Number> N sum(Iterable<N> values) {
		N r = null;
		for (N v : values) {
			if (v == null)
				continue;
			if (r == null)
				r = v;
			r = sum2(r, v);
		}
		return r;
	}

//	public static Long sum(Long... values) {
//		Long r = null;
//		for (Long v : values) {
//			if (v == null)
//				continue;
//			if (r == null)
//				r = v;
//			r += v;
//		}
//		return r;
//	}

	private static <N extends Number> N sum2(N a, N b) {
		if (a == null)
			return b;
		if (b == null)
			return a;

		if ((a instanceof Integer) && (b instanceof Integer)) {
			int r = (int) a + (int) b;
			return (N) (Integer) r;
		}

		if ((a instanceof Double) || (b instanceof Double)) {
			double r = (double) a + (double) b;
			return (N) (Double) r;
		}

		if ((a instanceof Float) && (b instanceof Long)) {
			double r = (double) a + (double) b;
			return (N) (Double) r;
		}

		if ((a instanceof Long) && (b instanceof Float)) {
			double r = (double) a + (double) b;
			return (N) (Double) r;
		}

		if ((a instanceof Float) || (b instanceof Float)) {
			float r = (float) a + (float) b;
			return (N) (Float) r;
		}

		if ((a instanceof Long) || (b instanceof Long)) {
			long r = (long) a + (long) b;
			return (N) (Long) r;
		}

		throw new RuntimeException("ainda não implementado");
	}

	private static <N extends Number> N sub2(N a, N b) {
		if (a == null)
			return b;
		if (b == null)
			return a;

		if ((a instanceof Integer) && (b instanceof Integer)) {
			int r = (int) a - (int) b;
			return (N) (Integer) r;
		}

		if ((a instanceof Double) || (b instanceof Double)) {
			double r = (double) a - (double) b;
			return (N) (Double) r;
		}

		if ((a instanceof Float) && (b instanceof Long)) {
			double r = (double) a - (double) b;
			return (N) (Double) r;
		}

		if ((a instanceof Long) && (b instanceof Float)) {
			double r = (double) a - (double) b;
			return (N) (Double) r;
		}

		if ((a instanceof Float) || (b instanceof Float)) {
			float r = (float) a - (float) b;
			return (N) (Float) r;
		}

		if ((a instanceof Long) || (b instanceof Long)) {
			long r = (long) a - (long) b;
			return (N) (Long) r;
		}

		throw new RuntimeException("ainda não implementado");
	}

	public static <N extends Number> N div(N a, N b) {
		if (a == null)
			return b;
		if (b == null)
			return a;

		if ((a instanceof Integer) && (b instanceof Integer)) {
			int r = (int) a / (int) b;
			return (N) (Integer) r;
		}

		if ((a instanceof Double) || (b instanceof Double)) {
			double r = (double) a / (double) b;
			return (N) (Double) r;
		}

		if ((a instanceof Float) && (b instanceof Long)) {
			double r = (double) a / (double) b;
			return (N) (Double) r;
		}

		if ((a instanceof Long) && (b instanceof Float)) {
			double r = (double) a / (double) b;
			return (N) (Double) r;
		}

		if ((a instanceof Float) || (b instanceof Float)) {
			float r = (float) a / (float) b;
			return (N) (Float) r;
		}

		if ((a instanceof Long) || (b instanceof Long)) {
			long r = (long) a / (long) b;
			return (N) (Long) r;
		}

		throw new RuntimeException("ainda não implementado");
	}

	public static <N extends Number> N sub(N a, N b) {
		return sub2(a, b);
	}

//	public static <N extends Number> N avg(N[] items) {
//		return avg(items);
//	}

	public static <N extends Number> N avg(N[] items) {
		N num = sum((N[]) items);
		int den = Colls.notNulls(items);
		N r = (N) div(num, den);
		return r;
	}


	public static String randomKey(int length) {
		return randomKey(length, true, true, true);
	}

	public static String randomKey(int length, boolean digits, boolean upper, boolean lower) {
		String r = "";
		do {
			int i = Maths.random(255);

			char type = (char) 0;

			if (Check.between(i, 48, 57))
				type = 'd';
			else if (Check.between(i, 65, 90))
				type = 'u';
			else if (Check.between(i, 97, 122))
				type = 'l';

			if (type == 'd') {
				if (!digits)
					continue;
			} else if (type == 'u') {
				if (!upper)
					continue;
			} else if (type == 'l') {
				if (!lower)
					continue;
			} else
				continue;

			char c = (char) i;
			
			r += c;

		} while (r.length() < length);
		
		return r;
	}

	public static int sign(int value) {
		if (value > 0)
			return +1;
		if (value < 0)
			return -1;
		return 0;
	}

	public static boolean isNumeric(String val) {
		try {
			Float.parseFloat(val);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static Float asNumeric(String val) {
		Float r;
		try {
			r = Float.parseFloat(val);
			return r;
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean isInt(String val) {
		try {
			Integer.parseInt(val);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static Integer asInt(String val) {
		Integer r;
		try {
			r = Integer.parseInt(val);
			return r;
		} catch (Exception e) {
			return null;
		}
	}
	
}
