package com.barchart.feed.base.provider;

import java.util.HashMap;
import java.util.Map;

import com.barchart.feed.api.data.Cuvol;
import com.barchart.feed.api.data.Instrument;
import com.barchart.feed.api.data.Market;
import com.barchart.feed.api.data.MarketData;
import com.barchart.feed.api.data.OrderBook;
import com.barchart.feed.api.data.PriceLevel;
import com.barchart.feed.api.data.Session;
import com.barchart.feed.api.data.TopOfBook;
import com.barchart.feed.api.data.Trade;

public final class MarketDataGetters {

	public interface MDGetter<V extends MarketData<V>> {
		
		public V get(Market market);
		
	}
	
	@SuppressWarnings({ "rawtypes", "serial" })
	private static final Map<Class, MDGetter> getters = 
			new HashMap<Class, MDGetter>() {{
				
				put(Market.class, new MDGetter<Market>() {

					@Override
					public Market get(final Market market) {
						return market;
					}
			
				});
				
				put(Instrument.class, new MDGetter<Instrument>(){

					@Override
					public Instrument get(final Market market) {
						return market.instrument();
					}
					
				});
				
				put(Trade.class, new MDGetter<Trade>(){

					@Override
					public Trade get(final Market market) {
						return market.lastTrade();
					}
					
				});
				
				put(OrderBook.class, new MDGetter<OrderBook>(){

					@Override
					public OrderBook get(final Market market) {
						return market.orderBook();
					}
					
				});
				
				put(PriceLevel.class, new MDGetter<PriceLevel>(){

					@Override
					public PriceLevel get(final Market market) {
						return market.lastBookUpdate();
					}
					
				});
				
				put(TopOfBook.class, new MDGetter<TopOfBook>(){

					@Override
					public TopOfBook get(final Market market) {
						return market.topOfBook();
					}
					
				});
				
				put(Cuvol.class, new MDGetter<Cuvol>() { 
					
					@Override
					public Cuvol get(final Market market) {
						return market.cuvol();
					}
					
				});
				
				put(Session.class, new MDGetter<Session>(){

					@Override
					public Session get(final Market market) {
						return market.session();
					}
					
				});
				
			}};
	
	@SuppressWarnings("unchecked")
	public static <V extends MarketData<V>> MDGetter<V> get(Class<V> clazz) {
		
		final MDGetter<V> getter = getters.get(clazz);
		
		if(getter != null) {
			return getter;
		}
		
		throw new IllegalArgumentException("Class not supported: " + clazz.getName()); 
		
	}
	
}
