/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.inst;

import java.util.List;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.util.concurrent.FutureNotifierBase;

public class InstrumentFuture extends FutureNotifierBase<
		List<Instrument>, InstrumentFuture> {
	
}