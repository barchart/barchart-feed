package com.barchart.feed.api.example;

import com.barchart.feed.api.data.client.TopOfBook;
import com.barchart.feed.api.data.framework.InstrumentEntity;
import com.barchart.feed.api.enums.SecurityType;
import com.barchart.feed.api.market.Agent;
import com.barchart.feed.api.market.InstrumentFilter;
import com.barchart.feed.api.market.MarketCallback;
import com.barchart.feed.api.market.Marketplace;

/**
 * This is a platform widget-like class.  It implements Agent, simulating the
 * use case where some framework will have reference to it.  If it was stand alone,
 * the user would probably have other life-cycle methods which would pass through
 * to the private agent instance.
 * 
 * Of note is the fact that it implements it's own custom filter, something which I
 * feel is going to get a lot more use than the filters baked into the Builder.
 */
public class FutureTermGraph implements Agent {
	
	private final String prefix;
	private final Agent agent;
	
	public FutureTermGraph(final Marketplace marketplace, final String prefix) {
		this.prefix = prefix;
		agent = marketplace.agentBuilder().build(new GraphCallback(), new SymbolFamilyFilter());
	}

	/**
	 * The callback which will hook updates to the top of book to the graph
	 */
	private class GraphCallback implements MarketCallback<TopOfBook> {

		@Override
		public void call(final TopOfBook top) {
			updateGraph(top.instrument(), top);
		}
		
	}
	
	
	/**
	 * An instrument filter for Futures with a specific prefix, such as Eurodollars.
	 */
	private class SymbolFamilyFilter implements InstrumentFilter {

		@Override
		public boolean include(final InstrumentEntity instrument) {
			
			if(instrument.symbol().startsWith(prefix) &&
					instrument.securityType() == SecurityType.FUTURE) {
				return true;
			} else {
				return false;
			}
		}
		
	}
	
	/**
	 * This is the fake business logic method, so assume some graph is being updated here.
	 * 
	 * @param inst
	 * @param top
	 */
	private void updateGraph(final InstrumentEntity inst, final TopOfBook top) {
		
	}

	@Override
	public void activate() {
		agent.activate();
	}

	@Override
	public void update() {
		agent.update();
	}

	@Override
	public void deactivate() {
		agent.deactivate();
	}

	@Override
	public void dismiss() {
		agent.dismiss();
	}
	
}
