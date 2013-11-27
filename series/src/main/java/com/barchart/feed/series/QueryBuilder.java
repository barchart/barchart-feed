package com.barchart.feed.series;

import org.joda.time.DateTime;

import rx.Observable;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.series.ContinuationPolicy;
import com.barchart.feed.api.series.Event;
import com.barchart.feed.api.series.Period;
import com.barchart.feed.api.series.SaleCondition;

public class QueryBuilder {
	private DataQuery query;
	private Instrument instr;
	private String symbol;
	private Period period;
	private DateTime start;
	private DateTime end;
	private int padding;
	private ContinuationPolicy policy;
	private Event[] events;
	private SaleCondition[] conditions;
	
	public QueryBuilder() {
		query = new DataQuery();
	}
	
	public Observable<DataQuery> build() {
		if(instr == null && symbol == null) {
			throw new IllegalStateException("No Instrument or Symbol specified.");
		}
		
		if(period == null && start == null && end == null) {
			 //new Observable<DataQuery>().create(func)
			period = Period.DAY;
			start = new DateTime().minusDays(90);
			
		}
		
		
		
		if(instr == null) {
			
		}else{
			
		}
		return null;//query;
	}
	
	public static QueryBuilder create() {
		return new QueryBuilder();
	}
	
	public QueryBuilder instrument(Instrument instrument) {
		if(instrument == null) {
			throw new IllegalArgumentException("Instrument cannot be null");
		}
		this.instr = instrument;
		query.instrument(instrument);
		return this;
	}
	
	public QueryBuilder symbol(String symbol) {
		if(symbol == null || symbol.length() < 1) {
			throw new IllegalArgumentException("Symbol cannot be null, or of zero length");
		}
		this.symbol = symbol;
		query.symbol(symbol);
		return this;
	}
	
	public QueryBuilder period(Period period) {
		if(period == null) {
			throw new IllegalArgumentException("Must specify a non null period");
		}
		this.period = period;
		query.period(period);
		return this;
	}
	
	public QueryBuilder start(DateTime date) {
		if(date == null) {
			throw new IllegalArgumentException("Must specify a non null start date or not at all");
		}
		this.start = date;
		query.start(date);
		return this;
	}
	
	public QueryBuilder end(DateTime date) {
		if(date == null) {
			throw new IllegalArgumentException("Must specify a non null end date or not at all");
		}
		this.end = date;
		query.end(date);
		return this;
	}
	
	public QueryBuilder padding(int numBars) {
		if(numBars < 0) {
			throw new IllegalArgumentException("Must specify a padding > -1 or not at all");
		}
		this.padding = numBars;
		query.padding(numBars);
		return this;
	}
	
	public QueryBuilder condition(SaleCondition... session) {
		if(session == null || session.length < 1) {
			throw new IllegalArgumentException("If specified, conditions must be non null and greater than 0");
		}
		this.conditions = session;
		query.condition(session);
		return this;
	}
	
	public QueryBuilder event(Event... events) {
		if(events == null || events.length < 1) {
			throw new IllegalArgumentException("If specified, events must be non null and greater than 0");
		}
		this.events = events;
		query.event(events);
		return this;
	}
	
	public QueryBuilder continuationPolicy(ContinuationPolicy policy) {
		if(policy == null) {
			throw new IllegalArgumentException("If specified, policy must be non null");
		}
		this.policy = policy;
		query.continuationPolicy(policy);
		return this;
	}
}
