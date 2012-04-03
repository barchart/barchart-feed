package com.ddfplus.feed.common.util.time;

import java.util.concurrent.atomic.AtomicBoolean;

public class StopWatch {

	private AtomicBoolean isRunning = new AtomicBoolean(false);

	private long start;
	private long stop;

	public void clear() {
		final long now = System.nanoTime();
		start = now;
		stop = now;
		isRunning.set(false);
	}

	public boolean isRunning() {
		return isRunning.get();
	}

	public boolean isExceeded(long duration) {
		return System.nanoTime() - start > duration;
	}

	public void start() {
		if (isRunning.compareAndSet(false, true)) {
			start = System.nanoTime(); // start timing
		}
	}

	public void startNow() {
		start = System.nanoTime(); // start timing
	}

	public void stop() {
		if (isRunning.compareAndSet(true, false)) {
			stop = System.nanoTime(); // stop timing
		}
	}

	public void stopNow() {
		stop = System.nanoTime(); // stop timing
	}

	public long nanoTime() {
		return stop - start;
	}

	public long nanoTimeNow() {
		return System.nanoTime() - start;
	}

	public String nanoString() {

		final double SECOND = 1000000000;
		final double MILLIS = 1000000;
		final double MICROS = 1000;

		double nanoDiff = stop - start;

		int second = (int) (nanoDiff / SECOND);

		int millis = (int) ((nanoDiff - second * SECOND) / MILLIS);

		int micros = (int) ((nanoDiff - second * SECOND - millis * MILLIS) / MICROS);
		int nanos = (int) (nanoDiff - second * SECOND - millis * MILLIS - micros
				* MICROS);
		return String.format("time: %d s %d ms %d us %d ns ", second, millis,
				micros, nanos);

	}

	@Override
	public String toString() {
		return "nanoTime: " + Long.toString(nanoTime());
	}

}
