package com.barchart.feed.api.model.meta;

import com.barchart.feed.api.util.Identifier;

public interface Available extends Metadata {

	Available NULL = new Available() {

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
