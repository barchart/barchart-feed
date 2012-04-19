/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market;

// 32 bit jvm: 24 bytes
// object 8
// long 8
// byte 1, word align to 8
class BenchClassByteLong {

	byte value1;

	long value2;

}
