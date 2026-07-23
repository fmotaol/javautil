package util.console;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.Dates;
import util.Strings;
import util.logical.Assert;
import util.threads.ThreadUtil;

public class Log {

	public static void log() {
		log("");
	}

	public static void log(String msg, Object... params) {
		log(true, msg, params);
	}

	public static void log(Object... params) {
		String msg = Strings.repeat("%", params.length, ", ");
		log(true, msg, params);
	}

//	Permite o agrupamento de v�rias mensagens para serem exibidas de uma �nica vez. Para tanto,
//	basta usar um mesmo objeto como agrupador entre elas, que enquanto o m�todo log estiver sendo 
//	chamado com o mesmo agrupador, elas ser�o armazenadas para posterior exibi��o.
//	A exibi��o acontecer� com a chamada de dischargeBatch(). A exibi��o tamb�m acontecer� se for invocado 
//	logBatch() com um agrupador diferente.

	public static void logBatch(Object grouper, String msg, Object... params) {
		if (grouper == null) {
			log(msg, params);
			return;
		}

		if ((grouper == previousBatchGrouper) || (previousBatchGrouper == null)) {
			String s = Strings.formatText(msg, params);
			msgsBatch.add(s);
			previousBatchGrouper = grouper;
			return;
		}

		dischargeBatch();
		logBatch(grouper, msg, params);
	}

	public static void dischargeBatch() {
		for (String msg : msgsBatch) {
			sysOut(true, msg);
		}
		msgsBatch.clear();
		previousBatchGrouper = null;
	}

	private static String lastMsg;

	public static String lastMsg() {
		return lastMsg;
	}

	private static void sysOut(boolean newLine, String msg) {
		lastMsg = msg;

		if (newLine)
			msg += "\n";
		sysOut(msg);
	}

	private static ArrayList<String> msgsBatch = new ArrayList<>();

	private static Object previousBatchGrouper = null;

	private static void log(boolean newLine, String msg, Object... params) {
		msg = Strings.formatText(msg, params);
		sysOut(newLine, msg);
	}

	private static ArrayList<Thread> lockQueue = new ArrayList<>();

	private static Map<Thread, List<String>> msgsQueue = new HashMap<>();

	public static void lock() {
		Thread t = Thread.currentThread();
		if (lockQueue.contains(t))
			return;
		lockQueue.add(t);
	}

	public static void unlock() {
		Thread ct = Thread.currentThread();
		if (!lockQueue.contains(ct))
			throw new RuntimeException("N�o existe bloqueio de log para esta thread");

		ThreadUtil.waitFor(() -> lockQueue.get(0) == ct);

		lockQueue.remove(0);

		dischargeNextMsgsQueue();
	}

	private static void dischargeNextMsgsQueue() {
		if (lockQueue.isEmpty())
			return;
		Thread t = lockQueue.get(0);
		dischargeMsgsQueue(t);
	}

	private static void sysOut(String msg) {

		Thread t = Thread.currentThread();
		if (!lockQueue.isEmpty()) {
			if (lockQueue.get(0) != t) {
				List<String> q = msgsQueue(t);
				q.add(msg);
				return;
			}
		}

		dischargeMsgsQueue(t);

		sysOut0(msg);
	}

	private synchronized static void dischargeMsgsQueue(Thread t) {
		List<String> q = msgsQueue(t);
		while (!q.isEmpty()) {
			String msg = q.get(0);
			sysOut0(msg);
			q.remove(0);
		}
	}

	private static List<String> msgsQueue(Thread t) {
		List<String> l = msgsQueue.get(t);
		if (l == null) {
			l = new ArrayList<>();
			msgsQueue.put(t, l);
		}

		return l;
	}

	private static void sysOut0(String msg) {
		msg = internalFormat(msg);
		System.out.print(msg);
	}

	public static int DEFAULT_TAB_COLUMN_WIDTH = 20;

	public static int[] DEFAULT_TAB_COLUMN_WIDTHS = new int[] { DEFAULT_TAB_COLUMN_WIDTH };

	public static boolean CONVERT_TABS = false;

	private static String internalFormat(String msg) {
		if ((CONVERT_TABS) && (msg.contains("\t"))) {
			return formatTabs(msg);
		}

		return msg;
	}

	public static String formatTabs(String msg) {
		return formatTabs(msg, new int[] { DEFAULT_TAB_COLUMN_WIDTH });
	}

