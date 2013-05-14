package com.barchart.feed.api.example;

import com.barchart.feed.api.data.client.CurrentSession;
import com.barchart.feed.api.data.value.Price;
import com.barchart.feed.api.data.value.Size;
import com.barchart.feed.api.market.Agent;
import com.barchart.feed.api.market.MarketCallback;
import com.barchart.feed.api.market.Marketplace;

/**
 * A mock trade signal generator which has built in agent life-cycle logic. The
 * agent dismisses itself inside its associated callback after the PNL condition
 * is met.
 * 
 * It also demonstrates the user filtering callback updates for new highs and
 * lows, i.e. the framework doesn't need to handle it.
 * 
 * Obviously don't share this super awesome trading strategy with anyone.
 */
public class DoubleDownTrader {

	private final Price targetPnL;
	private final Agent agent;

	private volatile Price high, low, currentPNL;
	private volatile Size tradeSize;

	public DoubleDownTrader(final Marketplace marketplace,
			final Price targetPnL) {
		this.targetPnL = targetPnL;

		/* User just wants to trade front month S&P future */
		agent = marketplace.agentBuilder().filter("ESM3").build(new DoubleDown());

	}

	private class DoubleDown implements MarketCallback<CurrentSession> {

		@Override
		public void call(final CurrentSession session) {

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
