package com.barchart.feed.api.series.services;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.series.temporal.TimeFrame;
import com.barchart.feed.api.series.temporal.TradingWeek;

public class Subscription implements rx.Subscription {
	private String symbol;
	private Instrument instrument;
    private TimeFrame[] timeFrames;
    private NodeDescriptor descriptor;
    private TradingWeek tradingWeek;
    
    
    public Subscription() {}
    
    public Subscription(NodeDescriptor descriptor, Instrument i, String symbol, TimeFrame[] timeFrames, TradingWeek week) {
    	this.descriptor = descriptor;
    	this.instrument = i;
    	this.symbol = symbol;
    	this.timeFrames = timeFrames;
    }
    
    /**
     * This is included because the historical server doesn't
     * "understand" the symbol as returned from the {@link Instrument} 
     * object at the time of this writing.
     */
    public String getSymbol() {
    	return symbol;
    }
    
	/**
	 * Returns the instrument object.
	 * @return the instrument object.
	 */
	public Instrument getInstrument() {
		return instrument;
	}

	/**
	 * Returns the underlying timeframes.
	 * @return the {@link TimeFrame}s
	 */
	public TimeFrame[] getTimeFrames() {
		return timeFrames;
	}
	
	/**
	 * Returns the {@link NodeDescriptor}
	 * @return the node descriptor
	 */
	public NodeDescriptor getNodeDescriptor() {
		return descriptor;
	}
	
	/**
	 * Returns the {@link TradingWeek}
	 * 
	 * @return the {@link TradingWeek}
	 */
	public TradingWeek getTradingWeek() {
		return tradingWeek;
	}

	@Override
	public void unsubscribe() {
		// TODO Auto-generated method stub
		
	}

}
