package com.barchart.feed.api.model.data.parameter;

import java.util.Set;

import com.barchart.util.value.api.Existential;

public interface ParamMap extends Existential {

	<V> V get(Param param);
	
	boolean has(Param param);
	
	Set<Param> keySet();
	
	@Override
	boolean isNull();
	
	ParamMap NULL = new ParamMap() {

		@Override
		public <V> V get(Param param) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean has(Param param) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Set<Param> keySet() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isNull() {
			return true;
		}
		
	};
	
}
