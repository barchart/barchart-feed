package com.ddfplus.feed.api.market;

import com.barchart.util.anno.EventListener;
import com.barchart.util.anno.NotMutable;
import com.barchart.util.anno.UsedOnce;
import com.barchart.util.values.api.Value;
import com.ddfplus.feed.api.instrument.values.MarketInstrument;
import com.ddfplus.feed.api.market.enums.MarketEvent;
import com.ddfplus.feed.api.market.enums.MarketField;

@NotMutable
public interface MarketTaker<V extends Value<V>> {

	/* NOTE: bind happens only once at taker registration */

	/** specify value type that will be provided in the {@link #onMarketEvent} */
	@UsedOnce
	MarketField<V> bindField();

	/** specify list of events this taker will subscribe to */
	@UsedOnce
	MarketEvent[] bindEvents();

	/** specify list of markets this taker will subscribe to */
	@UsedOnce
	MarketInstrument[] bindInstruments();

	//

	/**
	 * event handler method that will receive subscribed events for subscribed
	 * markets; returned values are frozen and disconnected from live market
	 */
	@EventListener(MarketEvent.class)
	void onMarketEvent(MarketEvent event, MarketInstrument instrument,
			@NotMutable V value);

	//

}
