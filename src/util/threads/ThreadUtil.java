package util.threads;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import util.Dates;

public class ThreadUtil {

	public static void waitUntil(Date time, long plusMillis) {
		Date d = Dates.shift(time, plusMillis);
		waitUntil(d);
	}

	public static void waitUntil(Date time) {
		Date now = new Date();

		long ms = Dates.spentTime(now, time);
		if (ms <= 0)
			return;

		sleep(ms);
	}

	public static void sleep(long milliseconds) {
		if (milliseconds <= 0)
			return;

		try {

			Thread.sleep(milliseconds);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static long elapsedTime(Date since) {
		return elapsedTime((new Date()), since);
	}

	public static long elapsedTime(Date until, Date since) {
		long r = until.getTime() - since.getTime();
		return r;
	}

	private static final int WAITFOR_SLEEP_CYCLE = 200;

	public static void waitFor(Callable<Boolean> condition) {
		waitFor(condition, null);
	}

	public static void waitFor(Callable<Boolean> condition, Long timeOut) {
		Date start = new Date();
		try {
			while (!condition.call()) {
				Thread.sleep(WAITFOR_SLEEP_CYCLE);
				
				if (timeOut != null)
					if (elapsedTime(start) > timeOut)
						throw new TimeoutException("Timeout atingindo");
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static <T, U> Callable<T> call(Consumer<U> consumer) {
		return new Callable<T>() {

			@Override
			public T call() throws Exception {
				consumer.accept(null);
				return null;
			}

		};
	}

}
