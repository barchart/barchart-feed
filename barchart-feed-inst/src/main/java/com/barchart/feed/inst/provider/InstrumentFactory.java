/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.inst.provider;

import java.util.Map;

import com.barchart.feed.api.fields.InstrumentField;
import com.barchart.feed.api.inst.Instrument;
import com.barchart.feed.api.inst.InstrumentGUID;
import com.barchart.missive.core.Missive;
import com.barchart.missive.core.Tag;
import com.barchart.missive.core.TagMap;
import com.barchart.proto.buf.inst.InstrumentDefinition;

public final class InstrumentFactory {
	
	private InstrumentFactory() {
		
	}
	
	public static Instrument buildFromProtoBuf(InstrumentDefinition instDef) {
		return InstrumentProtoBuilder.buildInstrument(instDef);
	}
	
	public static InstrumentDefinition buildProtoBuff(Instrument inst) {
		return InstrumentProtoBuilder.buildInstDef(inst);
	}
	
	@SuppressWarnings("rawtypes")
	public static final Instrument build(final Map<Tag, Object> map) {
		
		if(!map.containsKey(InstrumentField.GUID) ||
				map.get(InstrumentField.GUID) == null ||
				((InstrumentGUID) map.get(InstrumentField.GUID)).isNull()) {
			return Instrument.NULL_INSTRUMENT;
		}
		
		return Missive.build(InstrumentImpl.class, map);
	}
	
	public static final Instrument build(final TagMap map) {
		
		if(!map.contains(InstrumentField.GUID) ||
				map.get(InstrumentField.GUID) == null ||
				((InstrumentGUID) map.get(InstrumentField.GUID)).isNull()) {
			return Instrument.NULL_INSTRUMENT;
		}
		
		return Missive.build(InstrumentImpl.class, map);
	}

}
