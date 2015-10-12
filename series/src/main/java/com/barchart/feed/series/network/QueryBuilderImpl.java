package com.barchart.feed.series.network;

import java.beans.Expression;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.series.ContinuationPolicy;
import com.barchart.feed.api.series.CorporateActionType;
import com.barchart.feed.api.series.DataSeries;
import com.barchart.feed.api.series.Period;
import com.barchart.feed.api.series.TradingWeek;
import com.barchart.feed.api.series.VolumeType;
import com.barchart.feed.api.series.network.Analytic;
import com.barchart.feed.api.series.network.NodeType;
import com.barchart.feed.api.series.network.Query;
import com.barchart.feed.api.series.network.QueryBuilder;
import com.barchart.feed.series.TimeFrameImpl;
import com.barchart.feed.series.TradingWeekImpl;

/**
 * Builder for creating {@link Query} objects. {@link Query} objects are used as
 * arguments which enable fetching of {@link DataSeries} objects from the
 * {@link BarchartSeriesProvider}. In addition to raw {@link DataSeries}
 * objects, querys are also used to obtain {@link Analytic}s, {@link Spread}s,
 * and {@link Expression}s that result in collections of series data.
 * <p>
 * Instances of this builder, retain the settings of the last {@link Query}
 * built and returned, such that if {@link #build()} were to be called on it a
 * second time, a new instance identical to the previous {@link Query} instance
 * built, would be returned.
 *
 * The methods of this builder return the builder itself so that query objects
 * may be specified using the "fluent" api style syntax.
 *
 * @author metaware
 */
public class QueryBuilderImpl implements QueryBuilder {

	private int count;
	private int padding;
	private int nearestOffset;
	private NodeType nodeType = NodeType.IO;
	private String analyticSpecifier;
	private InstrumentID instrument;
	private final List<String> symbols = new ArrayList<String>();
	private String customQuery;
	private DateTime start;
	private DateTime end;
	private final List<Period> periods = new ArrayList<Period>();
	private List<CorporateActionType> actions = new ArrayList<CorporateActionType>();
	private ContinuationPolicy policy;
	private VolumeType volumeType;
	private TradingWeek tradingWeek = TradingWeekImpl.DEFAULT;

	/**
	 * Instantiates a new {@code QueryBuilder}
	 */
	public QueryBuilderImpl() {
	}

	@Override
	public DataQuery build() {

		if (instrument == null) {
			throw new IllegalStateException("No instrument specified.");
		}

		if (periods.size() < 1) {
			periods.add(Period.DAY);
		}

		if (analyticSpecifier == null || nodeType == null) {
			nodeType = NodeType.IO;
			analyticSpecifier = NodeType.IO.toString();
		} else if (analyticSpecifier != null && nodeType == null) {
			nodeType = NodeType.forString(analyticSpecifier);

		}

		return new DataQuery(
				customQuery,
				nodeType,
				analyticSpecifier,
				new ArrayList<String>(symbols),
				new ArrayList<Period>(periods),
				count,
				padding,
				nearestOffset,
				instrument,
				start,
				end,
				new ArrayList<CorporateActionType>(actions),
				policy,
				volumeType,
				tradingWeek);

	}

	@Override
	public DataQuery buildWithoutInstrument() {

		if (periods.size() < 1) {
			periods.add(Period.DAY);
		}

		if (analyticSpecifier == null || nodeType == null) {
			nodeType = NodeType.IO;
			analyticSpecifier = NodeType.IO.toString();
		} else if (analyticSpecifier != null && nodeType == null) {
			nodeType = NodeType.forString(analyticSpecifier);

		}

		return new DataQuery(
				customQuery,
				nodeType,
				analyticSpecifier,
				new ArrayList<String>(symbols),
				new ArrayList<Period>(periods),
				count,
				padding,
				nearestOffset,
				instrument,
				start,
				end,
				new ArrayList<CorporateActionType>(actions),
				policy,
				volumeType,
				tradingWeek);

	}

	/**
	 * Returns a new instance of {@link QueryBuilder}
	 *
	 * @return a new instance of {@link QueryBuilder}
	 */
	public static QueryBuilder create() {
		return new QueryBuilderImpl();
	}

	@Override
	public QueryBuilder customQuery(final String queryStr) {
		if (queryStr == null || queryStr.length() < 1) {
			throw new IllegalArgumentException("If specified, the query cannot be empty or null.");
		}

		this.customQuery = queryStr;
		return this;
	}

	@Override
	public QueryBuilder nodeType(final NodeType type) {
		if (type == null) {
			throw new IllegalArgumentException("If specified, the NodeType must be non null");
		}
		this.nodeType = type;
		return this;
	}

	@Override
	public QueryBuilder specifier(final String specifier) {
		if (specifier == null || specifier.length() < 1) {
			throw new IllegalArgumentException("If specified, the analytic specifier cannot be null, or of zero length");
		}
		this.analyticSpecifier = specifier;
		return this;
	}

	@Override
	public QueryBuilder symbol(final String symbol) {
		if (symbol == null || symbol.length() < 1) {
			throw new IllegalArgumentException("Symbol cannot be null, or of zero length");
		}
		this.symbols.add(symbol);
		return this;
	}

	/**
	 * Adds a {@link Period} (may be called more than once), to this query.
	 * Multiple Periods may be added to indicate different {@link TimeFrameImpl}
	 * s needed for certain {@link Analytic}s where multiple TimeFrames may be
	 * desired.
	 *
	 * @param period the Period to add.
	 * @return this {@code QueryBuilder}
	 */
	@Override
	public QueryBuilder instrument(final InstrumentID inst) {
		if (inst == null) {
			throw new IllegalArgumentException("Instrument cannot be null");
		}
		this.instrument = inst;
		return this;
	}

