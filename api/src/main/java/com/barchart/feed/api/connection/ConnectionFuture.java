package com.barchart.feed.api.connection;

import com.barchart.util.concurrent.FutureNotifierBase;

public class ConnectionFuture<V extends ConnectionLifecycle<V>> 
		extends FutureNotifierBase<V, ConnectionFuture<V>> {

}
