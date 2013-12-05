package com.barchart.feed.series;

import rx.Observable;

import com.barchart.feed.api.series.services.Query;


/**
 * Abstract factory which produces {@code Assembler} subclasses which
 * specialize in assembling dynamically customizable graphs of nodes 
 * whose inputs and outputs can be combined to produce one or more
 * configured outputs. 
 * 
 * @author metaware
 *
 * @param <T>
 */
public abstract class Assembler<T extends Observable<T>> {
	
	public static Assembler<? extends Observable<?>> assemble(Query query) {
		return null;
	}
	
	public static Assembler<? extends Observable<?>> sharedAssembly(Query query) {
        return null;
    }
	
	public static Assembler<? extends Observable<?>> share(Query query) {
        return null;
    }
}
