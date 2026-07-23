package util.tables;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;

public interface Map2K<K1, K2, V> {
	// Query Operations

	int size();

	boolean isEmpty();

	boolean containsKey(K1 key1, K2 key2);

	boolean containsValue(Object value);

	V get(K1 k1, K2 k2);

	V put(K1 key1, K2 key2, V value);

	V remove(K1 key1, K2 key2);

	void putAll(Map2K<? extends K1, ? extends K2, ? extends V> m);

	void clear();

	Set<Key2<K1, K2>> keySet();
	
	Set<K1> keySet1();

	Set<K2> keySet2();

	Set<K2> keySet2(K1 k1);

	Collection<V> values();

	Set<Map2K.Entry<K1, K2, V>> entrySet();
	
	interface Key2<K1, K2> {

		K1 getKey1();

		K2 getKey2();
		
	}

	interface Entry<K1, K2, V> {

		K1 getKey1();

		K2 getKey2();

		V getValue();

		V setValue(V value);

		boolean equals(Object o);

		int hashCode();

		public static <K extends Comparable<? super K>, V> Comparator<Map.Entry<K, V>> comparingByKey() {
			return (Comparator<Map.Entry<K, V>> & Serializable) (c1, c2) -> c1.getKey().compareTo(c2.getKey());
		}

		public static <K, V extends Comparable<? super V>> Comparator<Map.Entry<K, V>> comparingByValue() {
			return (Comparator<Map.Entry<K, V>> & Serializable) (c1, c2) -> c1.getValue().compareTo(c2.getValue());
		}

		public static <K, V> Comparator<Map.Entry<K, V>> comparingByKey(Comparator<? super K> cmp) {
			Objects.requireNonNull(cmp);
			return (Comparator<Map.Entry<K, V>> & Serializable) (c1, c2) -> cmp.compare(c1.getKey(), c2.getKey());
		}

		public static <K, V> Comparator<Map.Entry<K, V>> comparingByValue(Comparator<? super V> cmp) {
			Objects.requireNonNull(cmp);
			return (Comparator<Map.Entry<K, V>> & Serializable) (c1, c2) -> cmp.compare(c1.getValue(), c2.getValue());
		}
	}

	// Comparison and hashing

	int hashCode();

	// Defaultable methods

	default V getOrDefault(K1 key1, K2 key2, V defaultValue) {
		V v;
		return (((v = get(key1, key2)) != null) || containsKey(key1, key2)) ? v : defaultValue;
	}

	default void forEach(TriConsumer<? super K1, ? super K2, ? super V> action) {
		Objects.requireNonNull(action);
		for (Map2K.Entry<K1, K2, V> entry : entrySet()) {
			K1 k1;
			K2 k2;
			V v;
			try {
				k1 = entry.getKey1();
				k2 = entry.getKey2();
				v = entry.getValue();
			} catch (IllegalStateException ise) {
				// this usually means the entry is no longer in the map.
				throw new ConcurrentModificationException(ise);
			}
			action.accept(k1, k2, v);
		}
	}

	default void replaceAll(TriFunction<? super K1, ? super K2, ? super V, ? extends V> function) {
		Objects.requireNonNull(function);
		for (Map2K.Entry<K1, K2, V> entry : entrySet()) {
			K1 k1;
			K2 k2;
			V v;
			try {
				k1 = entry.getKey1();
				k2 = entry.getKey2();
				v = entry.getValue();
			} catch (IllegalStateException ise) {
				// this usually means the entry is no longer in the map.
				throw new ConcurrentModificationException(ise);
			}

			// ise thrown from function is not a cme.
			v = function.apply(k1, k2, v);

			try {
				entry.setValue(v);
			} catch (IllegalStateException ise) {
				// this usually means the entry is no longer in the map.
				throw new ConcurrentModificationException(ise);
			}
		}
	}

	default V putIfAbsent(K1 key1, K2 key2, V value) {
		V v = get(key1, key2);
		if (v == null) {
			v = put(key1, key2, value);
		}

		return v;
	}

	default boolean remove(K1 key1, K2 key2, Object value) {
		Object curValue = get(key1, key2);
		if (!Objects.equals(curValue, value) || (curValue == null && !containsKey(key1, key2))) {
			return false;
		}
		remove(key1, key2);
		return true;
	}

	default boolean replace(K1 key1, K2 key2, V oldValue, V newValue) {
		Object curValue = get(key1, key2);
		if (!Objects.equals(curValue, oldValue) || (curValue == null && !containsKey(key1, key2))) {
			return false;
		}
		put(key1, key2, newValue);
		return true;
	}

	default V replace(K1 key1, K2 key2, V value) {
		V curValue;
		if (((curValue = get(key1, key2)) != null) || containsKey(key1, key2)) {
			curValue = put(key1, key2, value);
		}
		return curValue;
	}

	default V computeIfAbsent(K1 key1, K2 key2, BiFunction<? super K1, ? super K2, ? extends V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		V v;
		if ((v = get(key1, key2)) == null) {
			V newValue;
			if ((newValue = mappingFunction.apply(key1, key2)) != null) {
				put(key1, key2, newValue);
				return newValue;
			}
		}

		return v;
	}

//	default V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
//		Objects.requireNonNull(remappingFunction);
//		V oldValue;
//		if ((oldValue = get(key)) != null) {
//			V newValue = remappingFunction.apply(key, oldValue);
//			if (newValue != null) {
//				put(key, newValue);
//				return newValue;
//			} else {
//				remove(key);
//				return null;
//			}
//		} else {
//			return null;
//		}
//	}
//
//	default V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
//		Objects.requireNonNull(remappingFunction);
//		V oldValue = get(key);
//
//		V newValue = remappingFunction.apply(key, oldValue);
//		if (newValue == null) {
//			// delete mapping
//			if (oldValue != null || containsKey(key)) {
//				// something to remove
//				remove(key);
//				return null;
//			} else {
//				// nothing to do. Leave things as they were.
//				return null;
//			}
//		} else {
//			// add or replace old mapping
//			put(key, newValue);
//			return newValue;
//		}
//	}

	default V merge(K1 key1, K2 key2, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
		Objects.requireNonNull(remappingFunction);
		Objects.requireNonNull(value);
		V oldValue = get(key1, key2);
		V newValue = (oldValue == null) ? value : remappingFunction.apply(oldValue, value);
		if (newValue == null) {
			remove(key1, key2);
		} else {
			put(key1, key2, newValue);
		}
		return newValue;
	}

}

