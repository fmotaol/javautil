package util.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class CacheMap_Old<K, V> implements Map<K, V> {
	
	private Map<K, V> map;

	public CacheMap_Old() {
		super();
	}

	public int size() {
		throw new RuntimeException("ainda não implementado");
//		return map.size();
	}

	public boolean isEmpty() {
		throw new RuntimeException("ainda não implementado");
//		return map.isEmpty();
	}

	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	public V get(Object key) {
		Cache<V> c = getCache(key);
		return c.get();
	}

	public V put(K key, V value) {
		Cache<V> c = getCache(key);
		c.update(value);
		return value;
	}

	public V remove(Object key) {
		
		map.remove(c);
		return r;
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		map.putAll(m);
	}

	public void clear() {
		map.clear();
	}

	public Set<K> keySet() {
		return map.keySet();
	}

	public Collection<V> values() {
		return map.values();
	}

	public Set<Entry<K, V>> entrySet() {
		return map.entrySet();
	}

	public boolean equals(Object o) {
		throw new RuntimeException("ainda não implementado");
//		return map.equals(o);
	}

	public int hashCode() {
		throw new RuntimeException("ainda não implementado");
//		return map.hashCode();
	}

	public V getOrDefault(Object key, V defaultValue) {
		return map.getOrDefault(key, defaultValue);
	}

	public void forEach(BiConsumer<? super K, ? super V> action) {
		map.forEach(action);
	}

	public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
		map.replaceAll(function);
	}

	public V putIfAbsent(K key, V value) {
		return map.putIfAbsent(key, value);
	}

	public boolean remove(Object key, Object value) {
		return map.remove(key, value);
	}

	public boolean replace(K key, V oldValue, V newValue) {
		return map.replace(key, oldValue, newValue);
	}

	public V replace(K key, V value) {
		return map.replace(key, value);
	}

	public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
		return map.computeIfAbsent(key, mappingFunction);
	}

	public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		return map.computeIfPresent(key, remappingFunction);
	}

	public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		return map.compute(key, remappingFunction);
	}

	public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
		return map.merge(key, value, remappingFunction);
	}

}
