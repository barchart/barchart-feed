package com.ddfplus.feed.common.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface ExecutorCallable {

	<V> Future<V> submit(Callable<V> task);

}
