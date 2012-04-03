/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.mock;

public interface MockMessageVisitor<Result, Param> {

	Result visit(MockMsgTrade message, Param param);

	Result visit(MockMsgBook message, Param param);

}
