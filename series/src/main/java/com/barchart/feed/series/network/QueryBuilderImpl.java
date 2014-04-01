package com.barchart.feed.series.network;

import java.beans.Expression;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.series.ContinuationPolicy;
import com.barchart.feed.api.series.TradingSession;
import com.barchart.feed.api.series.TradingWeek;
import com.barchart.feed.api.series.Period;
import com.barchart.feed.api.series.PeriodType;
import com.barchart.feed.api.series.DataSeries;
import com.barchart.feed.api.series.VolumeType;
import com.barchart.feed.api.series.network.Analytic;
import com.barchart.feed.api.series.network.NodeType;
import com.barchart.feed.api.series.network.Query;
import com.barchart.feed.api.series.network.QueryBuilder;
import com.barchart.feed.series.TimeFrameImpl;
import com.barchart.feed.series.TradingSessionImpl;
import com.barchart.feed.series.TradingWeekImpl;

/**
 * Builder for creating {@link Query} objects. {@link Query} objects are used as
 * arguments which enable fetching of {@link DataSeries} objects from the {@link BarchartSeriesProvider}.
 * In addition to raw {@link DataSeries} objects, querys are also used to obtain {@link Analytic}s,
 * {@link Spread}s, and {@link Expression}s that result in collections of series data.
 * <p>
 * Instances of this builder, retain the settings of the last {@link Query} built 
 * and returned, such that if {@link #build()} were to be called on it a second time,
 * a new instance identical to the previous {@link Query} instance built, would be
 * returned. 
 * 
 * The methods of this builder return the builder itself so that query objects may be
 * specified using the "fluent" api style syntax.
 * 
 * @author metaware
 */
@SuppressWarnings("unused")
public class QueryBuilderImpl implements QueryBuilder {
    private int padding;
    private int nearestOffset;
    private NodeType nodeType = NodeType.IO;
    private String analyticSpecifier;
    private List<String> symbols = new ArrayList<String>();
    private String customQuery;
    private DateTime start = PeriodType.DAY.resolutionInstant(new DateTime().minusDays(90));
    private DateTime end;
	private DataQuery query;
	private List<Period> periods = new ArrayList<Period>();
	private ContinuationPolicy policy;
	private VolumeType volumeType;
	private TradingWeek tradingWeek = TradingWeekImpl.DEFAULT;
	
	/**
	 * Instantiates a new {@code QueryBuilder}
	 */
	public QueryBuilderImpl() {
		query = new DataQuery();
	}
	
	/**
	 * Returns a new implementation of {@link Query}.
	 */
	@Override
	public DataQuery build() {
		if(symbols.size() < 1) {
			throw new IllegalStateException("No Symbol specified.");
		}
		
		if(periods.size() < 1) {
		    periods.add(Period.DAY);
		    query.periods.add(Period.DAY);
		}
		
		if(analyticSpecifier == null || nodeType == null) {
		    nodeType = NodeType.IO;
		    analyticSpecifier = query.analyticSpecifier = NodeType.IO.toString();
		}else if(analyticSpecifier != null && nodeType == null) {
		    nodeType = NodeType.forString(analyticSpecifier);
		    
		}
		
		query.nodeType = nodeType;
		
		return query;
	}
	
	/**
	 * Returns a new instance of {@link QueryBuilder}
	 * 
	 * @return	a new instance of {@link QueryBuilder}
	 */
	public static QueryBuilder create() {
		return new QueryBuilderImpl();
	}
	
	/**
	 * Provides a way to pass in a custom url to the historical
	 * data feed. This query string will be checked and used in
	 * place of the typical string being used, such that data 
	 * with extended filtering may be obtained. 
	 * 
	 * NOTE: This feature is not heavily tested as of this writing.
	 * @param	queryStr	a DDF url string 
	 * @return this {@code QueryBuilder}
	 */
	@Override
	public QueryBuilder customQuery(String queryStr) {
	    if(queryStr == null || queryStr.length() < 1) {
	        throw new IllegalArgumentException("If specified, the query cannot be empty or null.");
	    }
	    
	    this.customQuery = query.customQuery = queryStr;
	    query.hasCustomQuery = true;
	    return this;
	}
	
	/**
	 * Sets the {@link NodeType} indicating what type of node furnishes the
	 * expected data.
	 * 
	 * @param	type  the NodeType
	 * @return this {@code QueryBuilder}
	 */
	@Override
	public QueryBuilder nodeType(NodeType type) {
	    if(type == null) {
	        throw new IllegalArgumentException("If specified, the NodeType must be non null");
	    }
	    this.nodeType = query.nodeType = type;
	    return this;
	}
	
