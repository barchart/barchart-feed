package com.barchart.feed.api.series.services;

import com.barchart.feed.api.model.data.Market;

/**
 * Assemblers "assemble" data fed from two inputs. Namely, live 
 * market data and historical data. This data is then merged and then
 * output to a {@link TimeSeries} 
 * 
 * @author David Ray
 */
public interface Assembler {
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
