package com.barchart.feed.api.market;

import com.barchart.missive.api.Tag;
import com.barchart.missive.core.MissiveException;
import com.barchart.missive.core.TagFactory;

public final class MarketTagFactory {

	
	private MarketTagFactory() {
		
	}
	
	public static <V extends FrameworkElement<?>> MarketTag<V> 
			create(final Class<V> clazz) {
		
		final Tag<V> tag = TagFactory.create(clazz);
		
		return new MarketTag<V>() {

			@Override
			public V cast(Object o) throws MissiveException {
				return tag.cast(o);
			}

			@Override
			public Class<V> classType() {
				return tag.classType();
			}

			@Override
			public String name() {
				return tag.name();
			}

			@Override
			public int index() {
				return tag.index();
			}

			@Override
			public boolean isEnum() {
				return tag.isEnum();
			}

			@Override
			public boolean isList() {
				return tag.isList();
			}

			@Override
			public boolean isPrim() {
				return tag.isPrim();
			}

			@Override
			public boolean isComplex() {
				return tag.isComplex();
			}

			@Override
			public int compareTo(Tag<?> o) {
				return tag.compareTo(o);
			}
			
		};
		
	}
	
}
