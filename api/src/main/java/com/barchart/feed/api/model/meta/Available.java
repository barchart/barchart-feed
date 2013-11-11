package com.barchart.feed.api.model.meta;


public interface Available extends Metadata {

	Available NULL = new Available() {

		@Override
		public MetaType type() {
			return MetaType.AVAILABLE;
		}
		
		@Override
		public boolean isNull() {
			return true;
		}

		@Override
		public String description() {
			return "NULL AVAILABLE";
		}
		
		@Override
		public String toString() {
			return "NULL AVAILABLE";
		}

	};
	
}
