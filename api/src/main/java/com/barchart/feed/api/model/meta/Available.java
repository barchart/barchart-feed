package com.barchart.feed.api.model.meta;


public interface Available extends Metadata {

	Available NULL = new Available() {

		@Override
		public MetaType type() {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean isNull() {
			return true;
		}

		@Override
		public String description() {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public String toString() {
			throw new UnsupportedOperationException();
		}

	};
	
}
