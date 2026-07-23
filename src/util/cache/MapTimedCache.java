package util.cache;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

@Deprecated
public class MapTimedCache<K, V> extends TimedCache<Map<K, V>> implements Map<K, V> {

	private HashSet<K> invalidKeys = new HashSet<>();

	public MapTimedCache(long refreshInterval, Callable<Map<K, V>> updater) {
		super(refreshInterval, updater);
	}

//	public MapTimedCache(long refreshInterval) {
//		super(refreshInterval);
//	}

	@Override
	public int size() {
		return get().size();
	}

	@Override
	public boolean isEmpty() {
		return get().isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return get().containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		for (K k : keySet()) {
			V v = get(k);
			if (value.equals(v))
				return true;
		}
		return false;
	}

	@Override
	public V get(Object key) {
		if (!isValid((K) key))
			refresh();
		
		return get().get(key);
	}

	@Override
	public V put(K key, V value) {
		return get().put(key, value);
	}

	public boolean isValid(K key) {
		return super.isValid() && !invalidKeys.contains(key);
	}
	
	@Override
	public boolean isValid() {
		return super.isValid(); // && invalidKeys.isEmpty();
	}

	@Override
	public void refresh() {
		invalidKeys.clear();
		super.refresh();
	}

	@Override
	public void update(Map<K, V> value) {
		invalidKeys.clear();
		super.update(value);
	}

	public void invalidate(K key) {
		invalidKeys.add(key);
	}

	@Override
	public V remove(Object key) {
		if (!isValid((K) key))
			refresh();
		return get().remove(key);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		get().putAll(m);
	}

	@Override
	public void clear() {
		get().clear();
	}

	@Override
	public Set<K> keySet() {
		return get().keySet();
	}

	@Override
	public Collection<V> values() {
		if (!invalidKeys.isEmpty())
			refresh();
		return get().values();
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		if (!invalidKeys.isEmpty())
			refresh();
		return get().entrySet();
	}

}
