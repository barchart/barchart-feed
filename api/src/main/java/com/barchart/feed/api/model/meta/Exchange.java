package com.barchart.feed.api.model.meta;

import com.barchart.feed.api.model.meta.id.ExchangeID;


public interface Exchange extends Metadata {
	
	String timeZoneName();
	
	ExchangeID id();
	
	Exchange NULL = new Exchange() {

		@Override
		public MetaType type() {
			return MetaType.EXCHANGE;
		}
		
		@Override
		public ExchangeID id() {
			return ExchangeID.NULL;
		}
		
		@Override
		public boolean isNull() {
			return true;
		}

		@Override
		public String timeZoneName() {
			return "NULL TIME ZONE";
		}
		
		@Override
		public String description() {
			return "NULL EXCHANGE";
		}
		
		@Override
		public String toString() {
			return "NULL EXCHANGE";
		}

		

	};
	
}
