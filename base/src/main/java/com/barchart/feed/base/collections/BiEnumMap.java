package com.barchart.feed.base.collections;

import java.lang.reflect.Array;

/**
 * A fast array based bi-directional map between two sets of enums.
 * 
 * @author Gavin M Litchfield
 *
 * @param <K> 
 * @param <V>
 */
public class BiEnumMap<K extends Enum<K>, V extends Enum<V>> {

	private final K[] ks;
	private final V[] vs;
	
	/**
	 * Map maintains the explicit ordering between the array elements.  Arrays
	 * must contain the same number of elements.
	 * 
	 * @param keys
	 * @param vals
	 */
	@SuppressWarnings("unchecked")
	public BiEnumMap(final K[] keys, final V[] vals) {
		
		if(keys.length != vals.length) {
			throw new IllegalArgumentException("Arrays must be the same size");
		}
		
		int maxKeyOrd = 0;
		for(K k : keys) {
			if(maxKeyOrd < k.ordinal()) {
				maxKeyOrd = k.ordinal();
			}
		}
		
		int maxValOrd = 0;
		for(V v : vals) {
			if(maxValOrd < v.ordinal()) {
				maxValOrd = v.ordinal();
			}
		}
		
		this.ks = (K[]) Array.newInstance(keys.getClass().getComponentType(), maxValOrd + 1);
		this.vs = (V[]) Array.newInstance(vals.getClass().getComponentType(), maxKeyOrd + 1);
		
		for(int i = 0; i < keys.length; i++) {
			ks[vals[i].ordinal()] = keys[i];
			vs[keys[i].ordinal()] = vals[i];
		}
		
	}
	
	public BiEnumMap(final Class<K> keyClass, final Class<V> valClass) {
		
		this(keyClass.getEnumConstants(), valClass.getEnumConstants());
		
	}
	
	public V getValue(final K key) {
		if(key.ordinal() >= vs.length) {
			return null;
		}
		return vs[key.ordinal()];
	}

	public K getKey(final V val) {
		if(val.ordinal() >= ks.length) {
			return null;
		}
		return ks[val.ordinal()];
	}
	
}
