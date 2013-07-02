package com.barchart.feed.base.provider;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.api.filter.Filter;
import com.barchart.feed.api.filter.FilterUpdatable;
import com.barchart.feed.api.model.meta.Exchange;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.Metadata;

public class FilterImpl implements FilterUpdatable, Filter {

	protected static final Logger log = LoggerFactory
			.getLogger(FilterImpl.class);
					
	private final Set<Exchange> incExchanges = new HashSet<Exchange>();
	private final Set<Exchange> exExchanges = new HashSet<Exchange>();

	private final Set<Instrument> incInsts = new HashSet<Instrument>();
	private final Set<Instrument> exInsts = new HashSet<Instrument>();
	
	@Override
	public Filter filter() {
		return this;
	}

	@Override
	public void filter(Filter filter) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		incExchanges.clear();
		exExchanges.clear();
		incInsts.clear();
		exInsts.clear();
	}

	@Override
	public void include(Metadata... meta) {
		
		for(Metadata m : meta) {
		
			switch(m.type()) {
		
			default:
				// Ignore 
				continue;
			case INSTRUMENT:
				incInsts.add((Instrument)m);
				exInsts.remove((Instrument)m);
				continue;
			case EXCHANGE:
				incExchanges.add((Exchange)m);
				exExchanges.remove((Exchange)m);
			}
		
		}
		
	}

	@Override
	public void exclude(Metadata... meta) {
		
		for(Metadata m : meta) {
			
			switch(m.type()) {
		
			default:
				// Ignore 
				continue;
			case INSTRUMENT:
				exInsts.add((Instrument)m);
				incInsts.remove((Instrument)m);
				continue;
			case EXCHANGE:
				exExchanges.add((Exchange)m);
				incExchanges.remove((Exchange)m);
			}
		
		}
		
	}

	@Override
	public boolean hasMatch(Instrument instrument) {
		
		if (incInsts.contains(instrument)) {
			return true;
		}

		if (exInsts.contains(instrument)) {
			return false;
		}
		
		if (instrument.exchange().isNull()) {
			log.debug("Exchange is NULL for " + instrument.symbol() + " "
					+ instrument.exchangeCode());
			return false;
		}
		
		if (incExchanges.contains(instrument.exchange())) {
			return true;
		}

		if (exExchanges.contains(instrument.exchange())) {
			return false;
		}

		return false;
		
	}

	@Override
	public String expression() {
		throw new UnsupportedOperationException();
	}

}
