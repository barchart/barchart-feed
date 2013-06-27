package com.barchart.feed.api.model.meta;

import com.barchart.feed.api.util.Identifier;

/*
 * Use identifier and describable
 */
public interface Exchange extends Metadata {
	
//	@Deprecated
//	String name();
//	
//	@Deprecated
//	String code();
	
	public static Exchange NULL = new Exchange() {

		@Override
		public boolean isNull() {
			return true;
		}

//		@Override
//		public String name() {
//			return "NULL_EXCHANGE";
//		}
//
//		@Override
//		public String code() {
//			return "NULL_EXCHANGE";
//		}

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
