package com.barchart.feed.api.series;

import java.util.Map;

import rx.Observable;

import com.barchart.feed.api.series.analytics.Query;

public abstract class TimeSeriesObservable extends Observable<Span> {
    protected DataSeries<? extends DataPoint> series;
    protected Map<String, DataSeries<? extends DataPoint>> seriesMap;
    
    protected <E extends DataPoint> TimeSeriesObservable(rx.Observable.OnSubscribeFunc<Span> onSubscribe, DataSeries<E> series) {
        super(onSubscribe);
        this.series = series;
    }
    
    protected <E extends DataPoint> TimeSeriesObservable(rx.Observable.OnSubscribeFunc<Span> onSubscribe, Map<String, DataSeries<? extends DataPoint>> specifierToSeriesMap) {
    	super(onSubscribe);
    	this.seriesMap = specifierToSeriesMap;
    }
    
    public boolean hasMultipleSeries() {
    	return seriesMap != null;
    }
    
    public abstract Query getQuery();
    
    public abstract <E extends DataPoint> DataSeries<E> getTimeSeries();
    
    public abstract <E extends DataPoint> Map<String, DataSeries<? extends DataPoint>> getTimeSeriesMap();

}
