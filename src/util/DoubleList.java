package util;

import java.util.ArrayList;

import util.logical.Assert;

public class DoubleList<K, V> {

	public ArrayList<K> keys = new ArrayList<>();

	public ArrayList<V> values = new ArrayList<>();

	public void add(K key, V value) {
		keys.add(key);
		values.add(value);
	}

	public void remove(int index) {
		keys.remove(index);
		values.remove(index);
	}

	public boolean isEmpty() {
		return keys.isEmpty();
	}
	
	public int size() {
		return Assert.same(keys.size(), values.size());
	}

	public K key(int index) {
		return keys.get(index);
	}

	public V value(int index) {
		return values.get(index);
	}

	public V get(K key) {
		int i = singleIndexOf(key);
		V r = values.get(i);
		return r;
	}

	public V consume(K key) {
		int i = singleIndexOf(key);
		V r = values.get(i);
		keys.remove(i);
		values.remove(i);
		return r;
	}

	private int singleIndexOf(K key) {
		int i = keys.indexOf(key);
		Assert.that(i >= 0, "Chave não localizada: " + key);
		Assert.that(i == keys.lastIndexOf(key), "Duplicidade de chave");
		return i;
	}

}
