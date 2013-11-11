package com.barchart.feed.api.model.meta;


public interface Group extends Metadata {

	public static Group NULL = new Group() {
		
		@Override
		public MetaType type() {
			return MetaType.GROUP;
		}

		@Override
		public boolean isNull() {
			return true;
		}

		@Override
		public String description() {
			return "NULL GROUP";
		}
		
		@Override
		public String toString() {
			return "NULL GROUP";
		}
		
	};
	
}
