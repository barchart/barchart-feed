package com.barchart.feed.api.series.service;

import rx.Observable;
import rx.Observer;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.series.temporal.TimeFrame;
import com.barchart.feed.api.series.temporal.TradingWeek;

public interface Subscription extends rx.Subscription {

    /**
     * This is included because the historical server doesn't
     * "understand" the symbol as returned from the {@link Instrument} 
     * object at the time of this writing. Therefore this will have to
     * come from the {@link Query} passed in by the client.
     */
    public String getSymbol();

    /**
     * Returns the instrument object.
     * @return the instrument object.
     */
    public Instrument getInstrument();

    /**
     * Returns the underlying time frames.
     * @return the {@link TimeFrame}s
     */
    public TimeFrame[] getTimeFrames();

    /**
     * Returns the {@link NodeDescriptor}
     * @return the node descriptor
     */
    public NodeDescriptor getNodeDescriptor();

    /**
     * Returns the {@link TradingWeek}
     * 
     * @return the {@link TradingWeek}
     */
    public TradingWeek getTradingWeek();

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
    public boolean isDerivableFrom(Subscription other);

    /** 
     * Unsubscribes the submitted {@link Observer} from notifications
     * from the {@link Observable} that furnished this {@code Subscription}.
     */
    public void unsubscribe();

}