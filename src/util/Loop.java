package util;

import util.logical.Condition;

public class Loop {

	public static void repeat(Runnable r, int times) {
		for (int i = 0; i < times; i++) {
			r.run();
		}
	}

	public static void repeat(Runnable r, Condition until) {
		do {
			r.run();
		} while (!until.test());
	}

}
