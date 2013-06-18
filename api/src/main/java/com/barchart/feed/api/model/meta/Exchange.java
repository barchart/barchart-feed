package com.barchart.feed.api.model.meta;

import com.barchart.feed.api.model.Metadata;

public interface Exchange extends Metadata {
	
	String name();
	
	String code();
	
	public static Exchange NULL_EXCHANGE = new Exchange() {

		@Override
		public boolean isNull() {
			return true;
		}

		@Override
		public String name() {
			return "NULL_EXCHANGE";
		}

		@Override
		public String code() {
			return "NULL_EXCHANGE";
		}

	};
	
}
