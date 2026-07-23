package util.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import util.console.Log;

public abstract class CachedService {

	private Map<String, Cache<?>> cacheByCall = new HashMap<>();

	public static boolean logRequests = false;

	protected <V> Cache<V> getCache(String dataId, Callable<V> call) {
		Cache<V> c = (Cache<V>) cacheByCall.get(dataId);
		if (c == null) {
			c = newCache(dataId, call);
			cacheByCall.put(dataId, c);
		}
		return c;
	}

	protected abstract <V> Cache<V> newCache(String dataId, Callable<V> call);

	protected <V> V getInCache(String dataId, Callable<V> call) {
		synchronized (dataId) {
			if (logRequests)
				Log.log("Buscando em cache: " + dataId);

			Cache<V> c = getCache(dataId, call);
			return c.get();
		}
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
