package com.barchart.feed.series.service;

import java.util.Arrays;

import rx.Observable;
import rx.Observer;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.series.service.Node;
import com.barchart.feed.api.series.service.NodeDescriptor;
import com.barchart.feed.api.series.service.Query;
import com.barchart.feed.api.series.service.Subscription;
import com.barchart.feed.api.series.temporal.TimeFrame;
import com.barchart.feed.api.series.temporal.TradingWeek;


/**
 * Encapsulates information describing inputs and outputs of {@link Node} objects
 * and provides a mechanism by which Node outputs can be shared among clients.
 * <p>
 * Subscriptions also ascribe to the idioms of the RX package's {@link rx.Observable}
 * {@link rx.Subscription}s by providing implementations of the {@link rx.Subscription#unsubscribe()}
 * method.
 */
public class SeriesSubscription implements Subscription {
	private String symbol;
	private Instrument instrument;
	private String analyticSpecifier;
	private TimeFrame[] timeFrames;
    private TradingWeek tradingWeek;
    
    /**
     * Constructs a new {@code Subscription} objects.
     */
    public SeriesSubscription() {}
    
    public SeriesSubscription(String symbol, Instrument i, String analyticSpecifier, TimeFrame[] timeFrames, TradingWeek week) {
    	this.instrument = i;
    	this.symbol = symbol;
    	this.analyticSpecifier = analyticSpecifier;
    	this.timeFrames = timeFrames;
    	this.tradingWeek = week;
    }
    
    public SeriesSubscription(SeriesSubscription ss) {
        this.analyticSpecifier = ss.analyticSpecifier;
        this.instrument = ss.instrument;
        this.symbol = ss.symbol;
        this.timeFrames = new TimeFrame[ss.timeFrames.length]; int i = 0;
        for(TimeFrame tf : ss.timeFrames) {
            timeFrames[i++] = tf;
        }
        this.tradingWeek = ss.tradingWeek;
    }
    
    /**
     * This is included because the historical server doesn't
     * "understand" the symbol as returned from the {@link Instrument} 
     * object at the time of this writing. Therefore this will have to
     * come from the {@link Query} passed in by the client.
     */
    @Override
    public String getSymbol() {
    	return symbol;
    }
    
    /**
     * Returns the instrument object.
     * @return the instrument object.
     */
	@Override
    public Instrument getInstrument() {
		return instrument;
	}

	/**
     * Returns the underlying timeframes.
     * @return the {@link TimeFrame}s
     */
	@Override
    public TimeFrame[] getTimeFrames() {
		return timeFrames;
	}
	
	/**
	 * Sets the {@link TimeFrame} array
	 * @param tf
	 */
	public void setTimeFrames(TimeFrame[] tf) {
	    this.timeFrames = tf;
	}
	
	/**
	 * Adds the specified {@link TimeFrame} to this {@code Subscription}
	 * @param tf
	 */
	public void addTimeFrame(TimeFrame tf) {
	    TimeFrame[] array = new TimeFrame[timeFrames.length + 1];
	    System.arraycopy(timeFrames, 0, array, 0, timeFrames.length);
	    array[array.length - 1] = tf;
	    this.timeFrames = array;
	}
	
	public TimeFrame getTimeFrame(int index) {
	    return timeFrames[index];
	}
	
	/**
     * Returns the id of a required analytic if one is required.
     * @return the analytic specifier
     */
	@Override
    public String getAnalyticSpecifier() {
		return analyticSpecifier;
	}
	
	/**
     * Returns the {@link TradingWeek}
     * 
     * @return the {@link TradingWeek}
     */
	@Override
    public TradingWeek getTradingWeek() {
		return tradingWeek;
	}
	
	/**
     * Tests the "shareability" of this {@code Subscription}'s output
     * with the specified subscription. {@link NodeDescriptor} is not
     * tested here because if that can be simply determined and if the
     * test below is positive we can at least share this Subscription's
     * source or ancestor Subscription data.
     * 
     * @param other
     * @return
     */
	@Override
    public boolean isDerivableFrom(Subscription otherSubscription) {
		boolean retVal = false;
		SeriesSubscription other = (SeriesSubscription)otherSubscription;
		if((other.instrument.id().equals(instrument.id()) || 
		    (symbol != null && symbol.equals(other.symbol))) && 
			    other.tradingWeek.equals(tradingWeek)) {
			retVal = true;
		}
		 
		boolean lenEq = false;
		if(retVal && (retVal = lenEq = (other.timeFrames.length == timeFrames.length))) {
			for(int i = 0;i < timeFrames.length && lenEq;i++) {
				retVal &= timeFrames[i].isDerivableFrom(other.timeFrames[i]);
				if(!retVal) break;
			}
		}
				
		return retVal;
	}
	
	public void loadFromNodeDescriptor(String sourceKey, SeriesSubscription nextSubscription, AnalyticNodeDescriptor desc) {
        AnalyticNodeDescriptor id = desc.getInputNodeDescriptor(sourceKey);
        if (id == null)
            this.analyticSpecifier = "IO";
        else {
            this.analyticSpecifier = id.getSpecifier();
        }
        
        this.symbol = nextSubscription.symbol;
        this.instrument = nextSubscription.instrument;
        this.timeFrames = new TimeFrame[0];
        String[] timeframes = desc.getInputTimeframes(sourceKey);
        if ((timeframes == null) || (timeframes.length == 0)) {
            this.addTimeFrame(nextSubscription.getTimeFrame(0));
        } else {
            for (String timeframe: timeframes) {
                int index = desc.getTimeframeIndex(timeframe);
                if (index >= 0) {
                    this.addTimeFrame(nextSubscription.getTimeFrame(index));
                }
            }
        }
        this.tradingWeek = nextSubscription.tradingWeek;
   }

	/** 
     * Unsubscribes the submitted {@link Observer} from notifications
     * from the {@link Observable} that furnished this {@code Subscription}.
     */
	@Override
	public void unsubscribe() {
		// TODO Auto-generated method stub
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((analyticSpecifier == null) ? 0 : analyticSpecifier.hashCode());
		result = prime * result
				+ ((instrument.id() == null) ? 0 : instrument.id().hashCode());
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		result = prime * result + Arrays.hashCode(timeFrames);
		result = prime * result
				+ ((tradingWeek == null) ? 0 : tradingWeek.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SeriesSubscription other = (SeriesSubscription) obj;
		if (analyticSpecifier == null) {
			if (other.analyticSpecifier != null)
				return false;
		} else if (!analyticSpecifier.equals(other.analyticSpecifier))
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
	
	public String toString() {
	    StringBuilder sb = new StringBuilder("[ ").append(analyticSpecifier).append(" ").append(symbol).append(" ");
	    for(TimeFrame tf : timeFrames) {
	    	sb.append(tf).append(" ");
	    }
	    sb.append("]");
	    return sb.toString();
	}

}
