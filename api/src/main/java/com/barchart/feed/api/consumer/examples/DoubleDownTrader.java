package com.barchart.feed.api.consumer.examples;

import com.barchart.feed.api.consumer.Agent;
import com.barchart.feed.api.consumer.MarketCallback;
import com.barchart.feed.api.consumer.Marketplace;
import com.barchart.feed.api.consumer.data.Session;
import com.barchart.feed.api.consumer.enums.MarketEventType;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

public class DoubleDownTrader {
	
	private final Price targetPnL;
	private final Agent agent;

	private volatile Price high, low, currentPNL;
	private volatile Size tradeSize;

	public DoubleDownTrader(final Marketplace marketplace,
			final Price targetPnL) {
		this.targetPnL = targetPnL;

		/* User just wants to trade front month S&P future */
		//agent = marketplace.agentBuilder().filter("ESM3").build(new DoubleDown());
		agent = marketplace.newAgent(new DoubleDown(), MarketEventType.ALL);
	}

	private class DoubleDown implements MarketCallback<Session> {

		@Override
		public void call(final Session session, final MarketEventType type) {

			/* New High */
			if (session.high().greaterThan(high)) {
				sell(session.high(), tradeSize);
				high = session.high();
				tradeSize.mult(2);

				/* New Low */
			} else if (session.low().lessThan(low)) {
				buy(session.low(), tradeSize);
				low = session.low();
				tradeSize.mult(2);
			}

			/* Disable self if target PNL is reached */
			if (currentPNL.greaterThan(targetPnL)) {
				agent.deactivate();
			}

		}

	}

	/* Dummy business logic */
	private void buy(final Price price, final Size size) {

	}

	/* Dummy business logic */
	private void sell(final Price price, final Size size) {

	}


}