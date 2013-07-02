package com.barchart.feed.api.model.meta;

import com.barchart.feed.api.filter.Filterable.MetaType;
import com.barchart.feed.api.util.Identifier;

public interface Exchange extends Metadata {
	
	Exchange NULL = new Exchange() {

		@Override
		public MetaType type() {
			return MetaType.EXCHANGE;
		}
		
		
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
			return "NULL EXCHANGE";
		}
		
		@Override
		public String toString() {
			return "NULL EXCHANGE";
		}

	};
	
}
