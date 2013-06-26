package com.barchart.feed.api.model.meta;

/**
 * Top level interface for metadata objects.  Current extensions are instrument
 * and exchange.  Future extensions are channel and group.
 * 
 * @author Gavin M Litchfield
 *
 */
public interface Metadata {
	
	boolean isNull();

}
