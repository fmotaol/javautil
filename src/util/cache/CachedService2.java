package util.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public abstract class CachedService2 {
	
	private Map<Object, Cache<?>> cacheByCall = new HashMap<>();
	
	protected <V> Cache<V> getCache(Object methodRef, Callable<V> call) {
		Cache<V> c = (Cache<V>) cacheByCall.get(methodRef);
		if (c == null) {
			c = newCache(methodRef, call);
			cacheByCall.put(methodRef, c);
		}
		return c;
	}

	protected abstract <V> Cache<V> newCache(Object methodRef, Callable<V> call);

	protected <V> V getInCache(String dataId, Callable<V> call) {
		Cache<V> c = getCache(dataId, call);
		return c.get();
	}

	protected void invalidateCaches(String... dataIds) {
		for (String id : dataIds)
			invalidateCache(id);
	}

	private void invalidateCache(String dataId) {
		Cache<?> c = cacheByCall.get(dataId);
		if (c != null)
			c.invalidate();
	}
	
}
