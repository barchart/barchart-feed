package com.barchart.feed.api.series;

import rx.Observable;

public abstract class TimeSeriesObservable extends Observable<Span> {
    protected TimeSeries<?> series;
    
    protected <E extends TimePoint> TimeSeriesObservable(rx.Observable.OnSubscribeFunc<Span> onSubscribe, TimeSeries<E> series) {
        super(onSubscribe);
        this.series = series;
    }
    
    public abstract <E extends TimePoint> TimeSeries<E> getTimeSeries();

}
