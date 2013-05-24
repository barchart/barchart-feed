package com.barchart.feed.api.consumer.enums;

enum UniBookResult {

	/** update processed on top */
	TOP, //

	/** update processed in range */
	NORMAL, //

	/** update dropped due to out of range */
	DISCARD, //

	/** invalid update request */
	ERROR, //

}
