package com.barchart.feed.api.model.meta;

import com.barchart.feed.api.util.Identifier;


public interface Channel extends Metadata {

	Channel NULL = new Channel() {

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
			return "NULL CHANNEL";
		}
		
		@Override
		public String toString() {
			return "NULL CHANNEL";
		}
		
	};
	
}
