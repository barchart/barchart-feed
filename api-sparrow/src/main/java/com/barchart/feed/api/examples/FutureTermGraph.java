package com.barchart.feed.api.examples;

import com.barchart.feed.api.core.Agent;
import com.barchart.feed.api.core.InstrumentAcceptor;
import com.barchart.feed.api.core.MarketCallback;
import com.barchart.feed.api.core.Marketplace;
import com.barchart.feed.api.data.Exchange;
import com.barchart.feed.api.data.Instrument;
import com.barchart.feed.api.data.TopOfBook;
import com.barchart.feed.api.enums.MarketEventType;
import com.barchart.feed.api.enums.SecurityType;

public class FutureTermGraph implements Agent {
	
	private final String prefix;
	private final Agent agent;

	public FutureTermGraph(final Marketplace marketplace, final String prefix) {
		this.prefix = prefix;
		//agent = marketplace.agentBuilder().build(new GraphCallback(), new SymbolFamilyFilter());
		agent =  marketplace.newAgent(new GraphCallback(), MarketEventType.ALL);
	}

	/**
	 * The callback which will hook updates to the top of book to the graph
	 */
	private class GraphCallback implements MarketCallback<TopOfBook> {

		@Override
		public void call(final TopOfBook top, final MarketEventType type) {
			updateGraph(top);
		}

	}


	/**
	 * An instrument filter for Futures with a specific prefix, such as Eurodollars.
	 */
	private class SymbolFamilyFilter implements InstrumentAcceptor {

		@Override
		public boolean accept(final Instrument instrument) {

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
	 * @param top
	 */
	private void updateGraph(final TopOfBook top) {

	}

	@Override
	public void activate() {
		agent.activate();
	}

	@Override
	public void deactivate() {
		agent.deactivate();
	}

	@Override
	public void dismiss() {
		agent.dismiss();
	}

	@Override
	public void includeAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void include(CharSequence... symbols) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void include(Instrument... instruments) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exclude(CharSequence... symbols) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exclude(Instrument... instruments) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void include(Exchange... exchanges) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exclude(Exchange... exchanges) {
		// TODO Auto-generated method stub
		
	}

}
