/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.api.framework.inst;

import java.util.concurrent.Future;

import com.barchart.feed.api.consumer.data.Instrument;

// TODO Review
public interface InstrumentFuture extends Instrument, Future<Instrument> {

}
