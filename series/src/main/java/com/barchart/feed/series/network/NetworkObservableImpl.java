package com.barchart.feed.series.network;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import com.barchart.feed.api.series.DataPoint;
import com.barchart.feed.api.series.DataSeries;
import com.barchart.feed.api.series.network.Analytic;
import com.barchart.feed.api.series.network.NetworkNotification;
import com.barchart.feed.api.series.network.NetworkObservable;
import com.barchart.feed.api.series.network.Node;
import com.barchart.feed.api.series.network.Query;

public class NetworkObservableImpl implements NetworkObservable {
	protected Map<String, DataSeries<? extends DataPoint>> seriesMap;
	private Observable<NetworkNotification> delegate;
	
	protected <E extends DataPoint> NetworkObservableImpl(final BarchartSeriesProvider.SeriesSubscriber onSubscribe, 
    	Map<String, DataSeries<? extends DataPoint>> specifierToSeriesMap) {
		delegate = new rx.Observable<NetworkNotification>(onSubscribe) {
			public rx.Subscription subscribe(Observer<? super NetworkNotification> o) {
				return super.subscribe(o);
			}
		};
    	this.seriesMap = specifierToSeriesMap;
    }
	
	private void loadSeriesMap(List<Node<SeriesSubscription>> l) {
		if(seriesMap == null) {
			seriesMap = new HashMap<String, DataSeries<? extends DataPoint>>();
		}
		for(Node<SeriesSubscription> n : l) {
			
		}
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
     * Subscribes to update notifications for the {@link DataSeries} 
     * specified by specifier. Using this method, a client may choose
     * to subscribe to any {@link Node} in a given network regardless
     * of whether that particular Node is one of the main outputs.
     * 
     * @param obs			the Observer receiving notifications.
     * @param specifier		the identifier for the Node whose DataSeries 
	 *						has been updated, causing an event notification.
     * @return		a {@link rx.Subscription} which can be used to unsubscribe
     * 				from notifications.
     */
	@Override
	public Subscription subscribe(Observer<NetworkNotification> obs, String specifier) {
		return delegate.subscribe(obs);
	}

	/**
     * Subscribes to update notifications for all {@link DataSeries} 
     * which are the known outputs of a given network or analytic.
     * 
     * Interim notifications (notifications from outputs which participate 
     * in forming the end result but are not considered to be "end result" data),
     * may be subscribed to using the {@link #subscribe(String)}
     * method, and specifying the key for the {@link DataSeries} required.
     * 
     * This method returns a {@link CompositeSubscription} object which may be used to
     * unsubscribe or "turn off" notifications. When a network no-longer has 
     * subscribed clients, the entire network may be expelled meaning any attempts
     * to "re-subscribe" to this {@link Observable} will fail. As long as a given
     * network has not be dismantled, it can still be "re-subscribed" to. This can
     * be checked using the {@link #isSubscribableHold}.
     * 
     * @return	
     */
	@Override
	public CompositeSubscription subscribeAll(Observer<NetworkNotification> obs) {
		return null;
	}

	/**
     * Returns a boolean indicating whether this {@link Observable} is in a state
     * where it can allow "re-subscribing" or not. If this method returns true, 
     * it is gauranteed subscribable for long enough to make a follow up call to
     * {@link #subscribe(String)} or {@link #subscribeAll()} (5 seconds).
     * 
     * Gauranteed subscribable in this context is determined in "computer" time 
     * where 5 seconds is plenty of time to make a call to subscribe. 
     * 
     * If this method returns false, it means that the resources necessary to output
     * the data specified by the query this observable was returned in response to,
     * have been garbage collected or otherwise dismantled.
     * 
     * @return true if this Observable is "subscribable" or false if not.
     */
	@Override
	public boolean isSubscribableHold() {
		return false;
	}

	/**
     * Returns a list of the output keys of the main publishers of a given 
     * network or {@link Analytic}
     * @return	list of the output keys
     */
	@Override
	public List<String> getAllOutputKeys() {
		return null;
	}

	/**
     * Returns the {@link DataSeries} associated with the specified key.
     * 
     * @param key 	the key mapped to a given DataSeries
     * @return		the DataSeries mapped to the specified key.
     */
	@Override
	public <E extends DataPoint> DataSeries<E> getDataSeries(String key) {
		return null;
	}

	/**
     * Returns a list of all {@link DataSeries} which are the main publishers 
     * of a given network.
     * 
     * @return	a list of main publisher DataSeries.
     */
	@Override
	public <E extends DataPoint> List<DataSeries<E>> getAllDataSeries() {
		return null;
	}
}
