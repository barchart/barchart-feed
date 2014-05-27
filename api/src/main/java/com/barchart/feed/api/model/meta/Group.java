package com.barchart.feed.api.model.meta;


public interface Group extends Metadata {

	public static Group NULL = new Group() {
		
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