	public static String formatTabs(String msg, int[] columnWidths) {
		final String tab = "\t";
		int i = 0;
		int mark = 0;
		String cmsg = ANSI.removeAnsiChars(msg);
		while (msg.contains(tab)) {

			int pos = cmsg.indexOf(tab);
			mark += columnWidths[i];
			int diff = mark - pos;

			String branch = "";
			if (diff > 0)
				branch = Strings.repeat(" ", diff - 1);
			msg = msg.replaceFirst("\\" + tab, branch);
			cmsg = cmsg.replaceFirst("\\" + tab, branch);

			i++;
			if (i >= columnWidths.length)
				i = 0;
		}
		return msg;
	}

	public static void inline(String msg, Object... params) {
		log(false, msg, params);
	}

	public static void error(String msg, Object... params) {
		msg = Strings.formatText(msg, params);
		throw new RuntimeException(msg);
	}

	private static class TimeMonitor {

		Object actionId;

		int times = 0;

		Date firstStart;

		Date start;

		Date end;

		Long totalDuration = 0L;

		public TimeMonitor(Object actionId) {
			this.actionId = actionId;
		}

		void openAction(Object actionId, Date start) {
			this.actionId = actionId;
			Assert.that(this.start == null, "N�o foi encerrada a �ltima a��o " + actionId);
			this.start = start;
			if (firstStart == null)
				firstStart = start;
			this.end = null;
			times++;
		}

		void closeAction(Object actionId, Date end) {
			Assert.that(this.actionId.equals(actionId), "Erro interno");
			Assert.that(this.end == null, "N�o foi iniciada a a��o " + actionId);
			this.end = end;
			long elapsed = (int) (end.getTime() - start.getTime());
			this.start = null;
			totalDuration += elapsed;
		}

		float avgDuration() {
			return (float) totalDuration / (float) times;
		}

		void showAll() {
			log("Action %: %ms, % times, % ms/time", actionId, totalDuration, times, avgDuration());
		}

	}

	private static HashMap<Object, TimeMonitor> monitorByAction = new HashMap<>();

//	private static long ACTION_DURATION_THRESHOLD = 0;

	public static void startMonitoringTime(Object actionId) {
		Date now = new Date();
		TimeMonitor tm = monitorByAction.get(actionId);
		if (tm == null) {
			tm = new TimeMonitor(actionId);
			monitorByAction.put(actionId, tm);
		}
		tm.openAction(actionId, now);
	}

	public static boolean DEFAULT_END_SHOWS_TIME_MONITOR = true;

	public static void endMonitoringTime(Object action) {
		endMonitoringTime(action, DEFAULT_END_SHOWS_TIME_MONITOR);
	}

	public static void endMonitoringTime(Object actionId, boolean showDuration) {
		Date now = new Date();
		TimeMonitor tm = monitorByAction.get(actionId);
		Assert.that(tm != null, "N�o foi iniciada a a��o " + actionId);
		tm.closeAction(actionId, now);
		if (showDuration)
			tm.showAll();
	}

	public static void clearMonitoringTime(Object actionId) {
		monitorByAction.remove(actionId);
	}

	public enum ActionSort {
		TIMES, START, DURATION, AVG_DURATION
	};

	public static void reportMonitoringTimes(ActionSort sort) {
		reportMonitoringTimes(sort, false);
	}

	public static void reportMonitoringTimes(ActionSort sort, boolean desc) {
		ArrayList<TimeMonitor> r = new ArrayList<>();
		r.addAll(monitorByAction.values());

		if (sort != null) {
			Comparator<TimeMonitor> c = new Comparator<TimeMonitor>() {

				@Override
				public int compare(TimeMonitor o1, TimeMonitor o2) {
					int r = compare(sort, o1, o2);
					if (desc)
						r = -r;
					return r;
				}

				private int compare(ActionSort sort, TimeMonitor o1, TimeMonitor o2) {
					if (sort == ActionSort.TIMES)
						return Integer.compare(o1.times, o2.times);
					if (sort == ActionSort.START)
						return Dates.compare(o1.firstStart, o2.firstStart);
					if (sort == ActionSort.DURATION)
						return Long.compare(o1.totalDuration, o2.totalDuration);
					if (sort == ActionSort.AVG_DURATION)
						return Float.compare(o1.avgDuration(), o2.avgDuration());
					throw new RuntimeException("Op��o inv�lida: " + sort);
				}
			};
			r.sort(c);
		}

		for (TimeMonitor m : r) {
			m.showAll();
		}
	}

	public static void error(Throwable e) {
		throw new RuntimeException(e);
	}

}
