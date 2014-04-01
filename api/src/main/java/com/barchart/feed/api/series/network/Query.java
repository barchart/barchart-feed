package com.barchart.feed.api.series.network;

import java.util.List;

import org.joda.time.DateTime;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.series.ContinuationPolicy;
import com.barchart.feed.api.series.CorporateActionType;
import com.barchart.feed.api.series.DataPoint;
import com.barchart.feed.api.series.Period;
import com.barchart.feed.api.series.TradingSession;
import com.barchart.feed.api.series.TradingWeek;
import com.barchart.feed.api.series.VolumeType;

/**
 * A fluent interface for constructing time series queries.
 */
public interface Query {

	// TODO review applicability of these methods

	/**
	 * Returns a flag indicating whether this Query has a custom query
	 * configured.
	 *
	 * @return true if so, false if not.
	 */
	public boolean hasCustomQuery();

	/**
	 * Returns the custom query configured.
	 *
	 * @return
	 */
	public String getCustomQuery();

	/**
	 * Transforms this {@code Query} to a more robust {@link Subscription} type.
	 *
	 * @param i the {@code Instrument}
	 * @return this Query transformed to a {@link Subscription}.
	 */
	public Subscription toSubscription(Instrument i);

	/**
	 * Returns the analytic name requested. Queries should request only one of
	 * an instrument/ symbol and one of either an expression or analytic.
	 * Subsequent calls will overwrite the previous value.
	 *
	 * @return this query
	 */
	public String getAnalyticSpecifier();

	/**
	 * Returns the the symbol or expression requested. Subsequent calls will
	 * overwrite the previous value.
	 *
	 * @return this query
	 */
	public List<String> getSymbols();

	/**
	 * Returns the {@link Period} ({@link DataPoint} aggregation)
	 *
	 * @return the {@link Period}
	 */
	public List<Period> getPeriods();

	// END REVIEW

	/**
	 * Returns the instrument ID for this query.
	 */
	public InstrumentID getInstrument();

	/**
	 * Returns the {@link Period} ({@link DataPoint} aggregation)
	 */
	public Period getPeriod();

	/**
	 * Returns the start date (earliest bar)
	 *
	 * @return the start date
	 */
	public DateTime getStart();

	/**
	 * Returns the end date (latest bar)
	 *
	 * @return the end date
	 */
	public DateTime getEnd();

	/**
	 * The maximum number of bars to request, excluding padding, irrespective of
	 * start and end dates. Bars are counted according to the sort order, which
	 * is defined by the query service implementation. For descending results,
	 * this means X bars before the specified end date (if specified). For
	 * ascending queries, it means X bars after the specified start date.
	 */
	public int getCount();

	/**
	 * The number of additional bars to request beyond the specified start and
	 * end dates. The padding bars are always returned before the specified
	 * start date (if given); otherwise the padding bar count is simply added to
	 * getCount().
	 *
	 * This ensures that date series analytics have enough data available to
	 * properly calculate data points for all visible bars (i.e. a 200-day
	 * moving average requires 200 extra bars before the first visible bar in
	 * order to display accurate data.)
	 */
	public int getPadding();

	/**
	 * Returns the {@link TradingWeek} which is a collection of
	 * {@link TradingSession}s comprising an average week of trading.
	 *
	 * @return the {@link TradingWeek}
	 */
	public TradingWeek getTradingWeek();

	// EQUITIES ONLY

	/**
	 * Returns a list of corporate actions that prices should be adjusted by
	 * (splits, dividends, etc). Only applies to equities.
	 */
	public List<CorporateActionType> getCorporateActions();

	// FUTURES ONLY

	/**
	 * Returns the {@link ContinuationPolicy} for futures charts
	 *
	 * @return the {@link ContinuationPolicy}
	 */
	public ContinuationPolicy getContinuationPolicy();

	/**
	 * The "nearest month" offset for ContinuationPolicy.NEAREST time series
	 * requests (defaults to 1, the front month)
	 *
	 * @return the nearest offset for the {@link ContinuationPolicy}
	 */
	public int getNearestOffset();

	/**
	 * Returns the volume type to return for futures time series
	 * (single-contract, or total of all contracts for a root)
	 *
	 * @return the {@link VolumeType}
	 */
	public VolumeType getVolumeType();

}
