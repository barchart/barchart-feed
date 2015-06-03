package com.barchart.feed.api;

import com.barchart.util.common.identifier.Identifier;

public class AgentID extends Identifier<String, AgentID> {

	public AgentID(String id) {
		super(id, AgentID.class);
	}
	
	public static AgentID NULL = new AgentID("NULL") {

		@Override
		public boolean isNull() {
			return true;
		}

	};
	
}
