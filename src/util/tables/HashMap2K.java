package util.tables;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class HashMap2K<K1, K2, V> implements Map2K<K1, K2, V> {
	
	private HashMap<DoubleKey, V> map = new HashMap<>();

	public class DoubleKey implements Key2<K1, K2> {
		
		private K1 key1;
		
		private K2 key2;

		public DoubleKey(K1 key1, K2 key2) {
			this.key1 = key1;
			this.key2 = key2;
		}

		@Override
		public K1 getKey1() {
			return key1;
		}

		@Override
		public K2 getKey2() {
			return key2;
		}

		@Override
		public int hashCode() {
			return key1.hashCode() + key2.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof HashMap2K.DoubleKey))
				return false;
			DoubleKey k = (HashMap2K<K1, K2, V>.DoubleKey) obj;
			return key1.equals(k.key1) && key2.equals(k.key2);
		}

		@Override
		public String toString() {
			return map.toString();
		}
		
	}

	protected DoubleKey key(K1 key1, K2 key2) {
		return new DoubleKey(key1, key2);
	}
	
	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(K1 key1, K2 key2) {
		return map.containsKey(key(key1, key2));
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public V get(K1 key1, K2 key2) {
		return map.get(key(key1, key2));
	}

	@Override
	public V put(K1 key1, K2 key2, V value) {
		return map.put(key(key1, key2), value);
	}

	@Override
	public V remove(K1 key1, K2 key2) {
		return map.remove(key(key1, key2));
	}

	@Override
	public void putAll(Map2K<? extends K1, ? extends K2, ? extends V> m) {
		// TODO Auto-generated method stub
		throw new RuntimeException("ainda não implementado");
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public Set<Key2<K1, K2>> keySet() {
		throw new RuntimeException("ainda não implementado");
//		return (Set<Key2<K1, K2>>)map.keySet();
	}

	@Override
	public Collection<V> values() {
		return map.values();
	}

	@Override
	public Set<Entry<K1, K2, V>> entrySet() {
		// TODO Auto-generated method stub
		throw new RuntimeException("ainda não implementado");
	}

	@Override
	public Set<K1> keySet1() {
		// TODO Auto-generated method stub
		throw new RuntimeException("ainda não implementado");
	}

	@Override
	public Set<K2> keySet2() {
		// TODO Auto-generated method stub
		throw new RuntimeException("ainda não implementado");
	}

	@Override
	public Set<K2> keySet2(K1 k1) {
		// TODO Auto-generated method stub
		throw new RuntimeException("ainda não implementado");
	}
	
	
}
