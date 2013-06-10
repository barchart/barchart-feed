package com.barchart.feed.base.provider;

import java.util.HashMap;
import java.util.Map;

import com.barchart.feed.api.data.Cuvol;
import com.barchart.feed.api.data.Market;
import com.barchart.feed.api.data.MarketData;

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
				
				put(Cuvol.class, new MDGetter<Cuvol>() { 
					
					@Override
					public Cuvol get(final Market market) {
						return market.cuvol();
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
