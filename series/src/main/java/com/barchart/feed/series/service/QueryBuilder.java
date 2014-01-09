package com.barchart.feed.series.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.series.ITradingWeek;
import com.barchart.feed.api.series.Period;
import com.barchart.feed.api.series.PeriodType;
import com.barchart.feed.api.series.TimePoint;
import com.barchart.feed.api.series.service.ContinuationPolicy;
import com.barchart.feed.api.series.service.NodeDescriptor;
import com.barchart.feed.api.series.service.NodeType;
import com.barchart.feed.api.series.service.Query;
import com.barchart.feed.api.series.service.VolumeType;
import com.barchart.feed.series.TimeFrame;
import com.barchart.feed.series.TradingSession;
import com.barchart.feed.series.TradingWeek;

/**
 * Basically all lies now... ;)
 * 
 * 
 * Builds a new query. At the time of this writing, the only
 * "mandatory" entry is the symbol string, though the  period,
 * and startDate must also exist, they have defaults of {@link Period#DAY}
 * (which has a default {@link PeriodType} of {@link PeriodType#DAY}),
 * and a startDate equal to today minus 90 days (3 months). 
 * <p>
 * Typical usage is as follows:
 * <p>
 * <pre>
 * BarchartSeriesProvider provider = new BarchartSeriesProvider(MarketService);
 * Query query = QueryBuilder.create().symbol("ESZ3").build();
 * Observable<TimeSeries> series = provider.subscribe(query);
 * </pre>
 * <p>
 * 
 * <em><b>Note: These defaults may(have) changed</b></em>
 * 
 * @author David Ray
 */
@SuppressWarnings("unused")
public class QueryBuilder {
    private int padding;
    private int nearestOffset;
    private NodeType nodeType;
    private String analyticSpecifier;
    private List<String> symbols = new ArrayList<String>();
    private String customQuery;
    private DateTime start = PeriodType.DAY.resolutionInstant(new DateTime().minusDays(90));
    private DateTime end;
	private DataQuery query;
	private List<Period> periods = new ArrayList<Period>();
	private ContinuationPolicy policy;
	private VolumeType volumeType;
	private ITradingWeek tradingWeek = TradingWeek.DEFAULT;
	
	public QueryBuilder() {
		query = new DataQuery();
	}
	
	public DataQuery build() {
		if(symbols.size() < 1) {
			throw new IllegalStateException("No Symbol specified.");
		}
		
		if(periods.size() < 1) {
		    periods.add(Period.DAY);
		    query.periods.add(Period.DAY);
		}
		
		if(analyticSpecifier == null && nodeType == null) {
		    nodeType = NodeType.IO;
		    analyticSpecifier = query.analyticSpecifier = NodeType.IO.toString();
		}else if(analyticSpecifier != null && nodeType == null) {
		    nodeType = NodeType.ANALYTIC;
		}
		
		query.nodeType = nodeType;
		
		return query;
	}
	
	public static QueryBuilder create() {
		return new QueryBuilder();
	}
	
	public QueryBuilder customQuery(String queryStr) {
	    if(queryStr == null || queryStr.length() < 1) {
	        throw new IllegalArgumentException("If specified, the query cannot be empty or null.");
	    }
	    
	    this.customQuery = query.customQuery = queryStr;
	    query.hasCustomQuery = true;
	    return this;
	}
	
	public QueryBuilder nodeType(NodeType type) {
	    if(type == null) {
	        throw new IllegalArgumentException("If specified, the NodeType must be non null");
	    }
	    this.nodeType = query.nodeType = type;
	    return this;
	}
	
	public QueryBuilder specifier(String specifier) {
		if(specifier == null || specifier.length() < 1) {
			throw new IllegalArgumentException("If specified, the analytic specifier cannot be null, or of zero length");
		}
		this.analyticSpecifier = query.analyticSpecifier = specifier;
		return this;
	}
	
	public QueryBuilder symbol(String symbol) {
		if(symbol == null || symbol.length() < 1) {
			throw new IllegalArgumentException("Symbol cannot be null, or of zero length");
		}
		this.symbols.add(symbol);
		query.symbols.add(symbol);
		return this;
	}
	
	public QueryBuilder period(Period period) {
		if(period == null) {
			throw new IllegalArgumentException("Must specify a non null period");
		}
		this.periods.add(period);
		query.periods.add(period);
		return this;
	}
	
	public QueryBuilder start(DateTime date) {
		if(date == null) {
			throw new IllegalArgumentException("Must specify a non null start date or not at all");
		}
		this.start = query.start = date;
		return this;
	}
	
	public QueryBuilder end(DateTime date) {
		if(date == null) {
			throw new IllegalArgumentException("Must specify a non null end date or not at all");
		}
		this.end = query.end = date;
		return this;
	}
	
	public QueryBuilder padding(int numBars) {
		if(numBars < 0) {
			throw new IllegalArgumentException("Must specify a padding > -1 or not at all");
		}
		this.padding = query.padding = numBars;
		return this;
	}
	
	public QueryBuilder continuationPolicy(ContinuationPolicy policy) {
		if(policy == null) {
			throw new IllegalArgumentException("If specified, policy must be non null");
		}
		this.policy = query.policy = policy;
		return this;
	}
	
	public QueryBuilder nearestOffset(int offset) {
	    if(offset < 0) {
	        throw new IllegalArgumentException("Cannot specify a negative offset");
	    }
	    this.nearestOffset = query.nearestOffset = offset;
	    return this;
	}
	
	public QueryBuilder volumeType(VolumeType type) {
	    if(type == null) {
            throw new IllegalArgumentException("If specified, VolumeType must be non null");
        }
	    this.volumeType = query.volumeType = type;
	    return this;
	}
	
	public QueryBuilder tradingWeek(ITradingWeek week) {
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
	    private NodeType nodeType;
	    private String analyticSpecifier;
	    private List<String> symbols = new ArrayList<String>();
        private List<Period> periods = new ArrayList<Period>();
        private DateTime start = PeriodType.DAY.resolutionInstant(new DateTime().minusDays(90));
        private DateTime end;
	    private ContinuationPolicy policy;
	    private VolumeType volumeType;
	    private ITradingWeek tradingWeek = TradingWeek.DEFAULT;
	    
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
	        TimeFrame[] timeFrames = new TimeFrame[queryPeriods.size()];
	        for(int idx = 0;idx < timeFrames.length;idx++) {
	            timeFrames[idx] = new TimeFrame(queryPeriods.get(idx), query.getStart(), query.getEnd());
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
	    
	    /**
	     * Returns the {@link TradingWeek} which is a collection of {@link TradingSession}s comprising an average 
	     * week of trading.
	     * 
	     * @return  the {@link TradingWeek}
	     */
	    @Override
	    public ITradingWeek getTradingWeek() {
	        return tradingWeek;
	    }

	}
}
