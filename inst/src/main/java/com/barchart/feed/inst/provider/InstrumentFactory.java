/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.inst.provider;

import java.util.Map;

import com.barchart.feed.api.consumer.inst.InstrumentGUID;
import com.barchart.feed.api.framework.data.InstrumentEntity;
import com.barchart.feed.api.framework.data.InstrumentField;
import com.barchart.feed.inst.missive.BarchartFeedInstManifest;
import com.barchart.missive.api.Tag;
import com.barchart.missive.api.TagMap;
import com.barchart.missive.core.ObjectMapFactory;
import com.barchart.proto.buf.inst.InstrumentDefinition;

public final class InstrumentFactory {
	
	static {
		ObjectMapFactory.install(new BarchartFeedInstManifest());
	}
	
	private InstrumentFactory() {
		
	}
	
	public static InstrumentEntity buildFromProtoBuf(InstrumentDefinition instDef) {
		return InstrumentProtoBuilder.buildInstrument(instDef);
	}
	
	public static InstrumentDefinition buildProtoBuff(InstrumentEntity inst) {
		return InstrumentProtoBuilder.buildInstDef(inst);
	}
	
	@SuppressWarnings("rawtypes")
	public static final InstrumentEntity build(final Map<Tag, Object> map) {
		
		if(!map.containsKey(InstrumentField.GUID) ||
				map.get(InstrumentField.GUID) == null ||
				((InstrumentGUID) map.get(InstrumentField.GUID)).isNull()) {
			return InstrumentEntity.NULL_INSTRUMENT;
		}
		
		return ObjectMapFactory.build(InstrumentImpl.class, map);
	}
	
	public static final InstrumentEntity build(final TagMap map) {
		
		if(!map.contains(InstrumentField.GUID) ||
				map.get(InstrumentField.GUID) == null ||
				((InstrumentGUID) map.get(InstrumentField.GUID)).isNull()) {
			return InstrumentEntity.NULL_INSTRUMENT;
		}
		
		return ObjectMapFactory.build(InstrumentImpl.class, map);
	}

}
