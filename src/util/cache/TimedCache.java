package util.cache;

import java.util.Date;
import java.util.concurrent.Callable;

import util.Dates;

public class TimedCache<V> extends Cache<V> {
	
	private long validity;
	
	private Date lastUpdate;

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public TimedCache(long validity, Callable<V> updater) {
		super(updater);
		this.validity = validity;
	}

	public long getValidity() {
		return validity;
	}

	public void setValidity(long validity) {
		this.validity = validity;
	}

	@Override
	public boolean isValid() {
		if (lastUpdate == null)
			return false;
		Date now = new Date();
		Date v = Dates.shift(lastUpdate, validity);
		if (v.after(now))
			return true;
		return false;
	}

	@Override
	public synchronized void refresh() {
		super.refresh();
		lastUpdate = new Date();
	}

	public void update(V value) {
		super.update(value);
		lastUpdate = new Date();
	}

	@Override
	public void invalidate() {
		lastUpdate = null;
	}


}
