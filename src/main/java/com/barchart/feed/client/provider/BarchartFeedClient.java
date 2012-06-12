/**
 * 
 */
package com.barchart.feed.client.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.base.instrument.enums.InstrumentField;
import com.barchart.feed.base.instrument.values.MarketInstrument;
import com.barchart.feed.base.market.api.MarketRegListener;
import com.barchart.feed.base.market.api.MarketTaker;
import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.feed.ddf.datalink.api.DDF_FeedClient;
import com.barchart.feed.ddf.datalink.api.DDF_FeedStateListener;
import com.barchart.feed.ddf.datalink.api.DDF_MessageListener;
import com.barchart.feed.ddf.datalink.api.DDF_TimestampListener;
import com.barchart.feed.ddf.datalink.api.EventPolicy;
import com.barchart.feed.ddf.datalink.api.Subscription;
import com.barchart.feed.ddf.datalink.enums.DDF_FeedEvent;
import com.barchart.feed.ddf.datalink.enums.TP;
import com.barchart.feed.ddf.datalink.provider.DDF_FeedClientFactory;
import com.barchart.feed.ddf.instrument.api.DDF_Instrument;
import com.barchart.feed.ddf.instrument.provider.DDF_InstrumentProvider;
import com.barchart.feed.ddf.market.api.DDF_MarketProvider;
import com.barchart.feed.ddf.market.provider.DDF_MarketService;
import com.barchart.feed.ddf.message.api.DDF_BaseMessage;
import com.barchart.feed.ddf.message.api.DDF_ControlTimestamp;
import com.barchart.feed.ddf.message.api.DDF_MarketBase;
import com.barchart.util.values.api.Value;

/**
 * Single access point for Barchart feed services.
 * 
 */
public class BarchartFeedClient {

	private static final Logger log = LoggerFactory
			.getLogger(BarchartFeedClient.class);

	private final DDF_FeedClient feed;

	private final DDF_MarketProvider maker = DDF_MarketService.newInstance();

	private final CopyOnWriteArrayList<DDF_TimestampListener> timeStampListeners =
			new CopyOnWriteArrayList<DDF_TimestampListener>();

	private final Executor defaultExecutor = new Executor() {

		private final AtomicLong counter = new AtomicLong(0);

		final String name = "# DDF Client - " + counter.getAndIncrement();

		@Override
		public void execute(final Runnable task) {
			new Thread(task, name).start();
		}

	};

	/**
	 * Constructs a new feed client with the user's user name and password. The
	 * transport protocol defaults to TCP and a default executor are used.
	 * 
	 * @param username
	 * @param password
	 */
	public BarchartFeedClient(final String username, final String password) {

		feed =
				DDF_FeedClientFactory.newConnectionClient(TP.TCP, username,
						password, defaultExecutor);

		feed.bindMessageListener(msgListener);

		maker.add(instrumentSubscriptionListener);

	}

	/**
	 * Constructs a new feed client with the user's user name, password, and
	 * desired transport protocol. A default executor is used.
	 * 
	 * @param username
	 * @param password
	 * @param tp
	 */
	public BarchartFeedClient(final String username, final String password,
			final TP tp) {

		feed =
				DDF_FeedClientFactory.newConnectionClient(tp, username,
						password, defaultExecutor);

		feed.bindMessageListener(msgListener);

		maker.add(instrumentSubscriptionListener);

	}

	/**
	 * Constructs a new feed client with the user's user name, password, desired
	 * transport protocol, and framework executor.
	 * 
	 * @param username
	 * @param password
	 * @param tp
	 * @param executor
	 */
	public BarchartFeedClient(final String username, final String password,
			final TP tp, final Executor executor) {

		feed =
				DDF_FeedClientFactory.newConnectionClient(tp, username,
						password, executor);

		feed.bindMessageListener(msgListener);

		maker.add(instrumentSubscriptionListener);

	}

	/**
	 * Starts the data feed asynchronously. Notification of login success is
	 * reported by FeedStateListeners which are bound to this object.
	 */
	public void startup() {
		feed.startup();
	}

	/**
	 * Shuts down the data feed and clears all registered market takers.
	 */
	public void shutdown() {
		maker.clearAll();
		feed.shutdown();
	}

	/*
	 * This is where the instruments are registered and unregistered as needed
	 * by the market maker. Subscribe events are sent when the instrument has
	 * not been bound by a previously registered market taker. Unsubscribe
	 * events are sent only when the instrument is not needed by any previously
	 * registered market takers.
	 */
	private final MarketRegListener instrumentSubscriptionListener =
			new MarketRegListener() {

				@Override
				public void onRegistrationChange(
						final MarketInstrument instrument,
						final Set<MarketEvent> events) {

					/*
					 * The market maker denotes 'unsubscribe' with an empty
					 * event set
					 */
					if (events.isEmpty()) {
						log.debug("Unsubsctibing to "
								+ instrument.get(InstrumentField.ID));
						feed.unsubscribe(new Subscription(instrument, events));
					} else {
						log.debug("Subsctibing to "
								+ instrument.get(InstrumentField.ID));
						feed.subscribe(new Subscription(instrument, events));
					}

				}

			};

