package com.barchart.feed.api.series.network;

import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Observable;

import com.barchart.feed.api.series.DataPoint;
import com.barchart.feed.api.series.DataSeries;

public abstract class NetworkObservable extends rx.Observable<NetworkNotification> {

	protected NetworkObservable(final Observable.OnSubscribe<NetworkNotification> onSubscribe) {
        super(onSubscribe);
    }

    /**
     * Returns the original {@link Query} used to obtain this {@code NetworkObservable}
     *
     * @return	the original {@link Query} used to obtain this {@code NetworkObservable}
     */
	public abstract Query getQuery();
    /**
     * Returns a list of the output analytic specifier (keys) of the main publishers of a given
     * network or {@link Analytic}
     * @return	list of the output keys
     */
    public abstract List<String> getPublisherSpecifiers();
    /**
     * Returns the {@link DataSeries} associated with the specified key.
     *
     * @param key 	the key mapped to a given DataSeries
     * @return		the DataSeries mapped to the specified key.
     */
    public abstract <E extends DataPoint> DataSeries<E> getDataSeries(String key);
    /**
     * Returns a {@link Map} of {@link DataSeries} specified by the {@link Set} of
     * unique identifiers ({@link Node} names or specifiers) for those series.
     * @param keys  a set of identifiers which specify which series to return.
     * @return  a map view of the specified {@link DataSeries}.
     */
    public abstract <E extends DataPoint> Map<String, DataSeries<E>> getDataSeries(Set<String> keys);
    /**
     * Returns a list of all {@link DataSeries} which are the main publishers
     * of a given network.
     *
     * @return	a list of main publisher DataSeries.
     */
    public abstract <E extends DataPoint> Map<String, DataSeries<E>> getPublisherSeries();

}
