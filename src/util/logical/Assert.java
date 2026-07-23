package util.logical;

public class Assert {

	public static void that(boolean condition, String error) {
		if (!condition)
			throw new RuntimeException(error);
	}

	public static void not(boolean condition, String error) {
		if (condition)
			throw new RuntimeException(error);
	}

	public static void isNull(Object value, String error) {
		if (value != null)
			throw new RuntimeException(error);
	}

	public static void isNotNull(Object value, String error) {
		if (value == null)
			throw new RuntimeException(error);
	}

	public static void that(boolean condition) {
		that(condition, "Condição inválida");
	}

	public static void not(boolean condition) {
		that(condition, "Condição não prevista");
	}

	public static void isNull(Object value) {
		isNull(value, "elemento não é nulo");
	}

	public static void notNull(Object value) {
		isNotNull(value, "elemento é nulo");
	}

	public static void notNulls(Object... values) {
		for (Object v : values)
			Assert.that(v != null, "elemento nulo");
	}

	public static <T> void isNotEmpty(T[] array) {
		that(array.length > 1, "Array não pode ser vazio");
	}

	public static <T> void minLength(T[] array, int length) {
		that(array.length > length, "Array não pode ter menos de " + length + " elementos");
	}
	
	public static <T> T checkedNotNull(T object) {
		notNull(object);
		return object;
	}

	public static <T> void different(T o1, T o2) {
		notNulls(o1, o2);
		that(!o1.equals(o2), "valores iguais");
	}

	public static <T> T notDifferent(T... values) {
		return same(values);
	}

	public static <T> T same(T... values) {
		T r = null;
		for (T v : values) {
			if (r == null)
				r = v;
			if (v == null)
				continue;
			if (!r.equals(v))
				throw new RuntimeException("Valores diferentes");
		}
		return r;
	}

	public static <T> T error(String msg) {
		throw new RuntimeException(msg);
	}

	public static boolean sure(Boolean a) {
		Assert.notNull(a);
		return a;
	}
	
	public static int sure(Integer a) {
		Assert.notNull(a);
		return a;
	}

	public static long sure(Long a) {
		Assert.notNull(a);
		return a;
	}

	public static float sure(Float a) {
		Assert.notNull(a);
		return a;
	}

	public static double sure(Double a) {
		Assert.notNull(a);
		return a;
	}

	public static short sure(Short a) {
		Assert.notNull(a);
		return a;
	}

	public static char sure(Character a) {
		Assert.notNull(a);
		return a;
	}

}
