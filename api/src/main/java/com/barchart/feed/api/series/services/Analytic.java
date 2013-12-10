package com.barchart.feed.api.series.services;

import com.barchart.feed.api.series.Span;

public interface Analytic {
	/**
	 * Signals this {@code Analytic} to begin processing the
	 * specified span. Provides the underlying {@link Analytic}
	 * a chance to pre-process the data prior to having {@link Analytic#process(Span)}
	 * called on it.
	 * 
	 * @param span	the span of time in the {@link TimeSeries} to process.
	 * @return the span processed.
	 */
	public Span preProcess(Span span);
	/**
	 * Called immediately following the call to {@link Analytic#preProcess(Span)}
	 * to do the main work of the executing body of code.
	 * 
	 * @param span
	 * @return
	 */
	public Span process(Span span);
}
