package com.barchart.feed.api.model.meta;



public interface Channel extends Metadata {

	Channel NULL = new Channel() {

		@Override
		public MetaType type() {
			return MetaType.CHANNEL;
		}
		
		
		@Override
		public boolean isNull() {
			return true;
		}

		@Override
		public String description() {
			return "NULL CHANNEL";
		}
		
		@Override
		public String toString() {
			return "NULL CHANNEL";
		}
		
	};
	
}
