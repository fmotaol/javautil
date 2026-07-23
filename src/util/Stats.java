package util;

import java.util.Arrays;
import java.util.Comparator;

public class Stats {

	public static Double avg(Double... values) {
		Double s = sum(values);
		Double l = values.length + 0.0;
		if (l == 0.0)
			return null;
		return s / l;
	}

	public static Double wavg(Double[] values, Double[] weights) {
		throw new RuntimeException("ainda não implementado");
	}

	public static Double wavg(double v1, double v2, double w1, double w2) {
		return v1*w1 + v2*w2 / (w1 + w2);
	}

	public static Double sum(Double[] values) {
		Double r = 0.0;
		for (Double e : values)
			r += e;
		return r;
	}

	public static Double stableAvg(Double[] values, int elementsToDiscard) {
		values = sortByDispersion(values);
		Double[] s = Arrays.copyOf(values, values.length - elementsToDiscard);
		return avg(s);
	}

	public static Double distance(Double a, Double b) {
		return Math.abs(a - b);
	}
	
	private static Double[] sortByDispersion(Double[] values) {
		double a = avg(values);
		Arrays.sort(values, new Comparator<Double>() {

			@Override
			public int compare(Double o1, Double o2) {
				Double d1 = distance(o1, a);
				Double d2 = distance(o2, a);
				if (d1 > d2)
					return +1;
				if (d1 < d2)
					return -1;
				return 0;
			}
		});
		return values;
	}

	@Draft
	public static Double stableAvg(Double[] values) {
		return stableAvg(values, 1);
	}

	public static double[] toPrimitive(Double[] ar) {
		double[] r = new double[ar.length];
		for (int i = 0; i < ar.length; i++) {
			r[i] = ar[i];
		}
		return r;
	}

	public static Double[] toWrapper(double[] ar) {
		Double[] r = new Double[ar.length];
		for (int i = 0; i < ar.length; i++) {
			r[i] = ar[i];
		}
		return r;
	}

	public static Double min(Double... values) {
		Double r = null;
		for (Double v : values) {
			if (r == null)
				r = v;
			if (r > v)
				r = v;
		}
		return r;
	}

	public static Double max(Double... values) {
		Double r = null;
		for (Double v : values) {
			if (r == null)
				r = v;
			if (r < v)
				r = v;
		}
		return r;
	}

	public static Double linearProjection(Double[] history, int longitude) {
		if (history.length < 2)
			return null;
		int lasti = history.length - 1;
		double lastValue = history[lasti];
		Double[] proj = new Double[history.length - 1];

		for (int i = lasti - 1; i >= 0; i--) {
			double v = history[i];
			double delta = lastValue - v;
			proj[i] = lastValue + delta * longitude + i;
		}
		
		Double r = avg(proj);
		return r;
	}

}
