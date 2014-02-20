package com.barchart.feed.series.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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

public class NetworkObservableImpl extends NetworkObservable {
	protected Map<String, DataSeries<? extends DataPoint>> availablePublisherMap;
	private Map<String, Set<Observer<NetworkNotification>>> observerMap = new ConcurrentHashMap<String, Set<Observer<NetworkNotification>>>();
	private Map<Observer<NetworkNotification>, Set<String>> specifierMap = new ConcurrentHashMap<Observer<NetworkNotification>, Set<String>>();
	
	
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
	
	@Override
    public Set<Observer<NetworkNotification>> getObservers(String specifier) {
        return observerMap.get(specifier);
    }
    
    @Override
    public Set<String> getSubscribedNodeNames(Observer<NetworkNotification> obs) {
        return specifierMap.get(obs);
    }

    /**
	 * Returns a {@link Subscription} and starts the {@link DataSeries} update process.
	 * 
	 * This method overrides the rx.Observable.subscribe(Observer) method to include checks necessary for
	 * a key-based subscription mechanism. This method may be called by clients <em>after</em> 
	 * calling {@link #register(Observer, String)} which sets up the mapping between the observer
	 * and the specific data the client is interested in. The former also allows this observable
	 * to be used in the composite fashion intended by the RxJava library.
	 * 
	 * The preferred method of subscription is to simply call {@link #subscribe(Observer, String)} or
	 * {@link #subscribeAll(Observer)} which registers the observer for notification for a particular
	 * set of data and internally calls {@link #register(Observer, String)} making it unnecessary for
	 * clients to do so.
	 * 
	 * @param  obs     the Observer to be notified of updates.
	 * @throws IllegalStateException if {@link #register(Observer, String)} has not been called prior to this method.
	 */
	@Override
	public Subscription subscribe(Observer<? super NetworkNotification> obs) {
	    if(obs == null) {
	        throw new IllegalArgumentException("Cannot subscribe a null Observer");
	    }
//	    if(!specifierMap.containsKey(obs) || specifierMap.get(obs).isEmpty()) {
//	        throw new IllegalStateException("The registerTo() method must be called prior to subscribe() -OR PREFERRABLY-" +
//	            " the subscribe(Observer, String) / subscribeAll() methods should be called instead.");
//	    }
	    
	    return super.subscribe(obs);
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
	    if(obs == null) {
	        throw new IllegalArgumentException("Cannot subscribe a null observer");
	    }else if(specifier == null) {
	        throw new IllegalArgumentException("Specifier was null.");
	    }
	    
	    register(obs, specifier);
		return subscribe(obs);
	}
	
	/**
     * Subscribes to update notifications for the {@link DataSeries} 
     * specified by the set of specifiers specified. Using this method, a client may choose
     * to subscribe to one or more {@link Node} in a given network that is one
     * of the configured publisher nodes of that network.
     * 
     * @param obs           the NetworkObserver receiving notifications.
     * @param specifiers    the set of unique identifiers for the Nodes whose DataSeries 
     *                      have been updated, causing an event notification.
     * @return      a {@link rx.Subscription} which can be used to unsubscribe
     *              from notifications.
     */
	@Override
    public Subscription subscribe(Observer<NetworkNotification> obs, Set<String> specifiers) {
	    if(obs == null) {
            throw new IllegalArgumentException("Cannot subscribe a null observer");
        }else if(specifiers == null || specifiers.isEmpty()) {
            throw new IllegalArgumentException("Specifier was null or empty.");
        }
	    
	    for(String specifier : specifiers) {
	        register(obs, specifier);
	    }
	    
	    return subscribe(obs);
	}
	
	/**
     * Subscribes to update notifications for all {@link DataSeries} 
     * which are the known outputs ("main publishers") of a given network or analytic.
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
     * network has not be disposed of, it can still be "re-subscribed" to. This can
     * be checked using the {@link #isSubscribableHold}.
     * 
     * @return	
     */
	@Override
	public Subscription subscribeAll(Observer<NetworkNotification> obs) {
	    for(String key : availablePublisherMap.keySet()) {
	        register(obs, key);
	    }
	    return subscribe(obs);
	}

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
	@Override
	public boolean isSubscribableHold() {
		return false;
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
	
	/**
     * This method <em><b>must</b></em> be called prior to calling any method on this observer which would
     * result in a subsequent call to {@link #subscribe(Observer)} being executed either overtly
     * or "under the hood" via the many composition methods available on this {@link Observable}'s parent
     * class. It may be necessary to call this method once for every specifier a particular observer is
     * interested in observing, depending on the methods being called on this observer.
     * <p>
     * This method is an effort to allow this observer to participate in the many compositional 
     * configurations allowed by the RxJava framework. It is necessary because a subscription in this 
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
	@Override
	public void register(Observer<NetworkNotification> obs, String specifier) {
	    Set<String> obsList = null;
	    if((obsList = specifierMap.get(obs)) == null) {
	        specifierMap.put(obs, obsList = new HashSet<String>());
	    }
	    if(!obsList.contains(specifier)) {
	        obsList.add(specifier);
	    }
	    
	    Set<Observer<NetworkNotification>> l = null;
        if((l = observerMap.get(specifier)) == null) {
            observerMap.put(specifier, l = new HashSet<Observer<NetworkNotification>>());
        }
        if(!l.contains(obs)) {
            l.add(obs);
        }
	}
	
	/**
     * Returns a flag indicating whether the {@link Observer} specified has been registered for
     * specific outputs.
     * 
     * @param o        the observer to check
     * @return         true if so, false if not.
     */
	@Override
	public boolean isRegistered(Observer<NetworkNotification> o) {
	    return specifierMap.containsKey(o) && specifierMap.get(o).size() > 0;
	}

}