	/**
	 * Sets the identifier (either IO, or an {@link Analytic} name, or 
	 * a {@link Analytic} network name) which will be used to reify the
	 * particular node or network of nodes which output the required
	 * data specified.
	 * 
	 * @param	specifier	analytic, network or other name.
	 * @return this {@code QueryBuilder}
	 */
	@Override
	public QueryBuilder specifier(String specifier) {
		if(specifier == null || specifier.length() < 1) {
			throw new IllegalArgumentException("If specified, the analytic specifier cannot be null, or of zero length");
		}
		this.analyticSpecifier = query.analyticSpecifier = specifier;
		return this;
	}
	
	/**
	 * Specifies the string form of the {@link Instrument} to be used.
	 * 
	 * @param	symbol	the string form of the {@link Instrument}.
	 * @return this {@code QueryBuilder}	
	 */
	@Override
	public QueryBuilder symbol(String symbol) {
		if(symbol == null || symbol.length() < 1) {
			throw new IllegalArgumentException("Symbol cannot be null, or of zero length");
		}
		this.symbols.add(symbol);
		query.symbols.add(symbol);
		return this;
	}
	
	/**
	 * Adds a {@link Period} (may be called more than once), to this 
	 * query. Multiple Periods may be added to indicate different
	 * {@link TimeFrameImpl}s needed for certain {@link Analytic}s
	 * where multiple TimeFrames may be desired.
	 * 
	 * @param	period 		the Period to add.
	 * @return this {@code QueryBuilder}	
	 */
	@Override
	public QueryBuilder period(Period period) {
		if(period == null) {
			throw new IllegalArgumentException("Must specify a non null period");
		}
		this.periods.add(period);
		query.periods.add(period);
		return this;
	}
	
	/**
	 * The start time.
	 * 
	 * @param	the start time.
	 * @return this {@code QueryBuilder} 
	 */
	@Override
	public QueryBuilder start(DateTime date) {
		if(date == null) {
			throw new IllegalArgumentException("Must specify a non null start date or not at all");
		}
		this.start = query.start = date;
		return this;
	}
	
	/**
	 * The end time. May be null.
	 * 
	 * @param	the end time.
	 * @return this {@code QueryBuilder} 
	 */
	@Override
	public QueryBuilder end(DateTime date) {
		if(date == null) {
			throw new IllegalArgumentException("Must specify a non null end date or not at all");
		}
		this.end = query.end = date;
		return this;
	}
	
	/**
     * The number of bars to request. When used in conjunction with end date,
     * it specifies the number of bars in the future to retrieve. When used with
     * start date, it specifies the numbers of bars in the past to retrieve. When
     * used with both start and end dates, it requests an extra number of bars
     * before the start date, which is useful for guaranteeing enough data for
     * technical studies on fixed time range charts.
     * 
     * @param	numBars		
     * @return this {@code QueryBuilder} 
     */
	@Override
	public QueryBuilder padding(int numBars) {
		if(numBars < 0) {
			throw new IllegalArgumentException("Must specify a padding > -1 or not at all");
		}
		this.padding = query.padding = numBars;
		return this;
	}
	
	/**
     * Sets the {@link ContinuationPolicy} for futures charts
     * 
     * @param  the {@link ContinuationPolicy}
     * @return this {@code QueryBuilder}
     */
	@Override
	public QueryBuilder continuationPolicy(ContinuationPolicy policy) {
		if(policy == null) {
			throw new IllegalArgumentException("If specified, policy must be non null");
		}
		this.policy = query.policy = policy;
		return this;
	}
	
	/**
     * The "nearest month" offset for ContinuationPolicy.NEAREST time series
     * requests (defaults to 1, the front month)
     *
     * @param  the nearest offset for the {@link ContinuationPolicy}
     * @return this {@code QueryBuilder}
     */
	@Override
	public QueryBuilder nearestOffset(int offset) {
	    if(offset < 0) {
	        throw new IllegalArgumentException("Cannot specify a negative offset");
	    }
	    this.nearestOffset = query.nearestOffset = offset;
	    return this;
	}
	
	/**
     * Sets the volume type to return for futures time series (single-contract, or
     * total of all contracts for a root)
     * 
     * @param  the {@link VolumeType}
     * @return this {@code QueryBuilder}
     */
	@Override
	public QueryBuilder volumeType(VolumeType type) {
	    if(type == null) {
            throw new IllegalArgumentException("If specified, VolumeType must be non null");
        }
	    this.volumeType = query.volumeType = type;
	    return this;
	}
	
	/**
     * Sets the {@link TradingWeek} which is a collection of {@link TradingSession}s comprising an average 
     * week of trading. TradingSessions specify the hours of trading which users of this api would
     * like to have included in their data sets. Hours may be filtered or removed, but they may not
     * be added (obviously).
     * 
     * @param  the {@link TradingWeek}
     * @return this {@code QueryBuilder}
     */
	@Override
	public QueryBuilder tradingWeek(TradingWeek week) {
        if(week == null || week.length() < 1) {
            throw new IllegalArgumentException("If specified, the TradingWeek must be non null and contain configured sessions");
        }
        this.tradingWeek = query.tradingWeek = week;
        return this;
    }
	
