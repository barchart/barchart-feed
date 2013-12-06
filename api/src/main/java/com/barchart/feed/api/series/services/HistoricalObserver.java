package com.barchart.feed.api.series.services;

import rx.Observer;

/**
 * Implemented by the {@link BarchartSeriesProvider} specifically or any other
 * object interested in being provided with historical query data using the 
 * idioms of reactive programming.
 * 
 * @author David Ray
 *
 * @param <T>	Implementation of {@link HistoricalResult}
 */
public interface HistoricalObserver<T extends HistoricalResult> extends Observer<T> {
	/**
	 * Notifies the Observer that the Observable has finished sending push-based notifications.
	 * Called upon completion of the historical query.
	 */
	@Override
	public void onCompleted();
	/**
	 * Notifies the Observer that the Observable has experienced an error condition.
	 * Called if an error occurs during query or result formatting
	 * @param	e	{@link Throwable}
	 */
	@Override
	public void onError(Throwable e);
	/**
	 * Provides the Observer with new data.
	 * The Observable calls this closure 1 or more times, unless it calls onError in which case this closure may never be called.
	 * The Observable will not call this closure again after it calls either onCompleted or onError.
	 * <p>
	 * {@link HistoricalResult}s can result in more than 1 call to this method.
	 */
	@Override
	public void onNext(T historicalResult);
}
