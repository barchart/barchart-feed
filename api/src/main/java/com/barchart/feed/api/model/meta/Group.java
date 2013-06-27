package com.barchart.feed.api.model.meta;

import com.barchart.feed.api.util.Identifier;

public interface Group extends Metadata {

	public static Group NULL = new Group() {

		@Override
		public boolean isNull() {
			return true;
		}

		@Override
		public Identifier id() {
			return Identifier.NULL;
		}

		@Override
		public String description() {
			return "NULL";
		}
		
	};
	
}
