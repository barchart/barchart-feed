package com.barchart.feed.api.util;

import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public interface ListenableFuture<V> extends Future<V> {

	void addListener(Runnable runnable, Executor executor);
	
}
