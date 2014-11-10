package com.barchart.feed.api.model.data.parameter;

import java.util.Set;

import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

public class Example {

	public static void main(final String[] args) {
		
		final ParamMap map = new DummyParamMap();
		
		final Price VWAP = map.get(Param.SESSION_VWAP);
		
		final Size size = map.get(Param.TEST_SIZE);
		
	}
	
	public static class DummyParamMap implements ParamMap {

		final Price vwap = Price.ONE;
		final Size size = Size.ONE;
		
		@SuppressWarnings("unchecked")
		@Override
		public <V> V get(Param param) {
			
			switch(param) {
			default:
				throw new IllegalArgumentException();
			case SESSION_VWAP:
				return (V) param.type().cast(vwap);
			case TEST_SIZE:
				return (V) param.type().cast(size);
			}
			
		}

		@Override
		public boolean has(Param param) {
			return false;
		}

		@Override
		public Set<Param> keySet() {
			return null;
		}
		
	}
	
}
