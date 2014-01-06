package com.barchart.feed.api.series.service;

import rx.Observable;

/**
 * Observable which handles historical querys. For now this and its known 
 * implementor are very rudimentary.
 * 
 * @author David Ray
 *
 * @param <T>
 */
public abstract class HistoricalService<T extends HistoricalResult> extends Observable<T> {
	/** The root url used to contact the historical server */
	protected static final StringBuilder URL_PREFIX = new StringBuilder("http://ds01.ddfplus.com/historical/");
	
	/** The root url used to contact the historical handler for TICK data */
	protected static final String TICK_URL_SUFFIX = 
		"queryticks.ashx?username=${UNAME}&password=${PWORD}&symbol=${SYMB}&";
	
	/** The root url used to contact the historical handler for MINUTE data */
	protected static final String MINUTE_URL_SUFFIX = 
		"queryminutes.ashx?username=${UNAME}&password=${PWORD}&symbol=${SYMB}&";
	
	
	
	/**
	 * Constructs a new HistoricalService
	 * @param func
	 */
	public HistoricalService(Observable.OnSubscribeFunc<T> func) {
		super(func);
	}
	
	/**
	 * Starts a task after a short delay to produce query results which will be 
	 * returned to the specified observer's {@link HistoricalObserver#onNext(HistoricalResult)}
	 * method.
	 * 
	 * @param observer
	 * @param subscription
	 */
	public  abstract <S extends Subscription> void subscribe(HistoricalObserver<T> observer, S subscription);
	
	/**
     * Starts a task after a short delay to produce query results which will be 
     * returned to the specified observer's {@link HistoricalObserver#onNext(HistoricalResult)}
     * method. 
     * 
     * @param observer
     * @param subscription
     * @param customQuery
     */
    public abstract <S extends Subscription> void subscribe(HistoricalObserver<T> observer, S subscription, Query customQuery);
	
}
