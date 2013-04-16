package com.barchart.feed.base.provider;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.base.bar.api.MarketBar;
import com.barchart.feed.base.bar.api.MarketDoBar;
import com.barchart.feed.base.market.api.NEWMarketAgent;
import com.barchart.feed.base.market.api.NEW_MarketDo;
import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.util.values.api.Value;

public abstract class NEW_VarMarket extends DefMarket implements NEW_MarketDo {

	private static final Logger log = LoggerFactory.getLogger(NEW_VarMarket.class);
	
	private final List<NEWMarketAgent<?,?>> agents = 
			new CopyOnWriteArrayList<NEWMarketAgent<?,?>>();
	
	@Override
	public void fireEvents() {
		
		
	}

	@Override
	public void addAgent(NEWMarketAgent<?, ?> agent) {
		
		// Check agents conditions, if match this.getInstrument()

		agents.add(agent);
	}

	@Override
	public void removeAgent(final NEWMarketAgent<?, ?> agent) {
		if(agent != null) {
			agents.remove(agent);
		}
	}

	@Override
	public List<NEWMarketAgent<?, ?>> agents() {
		return Collections.unmodifiableList(agents);
	}

	@Override
	public Set<MarketEvent> events() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MarketDoBar loadBar(final MarketField<MarketBar> barField) {
		// TODO Auto-generated method stub
		return null;
	}

	protected final <T extends Value<T>> void set(final MarketField<T> field,
			final T value) {

		assert field != null;
		assert value != null;

		valueArray[field.ordinal()] = value;

	}
	
}
