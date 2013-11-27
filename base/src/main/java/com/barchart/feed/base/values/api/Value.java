/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.values.api;

public interface Value<T extends Value<T>> {

	T freeze();

	boolean isFrozen();

	/*
	 * TODO review null contract: should parent with all null children be also
	 * null? should zero values be treated as null?
	 */
	boolean isNull();

}
