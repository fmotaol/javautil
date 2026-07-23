package util.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AsyncTimedCache<V> extends TimedCache<V> {

	private static final long DEFAULT_REQUEST_ADVANCE = 1500;

	private ScheduledExecutorService executor;

	private long requestAdvance = DEFAULT_REQUEST_ADVANCE;

	private FutureTask<V> refreshment;

	public AsyncTimedCache(ScheduledExecutorService executor, long validity, Callable<V> updater) {
		super(validity, updater);
		this.executor = executor;
	}

	public long getRequestAdvance() {
		return requestAdvance;
	}

	public void setRequestAdvance(long requestAdvance) {
		this.requestAdvance = requestAdvance;
	}

	protected void scheduleRefresh() {

//		Log.log("Scheduling %...", this);

		refreshment = new FutureTask<>(() -> {
			refresh();
			return get();
		});

		long time;
		if (getLastUpdate() != null) {
			time = getValidity() - requestAdvance;
			if (time < 0)
				time = 0;
		} else
			time = 0;

		executor.schedule(refreshment, time, TimeUnit.MILLISECONDS);

//		Log.log("Scheduled.");
	}

	public void start() {
		scheduleRefresh();
	}

	@Override
	public synchronized void refresh() {
		super.refresh();
		refreshment = null;
		scheduleRefresh();
	}

}
