package com.barchart.feed.inst.provider;

import com.barchart.feed.api.data.Exchange;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.impl.ValueConst;

public final class ExchangeFactory {

private ExchangeFactory() {
		
	}
	
	public static Exchange fromCode(final String code) {
		
		return new Exchange() {
			
			private static final String NULL = "null";
			
			private final String id = code;
			
			@Override
			public String code() {
				return id;
			}
			
			@Override
			public Time lastUpdateTime() {
				return ValueConst.NULL_TIME;
			}

			@Override
			public boolean isNull() {
				return id.equals(NULL);
			}
			
			@Override
			public boolean equals(final Object o) {
				
				if(o instanceof Exchange) {
					return id.equals(((Exchange)o).code());
				}
				
				return true;
			}
			
			@Override
			public int hashCode() {
				return id.hashCode();
			}

			@Override
			public Exchange copy() {
				return this;
			}
			
		};
		
	}
	
}
