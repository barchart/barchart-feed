package com.barchart.feed.api.model.data.parameter;

import java.util.Set;

public interface ParamMap {

	<V> V get(Param param);
	
	boolean has(Param param);
	
	Set<Param> keySet();
	
}
