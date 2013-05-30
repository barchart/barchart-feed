package com.barchart.feed.inst.missive;

import com.barchart.feed.api.missive.BarchartFeedManifest;
import com.barchart.feed.inst.InstrumentField;
import com.barchart.feed.inst.provider.InstrumentBase;
import com.barchart.feed.inst.provider.InstrumentFutureImpl;
import com.barchart.feed.inst.provider.InstrumentImpl;
import com.barchart.missive.api.Tag;

@SuppressWarnings("serial")
public class BarchartFeedInstManifest extends BarchartFeedManifest {
	
	public BarchartFeedInstManifest() {
		super();
		
		put(InstrumentBase.class, InstrumentField.FIELDS);
		put(InstrumentImpl.class, new Tag<?>[0]);
		put(InstrumentFutureImpl.class, new Tag<?>[0]);
	}

}
