package com.barchart.feed.impl.timeseries;

import org.joda.time.DateTime;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.timeseries.ContinuationPolicy;
import com.barchart.feed.api.timeseries.Event;
import com.barchart.feed.api.timeseries.Period;
import com.barchart.feed.api.timeseries.SaleCondition;
import com.barchart.feed.api.timeseries.Query;
import com.barchart.feed.api.timeseries.VolumeType;

public class DataQuery implements Query {

	@Override
	public Query instrument(Instrument instrument) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Query symbol(String symbol) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Query period(Period period) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Query start(DateTime start) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Query end(DateTime end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Query padding(int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Query condition(SaleCondition... session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Query event(Event... events) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Query continuationPolicy(ContinuationPolicy policy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Query nearestOffset(int offset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Query volume(VolumeType type) {
		// TODO Auto-generated method stub
		return null;
	}

}