	/**
	 * Specifies the type and characteristics of data being requested.
	 */
	public class DataQuery implements Query {
	    private int padding;
	    private int nearestOffset;
	    private boolean hasCustomQuery;
	    private String customQuery;
	    private NodeType nodeType = NodeType.IO;
	    private String analyticSpecifier;
	    private List<String> symbols = new ArrayList<String>();
        private List<Period> periods = new ArrayList<Period>();
        private DateTime start = PeriodType.DAY.resolutionInstant(new DateTime().minusDays(90));
        private DateTime end;
	    private ContinuationPolicy policy;
	    private VolumeType volumeType;
	    private TradingWeek tradingWeek = TradingWeekImpl.DEFAULT;
	    
		/**
		 * Constructs a new {@code DataQuery}
		 */
		private DataQuery() {}
		
		/**
		 * Vehicle for passing through more specific and obscure
		 * queries to the historical service. Returns true if this
		 * {@code Query} object contains a custom query.
		 */
		@Override
		public boolean hasCustomQuery() {
		    return hasCustomQuery;
		}
		
		/**
		 * Returns the custom query parts if they exist.
		 */
		@Override
		public String getCustomQuery() {
		    return customQuery;
		}
		
		/**
	     * Transforms this {@code Query} to a more robust {@link SeriesSubscription} type.
	     * 
	     * @param   i     the {@code Instrument}
	     * @return        this Query transformed to a {@link Subscripton}.
	     */
	    public SeriesSubscription toSubscription(Instrument i) {
	        List<Period> queryPeriods = query.getPeriods();
	        TimeFrameImpl[] timeFrames = new TimeFrameImpl[queryPeriods.size()];
	        for(int idx = 0;idx < timeFrames.length;idx++) {
	            timeFrames[idx] = new TimeFrameImpl(queryPeriods.get(idx), query.getStart(), query.getEnd());
	        }
	        
	        //Flatten out the symbols list for now...
	        return new SeriesSubscription(getSymbols().get(0), i, analyticSpecifier, timeFrames, query.getTradingWeek());
	    }
	    
	    /**
	     * Returns the specified {@link NodeType}
	     * 
	     * @return     the user-specified {@link NodeType}
	     */
	    public NodeType getNodeType() {
	        return this.nodeType;
	    }
		
		/**
	     * Returns the symbol requested. Queries should request only one of an instrument,
	     * symbol or expression. Subsequent calls will overwrite the previous value.
	     * @return  this query
	     */
	    public String getAnalyticSpecifier() {
	        return analyticSpecifier;
	    }
	    
	    /**
	     * Returns the symbol of the underying series data.
	     * 
	     * @return	the symbol of the underying series data.
	     */
	    public List<String> getSymbols() {
	    	return symbols;
	    }

	    /**
	     * Returns a list of configured {@link Period}s.
	     * 
	     * @return     a list of Periods.
	     */
	    public List<Period> getPeriods() {
	        return periods;
	    }

	    /**
	     * Returns the start date (earliest bar)
	     * @return  the start date
	     */
	    public DateTime getStart() {
	        return start;
	    }

	    /**
	     * Returns the end date (latest bar)
	     * @return  the end date
	     */
	    public DateTime getEnd() {
	        return end;
	    }

	    /**
	     * The number of bars to request. When used in conjunction with end date,
	     * it specifies the number of bars in the future to retrieve. When used with
	     * start date, it specifies the numbers of bars in the past to retrieve. When
	     * used with both start and end dates, it requests an extra number of bars
	     * before the start date, which is useful for guaranteeing enough data for
	     * technical studies on fixed time range charts.
	     * 
	     * 
	     * @return  the padding
	     */
	    public int getPadding() {
	        return padding;
	    }
	    
	    /**
	     * Returns the {@link TradingWeek} which is a collection of {@link TradingSession}s comprising an average 
	     * week of trading.
	     * 
	     * @return  the {@link TradingWeek}
	     */
	    @Override
	    public TradingWeek getTradingWeek() {
	        return tradingWeek;
	    }

	    // FUTURES ONLY

	    /**
	     * Returns the {@link ContinuationPolicy} for futures charts
	     * 
	     * @return  the {@link ContinuationPolicy}
	     */
	    public ContinuationPolicy getContinuationPolicy() {
	        return policy;
	    }

	    /**
	     * The "nearest month" offset for ContinuationPolicy.NEAREST time series
	     * requests (defaults to 1, the front month)
	     *
	     * @return  the nearest offset for the {@link ContinuationPolicy}
	     */
	    public int getNearestOffset() {
	        return nearestOffset;
	    }

	    /**
	     * Returns the volume type to return for futures time series (single-contract, or
	     * total of all contracts for a root)
	     * 
	     * @return  the {@link VolumeType}
	     */
	    public VolumeType getVolumeType() {
	        return volumeType;
	    }
	}
}
