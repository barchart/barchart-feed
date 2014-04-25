package com.barchart.feed.api.model.meta.id;

import com.barchart.util.common.identifier.Identifier;

public class ChannelID extends Identifier<Integer, ChannelID> {

	public ChannelID(final Integer id) {
		super(id, ChannelID.class);
	}

	public static final ChannelID NULL = new ChannelID(-1) {

		@Override
		public boolean isNull() {
			return true;
		}

	};

}
