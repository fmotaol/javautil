package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;

public class Colls {

	public static <A, B> HashMap<A, B> copy(Map<A, B> from, Map<A, B> into) {
		HashMap<A, B> r = new HashMap<>();
		for (A k : from.keySet())
			r.put(k, from.get(k));
		return r;
	}

	public static <T> List<T> toList(Collection<T> col) {
		ArrayList<T> r = new ArrayList<T>();
		r.addAll(col);
		return r;
	}

	public static <T> List<T> toList(T[] array) {
		ArrayList<T> r = new ArrayList<T>();
		for (int i = 0; i < array.length; i++) {
			r.add(array[i]);
		}
		return r;
	}

	public static <T> T[] toArray(T... list) {
		return list;
	}

	public static <T> T[] asArray(List<T> list) {
		return (T[]) list.toArray();
	}

	public static <T> List<T> copy(List<T> templateList) {
		ArrayList<T> r = new ArrayList<>();
		r.addAll(templateList);
		return r;
	}

	public static <K, V> void addAll(Map<K, V> to, Map<K, V> from) {
		for (Entry<K, V> e : from.entrySet()) {
			to.put(e.getKey(), e.getValue());
		}
	}

	public static <T> List<T> filter(Collection<T> source, Predicate<T> filter) {
		ArrayList<T> r = new ArrayList<>();
		for (T e : source) {
			if (filter != null)
				if (!filter.test(e))
					continue;
			r.add(e);
		}
		return r;
	}

	public static <T> T findFirst(Collection<T> source, Predicate<T> condition) {
		for (T e : source) {
			if (condition.test(e))
				return e;
		}
		return null;
	}

	public static <T> T findOnlyOne(Collection<T> source, Predicate<T> condition) {
		T r = null;
		for (T e : source) {
			if (condition.test(e)) {
				if (r == null)
					r = e;
				else
					throw new RuntimeException("Encontrada mais de uma ocorrência");
			}
		}
		return r;
	}

	public static <T> T[] filter(T[] source, Predicate<T> filter) {
		ArrayList<T> ar = new ArrayList<>();
		for (T e : source) {
			if (filter != null)
				if (!filter.test(e))
					continue;
			ar.add(e);
		}
		T[] r = Arrays.copyOf(source, ar.size());
		r = ar.toArray(r);
		return r;
	}

	public static <K, V> Map<K, V> filter(Map<K, V> source, Predicate<K> filter) {
		HashMap<K, V> r = new HashMap<K, V>();
		for (K k : source.keySet()) {
			if (filter != null)
				if (!filter.test(k))
					continue;
			V v = source.get(k);
			r.put(k, v);
		}
		return r;
	}

	public static <I, O> List<O> transform(List<I> source, Function<I, O> operation) {
		ArrayList<O> r = new ArrayList<>();
		for (I e : source) {
			O d = operation.apply(e);
			r.add(d);
		}
		return r;
	}

	public static <I, O> O[] transform(I[] source, Function<I, O> operation) {
		O[] r = (O[]) Arrays.copyOf(source, source.length);
		for (int i = 0; i < source.length; i++) {
			I e = source[i];
			O d = operation.apply(e);
			r[i] = d;
		}
		return r;
	}

	public static <T> List<T> union(List<T>... lists) {
		ArrayList<T> r = new ArrayList<>();
		for (List<T> i : lists) {
			r.addAll(i);
		}
		return r;
	}

	public static int countNulls(Collection<?> col) {
		int r = 0;
		for (Object o : col)
			if (o == null)
				r++;
		return r;
	}

	public static <T> int countNulls(T[] items) {
		int r = 0;
		for (T i : items) {
			if (i == null)
				r++;
		}
		return r;
	}

	public static <T> T[] arrayPart(T[] array, int index, int size) {
		T[] r = Arrays.copyOf(array, size);
		for (int i = 0; i < r.length; i++) {
			r[i] = array[i + index];
		}
		return r;
	}

	public static <T> T[] arrayPart(T[] array, int index) {
		T[] r = arrayPart(array, index, array.length - index);
		return r;
	}

	public static <T> int notNulls(T[] items) {
		int r = 0;
		for (T i : items) {
			if (i != null)
				r++;
		}
		return r;
	}

	public static <T> T[] scroll(T[] array, T newItem) {
		for (int i = 0; i < array.length - 1; i++) {
			array[i] = array[i + 1];
		}
		array[array.length - 1] = newItem;
		return array;
	}

	public static <T> List<T> intersection(Collection<T> a, Collection<T> b) {
		ArrayList<T> r = new ArrayList<>();
		for (T e : a) {
			if (b.contains(e))
				r.add(e);
		}
		return r;
	}

	public static <T> List<T> absentList(Collection<T> outterList, Collection<T> innerList) {
		ArrayList<T> r = new ArrayList<>();
		for (T e : outterList) {
			if (!innerList.contains(e))
				r.add(e);
		}
		return r;
	}

	public static <T> List<T> changedElements(Collection<T> into, Collection<T> from, Comparator<T> idComparator) {

		ArrayList<T> r = new ArrayList<>();
		for (T a : from) {
			for (T b : into) {
				if (a == null || b == null)
					continue;
				if (idComparator.compare(a, b) != 0)
					continue;
				if (!a.equals(b))
					r.add(b);
			}
		}
		return r;
	}

	public static <T> T[] invertOrder(T[] array) {
		T[] r = Arrays.copyOf(array, array.length);
		for (int i = 0; i < r.length; i++) {
			int j = r.length - i - 1;
			r[j] = array[i];
		}
		return r;
	}

}
