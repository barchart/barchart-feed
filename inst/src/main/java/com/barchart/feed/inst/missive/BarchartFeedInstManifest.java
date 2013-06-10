package com.barchart.feed.inst.missive;

import com.barchart.feed.inst.InstrumentField;
import com.barchart.feed.inst.provider.InstrumentBase;
import com.barchart.feed.inst.provider.InstrumentImpl;
import com.barchart.missive.api.Tag;
import com.barchart.missive.core.Manifest;
import com.barchart.missive.core.ObjectMap;

@SuppressWarnings("serial")
public class BarchartFeedInstManifest extends Manifest<ObjectMap> {
	
	public BarchartFeedInstManifest() {
		super();
		
		put(InstrumentBase.class, InstrumentField.FIELDS);
		put(InstrumentImpl.class, new Tag<?>[0]);
		
	}

}
