/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface ExecutorCallable {

	<V> Future<V> submit(Callable<V> task);

}
