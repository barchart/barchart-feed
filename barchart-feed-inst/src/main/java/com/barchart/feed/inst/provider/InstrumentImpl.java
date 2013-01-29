package com.barchart.feed.inst.provider;

import java.util.Map;

import com.barchart.feed.inst.api.Instrument;
import com.barchart.feed.inst.api.InstrumentConst;
import com.barchart.feed.inst.api.InstrumentField;
import com.barchart.feed.inst.api.InstrumentGUID;
import com.barchart.missive.core.MissiveException;
import com.barchart.missive.core.Tag;
import com.barchart.missive.core.TagMap;
import com.barchart.missive.hash.HashTagMap;

class InstrumentImpl extends InstrumentBase implements Instrument {

	private final TagMap tmap;
	
	private final InstrumentGUID guid;
	
	@SuppressWarnings("rawtypes")
	InstrumentImpl(final Map<Tag, Object> map) {
		
		if(!map.containsKey(InstrumentField.ID) ||
				map.get(InstrumentField.ID) == null) {
			throw new IllegalArgumentException("Map must contain ID");
		}
		
		guid = new InstrumentGUIDImpl(Long.parseLong(
				map.get(InstrumentField.ID).toString()));
		
		tmap = new HashTagMap(map);
		
	}
	
	InstrumentImpl(final TagMap tagMap) {
		
		if(!tagMap.contains(InstrumentField.ID) ||
				tagMap.get(InstrumentField.ID) == null) {
			throw new IllegalArgumentException("Map must contain ID");
		}
		
		guid = new InstrumentGUIDImpl(Long.parseLong(
				tagMap.get(InstrumentField.ID).toString()));
		
		tmap = tagMap;
		
	}
	
	@Override
	public <V> V get(Tag<V> tag) throws MissiveException {
		return tmap.get(tag);
	}

	@Override
	public boolean contains(Tag<?> tag) {
		return tmap.contains(tag);
	}

	@Override
	public Tag<?>[] tags() {
		return tmap.tags();
	}

	@Override
	public int size() {
		return tmap.size();
	}

	@Override
	public Instrument freeze() {
		return this;
	}

	@Override
	public boolean isFrozen() {
		return true;
	}

	@Override
	public boolean isNull() {
		return this == InstrumentConst.NULL_INSTRUMENT;
	}

	@Override
	public InstrumentGUID getGUID() {
		return guid;
	}

}
