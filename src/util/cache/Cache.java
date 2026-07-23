package util.cache;

import java.util.concurrent.Callable;

public abstract class Cache<V> {

	private Callable<V> updater;

	private V value;

	public Cache() {
		super();
	}

	public Cache(Callable<V> updater) {
		super();
		this.updater = updater;
	}

	public synchronized void refresh() {
		if (updater == null)
			throw new RuntimeException("Cannot update because updater is null");

		try {

			value = updater.call();

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void update(V value) {
		this.value = value;
	}

	public abstract boolean isValid();

	public abstract void invalidate();

	public V get() {
		if (!isValid()) {
			switch (expirationPolicy) {
			case REFRESH_VALUE:
				refresh();
			case OLD_VALUE:
				return value;
			case NULL:
				return null;
			default:
				throw new RuntimeException("Opção inválida: " + expirationPolicy);
			}
		}

		return value;
	}

	public enum ExpirationPolicy {
		OLD_VALUE, REFRESH_VALUE, NULL
	}

	private ExpirationPolicy expirationPolicy = ExpirationPolicy.REFRESH_VALUE;

	public ExpirationPolicy getExpirationPolicy() {
		return expirationPolicy;
	}

	public void setExpirationPolicy(ExpirationPolicy onExpiredPolicy) {
		this.expirationPolicy = onExpiredPolicy;
	}

}
