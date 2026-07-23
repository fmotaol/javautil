package util.logical;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

public class Check {

	public static boolean anyNull(Object... values) {
		return anyElementNull(values);
	}

	public static <T> boolean any(T[] list, Predicate<T> criteria) {
		for (T e : list) {
			if (criteria.test(e))
				return true;
		}
		return false;
	}

	public static <T> boolean any(Collection<T> list, Predicate<T> criteria) {
		for (T e : list) {
			if (criteria.test(e))
				return true;
		}
		return false;
	}

	public static <T> boolean every(T[] list, Predicate<T> criteria) {
		for (T e : list) {
			if (!criteria.test(e))
				return false;
		}
		return true;
	}

	public static <T> boolean every(List<T> list, Predicate<T> criteria) {
		for (T e : list) {
			if (!criteria.test(e))
				return false;
		}
		return true;
	}
	
	public static boolean anyElementNull(Object[] values) {
		for (Object v : values)
			if (v == null)
				return true;
		return false;
	}

	public static boolean anyElementNull(Collection<?> values) {
		for (Object v : values)
			if (v == null)
				return true;
		return false;
	}

	public static boolean everyNull(Object... values) {
		return everyElementNull(values);
	}

	public static boolean everyElementNull(Object[] values) {
		for (Object v : values)
			if (v == null)
				return true;
		return false;
	}

	public static <T> boolean allSame(T... values) {
		Assert.that(values.length > 1, "Lista incompleta");
		T r = values[0];
		for (T v : values) {
			if (!equalValues(r, v))
				return false;
		}
		return true;
	}

	public static <T extends Object> boolean isAnyOf(T value, T[] list) {
		for (Object o : list) {
			if (o == null)
				continue;
			if (o.equals(value))
				return true;
		}
		return false;
	}

	public static <T extends Object> boolean isAny(T value, T... list) {
		return isAnyOf(value, list);
	}

	public static <T extends Comparable<T>> int compare(T a, T b) {
		return a.compareTo(b);
	}

//	public static <T extends Comparable<T>> T greatest(T... values) {
//		T r = null;
//		for (T t : values) {
//			if (t == null)
//				continue;
//			if (r == null)
//				r = t;
//			if (compare(t, r) == 1)
//				r = t;
//		}
//		return r;
//	}

//	@Deprecated
//	public static <T extends Comparable<T>> T smallest(T... values) {
//		return lowest(values);
//	}

	public static <T extends Comparable<T>> T lowest(T... values) {
		T r = null;
		for (T t : values) {
			if (t == null)
				continue;
			if (r == null)
				r = t;
			if (compare(t, r) == -1)
				r = t;
		}
		return r;
	}

	@Deprecated
	public static <T extends Comparable<T>> T greatest(Collection<T> set) {
		return greatest(set);
	}

	public static <T extends Comparable<T>> T highest(T... values) {
		T r = null;
		for (T t : values) {
			if (t == null)
				continue;
			if (r == null)
				r = t;
			if (compare(t, r) == 1)
				r = t;
		}
		return r;
	}

	public static <T> boolean contains(T[] array, T element) {
		for (T e : array) {
			if (e.equals(element))
				return true;
		}

		return false;
	}

	public static <T> boolean equalValues(Object a, Object b) {
		if ((a == null) && (b == null))
			return true;
		if ((a == null) || (b == null))
			return false;
		return a.equals(b);
	}

	public static <T> T coalesce(T... options) {
		for (T o : options) {
			if (o != null)
				return o;
		}
		return null;
	}

	public static <T> T ifThen(boolean condition, T valueThen) {
		if (condition)
			return valueThen;
		else
			return null;
	}

	public static <T> T ifThen(boolean condition, T valueThen, T valueElse) {
		if (condition)
			return valueThen;
		else
			return valueElse;
	}

	public static <T> T caseWhen(boolean case0, T then0, boolean case1, T then1) {
		if (case0)
			return then0;
		if (case1)
			return then1;
		return null;
	}

	public static <T> T caseWhen(boolean case0, T then0, boolean case1, T then1, String error) {
		if (!case0 && !case1)
			throw new RuntimeException(error);
		return caseWhen(case0, then0, case1, then1);
	}

	public static <T> T caseWhen(boolean case0, T then0, boolean case1, T then1, boolean case2, T then2) {
		if (case0)
			return then0;
		if (case1)
			return then1;
		if (case2)
			return then2;
		return null;
	}

	public static <T> T caseWhen(boolean case0, T then0, boolean case1, T then1, boolean case2, T then2, String error) {
		if (!case0 && !case1 && !case2)
			throw new RuntimeException(error);
		return caseWhen(case0, then0, case1, then1, case2, then2);
	}

	public static <T> T caseWhen(boolean case0, T then0, boolean case1, T then1, boolean case2, T then2, boolean case3,
			T then3) {
		if (case0)
			return then0;
		if (case1)
			return then1;
		if (case2)
			return then2;
		if (case3)
			return then3;
		return null;
	}

	public static <T> T caseWhen(boolean case0, T then0, boolean case1, T then1, boolean case2, T then2, boolean case3,
			T then3, String error) {
		if (!case0 && !case1 && !case2 && !case3)
			throw new RuntimeException(error);
		return caseWhen(case0, then0, case1, then1, case2, then2, case3, then3);
	}

	public static <V, R> R switchCase(V var, V val0, R ret0, V val1, R ret1) {
		return caseWhen(var == val0, ret0, var == val1, ret1);
	}

	public static <V, R> R switchCase(V var, V val0, R ret0, V val1, R ret1, V val2, R ret2) {
		return caseWhen(var == val0, ret0, var == val1, ret1, var == val2, ret2);
	}

	public static <V, R> R switchCase(V var, V val0, R ret0, V val1, R ret1, V val2, R ret2, V val3, R ret3) {
		return caseWhen(var == val0, ret0, var == val1, ret1, var == val2, ret2, var == val3, ret3);
	}

	public static boolean between(int v, int lower, int higher) {
		return (v >= lower) && (v <= higher);
	}

	public static boolean between(double v, double lower, double higher) {
		return (v >= lower) && (v <= higher);
	}

	public static boolean between(Date v, Date lower, Date higher) {
		return v.equals(lower) || v.equals(higher) || (v.after(lower) && v.before(higher));
	}


}
