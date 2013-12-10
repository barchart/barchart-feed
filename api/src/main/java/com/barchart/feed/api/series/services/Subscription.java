package com.barchart.feed.api.series.services;

import java.util.Arrays;

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
     * object at the time of this writing. Therefore this will have to
     * come from the {@link Query} passed in by the client.
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((descriptor == null) ? 0 : descriptor.hashCode());
		result = prime * result
				+ ((instrument.id() == null) ? 0 : instrument.id().hashCode());
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		result = prime * result + Arrays.hashCode(timeFrames);
		result = prime * result
				+ ((tradingWeek == null) ? 0 : tradingWeek.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Subscription other = (Subscription) obj;
		if (descriptor == null) {
			if (other.descriptor != null)
				return false;
		} else if (!descriptor.equals(other.descriptor))
			return false;
		if (instrument == null) {
			if (other.instrument != null)
				return false;
		} else if (!instrument.id().equals(other.instrument.id()))
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		if (!Arrays.equals(timeFrames, other.timeFrames))
			return false;
		if (tradingWeek == null) {
			if (other.tradingWeek != null)
				return false;
		} else if (!tradingWeek.equals(other.tradingWeek))
			return false;
		return true;
	}

}
