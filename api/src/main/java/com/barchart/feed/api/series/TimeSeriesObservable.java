package com.barchart.feed.api.series;

import java.util.Map;

import rx.Observable;

import com.barchart.feed.api.series.service.Query;

public abstract class TimeSeriesObservable extends Observable<Span> {
    protected TimeSeries<? extends TimePoint> series;
    protected Map<String, TimeSeries<? extends TimePoint>> seriesMap;
    
    protected <E extends TimePoint> TimeSeriesObservable(rx.Observable.OnSubscribeFunc<Span> onSubscribe, TimeSeries<E> series) {
        super(onSubscribe);
        this.series = series;
    }
    
    protected <E extends TimePoint> TimeSeriesObservable(rx.Observable.OnSubscribeFunc<Span> onSubscribe, Map<String, TimeSeries<? extends TimePoint>> specifierToSeriesMap) {
    	super(onSubscribe);
    	this.seriesMap = specifierToSeriesMap;
    }
    
    public boolean hasMultipleSeries() {
    	return seriesMap != null;
    }
    
    public abstract Query getQuery();
    
    public abstract <E extends TimePoint> TimeSeries<E> getTimeSeries();
    
    public abstract <E extends TimePoint> Map<String, TimeSeries<? extends TimePoint>> getTimeSeriesMap();

}
