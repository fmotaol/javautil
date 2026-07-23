package util.tables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import util.logical.Assert;

public class MultiLevelHashMap2K<K1, K2, V> implements Map2K<K1, K2, V> {
	
	private Map<K1, Map<K2, V>> map = new HashMap<>();

	public int size() {
		int r = 0;
		for (Map.Entry<K1, Map<K2, V>> e : map.entrySet()) {
			Map<K2, V> m = e.getValue();
			r += m.size();
		}
		return r;
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public boolean containsKey(K1 key1, K2 key2) {
		if (!map.containsKey(key1))
			return false;
		Map<K2, V> e = map.get(key1);
		Assert.that(e != null, "Erro interno");
		return e.containsKey(key2);
	}

	public boolean containsValue(Object value) {
		for (Map.Entry<K1, Map<K2, V>> e : map.entrySet()) {
			Map<K2, V> m = e.getValue();
			if (m.containsValue(value))
				return true;					
		}
		return false;
	}

	public V get(K1 key1, K2 key2) {
		Map<K2, V> m = map.get(key1);
		if (m == null)
			return null;
		return m.get(key2);
	}

	public V put(K1 key1, K2 key2, V value) {
		Map<K2, V> m = map.get(key1);
		if (m == null) {
			m = new HashMap<>();
			map.put(key1, m);
		}
		return m.put(key2, value);
	}

	public V remove(K1 key1, K2 key2) {
		Map<K2, V> m = map.get(key1);
		if (m == null) 
			return null;
		V r = m.remove(key2);
		if (m.isEmpty())
			map.remove(key1);
		return r;
	}

	public void putAll(Map2K<? extends K1, ? extends K2, ? extends V> m) {
//		for (K1 k1 : m.keySet1()) {
//			for (K2 k2 : m.keySet2(k1)) {
//				V v = m.get(k1, k2);
//				put(k1, k2, v);
//			}
//		}
		throw new RuntimeException("ainda não implementado");
	}

	public void clear() {
		map.clear();
	}

	public Set<Key2<K1, K2>> keySet() {
		HashSet<Key2<K1, K2>> r = new HashSet<>();
		for (K1 k1 : map.keySet()) {
			Map<K2, V> m = map.get(k1);
			for (K2 k2 : m.keySet()) {
				Key2<K1, K2> e = new Key2<K1, K2>() {

					@Override
					public K1 getKey1() {
						return k1;
					}

					@Override
					public K2 getKey2() {
						return k2;
					}
				};
				r.add(e);
			}
		}
		return r;
	}

	public Collection<V> values() {
		ArrayList<V> r = new ArrayList<>();
		for (K1 k1 : map.keySet()) {
			Map<K2, V> m = map.get(k1);
			Collection<? extends V> vs = m.values();
			r.addAll(vs);
		}
		return r;
	}

	class Entry3 implements Entry<K1, K2, V> {
		
		K1 k1;
		K2 k2;		
		V v;

		public Entry3(K1 k1, K2 k2, V v) {
			super();
			this.k1 = k1;
			this.k2 = k2;
			this.v = v;
		}

		@Override
		public K1 getKey1() {
			return k1;
		}

		@Override
		public K2 getKey2() {
			return k2;
		}

		@Override
		public V getValue() {
			return v;
		}

		@Override
		public V setValue(V value) {
			v = value;
			return v;
		}
		
	}
	
	public Set<Entry<K1, K2, V>> entrySet() {
		HashSet<Entry<K1, K2, V>> r = new HashSet<>();
		for (K1 k1 : map.keySet()) {
			Map<K2, V> m = map.get(k1);
			for (K2 k2 : m.keySet()) {
				V v = m.get(k2);
				Entry<K1, K2, V> e = new Entry3(k1, k2, v); 
				r.add(e);
			}
		}
		return r;
	}

	public int hashCode() {
		return map.hashCode();
	}

	public V getOrDefault(K1 key1, K2 key2, V defaultValue) {
		Map<K2, V> m = map.get(key1);
		if (m == null)
			return defaultValue;
		V v = m.get(key2);
		if (v != null)
			return v;
		else
			return defaultValue;
	}

	public void forEach(TriConsumer<? super K1, ? super K2, ? super V> action) {
		for (K1 k1 : map.keySet()) {
			Map<K2, V> m = map.get(k1);
			m.forEach((k2, v) -> action.accept(k1, k2, v));
		}
	}

	public void replaceAll(TriFunction<? super K1, ? super K2, ? super V, ? extends V> function) {
		for (K1 k1 : map.keySet()) {
			Map<K2, V> m = map.get(k1);
			m.forEach((k2, v) -> function.apply(k1, k2, v));
		}
	}

	public V putIfAbsent(K1 key1, K2 key2, V value) {
		throw new RuntimeException("ainda não implementado");
//		return map.putIfAbsent(key1, key2, value);
	}

	public boolean remove(K1 key1, K2 key2, Object value) {
		throw new RuntimeException("ainda não implementado");
//		return map.remove(key1, key2, value);
	}

	public boolean replace(K1 key1, K2 key2, V oldValue, V newValue) {
		throw new RuntimeException("ainda não implementado");
//		return map.replace(key1, key2, oldValue, newValue);
	}

	public V replace(K1 key1, K2 key2, V value) {
		throw new RuntimeException("ainda não implementado");
//		return map.replace(key1, key2, value);
	}

	public V computeIfAbsent(K1 key1, K2 key2, BiFunction<? super K1, ? super K2, ? extends V> mappingFunction) {
		throw new RuntimeException("ainda não implementado");
//		return map.computeIfAbsent(key1, key2, mappingFunction);
	}

	public V merge(K1 key1, K2 key2, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
		throw new RuntimeException("ainda não implementado");
//		return map.merge(key1, key2, value, remappingFunction);
	}

	@Override
	public Set<K1> keySet1() {
		return map.keySet();
	}

	@Override
	public Set<K2> keySet2() {
		HashSet<K2> r = new HashSet<>();
		for (K1 k1 : map.keySet()) {
			Map<K2, V> m = map.get(k1);
			r.addAll(m.keySet());
		}
		return r;
	}

	@Override
	public Set<K2> keySet2(K1 k1) {
		Map<K2, V> m = map.get(k1);
		return m.keySet();
	}

}
