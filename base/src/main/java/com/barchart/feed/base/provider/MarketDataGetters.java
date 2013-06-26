package com.barchart.feed.base.provider;

import java.util.HashMap;
import java.util.Map;

import com.barchart.feed.api.model.data.BookSet;
import com.barchart.feed.api.model.data.Cuvol;
import com.barchart.feed.api.model.data.Market;
import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.api.model.data.MarketData;
import com.barchart.feed.api.model.data.Session;
import com.barchart.feed.api.model.data.SessionSet;
import com.barchart.feed.api.model.data.Trade;

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
						return market.freeze();
					}
			
				});
				
				put(Trade.class, new MDGetter<Trade>() {

					@Override
					public Trade get(final Market market) {
						return market.trade().freeze();
					}
					
				});
				
				put(Book.class, new MDGetter<Book>() {

					@Override
					public Book get(final Market market) {
						return market.book().freeze();
					}
					
				});
				
				put(BookSet.class, new MDGetter<BookSet>() {

					@Override
					public BookSet get(Market market) {
						return market.bookSet();
					}
					
				});
				
				put(Cuvol.class, new MDGetter<Cuvol>() { 
					
					@Override
					public Cuvol get(final Market market) {
						return market.cuvol().freeze();
					}
					
				});
				
				put(Session.class, new MDGetter<Session>() {

					@Override
					public Session get(final Market market) {
						return market.session().freeze();
					}
					
				});
				
				put(SessionSet.class, new MDGetter<SessionSet>() {

					@Override
					public SessionSet get(Market market) {
						return market.sessionSet();
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
