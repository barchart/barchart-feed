package com.barchart.feed.series.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.barchart.feed.api.series.DataPoint;
import com.barchart.feed.api.series.DataSeries;
import com.barchart.feed.api.series.network.Analytic;
import com.barchart.feed.api.series.network.NetworkObservable;
import com.barchart.feed.api.series.network.Node;
import com.barchart.feed.api.series.network.Query;

public class NetworkObservableImpl extends NetworkObservable {
	protected Map<String, DataSeries<? extends DataPoint>> availablePublisherMap;
	
	
	protected <E extends DataPoint> NetworkObservableImpl(final BarchartSeriesProvider.SeriesSubscribeFunc onSubscribe, 
    	Map<String, DataSeries<? extends DataPoint>> specifierToSeriesMap) {
	    
	    super(onSubscribe);
	    
	    onSubscribe.setObservable(this);
	    
		this.availablePublisherMap = specifierToSeriesMap;
    }
	
	/**
     * Returns the original {@link Query} used to obtain this {@code NetworkObservable}
     * 
     * @return	the original {@link Query} used to obtain this {@code NetworkObservable}
     */
	@Override
	public Query getQuery() {
		return null;
	}
	
	/**
     * Returns a list of the output analytic specifier (keys) of the main publishers of a given 
     * network or {@link Analytic}
     * @return  list of the output keys
     */
	@Override
	public List<String> getPublisherSpecifiers() {
		return new ArrayList<String>(availablePublisherMap.keySet());
	}

	/**
     * Returns the {@link DataSeries} associated with the specified key.
     * 
     * @param key 	the key mapped to a given DataSeries
     * @return		the DataSeries mapped to the specified key.
     */
	@SuppressWarnings("unchecked")
    @Override
	public <E extends DataPoint> DataSeries<E> getDataSeries(String key) {
		return (DataSeries<E>)availablePublisherMap.get(key);
	}
	
	/**
     * Returns a {@link Map} of {@link DataSeries} specified by the {@link Set} of 
     * unique identifiers ({@link Node} names or specifiers) for those series.
     * @param keys  a set of identifiers which specify which series to return.
     * @return  a map view of the specified {@link DataSeries}.
     */
    @SuppressWarnings("unchecked")
    public <E extends DataPoint> Map<String, DataSeries<E>> getDataSeries(Set<String> keys) {
        Map<String, DataSeries<E>> returnVal = new HashMap<String, DataSeries<E>>();
        for(String key : keys) {
            returnVal.put(key, (DataSeries<E>)availablePublisherMap.get(key));
        }
        return returnVal;
    }

	/**
     * Returns a list of all {@link DataSeries} which are the main publishers 
     * of a given network.
     * 
     * @return	a list of main publisher DataSeries.
     */
	@SuppressWarnings("unchecked")
    @Override
	public <E extends DataPoint> Map<String, DataSeries<E>> getPublisherSeries() {
		Map<String, DataSeries<E>> map = new HashMap<String, DataSeries<E>>();
		for(String key : availablePublisherMap.keySet()) {
			map.put(key, (DataSeries<E>)availablePublisherMap.get(key));
		}
		return map;
	}

}