	@Override
	public QueryBuilder period(final Period period) {
		if (period == null) {
			throw new IllegalArgumentException("Must specify a non null period");
		}
		this.periods.add(period);
		return this;
	}

	@Override
	public QueryBuilder start(final DateTime date) {
		if (date == null) {
			throw new IllegalArgumentException("Must specify a non null start date");
		}
		this.start = date;
		return this;
	}

	@Override
	public QueryBuilder end(final DateTime date) {
		if (date == null) {
			throw new IllegalArgumentException("Must specify a non null end date or not at all");
		}
		this.end = date;
		return this;
	}

	@Override
	public QueryBuilder count(final int numBars) {
		if (numBars < 0) {
			throw new IllegalArgumentException("Must specify a bar count > -1 or not at all");
		}
		this.count = numBars;
		return this;
	}

	@Override
	public QueryBuilder padding(final int numBars) {
		if (numBars < 0) {
			throw new IllegalArgumentException("Must specify a padding > -1 or not at all");
		}
		this.padding = numBars;
		return this;
	}

	@Override
	public QueryBuilder corporateActions(final CorporateActionType... types) {
		if (types == null || types.length == 0) {
			throw new IllegalArgumentException("Must specify at least one action type");
		}
		this.actions = Arrays.asList(types);
		return this;
	}

	@Override
	public QueryBuilder continuationPolicy(final ContinuationPolicy policy) {
		if (policy == null) {
			throw new IllegalArgumentException("If specified, policy must be non null");
		}
		this.policy = policy;
		return this;
	}

	@Override
	public QueryBuilder nearestOffset(final int offset) {
		if (offset < 0) {
			throw new IllegalArgumentException("Cannot specify a negative offset");
		}
		this.nearestOffset = offset;
		return this;
	}

	@Override
	public QueryBuilder volumeType(final VolumeType type) {
		if (type == null) {
			throw new IllegalArgumentException("If specified, VolumeType must be non null");
		}
		this.volumeType = type;
		return this;
	}

	@Override
	public QueryBuilder tradingWeek(final TradingWeek week) {
		if (week == null || week.length() < 1) {
			throw new IllegalArgumentException(
					"If specified, the TradingWeek must be non null and contain configured sessions");
		}
		this.tradingWeek = week;
		return this;
	}

	/**
	 * Specifies the type and characteristics of data being requested.
	 */
	public class DataQuery implements Query {

		private final String customQuery;
		private final NodeType nodeType;
		private final String analyticSpecifier;
		private final List<String> symbols;
		private final List<Period> periods;
		private final int count;
		private final int padding;
		private final int nearestOffset;
		private final InstrumentID instrument;
		private final DateTime start;
		private final DateTime end;
		private final List<CorporateActionType> actions;
		private final ContinuationPolicy policy;
		private final VolumeType volumeType;
		private final TradingWeek tradingWeek;

		/**
		 * Constructs a new {@code DataQuery}
		 */
		private DataQuery(final String customQuery,
				final NodeType nodeType,
				final String analyticSpecifier,
				final List<String> symbols,
				final List<Period> periods,
				final int count,
				final int padding,
				final int nearestOffset,
				final InstrumentID instrument,
				final DateTime start,
				final DateTime end,
				final List<CorporateActionType> actions,
				final ContinuationPolicy policy,
				final VolumeType volumeType,
				final TradingWeek tradingWeek) {

			this.customQuery = customQuery;
			this.nodeType = nodeType;
			this.analyticSpecifier = analyticSpecifier;
			this.symbols = symbols;
			this.periods = periods;
			this.count = count;
			this.padding = padding;
			this.nearestOffset = nearestOffset;
			this.instrument = instrument;
			this.start = start;
			this.end = end;
			this.actions = actions;
			this.policy = policy;
			this.volumeType = volumeType;
			this.tradingWeek = tradingWeek;

		}

		@Override
		public boolean hasCustomQuery() {
			return customQuery != null;
		}

		@Override
		public String getCustomQuery() {
			return customQuery;
		}

		@Override
		public SeriesSubscription toSubscription(final Instrument i) {
			final TimeFrameImpl[] timeFrames = new TimeFrameImpl[periods.size()];
			for (int idx = 0; idx < timeFrames.length; idx++) {
				timeFrames[idx] = new TimeFrameImpl(periods.get(idx), start, end);
			}

			// Flatten out the symbols list for now...
			return new SeriesSubscription(getSymbols().get(0), i, analyticSpecifier, timeFrames, tradingWeek);
		}

		public NodeType getNodeType() {
			return this.nodeType;
		}

		@Override
		public String getAnalyticSpecifier() {
			return analyticSpecifier;
		}

		@Override
		public List<String> getSymbols() {
			return symbols;
		}

		@Override
		public List<Period> getPeriods() {
			return periods;
		}

		@Override
		public InstrumentID getInstrument() {
			return instrument;
		}

		@Override
		public Period getPeriod() {
			return periods.get(0);
		}

		@Override
		public DateTime getStart() {
			return start;
		}

		@Override
		public DateTime getEnd() {
			return end;
		}

		@Override
		public int getCount() {
			return count;
		}

		@Override
		public int getPadding() {
			return padding;
		}

		@Override
		public TradingWeek getTradingWeek() {
			return tradingWeek;
		}

		// EQUITIES ONLY

		@Override
		public List<CorporateActionType> getCorporateActions() {
			return actions;
		}

		// FUTURES ONLY

		@Override
		public ContinuationPolicy getContinuationPolicy() {
			return policy;
		}

		@Override
		public int getNearestOffset() {
			return nearestOffset;
		}

		@Override
		public VolumeType getVolumeType() {
			return volumeType;
		}

	}

}
