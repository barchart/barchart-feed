package com.barchart.feed.api.series.network;

import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import com.barchart.feed.api.series.DataPoint;
import com.barchart.feed.api.series.DataSeries;

public abstract class NetworkObservable extends rx.Observable<NetworkNotification> {
    
    protected NetworkObservable(
        rx.Observable.OnSubscribeFunc<NetworkNotification> onSubscribe) {
        super(onSubscribe);
    }
    
    public abstract Set<Observer<NetworkNotification>> getObservers(String specifier);
    
    public abstract Set<String> getSubscribedNodeNames(Observer<NetworkNotification> obs);
    
    /**
     * Returns the original {@link Query} used to obtain this {@code NetworkObservable}
     * 
     * @return	the original {@link Query} used to obtain this {@code NetworkObservable}
     */
	public abstract Query getQuery();
	/**
	 * This method <em><b>must</b></em> be called prior to calling any method on this observer which would
	 * result in a subsequent call to {@link #subscribe(Observer)} being executed either overtly
	 * or "under the hood" via the many composition methods available on this {@link Observable}'s parent
	 * class. It may be necessary to call this method once for every specifier a particular observer is
	 * interested in observing, depending on the methods being called on this observer.
	 * <p>
	 * This method is an effort to allow this observer to participate in the many compositional 
	 * configurations allowed by the rx framework. It is necessary because a subscription in this 
	 * context involves both an {@link Observer} <em>AND</em> a subscribed-to target (indicated by 
	 * the specifier node-name key). An attempt to subscribe without indicating a node (hence, desired
	 * output) has no meaning in this context, therefore prior mapping of the observer to the output
	 * desired is necessary. 
	 * <p>
	 * NOTE: It is <em><b>NOT</b></em> necessary to call this method if {@link #subscribe(Observer, String)} or
	 * {@link #subscribeAll(Observer)} are called - because those methods make a call to this method 
	 * automatically.
	 * 
	 * @param o           the {@link Observer} being registered  
	 * @param specifier   one of the keys/outputs the specified observer is interested in observing.
	 */
	public abstract void registerTo(Observer<NetworkNotification> o, String specifier);
	/**
	 * Returns a flag indicating whether the {@link Observer} specified has been registered for
	 * specific outputs.
	 * 
	 * @param o        the observer to check
	 * @return         true if so, false if not.
	 */
	public abstract boolean isRegistered(Observer<NetworkNotification> o);
    /**
     * Subscribes to update notifications for the {@link DataSeries} 
     * specified by specifier. Using this method, a client may choose
     * to subscribe to any {@link Node} in a given network regardless
     * of whether that particular Node is one of the main outputs.
     * 
     * @param obs			the NetworkObserver receiving notifications.
     * @param specifier		the identifier for the Node whose DataSeries 
	 *						has been updated, causing an event notification.
     * @return		a {@link rx.Subscription} which can be used to unsubscribe
     * 				from notifications.
     */
    public abstract Subscription subscribe(Observer<NetworkNotification> obs, String specifier);
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
    public abstract CompositeSubscription subscribeAll(Observer<NetworkNotification> obs);
    /**
     * Returns a boolean indicating whether this {@link Observable} is in a state
     * where it can allow "re-subscribing" or not. If this method returns true, 
     * it is guaranteed subscribable for long enough to make a follow up call to
     * {@link #subscribe(String)} or {@link #subscribeAll()} (5 seconds).
     * 
     * Guaranteed subscribable in this context is determined in "computer" time 
     * where 5 seconds is plenty of time to make a call to subscribe. 
     * 
     * If this method returns false, it means that the resources necessary to output
     * the data specified by the query this observable was returned in response to,
     * have been garbage collected or otherwise dismantled.
     * 
     * @return true if this Observable is "subscribable" or false if not.
     */
    public abstract boolean isSubscribableHold();
    /**
     * Returns a list of the output analytic specifier (keys) of the main publishers of a given 
     * network or {@link Analytic}
     * @return	list of the output keys
     */
    public abstract List<String> getMainPublisherSpecifiers();
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
    public abstract <E extends DataPoint> Map<String, DataSeries<E>> getMainPublisherSeries();

}