	/*
	 * This is the default message listener. Users wishing to handle raw
	 * messages will need to implement their own feed client.
	 */
	private final DDF_MessageListener msgListener = new DDF_MessageListener() {

		@Override
		public void handleMessage(final DDF_BaseMessage message) {

			if (message instanceof DDF_ControlTimestamp) {
				for (final DDF_TimestampListener listener : timeStampListeners) {
					listener.handleTimestamp(((DDF_ControlTimestamp) message)
							.getStampUTC());
				}
			}

			if (message instanceof DDF_MarketBase) {
				final DDF_MarketBase marketMessage = (DDF_MarketBase) message;
				maker.make(marketMessage);
			}

		}

	};

	/**
	 * Applications which need to react to the connectivity state of the feed
	 * instantiate a DDF_FeedStateListener and bind it to the client.
	 * 
	 * @param listener
	 *            The listener to be bound.
	 */
	public void bindFeedStateListener(final DDF_FeedStateListener listener) {
		feed.bindStateListener(listener);
	}

	/**
	 * Applications which require timestamp or hearbeat messages from the data
	 * server instantiate a DDF_TimestampListener and bind it to the client.
	 * 
	 * @param listener
	 */
	public void bindTimestampListener(final DDF_TimestampListener listener) {
		timeStampListeners.add(listener);
	}

	/**
	 * Users wishing to modify the feed client's response to feed connectivity
	 * or other events can bind an instance of EventPolicy to a specific
	 * DDF_FeedEvent.
	 * <p>
	 * Note: There are two default behaviors set initially for convenience. On
	 * any disconnect or failed login, the feed client will wait two seconds and
	 * then attempt to reconnect. On successful login, all instruments which had
	 * been subscribed to by registered market takers will be resubscribed.
	 * 
	 * @param event
	 *            The event to specify a policy for.
	 * @param policy
	 *            A user defined action to be performed on a specific feed
	 *            event.
	 */
	public void setFeedEventPolicy(final DDF_FeedEvent event,
			final EventPolicy policy) {
		feed.setPolicy(event, policy);
	}

	//

	/**
	 * Adds a market taker to the client. This performs instrument registration
	 * with the market maker as well as subscribing to the required data from
	 * the feed.
	 * 
	 * @param taker
	 *            The market taker to be added.
	 * @return True if the taker was successfully added.
	 */
	public boolean isTakerRegistered(final MarketTaker<?> taker) {
		return maker.isRegistered(taker);
	}

	/**
	 * Adds a market taker to the client. This performs instrument registration
	 * with the market maker as well as subscribing to the required data from
	 * the feed.
	 * 
	 * @param taker
	 *            The market taker to be added.
	 * @return True if the taker was successfully added.
	 */
	public <V extends Value<V>> boolean addTaker(final MarketTaker<V> taker) {
		return maker.register(taker);
	}

	/**
	 * Removes a market taker from the client. If no other takers require its
	 * instruments, they are unsubscribed from the feed.
	 * 
	 * @param taker
	 *            THe market taker to be removed.
	 * @return True if the taker was successfully removed.
	 */
	public <V extends Value<V>> boolean removeTaker(final MarketTaker<V> taker) {
		return maker.unregister(taker);
	}

	/**
	 * Retrieves the instrument object denoted by symbol. The local instrument
	 * cache will be checked first. If the instrument is not stored locally, a
	 * remote call to the instrument service is made.
	 * 
	 * @return NULL_INSTRUMENT if the symbol is not resolved.
	 */
	public MarketInstrument lookup(final String symbol) {
		return DDF_InstrumentProvider.find(symbol);
	}

	/**
	 * Retrieves a list of instrument objects denoted by symbols provided. The
	 * local instrument cache will be checked first. If any instruments are not
	 * stored locally, a remote call to the instrument service is made.
	 * 
	 * @return An empty list if no symbols can be resolved.
	 */
	public List<MarketInstrument> lookup(final List<String> symbolList) {
		final List<DDF_Instrument> list =
				DDF_InstrumentProvider.find(symbolList);

		return new ArrayList<MarketInstrument>(list);
	}

	/**
	 * Makes a query to the market maker for a snapshot of a market field for a
	 * specific instrument. The returned values are frozen and disconnected from
	 * live market.
	 * 
	 * @return NULL_VALUE for all fields if market is not present.
	 */
	public <S extends MarketInstrument, V extends Value<V>> V take(
			final S instrument, final MarketField<V> field) {
		return maker.take(instrument, field);
	}

}
