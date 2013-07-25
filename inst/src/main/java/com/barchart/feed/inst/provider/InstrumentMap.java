package com.barchart.feed.inst.provider;

import java.util.Collections;
import java.util.Set;

import org.openfeed.proto.inst.InstrumentDefinition;

public interface InstrumentMap {

	void clear();
	
	boolean containsKey(String key);
	
	InstrumentDefinition get(String symbol);
	
	void put(String key, InstrumentDefinition value);
	
	int size();
	
	void close();
	
	Set<String> keySet();
	
	InstrumentMap NULL = new InstrumentMap() {

		@Override
		public void clear() {
			
		}

		@Override
		public boolean containsKey(String key) {
			return false;
		}

		@Override
		public InstrumentDefinition get(String symbol) {
			return InstrumentDefinition.getDefaultInstance();
		}

		@Override
		public void put(String key, InstrumentDefinition value) {
			
		}

		@Override
		public int size() {
			return 0;
		}

		@Override
		public void close() {
			
		}

		@Override
		public Set<String> keySet() {
			return Collections.emptySet();
		}
		
	};
	
}
