package com.barchart.feed.api.series.services;

import com.barchart.feed.api.model.data.Market;

/**
 * Assemblers "assemble" data fed from two inputs. Namely, live 
 * market data and historical data. This data is then merged and then
 * output to a {@link TimeSeries} <em><b>without</b></em> attention
 * to {@link Period} aggregation or rule formatting (with regard to
 * futures expiration or continuation, for example...) 
 * 
 * @author David Ray
 */
public interface Assembler {
	/**
	 * Returns the {@link Subscription} this Assembler is initialized with.
	 * 
	 * @return
	 */
	public Subscription getSubscription();
	
	/**
	 * Called by {@link MarketObserver<Market>} 
	 * @param m
	 */
	public void onNextMarket(Market m);
	
	/**
	 * Called by {@link HistoricalObserver}
	 * @param result
	 */
	public <T extends HistoricalResult> void onNextHistorical(T result);
	
}
