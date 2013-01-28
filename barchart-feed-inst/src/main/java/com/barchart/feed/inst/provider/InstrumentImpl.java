package com.barchart.feed.inst.provider;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.barchart.feed.inst.api.Instrument;
import com.barchart.feed.inst.api.InstrumentConst;
import com.barchart.feed.inst.api.InstrumentField;
import com.barchart.feed.inst.api.InstrumentGUID;
import com.barchart.missive.core.MissiveException;
import com.barchart.missive.core.Tag;

class InstrumentImpl implements Instrument {

	@SuppressWarnings("rawtypes")
	private final Map<Tag, Object> map = new HashMap<Tag, Object>();
	private final InstrumentGUID guid;
	
	@SuppressWarnings("rawtypes")
	InstrumentImpl(final Map<Tag, Object> map) {
		
		if(!map.containsKey(InstrumentField.ID) ||
				map.get(InstrumentField.ID) == null) {
			throw new IllegalArgumentException("Map must contain ID");
		}
		
		guid = new InstrumentGUIDImpl(Long.parseLong(map.get(InstrumentField.ID).toString()));
		
		for(final Entry<Tag, Object> e : map.entrySet()) {
			map.put(e.getKey(), e.getKey().cast(e.getValue()));
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <V> V get(Tag<V> tag) throws MissiveException {
		return (V) map.get(tag);
	}

	@Override
	public boolean contains(Tag<?> tag) {
		return map.containsKey(tag);
	}

	@Override
	public Tag<?>[] tags() {
		return map.keySet().toArray(new Tag<?>[0]);
	}

	@Override
	public int size() {
		return map.size();
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
