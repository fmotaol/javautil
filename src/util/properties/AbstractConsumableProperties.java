package util.properties;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public abstract class AbstractConsumableProperties implements ConsumableProperties {

	private HashSet<String> unconsumedKeys = new HashSet<>();

	protected static String valueAsString(Object value) {
		if (value == null)
			return null;
		return value.toString();
	}

	@Override
	public abstract String getContext();

	public Boolean consumeBooleanProperty(String key, Boolean defaultValue) {
		String sv = consumeProperty(key, valueAsString(defaultValue));
		Boolean v;
		try {
			v = Boolean.parseBoolean(sv);
		} catch (Throwable e) {
			throw new RuntimeException("Propriedade " + key + " requer um valor booleano, mas foi encontrado: " + sv);
		}
		return v;
	}

	public Integer consumeIntegerProperty(String key, Integer defaultValue) {
		String sv = consumeProperty(key, valueAsString(defaultValue));
		Integer v;
		try {
			v = Integer.parseInt(sv);
		} catch (Throwable e) {
			throw new RuntimeException("Propriedade " + key + " requer um valor inteiro, mas foi encontrado: " + sv);
		}
		return v;
	}

	public Double consumeDoubleProperty(String key, Double defaultValue) {
		String sv = consumeProperty(key, valueAsString(defaultValue));
		Double v;
		try {
			v = Double.parseDouble(sv);
		} catch (Throwable e) {
			throw new RuntimeException("Propriedade " + key + " requer um valor numérico, mas foi encontrado: " + sv);
		}
		return v;
	}

	public boolean consumeBooleanProperty(String key) {
		String sv = consumeProperty(key);
		boolean v;
		try {
			v = Boolean.parseBoolean(sv);
		} catch (Throwable e) {
			throw new RuntimeException("Propriedade " + key + " requer um valor booleano, mas foi encontrado: " + sv);
		}
		return v;
	}

	public int consumeIntegerProperty(String key) {
		String sv = consumeProperty(key);
		int v;
		try {
			v = Integer.parseInt(sv);
		} catch (Throwable e) {
			throw new RuntimeException("Propriedade " + key + " requer um valor inteiro, mas foi encontrado: " + sv);
		}
		return v;
	}

	public double consumeDoubleProperty(String key) {
		String sv = consumeProperty(key);
		double v;
		try {
			v = Double.parseDouble(sv);
		} catch (Throwable e) {
			throw new RuntimeException("Propriedade " + key + " requer um valor numérico, mas foi encontrado: " + sv);
		}
		return v;
	}

	public Set<String> getUnconsumedKeys() {
		return unconsumedKeys;
	}

	public Set<String> listUnconsumedKeys(Function<String, Boolean> criteria) {
		HashSet<String> r = new HashSet<String>();
		for (String k : unconsumedKeys) {
			if (criteria.apply(k))
				r.add(k);
		}
		return r;

	}

	public Set<String> listUnconsumedKeysStartingWith(String s) {
		return listUnconsumedKeys((k) -> k.startsWith(s));
	}

	protected void loadUnconsumedKeys(Collection<String> propertyNames) {
		unconsumedKeys.addAll(propertyNames);
	}

	public String consumeProperty(String key) {
		if (!containsKey(key))
			throw new RuntimeException("Propriedade " + key + " requerida");
		String r = readProperty(key);
		unconsumedKeys.remove(key);
		return r;
	}

	protected abstract String readProperty(String key);

	protected abstract String readProperty(String key, String defaultValue);

	protected abstract boolean containsKey(String key);
	
	protected abstract String[] readMultipleKeyProperties(String key);

	public String consumeProperty(String key, String defaultValue) {
		String r = readProperty(key, defaultValue);
		unconsumedKeys.remove(key);
		return r;
	}

	public String[] consumeMultipleKeyProperties(String key) {
		String[] r = readMultipleKeyProperties(key);
		unconsumedKeys.remove(key);
		return r;
	}

}
