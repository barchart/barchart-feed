package com.barchart.feed.api.series.network;

import java.beans.Expression;

import org.joda.time.DateTime;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.series.ContinuationPolicy;
import com.barchart.feed.api.series.TradingSession;
import com.barchart.feed.api.series.TradingWeek;
import com.barchart.feed.api.series.Period;
import com.barchart.feed.api.series.DataSeries;
import com.barchart.feed.api.series.VolumeType;

/**
 * Builder for creating {@link IQuery} objects. {@link IQuery} objects are used as
 * arguments which enable fetching of {@link DataSeries} objects from the {@link BarchartSeriesProvider}.
 * In addition to raw {@link DataSeries} objects, querys are also used to obtain {@link Analytic}s,
 * {@link Spread}s, and {@link Expression}s that result in collections of series data.
 * <p>
 * Instances of this builder, retain the settings of the last {@link IQuery} built 
 * and returned, such that if {@link #build()} were to be called on it a second time,
 * a new instance identical to the previous {@link IQuery} instance built, would be
 * returned. 
 * 
 * The methods of this builder return the builder itself so that query objects may be
 * specified using the "fluent" api style syntax.
 * 
 * @author metaware
 */
public interface QueryBuilder {

	/**
	 * Returns a new implementation of {@link IQuery}.
	 */
	public Query build();
	/**
	 * Provides a way to pass in a custom url to the historical
	 * data feed. This query string will be checked and used in
	 * place of the typical string being used, such that data 
	 * with extended filtering may be obtained. 
	 * 
	 * NOTE: This feature is not heavily tested as of this writing.
	 * @param	queryStr	a DDF url string 
	 * @return this {@code IQueryBuilder}
	 */
	public QueryBuilder customQuery(String queryStr);
	/**
	 * Sets the {@link NodeType} indicating what type of node furnishes the
	 * expected data.
	 * 
	 * @param	type  the NodeType
	 * @return this {@code IQueryBuilder}
	 */
	public QueryBuilder nodeType(NodeType type);
	/**
	 * Sets the identifier (either IO, or an {@link Analytic} name, or 
	 * a {@link Analytic} network name) which will be used to reify the
	 * particular node or network of nodes which output the required
	 * data specified.
	 * 
	 * @param	specifier	analytic, network or other name.
	 * @return this {@code IQueryBuilder}
	 */
	public QueryBuilder specifier(String specifier);
	/**
	 * Specifies the string form of the {@link Instrument} to be used.
	 * 
	 * @param	symbol	the string form of the {@link Instrument}.
	 * @return this {@code IQueryBuilder}	
	 */
	public QueryBuilder symbol(String symbol);
	/**
	 * Adds a {@link Period} (may be called more than once), to this 
	 * query. Multiple Periods may be added to indicate different
	 * {@link TimeFrame}s needed for certain {@link Analytic}s which
	 * where mulitple TimeFrames may be desired.
	 * 
	 * @param	period 		the Period to add.
	 * @return this {@code IQueryBuilder}	
	 */
	public QueryBuilder period(Period period);
	/**
	 * The start time.
	 * 
	 * @param	the start time.
	 * @return this {@code IQueryBuilder} 
	 */
	public QueryBuilder start(DateTime date);
	/**
	 * The end time. May be null.
	 * 
	 * @param	the end time.
	 * @return this {@code IQueryBuilder} 
	 */
	public QueryBuilder end(DateTime date);
	/**
     * The number of bars to request. When used in conjunction with end date,
     * it specifies the number of bars in the future to retrieve. When used with
     * start date, it specifies the numbers of bars in the past to retrieve. When
     * used with both start and end dates, it requests an extra number of bars
     * before the start date, which is useful for guaranteeing enough data for
     * technical studies on fixed time range charts.
     * 
     * @param	numBars		
     * @return this {@code IQueryBuilder} 
     */
	public QueryBuilder padding(int numBars);
	/**
     * Sets the {@link ContinuationPolicy} for futures charts
     * 
     * @param  the {@link ContinuationPolicy}
     * @return this {@code IQueryBuilder}
     */
	public QueryBuilder continuationPolicy(ContinuationPolicy policy);
	/**
     * The "nearest month" offset for ContinuationPolicy.NEAREST time series
     * requests (defaults to 1, the front month)
     *
     * @return  the nearest offset for the {@link ContinuationPolicy}
     */
	public QueryBuilder nearestOffset(int offset);
	/**
     * Sets the volume type to return for futures time series (single-contract, or
     * total of all contracts for a root)
     * 
     * @param  the {@link VolumeType}
     * @return this {@code IQueryBuilder}
     */
	public QueryBuilder volumeType(VolumeType type);
	/**
     * Sets the {@link TradingWeek} which is a collection of {@link TradingSession}s comprising an average 
     * week of trading. TradingSessions specify the hours of trading which users of this api would
     * like to have included in their data sets. Hours may be filtered or removed, but they may not
     * be added (obviously).
     * 
     * @param  the {@link TradingWeek}
     * @return this {@code IQueryBuilder}
     */
	public QueryBuilder tradingWeek(TradingWeek week);

}