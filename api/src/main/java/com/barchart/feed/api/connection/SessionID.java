package com.barchart.feed.api.connection;

import com.barchart.util.common.identifier.Identifier;

class SessionID extends Identifier<String, SessionID> {
	
	public SessionID(final String id) {
		super(id, SessionID.class);
	}
	
	public static final SessionID NULL = new SessionID("NULL_SESSION_ID") {
		
		@Override
		public boolean isNull() {
			return true;
		}
		
	};

}
