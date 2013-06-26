package com.barchart.feed.api.model.meta;


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
